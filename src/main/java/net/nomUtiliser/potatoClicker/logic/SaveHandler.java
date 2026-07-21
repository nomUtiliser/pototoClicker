package net.nomUtiliser.potatoClicker.logic;

import net.minheur.potoflux.logger.PtfLogger;
import net.minheur.potoflux.utils.Json;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.PototoClickerLogCategories;
import net.nomUtiliser.potatoClicker.logic.data.Save;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

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
            CounterHandler.loadSave(save);
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
        clicker.getSchedulersMap();
        for (ScheduledExecutorService scheduledFuture : clicker.getSchedulersMap().values()) {
            scheduledFuture.shutdown();
        }

    }
}
