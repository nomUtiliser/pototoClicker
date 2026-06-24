package net.nomUtiliser.potatoClicker.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.utils.SmartSupplier;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.tabs.all.ClickerTab;

public class Tabs {
    private static final RegistryList<Tab> LIST = new RegistryList<>();

    // example tab
    public static final SmartSupplier<Tab> MY_TAB = LIST.add(() -> new Tab(new ResourceLocation(PotatoClicker.MOD_ID, "your_tab_id"), ClickerTab.class));

    public static void register(RegisterTabsEvent event) {
        LIST.register(event.reg);
    }
}
