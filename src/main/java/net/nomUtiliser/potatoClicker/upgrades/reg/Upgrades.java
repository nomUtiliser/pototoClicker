package net.nomUtiliser.potatoClicker.upgrades.reg;


import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.utils.SmartSupplier;
import net.nomUtiliser.potatoClicker.upgrades.*;
import org.jetbrains.annotations.NotNull;


public class Upgrades {
    public static final RegistryList<AbstractUpgrade> LIST = new RegistryList<>();

    public static final SmartSupplier<AbstractUpgrade> CLICKER = LIST.add(Clicker::new);

    public static final SmartSupplier<AbstractUpgrade> TRACTEUR = LIST.add(Tracteur::new);

    public static final SmartSupplier<AbstractUpgrade> FARM = LIST.add(Farm::new);

    public static final SmartSupplier<AbstractUpgrade> SILO = LIST.add(Silo::new);

public static final SmartSupplier<AbstractUpgrade> GRAIN = LIST.add(Grain::new);

    public static void register(@NotNull RegisterUpgradesEvent event) {
        LIST.register(event.reg);
    }
}
