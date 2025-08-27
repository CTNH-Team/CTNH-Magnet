package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;

import static tech.vixhentx.mcmod.ctnhmagnet.api.utils.CapInfoUtils.getMagnetProvider;

public class ProviderChangeEvent {
    public static void onAddProvider(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        //TODO: impl spread logic
        getMagnetProvider(level,pos).ifPresent(IMagnetProvider::spread);
    }
    public static void onRemoveProvider(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        //TODO: impl unspread logic
        getMagnetProvider(level,pos).ifPresent(IMagnetProvider::unspread);
    }
}
