package net.nomUtiliser.potatoClicker.logic.data;

import com.google.gson.annotations.SerializedName;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;


public class Save {
    @SerializedName("potatoes")
    public BigInteger potatoCount;

    public Upgrade[] upgrades;
    static int i = 0;
    public static @NotNull Save mkNewEmptySave() {
        Save s = new Save();
        List<AbstractUpgrade> allUpgrades = PotatoClicker.upgradesEvent.reg.getAll()
                .stream().sorted(
                        Comparator.comparing(
                                upgrade-> !upgrade.id().getNamespace().equals(PotatoClicker.MOD_ID)
                        )
                ).toList();
        s.upgrades = new Upgrade[allUpgrades.size()];
        for (AbstractUpgrade u : allUpgrades) {
            s.upgrades[i] = new Upgrade();
            s.upgrades[i].id = u.getName();
            s.upgrades[i].quantity = 0;
            i++;
        }
        s.potatoCount = BigInteger.valueOf(0);
        return s;
    }
}
