package net.nomUtiliser.potatoClicker.upgrades;

import net.minheur.potoflux.registry.IRegistryType;

import java.math.BigInteger;

public abstract class AbstractUpgrade implements IRegistryType {
    protected final BigInteger baseCost;
    protected final String name;
    protected final BigInteger baseIncome;

    protected AbstractUpgrade(BigInteger baseCost, String name, BigInteger baseIncome) {
        this.baseCost = baseCost;
        this.name = name;
        this.baseIncome = baseIncome;
    }

    public BigInteger getBaseCost() {
        return baseCost;
    }

    public String getName() {
        return name;
    }

    public BigInteger getBaseIncome() {
        return baseIncome;
    }
}
