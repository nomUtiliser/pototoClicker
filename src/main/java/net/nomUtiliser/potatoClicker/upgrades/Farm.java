package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;

import java.math.BigInteger;

public class Farm extends AbstractUpgrade{
    public Farm() {
        super(BigInteger.valueOf(500), "Farm", BigInteger.valueOf(10));
    }


    @Override
    public ResourceLocation id() {
        return new ResourceLocation(PotatoClicker.MOD_ID, "farm");
    }
}
