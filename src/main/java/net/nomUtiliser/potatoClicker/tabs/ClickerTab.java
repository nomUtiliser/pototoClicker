package net.nomUtiliser.potatoClicker.tabs;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
import net.nomUtiliser.potatoClicker.upgrades.reg.Upgrades;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ClickerTab extends BaseVTab<VBox> {
    private ImageView potatoImg;
    private ScrollPane scrollPane;
    private VBox upgradesContainer;
    
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    private Label moneyPanel;
    
    @Override
    protected void setPanel() {
        upgradesContainer = new VBox(10);
        // Add more upgrade items to make the container larger
        addUpgrades();

        // Set container size to ensure adequate height
        upgradesContainer.setPrefHeight(1000);
        upgradesContainer.setPrefWidth(200);
        upgradesContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        scrollPane = new ScrollPane(upgradesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(upgradesContainer);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        // Make the scroll pane take up all available space in the HBox
        VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(upgradesContainer, javafx.scene.layout.Priority.ALWAYS);
        
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
        HBox.setHgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(potatoImg, javafx.scene.layout.Priority.NEVER);
        HBox.setHgrow(moneyPanel, javafx.scene.layout.Priority.NEVER);
        
        vContent.getChildren().add(mainContainer);
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
        }
    }

    /**
     * Creates a styled upgrade item with name, cost, and purchase button
     */
    private VBox createUpgradeItem(AbstractUpgrade upgrade) {
        // Create main container for the upgrade item
        String name = upgrade.getName();
        BigInteger costValue = upgrade.getBaseCost();
        VBox upgradeBox = new VBox(5);
        upgradeBox.getStyleClass().add("upgrade-item");
        
        // Upgrade name label
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("upgrade-name");
        // quantity label
        Label quanLabel = new Label(Functions.formatMessage("Quantity: $$1",getQuantityUpgrade(upgrade)));
        quanLabel.getStyleClass().add("upgrade-quantity");
        // Cost label
        Label costLabel = new Label(Functions.formatMessage("cost: $$1 potatoes", costValue));
        costLabel.getStyleClass().add("upgrade-cost");
        // Purchase button
        Button purchaseButton = new Button("Buy");
        purchaseButton.getStyleClass().add("purchase-button");
        purchaseButton.setPrefSize(80, 25);
        purchaseButton.setOnAction(e -> {
            // Handle purchase logic here
            if (CounterHandler.getSave() != null) {
                // Example purchase logic - check if player has enough potatoes
                if (CounterHandler.getSave().potatoCount.compareTo(costValue) >= 0) {
                    CounterHandler.getSave().potatoCount = CounterHandler.getSave().potatoCount.subtract(costValue);
                    buyUpgrade(upgrade);
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

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        try {
            System.out.println("added");
            scheduler.scheduleAtFixedRate(() -> {
                addMoney(upgrade.getBaseIncome());
            }, 0, 1, TimeUnit.SECONDS);
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

    private void buyUpgrade(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        for (Upgrade u : CounterHandler.getSave().upgrades) {
            if (upgrade.getName().equals(u.id)) {
                u.quantity = u.quantity.add(BigInteger.valueOf(1));
                break;
            }
        }
    }

    private void addMoney(BigInteger addedMoneyAmount) {
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