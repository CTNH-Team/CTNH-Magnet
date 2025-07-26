package tech.vixhentx.mcmod.ctnhmagnet.common.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.chunk.LevelChunk;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetChunkStorage;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;

public class MagnetChunkStorage implements IMagnetChunkStorage {
    @Getter
    private final Long2ObjectMap<MagnetVector> magnetFields = new Long2ObjectOpenHashMap<>();
    private final LevelChunk chunk;

    public MagnetChunkStorage(LevelChunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void setMagnetField(BlockPos pos, MagnetVector magnetVector) {
        IMagnetChunkStorage.super.setMagnetField(pos, magnetVector);
        chunk.setUnsaved(true);
    }

    @Override
    public void removeMagnetField(BlockPos pos) {
        IMagnetChunkStorage.super.removeMagnetField(pos);
        chunk.setUnsaved(true);
    }

    @Override
    public void clear() {
        IMagnetChunkStorage.super.clear();
        chunk.setUnsaved(true);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Long2ObjectMap.Entry<MagnetVector> entry : magnetFields.long2ObjectEntrySet()) {
            CompoundTag fieldTag = new CompoundTag();
            fieldTag.putLong("pos", entry.getLongKey());
            fieldTag.put("magnetVector", entry.getValue().serializeNBT());
            listTag.add(fieldTag);
        }
        nbt.put("magnetFields", listTag);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        magnetFields.clear();
        if(nbt.contains("magnetFields",ListTag.TAG_LIST)){
            ListTag listTag = nbt.getList("magnetFields", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag fieldTag = listTag.getCompound(i);
                long pos = fieldTag.getLong("pos");
                MagnetVector magnetVector = MagnetVector.fromNBT(fieldTag.getCompound("magnetVector"));
                magnetFields.put(pos, magnetVector);
            }
        }
    }
}
