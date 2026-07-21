package net.nomUtiliser.potatoClicker.utility;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import net.minheur.potoflux.Functions;
import net.nomUtiliser.potatoClicker.logic.CounterHandler;
import net.nomUtiliser.potatoClicker.logic.data.Upgrade;
import net.nomUtiliser.potatoClicker.tabs.ClickerTab;
import net.nomUtiliser.potatoClicker.upgrades.AbstractUpgrade;

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
    private Boolean animIsOn= false;
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
        clicker.getSchedulersMap().put("pototoPerSecInt", scheduler);
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

    public void potatoClicked(Pane pane, double mouseX, double mouseY) {
        ImageView effect = new ImageView(new Image("textures/pototo.png"));
        effect.setFitWidth(50);
        effect.setFitHeight(50);


        double offsetX = Math.random() * 20 - 20;
        double offsetY = Math.random() * 20 - 20;
        double directionX = Math.random() * 100 - 50;
        double directionY = Math.random() * 100 - 50;
        effect.setLayoutX(mouseX + offsetX);
        effect.setLayoutY(mouseY + offsetY);

        pane.getChildren().add(effect);

        // Animation
        FadeTransition fade = new FadeTransition(Duration.millis(1000), effect);
        fade.setFromValue(1);
        fade.setToValue(0.2);

        TranslateTransition move = new TranslateTransition(Duration.millis(500), effect);
        move.setByY(directionY);
        move.setByX(directionX);

        ParallelTransition animation = new ParallelTransition(fade, move);

        animation.setOnFinished(e -> pane.getChildren().remove(effect));
        animation.play();

        if (animIsOn) return;
        ScaleTransition anim = new ScaleTransition(Duration.millis(500), pane);
        anim.setToX(1.2);
        anim.setToY(1.2);

        anim.setAutoReverse(true);
        anim.setCycleCount(2);
        anim.setInterpolator(Interpolator.EASE_OUT);
        anim.setOnFinished( e ->
                animIsOn = false
        );
        animIsOn = true;
        anim.play();

    }
}
