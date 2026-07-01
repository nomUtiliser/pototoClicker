package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.registry.IRegistryType;

import java.math.BigInteger;

public abstract class AbstractUpgrade implements IRegistryType {
    protected final BigInteger baseCost;

    protected AbstractUpgrade(BigInteger baseCost) {
        this.baseCost = baseCost;
    }
}
