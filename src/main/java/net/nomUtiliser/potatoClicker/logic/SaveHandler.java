package net.nomUtiliser.potatoClicker.logic;

import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.Json;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.PototoClickerLogCategories;
import net.nomUtiliser.potatoClicker.logic.data.Save;
import net.nomUtiliser.potatoClicker.logic.data.Upgrade;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class SaveHandler {

    public static final Path savePath = PotatoClicker.getModDir().resolve("save.json");
    private static ClickerTab clicker = null;
    public SaveHandler(ClickerTab clicker) {
        SaveHandler.clicker = clicker;
    }
    public static void loadFromFile() {
        try {
            if (!Files.exists(savePath)) {
                PtfLogger.warning("No save file found!", PototoClickerLogCategories.SAVE);
                mkNewSave();
                return;
            }
            String content = Files.readString(savePath);
            Save save = Json.GSON.fromJson(content, Save.class);
            Save newSave = new Save();
            List<AbstractUpgrade> allUpgrades = PotatoClicker.upgradesEvent.reg.getAll()
                    .stream().sorted(
                            Comparator.comparing(
                                    upgrade-> !upgrade.id().getNamespace().equals(PotatoClicker.MOD_ID)
                            )
                    ).toList();
            newSave.upgrades = new Upgrade[allUpgrades.size()];
            int i = 0;
            for (AbstractUpgrade upgrade : allUpgrades) {
                boolean exist = false;
                for (Upgrade u : save.upgrades) {
                    if (upgrade.getName().equals(u.id)) {
                        newSave.upgrades[i] = new Upgrade();
                        newSave.upgrades[i].id = upgrade.getName();
                        newSave.upgrades[i].quantity = u.quantity;
                        exist = true;
                        break;
                    }
                }
                if (!exist)  {
                    System.out.println(upgrade.getName());
                    newSave.upgrades[i] = new Upgrade();
                    newSave.upgrades[i].id = upgrade.getName();
                    newSave.upgrades[i].quantity = BigInteger.valueOf(0);
                }
                i++;
            }
            CounterHandler.loadSave(newSave);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Failed to get save!", PototoClickerLogCategories.SAVE);
            CounterHandler.loadEmptySave();
        }
    }
    public static void mkNewSave() {
        CounterHandler.loadEmptySave();
        saveToFile();
    }
    public static void saveToFile() {
        Save save = CounterHandler.getSave();
        if (save == null) return;

        String content = Json.GSON.toJson(save);
        try {
            Files.writeString(savePath, content);
        } catch (IOException e) {
            e.printStackTrace();
            PtfLogger.error("Failed to create new file", PototoClickerLogCategories.SAVE);
        }
    }

    public static void closeScheduler() {
        for (ScheduledExecutorService scheduledFuture : clicker.getSchedulersMap().values()) {
            scheduledFuture.shutdown();
        }

    }
}
