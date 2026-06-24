package net.nomUtiliser.potatoClicker.translations;

import net.minheur.potoflux.translations.AbstractTranslationsRegistry;
import net.nomUtiliser.potatoClicker.PotatoClicker;


public class ExampleModTranslations extends AbstractTranslationsRegistry {
    public ExampleModTranslations() {
        super(PotatoClicker.MOD_ID);
    }

    @Override
    protected void makeTranslation() {
        addClickerTab("name")
                .en("Potato Clicker");
        addClickerTab("title")
                .en("Welcome to Potato Clicker!");
    }

    // tabs helper
    private TranslationBuilder addClickerTab(String... children) {
        return addTab("clicker", children);
    }
}
