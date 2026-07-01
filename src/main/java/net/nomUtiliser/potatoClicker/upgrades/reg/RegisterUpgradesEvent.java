package net.nomUtiliser.potatoClicker.upgrades.reg;

import net.minheur.potoflux.loader.mod.events.IEvent;

public class RegisterUpgradesEvent implements IEvent {
    public final UpgradesRegistry reg = new UpgradesRegistry();

    @Override
    public void close() {
        reg.close();
    }
}
