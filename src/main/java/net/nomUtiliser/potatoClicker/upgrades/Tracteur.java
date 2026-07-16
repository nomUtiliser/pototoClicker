package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;

import java.math.BigInteger;

public class Tracteur extends AbstractUpgrade{
    public Tracteur() {
        super(BigInteger.valueOf(100), "Tracteur", BigInteger.valueOf(10));
    }
    @Override
    public ResourceLocation id() {
        return new ResourceLocation(PotatoClicker.MOD_ID, "tracteur");
    }
}
