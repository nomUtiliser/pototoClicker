package net.nomUtiliser.potatoClicker.tabs;

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
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import net.nomUtiliser.potatoClicker.utility.MoneyManager;
import net.nomUtiliser.potatoClicker.utility.UpgradeManager;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class ClickerTab extends BaseVTab<VBox> {
    private final UpgradeManager upgradeManager;
    private final MoneyManager moneyManager;
    public ClickerTab() throws InterruptedException {
        upgradeManager = new UpgradeManager(this);
        moneyManager = new MoneyManager(this);
        upgradeManager.addUpgrades();
    }
    private Map<AbstractUpgrade, Label> costLabels;

    public Map<AbstractUpgrade, Label> getCostLabels() {
        if (costLabels == null) {
            costLabels = new HashMap<>();
        }
        return costLabels;
    }
    public Map<String, ScheduledFuture<?>> getSchedulersMap() {
        return schedulersMap;
    }

    public Label getPototoPerSec() {
        return pototoPerSec;
    }
    public VBox getUpgradesContainer() {
        return upgradesContainer;
    }

    private ImageView potatoImg;
    private ScrollPane scrollPane;
    private VBox upgradesContainer;
    private Label pototoPerSec;
    private Map<String, ScheduledFuture<?>> schedulersMap;
    private BigInteger pototoPerSecInt;
    public BigInteger getPototoPerSecInt() {
        return pototoPerSecInt;
    }
    public void setPototoPerSecInt(BigInteger value) {
        this.pototoPerSecInt = value;
    }
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    private Label moneyPanel;
    public void setMoneyPanelPototo(String text) {
        moneyPanel.setText(text);
    }
    @Override
    protected void setPanel() {
        schedulersMap = new HashMap<>();
        upgradesContainer = new VBox(10);

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
        potatoImg.setOnMouseClicked(e -> moneyManager.addMoney(BigInteger.valueOf(1)));
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