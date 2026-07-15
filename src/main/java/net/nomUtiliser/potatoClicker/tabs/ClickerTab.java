package net.nomUtiliser.potatoClicker.tabs;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.logic.data.Upgrade;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClickerTab extends BaseVTab<VBox> {
    private Map<AbstractUpgrade, Label> costLabels;

    private Map<AbstractUpgrade, Label> getCostLabels() {
        if (costLabels == null) {
            costLabels = new HashMap<>();
        }
        return costLabels;
    }
    private ImageView potatoImg;
    private ScrollPane scrollPane;
    private VBox upgradesContainer;
    private Label pototoPerSec;
    private Map<String, ScheduledFuture<?>> schedulersMap;
    private Map<String, BigInteger> pototoPerSecMap;
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    private Label moneyPanel;
    
    @Override
    protected void setPanel() {
        schedulersMap = new HashMap<>();
        upgradesContainer = new VBox(10);
        // Add more upgrade items to make the container larger
        addUpgrades();
        // Set container size to ensure adequate height
        upgradesContainer.setPrefHeight(1000);
        upgradesContainer.setPrefWidth(200);
        upgradesContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pototoPerSec = new Label("O Potatoes/s");
        pototoPerSec.setPrefSize(300, 20);
        pototoPerSec.setMaxSize(300, 20);
        pototoPerSec.setMinSize(300,20);
        scrollPane = new ScrollPane(upgradesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(upgradesContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // Make the scroll pane take up all available space in the HBox
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        VBox.setVgrow(upgradesContainer, Priority.ALWAYS);
        
        // Load potato image from resources
        try {
            Image potatoImage = new Image("textures/pototo.png");
            potatoImg = new ImageView(potatoImage);
            potatoImg.setFitWidth(200);
            potatoImg.setFitHeight(200);
            potatoImg.setPreserveRatio(true);
        } catch (Exception e) {
            // If image loading fails, create a simple label as fallback
            potatoImg = new ImageView();
            PtfLogger.error("Failed to load potato");
            System.err.println("Failed to load potato image: " + e.getMessage());
        }
        moneyPanel = new Label();
        moneyPanel.setMaxSize(100, 60);
        moneyPanel.setPrefSize(100, 30);
        moneyPanel.getStyleClass().add("moneyPanel");
        if (CounterHandler.getSave()== null) return;
        moneyPanel.setText(Functions.formatMessage("$$1 potatoes", CounterHandler.getSave().potatoCount));
        
        // Create HBox to put ScrollPane on the right side with proper stretching
        HBox mainContainer = new HBox();
        mainContainer.setMinHeight(400);
        mainContainer.getChildren().addAll(potatoImg, moneyPanel, scrollPane);
        mainContainer.setSpacing(10);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setMaxSize(700, Double.MAX_VALUE);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(potatoImg, Priority.NEVER);
        HBox.setHgrow(moneyPanel, Priority.NEVER);
        
        vContent.getChildren().addAll(pototoPerSec, mainContainer);
        potatoImg.setOnMouseClicked(e -> addMoney(BigInteger.valueOf(1)));
    }

    private void addUpgrades() {
        List<AbstractUpgrade> allUpgrades = PotatoClicker.upgradesEvent.reg.getAll()
                .stream().sorted(
                        Comparator.comparing(
                                upgrade-> !upgrade.id().getNamespace().equals(PotatoClicker.MOD_ID)
                        )
                ).toList();
        for (AbstractUpgrade upgrade : allUpgrades) {
            upgradesContainer.getChildren().add(createUpgradeItem(upgrade));
            addUpgradeIncome(upgrade);
            calPototoPerSec(upgrade);
        }
    }

    /**
     * Creates a styled upgrade item with name, cost, and purchase button
     */

    private VBox createUpgradeItem(AbstractUpgrade upgrade) {
        // Create main container for the upgrade item
        String name = upgrade.getName();
        assert CounterHandler.getSave() != null;
        VBox upgradeBox = new VBox(5);
        upgradeBox.getStyleClass().add("upgrade-item");
        
        // Upgrade name label
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("upgrade-name");
        // quantity label
        Label quanLabel = new Label(Functions.formatMessage("Quantity: $$1",getQuantityUpgrade(upgrade)));
        quanLabel.getStyleClass().add("upgrade-quantity");
        // Cost label
        Label costLabel = new Label(Functions.formatMessage("cost: $$1 potatoes", calculateCost(upgrade)));
        costLabel.getStyleClass().add("upgrade-cost");
        getCostLabels().put(upgrade, costLabel);
        // Purchase button
        Button purchaseButton = new Button("Buy");
        purchaseButton.getStyleClass().add("purchase-button");
        purchaseButton.getStyleClass().add(Functions.formatMessage(".$$1-purchaseButton", name));
        purchaseButton.setPrefSize(80, 25);
        purchaseButton.setOnAction(e -> {
            // Handle purchase logic here
            if (CounterHandler.getSave() != null) {
                // Example purchase logic - check if player has enough potatoes
                if (CounterHandler.getSave().potatoCount.compareTo(calculateCost(upgrade)) >= 0) {
                    CounterHandler.getSave().potatoCount = CounterHandler.getSave().potatoCount.subtract(calculateCost(upgrade));
                    buyUpgrade(upgrade);
                    refreshCost(upgrade);
                    quanLabel.setText(Functions.formatMessage("Quantity: $$1",getQuantityUpgrade(upgrade)));
                    moneyPanel.setText(Functions.formatMessage("$$1 patate", CounterHandler.getSave().potatoCount));
                    // Update UI accordingly (this would be extended in a real implementation)
                }
            }
        });
        // Add components to the upgrade box
        HBox buttonContainer = new HBox(5);
        buttonContainer.getChildren().add(purchaseButton);
        buttonContainer.getStyleClass().add("button-container");
        upgradeBox.getChildren().addAll(nameLabel, quanLabel, costLabel, buttonContainer);
        return upgradeBox;
    }


    public void addUpgradeIncome(AbstractUpgrade upgrade) {
        if (schedulersMap.containsKey(upgrade.getName())) {
            schedulersMap.get(upgrade.getName()).cancel(false);
        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> task= scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                try {
                    assert CounterHandler.getSave() != null;
                    for (Upgrade u : CounterHandler.getSave().upgrades) {
                        if (upgrade.getName().equals(u.id)) {
                            addMoney(upgrade.getBaseIncome().multiply(u.quantity));

                            pototoPerSec.setText(Functions.formatMessage("$$1 Potatoes/S", upgrade.getBaseIncome().multiply(u.quantity)));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
    }, 0, 1, TimeUnit.SECONDS);
        schedulersMap.put(upgrade.getName(), task);
    }

    private void calPototoPerSec(AbstractUpgrade upgrade) {
        try {
            assert CounterHandler.getSave() != null;
            for (Upgrade u : CounterHandler.getSave().upgrades) {
                if (upgrade.getName().equals(u.id)) {
                    pototoPerSecMap.remove(upgrade.getName());
                    pototoPerSecMap.put(upgrade.getName(), upgrade.getBaseIncome().multiply(u.quantity));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getQuantityUpgrade(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        for (Upgrade u : CounterHandler.getSave().upgrades) {
            if (upgrade.getName().equals(u.id)) {
                return u.quantity.toString();
            }
        }
        return "0";
    }

    public BigInteger calculateNewPrice(BigInteger price, double multiply) {
        BigDecimal newPrice = new BigDecimal(price).multiply(new BigDecimal(multiply));
        return newPrice.setScale(0, RoundingMode.CEILING).toBigInteger();
    };

    private void refreshCost(AbstractUpgrade upgrade) {
        Label label = getCostLabels().get(upgrade);
        if (label != null) {
            label.setText(Functions.formatMessage("cost: $$1 potatoes", calculateCost(upgrade)));
        }
    }

    private BigInteger calculateCost(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        return Arrays.stream(CounterHandler.getSave().upgrades)
                .filter(u -> upgrade.getName().equals(u.id))
                .findFirst()
                .map(u -> calculateNewPrice(upgrade.getBaseCost().multiply(u.quantity), 1.7))
                .orElse(BigInteger.valueOf(-1));
    }

    private void buyUpgrade(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        for (Upgrade u : CounterHandler.getSave().upgrades) {
            if (upgrade.getName().equals(u.id)) {
                u.quantity = u.quantity.add(BigInteger.valueOf(1));
                addUpgradeIncome(upgrade);
                refreshCost(upgrade);
                break;
            }
        }
    }

    public void addMoney(BigInteger addedMoneyAmount) {
        if (CounterHandler.getSave() == null) return;
        CounterHandler.getSave().potatoCount =CounterHandler.getSave().potatoCount.add(addedMoneyAmount);
        moneyPanel.setText(Functions.formatMessage("$$1 potatoes", CounterHandler.getSave().potatoCount));
    }

    @Override
    protected String getTitle() {
        return Translations.get("potatoClicker:tabs.clicker.title");
    }

    @Override
    public String getName() {
        return Translations.get("potatoClicker:tabs.clicker.name");
    }
}