package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.client;

import net.minecraftforge.event.level.ChunkEvent;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.MagnetChunkClientCache;

public class ChunkCacheEventHandler {
    public static void onChunkUnload(ChunkEvent.Unload event) {
        if(!event.getLevel().isClientSide()) return;

        MagnetChunkClientCache.clearChunk(event.getChunk().getPos());
    }
}
