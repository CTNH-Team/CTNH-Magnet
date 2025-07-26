package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;

public interface IMagnetChunkStorage extends INBTSerializable<CompoundTag> {
    Long2ObjectMap<MagnetVector> getMagnetFields();
    default void setMagnetField(BlockPos pos, MagnetVector magnetVector){
        getMagnetFields().put(pos.asLong(), magnetVector);
    }
    default void removeMagnetField(BlockPos pos){
        getMagnetFields().remove(pos.asLong());
    }
    default MagnetVector getMagnetField(BlockPos pos){
        return getMagnetFields().getOrDefault(pos.asLong(),new MagnetVector());
    }
    default void clear(){
        getMagnetFields().clear();
    }
}
