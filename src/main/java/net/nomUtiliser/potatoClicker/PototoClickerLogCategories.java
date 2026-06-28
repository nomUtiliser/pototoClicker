package net.nomUtiliser.potatoClicker;

import net.minheur.potoflux.logger.ILogCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PototoClickerLogCategories implements ILogCategory {
    POTOTO_CLICKER("PototoClicker"),
    SAVE(POTOTO_CLICKER, "save");

    private final String code;
    private final String[] more;

    PototoClickerLogCategories(String code, String... more) {
        this.code = code;
        this.more = more;
    }

    PototoClickerLogCategories(ILogCategory parent, String... more) {
        this.code = parent.code();
        List<String> allMore = new ArrayList<>();
        allMore.addAll(Arrays.stream(parent.more()).toList());
        allMore.addAll(Arrays.stream(more).toList());
        this.more = allMore.toArray(String[]::new);
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String[] more() {
        return more;
    }
}
