package net.nomUtiliser.potatoClicker.utility;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.minheur.potoflux.Functions;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.logic.data.Upgrade;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpgradeManager {
    ClickerTab clicker = new ClickerTab();
    MoneyManager moneyManager = new MoneyManager();
    private Map<AbstractUpgrade, Label> getCostLabels() {
        if (costLabels == null) {
            costLabels = new HashMap<>();
        }
        return costLabels;
    }
    public HBox createUpgradeItem(AbstractUpgrade upgrade) {
        // Create main container for the upgrade item
        String name = upgrade.getName();
        assert CounterHandler.getSave() != null;
        VBox upgradeBox = new VBox(5);
        HBox rightPart = new HBox(5);
        rightPart.getStyleClass().add("upgrade-item");
        Image img;
        URL ress = getClass().getResource(Functions.formatMessage("/textures/$$1.png", upgrade.getName()));
        System.out.println(ress);
        if (ress!= null) {
            img = new Image(Functions.formatMessage("textures/$$1.png", upgrade.getName()));
        } else {
            img = new Image("textures/noImgFound.png");
        }
        Pane space = new Pane();
        space.setMinWidth(100);
        ImageView UpgradeImg = new ImageView(img);
        UpgradeImg.setFitWidth(80);
        UpgradeImg.setFitHeight(80);
        UpgradeImg.setPreserveRatio(true);
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
                    moneyManager.removeMoney(calculateCost(upgrade));
                    buyUpgrade(upgrade);
                    refreshCost(upgrade);
                    quanLabel.setText(Functions.formatMessage("Quantity: $$1",getQuantityUpgrade(upgrade)));
                    // Update UI accordingly (this would be extended in a real implementation)
                }
            }
        });
        // Add components to the upgrade box
        HBox buttonContainer = new HBox(5);
        buttonContainer.getChildren().add(purchaseButton);
        buttonContainer.getStyleClass().add("button-container");
        upgradeBox.getChildren().addAll(nameLabel, quanLabel, costLabel, buttonContainer);
        rightPart.getChildren().addAll(upgradeBox, space, UpgradeImg);
        return rightPart;
    }

    public BigInteger calculateNewPrice(BigInteger price, double multiply, BigInteger upgradeNumber) {
        BigDecimal newPrice = new BigDecimal(price).multiply(new BigDecimal(upgradeNumber)).multiply(new BigDecimal(multiply));
        if (Objects.equals(upgradeNumber, BigInteger.valueOf(0))) {
            return price;
        } else {
            return newPrice.setScale(0, RoundingMode.CEILING).toBigInteger();
        }

    };

    public void refreshCost(AbstractUpgrade upgrade) {
        Label label = getCostLabels().get(upgrade);
        if (label != null) {
            label.setText(Functions.formatMessage("cost: $$1 potatoes", calculateCost(upgrade)));
        }
    }
    public String getQuantityUpgrade(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        for (Upgrade u : CounterHandler.getSave().upgrades) {
            if (upgrade.getName().equals(u.id)) {
                return u.quantity.toString();
            }
        }
        return "0";
    }
    public BigInteger calculateCost(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        return Arrays.stream(CounterHandler.getSave().upgrades)
                .filter(u -> upgrade.getName().equals(u.id))
                .findFirst()
                .map(u -> calculateNewPrice(upgrade.getBaseCost(), 1.7, u.quantity))
                .orElse(BigInteger.valueOf(-1));
    }

    public void buyUpgrade(AbstractUpgrade upgrade) {
        assert CounterHandler.getSave() != null;
        for (Upgrade u : CounterHandler.getSave().upgrades) {
            if (upgrade.getName().equals(u.id)) {
                u.quantity = u.quantity.add(BigInteger.valueOf(1));
                addUpgradeIncome(upgrade);
                break;
            }
        }
    }
}
