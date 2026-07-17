package net.nomUtiliser.potatoClicker;

import net.minheur.potoflux.PotoFlux;
import net.minheur.potoflux.loader.PotoFluxLoadingContext;
import net.minheur.potoflux.loader.mod.Mod;
import net.minheur.potoflux.loader.mod.ModEventBus;
import net.minheur.potoflux.loader.mod.events.RegisterLangEvent;
import net.minheur.potoflux.logger.LogCategories;
import net.minheur.potoflux.logger.PtfLogger;
import net.nomUtiliser.potatoClicker.actionRun.PototoClickerRun;
import net.nomUtiliser.potatoClicker.style.PototoStylesheets;
import net.nomUtiliser.potatoClicker.tabs.Tabs;
import net.nomUtiliser.potatoClicker.translations.ExampleModTranslations;
import net.nomUtiliser.potatoClicker.upgrades.reg.Events;
import net.nomUtiliser.potatoClicker.upgrades.reg.RegisterUpgradesEvent;
import net.nomUtiliser.potatoClicker.upgrades.reg.Upgrades;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Mod(modId = PotatoClicker.MOD_ID, version = "1.0", compatibleVersions = {"8.0.1"}, compatibleVersionUrl = "https://nomutiliser.github.io/nomutiliser/pototoClicker/version.json")
public class PotatoClicker {
    public static final String MOD_ID = "potatoClicker";
    public static final RegisterUpgradesEvent upgradesEvent = new RegisterUpgradesEvent();

    public PotatoClicker() {
        ModEventBus modEventBus = PotoFluxLoadingContext.get().getModEventBus();

        modEventBus.addListener(Upgrades::register);
        modEventBus.addListener(Events::register);
        modEventBus.addListener(PototoClickerRun::register);
        modEventBus.addListener(PototoStylesheets::register);
        modEventBus.addListener(Tabs::register);
        modEventBus.addListener(this::onRegisterLang);
    }

    private void onRegisterLang(RegisterLangEvent event) {
        event.registerLang(new ExampleModTranslations());
    }

    public static Path getModDir() {
        Path dir = PotoFlux.getModDataDir().resolve(MOD_ID);
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {}
        return dir;
    }

    public static String getVersion() {
        try {
            Properties props = new Properties();
            props.load(PotatoClicker.class.getResourceAsStream("/modVersion.properties"));

            return props.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Could not get version for mod " + MOD_ID, LogCategories.MOD_LOADER);
            return null;
        }
    }
}
