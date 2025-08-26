package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Unmodifiable;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.MagnetChunkClientCache;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.C2S.ChunkMagnetFieldRequestPacket;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

import static tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking.NETWORK;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
final class MagnetFieldManagerClient extends AbstractMagnetFieldManager {
    MagnetFieldManagerClient(Level level) {
        super(level);
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(ChunkPos chunkPos) {
        var storage = MagnetChunkClientCache.getChunk(chunkPos);
        if(storage != null) return storage;

        //null means unsynced
        var packet = new ChunkMagnetFieldRequestPacket();
        packet.chunkToRequest = chunkPos;
        NETWORK.sendToServer(packet);

        return new Long2ObjectOpenHashMap<>();
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(int x, int z) {
        return getChunkMagnetFields(new ChunkPos(x,z));
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(BlockPos pos) {
        return getChunkMagnetFields(new ChunkPos(pos));
    }

    //Not Recommended
    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(LevelChunk chunk) {
        return getChunkMagnetFields(chunk.getPos());
    }

    //only for receiving packet
    @Override
    public void setMagnetFields(List<Long2ObjectMap.Entry<MagnetVector>> magnetFields) {
        for(var entry : magnetFields){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            ChunkPos chunkPos = new ChunkPos(pos);
            var vec = entry.getValue();

            var storage = MagnetChunkClientCache.getChunk(chunkPos);

            //null means unsynced, no need to update
            if(storage!=null) storage.put(entry.getLongKey(), vec);
        }
    }

    @Override
    public @Unmodifiable List<Long2ObjectMap.Entry<MagnetVector>> getMagnetFields(LongList pos) {
        List<Long2ObjectMap.Entry<MagnetVector>> ret = new ArrayList<>();
        for(long p : pos){
            var chunkStorage = getChunkMagnetFields(BlockPos.of(p));
            ret.add(new AbstractLong2ObjectMap.BasicEntry<>(p,chunkStorage.getOrDefault(p, MagnetVector.ZERO)));
        }
        return ret;
    }
}
