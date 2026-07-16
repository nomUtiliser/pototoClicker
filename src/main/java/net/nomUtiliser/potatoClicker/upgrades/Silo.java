package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;

import java.math.BigInteger;

public class Silo extends AbstractUpgrade{
    public Silo() {
        super(BigInteger.valueOf(2000), "silo de patate", BigInteger.valueOf(25));
    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation(PotatoClicker.MOD_ID, "silo");
    }
}
