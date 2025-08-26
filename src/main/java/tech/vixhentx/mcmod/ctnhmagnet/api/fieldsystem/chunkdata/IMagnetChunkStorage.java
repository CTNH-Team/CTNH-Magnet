package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

public interface IMagnetChunkStorage extends INBTSerializable<CompoundTag> {
    void setMagnetFields(Long2ObjectMap<MagnetVector> magnetFields);
    Long2ObjectMap<MagnetVector> getMagnetFields();

    void markDirty();
}
