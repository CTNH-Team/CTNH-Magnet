package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.MagnetChunkClientCache;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.C2S.ChunkMagnetFieldRequestPacket;

import javax.annotation.ParametersAreNonnullByDefault;

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
        if(level.getGameTime() % 10 == 0){
        var packet = new ChunkMagnetFieldRequestPacket();
        packet.chunkToRequest = chunkPos;
        NETWORK.sendToServer(packet);
        }

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
    public void setMagnetFields(Long2ObjectMap<MagnetVector> magnetFields) {
        for(var entry : magnetFields.long2ObjectEntrySet()){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            ChunkPos chunkPos = new ChunkPos(pos);
            var vec = entry.getValue();

            var storage = MagnetChunkClientCache.getChunk(chunkPos);

            //null means unsynced, no need to update
            if(storage!=null) storage.put(entry.getLongKey(), vec);
        }
    }

    @Override
    public Long2ObjectMap<MagnetVector> getMagnetFields(LongList pos) {
        Long2ObjectMap<MagnetVector> ret = new Long2ObjectOpenHashMap<>();
        for(long p : pos){
            var chunkStorage = getChunkMagnetFields(BlockPos.of(p));
            ret.put(p,chunkStorage.getOrDefault(p, new MagnetVector()));
        }
        return ret;
    }

    //only for receiving packet
    @Override
    public void accumulateMagnetFields(Long2ObjectMap<MagnetVector> toAdds) {
        for(var entry : toAdds.long2ObjectEntrySet()){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            MagnetVector vec = entry.getValue();
            getChunkMagnetFields(pos).computeIfAbsent(pos.asLong(),__->new MagnetVector()).add(vec);
        }
    }

    //only for receiving packet
    @Override
    public void dispersalMagnetFields(Long2ObjectMap<MagnetVector> toSubs) {
        for(var entry : toSubs.long2ObjectEntrySet()){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            MagnetVector vec = entry.getValue();
            getChunkMagnetFields(pos).computeIfAbsent(pos.asLong(),__->new MagnetVector()).sub(vec);
        }
    }

    //modifiable
    @Override
    public MagnetVector getMagnetField(BlockPos pos) {
        return getChunkMagnetFields(pos).computeIfAbsent(pos.asLong(),__->new MagnetVector());
    }

    @Override
    public void setMagnetField(BlockPos pos, MagnetVector magnetVector) {
        getChunkMagnetFields(pos).put(pos.asLong(), magnetVector);
    }

    @Override
    public void accumulateMagnetField(BlockPos pos, MagnetVector toAdd) {
        getMagnetField(pos).add(toAdd);
    }

    @Override
    public void dispersalMagnetField(BlockPos pos, MagnetVector toSub) {
        getMagnetField(pos).sub(toSub);
    }
}
