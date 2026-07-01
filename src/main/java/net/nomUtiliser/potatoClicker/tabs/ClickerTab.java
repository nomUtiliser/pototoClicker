package net.nomUtiliser.potatoClicker.tabs;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;

import java.math.BigInteger;
import java.util.Arrays;


public class ClickerTab extends BaseVTab<VBox> {
    private Button potato;
    private ScrollPane scrollPane;
    private VBox upgradesContainer;
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    private Label moenyPanel;
    @Override
    protected void setPanel() {
        upgradesContainer = new VBox(10);
        // Add some upgrade items with better styling
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 1", "Cost: 10 patates", 10));
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 2", "Cost: 50 patates", 50));
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 3", "Cost: 100 patates", 100));
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 4", "Cost: 500 patates", 500));
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 5", "Cost: 1000 patates", 1000));
        upgradesContainer.getChildren().add(createUpgradeItem("Upgrade 6", "Cost: 5000 patates", 5000));


        scrollPane = new ScrollPane(upgradesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(upgradesContainer);
        scrollPane.setMaxSize(320, 500);
        scrollPane.setPrefHeight(500);
        scrollPane.setPrefWidth(320);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        upgradesContainer.getStyleClass().add("pototoPanel");
        potato= new Button();
        potato.getStyleClass().add("potatoSacred");
        potato.setMaxSize(100, 60);
        potato.setPrefSize(100, 30);
        potato.setText("patate");
        moenyPanel= new Label();
        moenyPanel.setMaxSize(250, 60);
        moenyPanel.setPrefSize(250, 30);
        if (CounterHandler.getSave() == null) return;
        moenyPanel.setText(Functions.formatMessage("$$1 patate", CounterHandler.getSave().potatoCount));
        vContent.getChildren().addAll(potato,moenyPanel,scrollPane);
        potato.setOnAction(e-> addMoney(1) );
    }

    private void loadUpgrade() {

    }

    /**
     * Creates a styled upgrade item with name, cost, and purchase button
     */
    private VBox createUpgradeItem(String name, String cost, int costValue) {
        // Create main container for the upgrade item
        VBox upgradeBox = new VBox(5);
        upgradeBox.getStyleClass().add("upgrade-item");
        
        // Upgrade name label
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("upgrade-name");
        
        // Cost label
        Label costLabel = new Label(cost);
        costLabel.getStyleClass().add("upgrade-cost");
        
        // Purchase button
        Button purchaseButton = new Button("Buy");
        purchaseButton.getStyleClass().add("purchase-button");
        purchaseButton.setPrefSize(80, 25);
        purchaseButton.setOnAction(e -> {
            // Handle purchase logic here
            if (CounterHandler.getSave() != null) {
                // Example purchase logic - check if player has enough potatoes
                if (CounterHandler.getSave().potatoCount.compareTo(BigInteger.valueOf(costValue)) >= 0) {
                    CounterHandler.getSave().potatoCount = CounterHandler.getSave().potatoCount.subtract(BigInteger.valueOf(costValue));
                    moenyPanel.setText(Functions.formatMessage("$$1 patate", CounterHandler.getSave().potatoCount));
                    // Update UI accordingly (this would be extended in a real implementation)
                }
            }
        });
        
        // Add components to the upgrade box
        HBox buttonContainer = new HBox(5);
        buttonContainer.getChildren().add(purchaseButton);
        buttonContainer.getStyleClass().add("button-container");
        
        upgradeBox.getChildren().addAll(nameLabel, costLabel, buttonContainer);
        
        return upgradeBox;
    }

    private void addMoney(int addedMoneyAmount) {
        if (CounterHandler.getSave() == null) return;
        CounterHandler.getSave().potatoCount =CounterHandler.getSave().potatoCount.add(BigInteger.valueOf(addedMoneyAmount));
        moenyPanel.setText(Functions.formatMessage("$$1 patate", CounterHandler.getSave().potatoCount));
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
