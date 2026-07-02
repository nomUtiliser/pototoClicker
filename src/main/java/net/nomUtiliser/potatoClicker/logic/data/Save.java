package net.nomUtiliser.potatoClicker.logic.data;

import com.google.gson.annotations.SerializedName;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;


public class Save {
    @SerializedName("potatoes")
    public BigInteger potatoCount;

    public Upgrade[] upgrades;

    public static @NotNull Save mkNewEmptySave() {
        Save s = new Save();
        List<AbstractUpgrade> allUpgrades = PotatoClicker.upgradesEvent.reg.getAll()
                .stream().sorted(
                        Comparator.comparing(
                                upgrade-> !upgrade.id().getNamespace().equals(PotatoClicker.MOD_ID)
                        )
                ).toList();
        for (AbstractUpgrade u : allUpgrades) {

        }
        s.potatoCount = BigInteger.valueOf(0);
        s.upgrades = new Upgrade[]{};
        return s;
    }
}
