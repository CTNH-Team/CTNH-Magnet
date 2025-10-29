package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

public interface IMagnetChunkStorage extends INBTSerializable<CompoundTag> {
    MagnetProviderSet getSources();
    //long:实际磁场pos
    Long2ObjectMap<MagnetVector> getMagnetFields();

    //场源变化时调用,通知区块保存
    void markDirty();
    //区块加载时调用,将反序列化数据加载
    void loadSources();
}
