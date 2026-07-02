package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.registry.IRegistryType;

import java.math.BigInteger;

public abstract class AbstractUpgrade implements IRegistryType {
    protected final BigInteger baseCost;
    protected final String name;

    protected AbstractUpgrade(BigInteger baseCost, String name) {
        this.baseCost = baseCost;
        this.name = name;
    }

    public BigInteger getBaseCost() {
        return baseCost;
    }

    public String getName() {
        return name;
    }
}
