package net.nomUtiliser.potatoClicker.logic.data;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public class Save {
    @SerializedName("potatoes")
    public BigInteger potatoCount;

    public Upgrade[] upgrades;

    public static @NotNull Save mkNewEmptySave() {
        Save s = new Save();
        s.potatoCount = BigInteger.valueOf(0);
        s.upgrades = new Upgrade[]{};
        return s;
    }
}
