package tech.vixhentx.mcmod.ctnhmagnet.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.utils.MagnetFieldManager;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

public class BlockEventHandler {
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();
        BlockEntity be = level.getBlockEntity(pos);
        if(!(be instanceof IMagnetProvider provider) || (!be.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_PROVIDER).isPresent())) return;
        MagnetFieldManager.setMagnetField(pos,level,provider.getMagnetField());
    }

    public static void onBlockBreak(BlockEvent.BreakEvent event) {

    }
}
