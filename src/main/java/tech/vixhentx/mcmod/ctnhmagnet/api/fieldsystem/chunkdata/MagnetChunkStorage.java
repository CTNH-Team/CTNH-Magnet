package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

public class MagnetChunkStorage implements IMagnetChunkStorage {
    @Getter @Setter @Nullable
    private Long2ObjectMap<MagnetVector> magnetFields = null;
    private final LevelChunk chunk;

    public MagnetChunkStorage(LevelChunk chunk) {
        this.chunk = chunk;
        if(!chunk.getLevel().isClientSide())
            magnetFields = new Long2ObjectOpenHashMap<>();
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

    @Override
    public void markDirty() {
        chunk.setUnsaved(true);
    }
}
