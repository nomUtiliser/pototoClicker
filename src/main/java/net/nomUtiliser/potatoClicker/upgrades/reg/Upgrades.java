package net.nomUtiliser.potatoClicker.upgrades.reg;


import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import net.nomUtiliser.potatoClicker.upgrades.Clicker;
import org.jetbrains.annotations.NotNull;


public class Upgrades {
    public static final RegistryList<AbstractUpgrade> LIST = new RegistryList<>();

    public static final SmartSupplier<AbstractUpgrade> CLICKER = LIST.add(Clicker::new);

    public static void register(@NotNull RegisterUpgradesEvent event) {
        LIST.register(event.reg);
    }
}
