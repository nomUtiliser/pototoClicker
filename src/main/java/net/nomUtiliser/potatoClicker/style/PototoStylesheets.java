package net.nomUtiliser.potatoClicker.style;

import net.minheur.potoflux.loader.mod.events.RegisterStylesheetsEvent;
import net.minheur.potoflux.registry.RegistryList;
import net.minheur.potoflux.styles.StylesheetEntry;
import net.minheur.potoflux.utils.SmartSupplier;
import net.minheur.potoflux.utils.ressourcelocation.ResourceLocation;
import net.nomUtiliser.potatoClicker.PotatoClicker;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PototoStylesheets {
    private static final RegistryList<StylesheetEntry> LIST = new RegistryList<>();

    public static final SmartSupplier<StylesheetEntry> MAIN = LIST.add(() -> new StylesheetEntry(
            new ResourceLocation(PotatoClicker.MOD_ID, "main"),
            buildExternal("/styles/main.css")
    ));

    public static String buildExternal(String target) {
        return Objects.requireNonNull(
                PototoStylesheets.class.getResource(target)
        ).toExternalForm();
    }

    public static void register(@NotNull RegisterStylesheetsEvent event) {
        LIST.register(event.reg);
    }
}
