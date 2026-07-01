package net.nomUtiliser.potatoClicker.upgrades.reg;

import net.minheur.potoflux.loader.mod.events.RegisterModEventsEvent;
import net.minheur.potoflux.loader.mod.post.ModEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import org.jetbrains.annotations.NotNull;

public class Events {
    public static final RegistryList<ModEvent> LIST = new RegistryList<>();

    public static final SmartSupplier<ModEvent> UPGRADE_EVENT = LIST.add(() -> new ModEvent(
            new ResourceLocation(PotatoClicker.MOD_ID, "upgrade"),
            PotatoClicker.upgradesEvent
    ));

    public static void register(@NotNull RegisterModEventsEvent event) {
        LIST.register(event.reg);
    }
}
