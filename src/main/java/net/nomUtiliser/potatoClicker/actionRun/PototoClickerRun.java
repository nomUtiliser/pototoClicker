package net.nomUtiliser.potatoClicker.actionRun;

import net.minheur.potoflux.actionRuns.regs.ActionRun;
import net.minheur.potoflux.loader.mod.events.RegisterRunsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.logic.SaveHandler;
import org.jetbrains.annotations.NotNull;

public class PototoClickerRun {
    private static final RegistryList<ActionRun> LIST_CLOSE = new RegistryList<>();
    private static final RegistryList<ActionRun> LIST_START_LOGIC = new RegistryList<>();

    public static final SmartSupplier<ActionRun> SAVE_FILE = LIST_CLOSE.add(() -> new ActionRun(
            new ResourceLocation(PotatoClicker.MOD_ID, "save"), SaveHandler::saveToFile
    ));

    public static final SmartSupplier<ActionRun> LOAD_FILE = LIST_START_LOGIC.add(() -> new ActionRun(
            new ResourceLocation(PotatoClicker.MOD_ID, "load"), SaveHandler::loadFromFile
    ));

    public static void register(@NotNull RegisterRunsEvent event) {
        LIST_CLOSE.register(event.closeReg);
        LIST_START_LOGIC.register(event.startLogicReg);
    }
}
