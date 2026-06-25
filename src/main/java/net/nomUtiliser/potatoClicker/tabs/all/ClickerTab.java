package net.nomUtiliser.potatoClicker.tabs.all;

import javafx.scene.layout.VBox;
import net.minheur.potoflux.Functions;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;
import java.awt.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ClickerTab extends BaseVTab<VBox> {
    private Button potato;
    @Override
    protected void instantiate() {
        PANEL = new VBox();
    }
    protected int money;
    private Label moenyPanel;
    @Override
    protected void setPanel() {
        potato= new Button();
        potato.setMaxSize(250, 60);
        potato.setPrefSize(250, 30);
        potato.setText("patate");
        moenyPanel= new Label();
        moenyPanel.setMaxSize(250, 60);
        moenyPanel.setPrefSize(250, 30);
        moenyPanel.setText("0 patate");
        vContent.getChildren().addAll(potato,moenyPanel);
        potato.setOnAction(e-> addMoney() );
    }
    private void setUP() {

    }
    private void addMoney() {
        money++;
        moenyPanel.setText(Functions.formatMessage("$$1 patate", money));
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
