package your.packagename.tabs.all;

import javafx.scene.layout.VBox;
import net.minheur.potoflux.screen.tabs.BaseVTab;
import net.minheur.potoflux.translations.Translations;

public class YourTabClass extends BaseVTab<VBox> {
    @Override
    protected void instantiate() {
        PANEL = new VBox();
    }

    @Override
    protected void setPanel() {
        // add here content
    }

    @Override
    protected String getTitle() {
        return Translations.get("yourmodid:tabs.yourTab.title");
    }

    @Override
    public String getName() {
        return Translations.get("yourmodid:tabs.yourTab.name");
    }
}
