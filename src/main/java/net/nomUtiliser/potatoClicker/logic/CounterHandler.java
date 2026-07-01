package net.nomUtiliser.potatoClicker.logic;

import net.nomUtiliser.potatoClicker.logic.data.Save;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CounterHandler {
    private static Save save;

    public static void loadSave(@NotNull Save save) {
        CounterHandler.save = save;
    }

    public static void loadEmptySave() {
        save = new Save();
    }

    public static @Nullable Save getSave() {
        return save;
    }
}
