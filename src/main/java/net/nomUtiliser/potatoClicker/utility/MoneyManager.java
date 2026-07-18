package net.nomUtiliser.potatoClicker.utility;

import javafx.application.Platform;
import net.minheur.potoflux.Functions;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.logic.data.Upgrade;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;
import net.nomUtiliser.potatoClicker.upgrades.Clicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MoneyManager {
    private final ClickerTab clicker;

    public MoneyManager(ClickerTab clicker) {
        this.clicker = clicker;
    }

    public void calPototoPerSec(List<AbstractUpgrade> allUpgrade) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> task= scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                try {
                    clicker.setPototoPerSecInt(BigInteger.valueOf(0));
                    for (AbstractUpgrade upgrade : allUpgrade) {
                        for (Upgrade u : CounterHandler.getSave().upgrades) {
                            if (upgrade.getName().equals(u.id)) {
                                clicker.setPototoPerSecInt(clicker.getPototoPerSecInt().add(upgrade.getBaseIncome().multiply(u.quantity)));
                            }
                        }
                    }
                    clicker.getPototoPerSec().setText(Functions.formatMessage("$$1 Potatoes/S", getPototoUnits(clicker.getPototoPerSecInt())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }, 0, 100, TimeUnit.MILLISECONDS);
        clicker.getSchedulersMap().put("pototoPerSecInt", task);
    }

    public String getPototoUnits (BigInteger potato) {
        BigDecimal money = new BigDecimal(potato);
        String prefix = "";
        if (potato.compareTo(BigInteger.valueOf(1_000_000_000)) >= 0) {
            money = money.divide(BigDecimal.valueOf(1_000_000_000), 1 , RoundingMode.DOWN);
            prefix= "B";
        } else if (potato.compareTo(BigInteger.valueOf(1_000_000)) >= 0) {
            money = money.divide(BigDecimal.valueOf(1_000_000), 1 , RoundingMode.DOWN);
            prefix= "M";
        } else if (potato.compareTo(BigInteger.valueOf(1_000)) >= 0) {
            money = money.divide(BigDecimal.valueOf(1_000), 1, RoundingMode.DOWN);
            prefix= "K";
        }
        String moneyText = Functions.formatMessage("$$1$$2", money, prefix);
        return moneyText;
    }

    public void addMoney(BigInteger addedMoneyAmount) {
        if (CounterHandler.getSave() == null) return;
        CounterHandler.getSave().potatoCount =CounterHandler.getSave().potatoCount.add(addedMoneyAmount);
        clicker.setMoneyPanelPototo(Functions.formatMessage("$$1 potatoes", getPototoUnits(CounterHandler.getSave().potatoCount)));
    }

    public void removeMoney(BigInteger rmMoneyAount) {
        assert CounterHandler.getSave() != null;
        CounterHandler.getSave().potatoCount = CounterHandler.getSave().potatoCount.subtract(rmMoneyAount);
        clicker.setMoneyPanelPototo(Functions.formatMessage("$$1 potatoes", getPototoUnits(CounterHandler.getSave().potatoCount)));
    }
}
