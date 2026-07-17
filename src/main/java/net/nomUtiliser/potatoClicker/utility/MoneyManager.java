package net.nomUtiliser.potatoClicker.utility;

import net.minheur.potoflux.Functions;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;


public class MoneyManager {
    ClickerTab clicker = new ClickerTab();

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
