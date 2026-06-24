package your.packagename.tabs;

import net.minheur.potoflux.loader.mod.events.RegisterTabsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.screen.tabs.Tab;
import net.minheur.potoflux.utils.SmartSupplier;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import your.packagename.ExampleMod;
import your.packagename.tabs.all.YourTabClass;

public class Tabs {
    private static final RegistryList<Tab> LIST = new RegistryList<>();

    // example tab
    public static final SmartSupplier<Tab> MY_TAB = LIST.add(() -> new Tab(new ResourceLocation(ExampleMod.MOD_ID, "your_tab_id"), YourTabClass.class));

    public static void register(RegisterTabsEvent event) {
        LIST.register(event.reg);
    }
}
