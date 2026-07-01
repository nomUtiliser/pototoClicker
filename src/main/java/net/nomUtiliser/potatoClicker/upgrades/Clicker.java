package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;

import java.math.BigInteger;

public class Clicker extends AbstractUpgrade {
    public Clicker() {
        super(BigInteger.valueOf(10));
    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation(PotatoClicker.MOD_ID, "clicker");
    }
}
