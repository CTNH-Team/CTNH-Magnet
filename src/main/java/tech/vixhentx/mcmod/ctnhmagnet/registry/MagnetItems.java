package tech.vixhentx.mcmod.ctnhmagnet.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import tech.vixhentx.mcmod.ctnhmagnet.common.item.MagnetSightGlassItem;

import static tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetRegistration.REGISTRATE;

public class MagnetItems {
    public static void init(){

    }
    public static ItemEntry<MagnetSightGlassItem> MAGNET_SIGHT_GLASS = REGISTRATE.item("magnet_sight_glass", MagnetSightGlassItem::new)
            .register();
}
