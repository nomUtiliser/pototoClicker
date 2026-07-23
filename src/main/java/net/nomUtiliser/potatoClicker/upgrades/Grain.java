package net.nomUtiliser.potatoClicker.upgrades;


import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;

import java.math.BigInteger;

public class Grain extends AbstractUpgrade{
    public Grain() {
        super(BigInteger.valueOf(5000), "Grain de patate dorée", BigInteger.valueOf(35));
    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation(PotatoClicker.MOD_ID, "grain");
    }
}
