package net.nomUtiliser.potatoClicker.logic.data;

import com.google.gson.annotations.SerializedName;

public class Save {
    @SerializedName("potatoes")
    public int potatoCount;

    public Upgrade[] upgrades;
}
