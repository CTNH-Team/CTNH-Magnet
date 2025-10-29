package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

import static tech.vixhentx.mcmod.ctnhmagnet.api.utils.CapInfoUtils.getMagnetProvider;

public class ProviderChangeEvent {
    public static void onAddProvider(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        getMagnetProvider(level,pos).ifPresent(provider -> {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE).resolve().get()
                    .getSources().addProvider(provider);
        });
    }
    public static void onRemoveProvider(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        getMagnetProvider(level,pos).ifPresent(provider -> {
            LevelChunk chunk = level.getChunkAt(pos);
            chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE).resolve().get()
                    .getSources().removeProvider(provider);
        });
    }
}
