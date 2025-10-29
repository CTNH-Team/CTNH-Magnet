package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.IMagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.api.utils.CapInfoUtils;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

public class ChunkInitialSpreadHandler {
    public static void onChunkLoad(ChunkEvent.Load event){
        if(event.getLevel().isClientSide() || event.getPhase() != EventPriority.LOWEST) return;
        final LevelChunk chunk = (LevelChunk) event.getChunk();
        chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE)
                .ifPresent(IMagnetChunkStorage::loadSources);
    }
}
