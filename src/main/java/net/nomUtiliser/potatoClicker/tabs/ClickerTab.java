package net.nomUtiliser.potatoClicker.tabs;

import javafx.scene.layout.VBox;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;


public class ClickerTab extends BaseVTab<VBox> {
    private Button potato;
    @Override
    protected void instantiate() {
        PANEL = new VBox();
        PANEL.getStyleClass().add("pototoClicker");
    }

    private Label moenyPanel;
    @Override
    protected void setPanel() {
        potato= new Button();
        potato.getStyleClass().add("potatoSacred");
        potato.setMaxSize(250, 60);
        potato.setPrefSize(250, 30);
        potato.setText("patate");
        moenyPanel= new Label();
        moenyPanel.setMaxSize(250, 60);
        moenyPanel.setPrefSize(250, 30);
        moenyPanel.setText("0 patate");

        vContent.getChildren().addAll(potato,moenyPanel);
        potato.setOnAction(e-> addMoney(1) );
    }

    private void addMoney(int addedMoneyAmount) {
        if (CounterHandler.getSave() == null) return;
        CounterHandler.getSave().potatoCount += addedMoneyAmount;
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
