package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap.BasicEntry;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;

import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.IMagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.MagnetFieldSyncPacket;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking.NETWORK;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
final class MagnetFieldManagerServer extends AbstractMagnetFieldManager {
    MagnetFieldManagerServer(Level level) {
        super(level);
    }

    Optional<IMagnetChunkStorage> getMagnetFieldStorageOptional(LevelChunk chunk){
        return chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE).resolve();
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(LevelChunk chunk) {
        return getMagnetFieldStorageOptional(chunk).get().getMagnetFields();
    }

    @Override
    public void setMagnetFields(List<Entry<MagnetVector>> magnetFields) {
        for(var entry : magnetFields){
            var pos = BlockPos.of(entry.getLongKey());
            IMagnetChunkStorage chunkStorage = getMagnetFieldStorageOptional(level.getChunkAt(pos)).get();
            chunkStorage.markDirty();

            var storage = chunkStorage.getMagnetFields();
            storage.put(entry.getLongKey(),entry.getValue());
        }
        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = magnetFields;
        NETWORK.sendToAll(packet);
    }

    @Override
    public List<Entry<MagnetVector>> getMagnetFields(LongList pos) {
        List<Entry<MagnetVector>> ret = new ArrayList<>();
        for(long p : pos){
            var chunkStorage = getChunkMagnetFields(BlockPos.of(p));
            ret.add(new BasicEntry<>(p,chunkStorage.getOrDefault(p, MagnetVector.ZERO)));
        }
        return ret;
    }
}
