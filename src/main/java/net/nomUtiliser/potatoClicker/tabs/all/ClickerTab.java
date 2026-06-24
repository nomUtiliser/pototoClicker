package net.nomUtiliser.potatoClicker.tabs.all;

import javafx.scene.layout.VBox;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;
import java.awt.*;
import javafx.scene.control.Button;


public class ClickerTab extends BaseVTab<VBox> {
    private Button potato;
    @Override
    protected void instantiate() {
        PANEL = new VBox();
    }

    @Override
    protected void setPanel() {
        potato= new Button();
        potato.setMaxSize(250, 60);
        potato.setPrefSize(250, 30);
        potato.setText("🥔");
        vContent.getChildren().add(potato);
    }
    private void setUP() {

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
