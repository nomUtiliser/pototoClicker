package net.nomUtiliser.potatoClicker.logic.data;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Save {
    @SerializedName("potatoes")
    public BigInteger potatoCount;

    public Upgrade[] upgrades;
}
