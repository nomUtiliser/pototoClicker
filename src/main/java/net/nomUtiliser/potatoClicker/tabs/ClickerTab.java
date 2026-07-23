package net.nomUtiliser.potatoClicker.tabs;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
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
import net.nomUtiliser.potatoClicker.logic.SaveHandler;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import net.nomUtiliser.potatoClicker.utility.MoneyManager;
import net.nomUtiliser.potatoClicker.utility.UpgradeManager;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;

public class ClickerTab extends BaseVTab<VBox> {
    // link utility func
    private final UpgradeManager upgradeManager;
    private final MoneyManager moneyManager;
    private final SaveHandler SaveHandler;
    public ClickerTab() throws InterruptedException {
        upgradeManager = new UpgradeManager(this);
        moneyManager = new MoneyManager(this);
        SaveHandler = new SaveHandler(this);
        upgradeManager.addUpgrades();
    }
    // right part
    private Pane pototoImgPane;
    private ImageView potatoImg;
    //left part
    private ScrollPane scrollPane;
    private VBox upgradesContainer;
    //center part
    private Label pototoPerSec;
    private Label moneyPanel;
    //money INt...
    private Map<AbstractUpgrade, Label> costLabels;
    private Map<String, ScheduledExecutorService> schedulersMap;
    private BigInteger pototoPerSecInt;
    // get for other java class
    public Map<AbstractUpgrade, Label> getCostLabels() {
        if (costLabels == null) {
            costLabels = new HashMap<>();
        }
        return costLabels;
    }
    public Map<String, ScheduledExecutorService> getSchedulersMap() {
        return schedulersMap;
    }
    public Label getPototoPerSec() {
        return pototoPerSec;
    }
    public VBox getUpgradesContainer() {
        return upgradesContainer;
    }
    public BigInteger getPototoPerSecInt() {
        return pototoPerSecInt;
    }
    public void setPototoPerSecInt(BigInteger value) {
        this.pototoPerSecInt = value;
    }
    // instantiate
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    public void setMoneyPanelPototo(String text) {
        moneyPanel.setText(text);
    }
    @Override
    protected void setPanel() {
        // create hashmap to stock scheduler
        schedulersMap = new HashMap<>();
        //create upgradesContainer
        upgradesContainer = new VBox(10);
        // Set container size to ensure adequate height
        upgradesContainer.setPrefHeight(1000);
        upgradesContainer.setPrefWidth(200);
        upgradesContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        // create potato per sec display label
        pototoPerSec = new Label("O Potatoes/s");
        pototoPerSec.setPrefSize(300, 20);
        pototoPerSec.setMaxSize(300, 20);
        pototoPerSec.setMinSize(300,20);
        // create scroll pane
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
        }
        // create moneyPanel to display you money
        moneyPanel = new Label();
        moneyPanel.setMaxSize(100, 60);
        moneyPanel.setPrefSize(100, 30);
        moneyPanel.getStyleClass().add("moneyPanel");
        if (CounterHandler.getSave()== null) return;
        moneyPanel.setText(Functions.formatMessage("$$1 potatoes", CounterHandler.getSave().potatoCount));

        // Create HBox to put ScrollPane on the right side with proper stretching
        pototoImgPane = new Pane();
        pototoImgPane.getChildren().add(potatoImg);
        //create mainContainer to put 3 part
        HBox mainContainer = new HBox();
        mainContainer.setMinHeight(400);
        mainContainer.getChildren().addAll(pototoImgPane, moneyPanel, scrollPane);
        mainContainer.setSpacing(10);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setMaxSize(700, Double.MAX_VALUE);
        // config HBox
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(potatoImg, Priority.NEVER);
        HBox.setHgrow(moneyPanel, Priority.NEVER);
        // add everything to the VBOx
        vContent.getChildren().addAll(pototoPerSec, mainContainer);
        //display animation and add money when clicked
        potatoImg.setOnMouseClicked(e -> {
            moneyManager.addMoney(BigInteger.valueOf(1));
            moneyManager.potatoClicked(pototoImgPane, e.getX(), e.getY());
        });
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