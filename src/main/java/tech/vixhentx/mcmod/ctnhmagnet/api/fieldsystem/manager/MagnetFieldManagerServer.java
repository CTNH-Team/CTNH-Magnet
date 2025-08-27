package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetValues;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.IMagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.MagnetFieldSyncPacket;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

import javax.annotation.ParametersAreNonnullByDefault;

import static tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking.NETWORK;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
final class MagnetFieldManagerServer extends AbstractMagnetFieldManager {
    MagnetFieldManagerServer(Level level) {
        super(level);
    }

    IMagnetChunkStorage getMagnetFieldStorage(LevelChunk chunk){
        return chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE).resolve()
                .orElseThrow(()->new IllegalStateException("MagnetChunkStorage Capability not present!"));
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(LevelChunk chunk) {
        return getMagnetFieldStorage(chunk).getMagnetFields();
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(BlockPos pos) {
        return getChunkMagnetFields(level.getChunkAt(pos));
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(int x, int z) {
        return getChunkMagnetFields(level.getChunk(x,z));
    }

    @Override
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(ChunkPos chunkPos) {
        return getChunkMagnetFields(level.getChunk(chunkPos.x,chunkPos.z));
    }

    @Override
    public void setMagnetFields(Long2ObjectMap<MagnetVector> magnetFields) {
        for(var entry : magnetFields.long2ObjectEntrySet()){
            var pos = BlockPos.of(entry.getLongKey());
            IMagnetChunkStorage chunkStorage = getMagnetFieldStorage(level.getChunkAt(pos));
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
    public Long2ObjectMap<MagnetVector> getMagnetFields(LongList pos) {
        Long2ObjectMap<MagnetVector> ret = new Long2ObjectOpenHashMap<>();
        for(long p : pos){
            var chunkStorage = getChunkMagnetFields(BlockPos.of(p));
            var vec = chunkStorage.getOrDefault(p,new MagnetVector());
            ret.put(p, vec);
        }
        return ret;
    }

    @Override
    public void accumulateMagnetFields(Long2ObjectMap<MagnetVector> toAdds) {
        Long2ObjectMap<MagnetVector> changes = new Long2ObjectOpenHashMap<>();
        for(var entry : toAdds.long2ObjectEntrySet()){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            MagnetVector vec = entry.getValue();

            var storage = getMagnetFieldStorage(level.getChunkAt(pos));
            MagnetVector target = storage.getMagnetFields().computeIfAbsent(pos.asLong(),l->new MagnetVector());
            target.add(vec);

            if(target.length() < MagnetValues.MagMin){
                target = MagnetVector.ZERO;
                storage.getMagnetFields().remove(pos.asLong());
            }

            storage.markDirty();

            changes.put(pos.asLong(),target);
        }

        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = changes;
        NETWORK.sendToAll(packet);
    }

    @Override
    public void dispersalMagnetFields(Long2ObjectMap<MagnetVector> toSubs) {
        Long2ObjectMap<MagnetVector> changes = new Long2ObjectOpenHashMap<>();
        for(var entry : toSubs.long2ObjectEntrySet()){
            BlockPos pos = BlockPos.of(entry.getLongKey());
            MagnetVector vec = entry.getValue();

            var storage = getMagnetFieldStorage(level.getChunkAt(pos));
            MagnetVector target = storage.getMagnetFields().computeIfAbsent(pos.asLong(),l->new MagnetVector());
            target.sub(vec);

            if(target.length() < MagnetValues.MagMin){
                target = MagnetVector.ZERO;
                storage.getMagnetFields().remove(pos.asLong());
            }
            storage.markDirty();

            changes.put(pos.asLong(),target);
        }

        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = changes;
        NETWORK.sendToAll(packet);
    }

    @Override
    public MagnetVector getMagnetField(BlockPos pos) {
        return getChunkMagnetFields(pos).getOrDefault(pos.asLong(),new MagnetVector());
    }

    @Override
    public void setMagnetField(BlockPos pos, MagnetVector magnetVector) {
        var storage = getMagnetFieldStorage(level.getChunkAt(pos));
        if(!(magnetVector.length() < MagnetValues.MagMin))
            storage.getMagnetFields().put(pos.asLong(),magnetVector);
        else
            storage.getMagnetFields().remove(pos.asLong());
        storage.markDirty();
        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = new Long2ObjectArrayMap<>(1);
        packet.changes.put(pos.asLong(),magnetVector);
        NETWORK.sendToAll(packet);
    }

    @Override
    public void accumulateMagnetField(BlockPos pos, MagnetVector toAdd) {
        var storage = getMagnetFieldStorage(level.getChunkAt(pos));
        var target = storage.getMagnetFields().computeIfAbsent(pos.asLong(),l->new MagnetVector());
        target.add(toAdd);

        if(target.length() < MagnetValues.MagMin){
            target = MagnetVector.ZERO;
            storage.getMagnetFields().remove(pos.asLong());
        }

        storage.markDirty();
        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = new Long2ObjectArrayMap<>(1);
        packet.changes.put(pos.asLong(),target);
        NETWORK.sendToAll(packet);
    }

    @Override
    public void dispersalMagnetField(BlockPos pos, MagnetVector toSub) {
        var storage = getMagnetFieldStorage(level.getChunkAt(pos));
        var target = storage.getMagnetFields().computeIfAbsent(pos.asLong(),l->new MagnetVector());
        target.sub(toSub);

        if(target.length() < MagnetValues.MagMin){
            target = MagnetVector.ZERO;
            storage.getMagnetFields().remove(pos.asLong());
        }

        storage.markDirty();
        //Sync
        var packet = new MagnetFieldSyncPacket();
        packet.changes = new Long2ObjectArrayMap<>(1);
        packet.changes.put(pos.asLong(),target);
        NETWORK.sendToAll(packet);
    }
}
