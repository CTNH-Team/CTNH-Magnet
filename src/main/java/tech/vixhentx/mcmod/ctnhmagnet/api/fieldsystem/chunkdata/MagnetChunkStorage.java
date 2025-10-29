package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import it.unimi.dsi.fastutil.longs.*;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.LevelChunk;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

public class MagnetChunkStorage implements IMagnetChunkStorage {
    private long[] rawSources;
    @Getter
    private final Long2ObjectMap<MagnetVector> magnetFields = new Long2ObjectOpenHashMap<>();
    @Getter
    private final MagnetProviderSet sources ;
    private final LevelChunk chunk;

    public MagnetChunkStorage(LevelChunk chunk) {
        this.chunk = chunk;
        sources = new MagnetProviderSet(chunk);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putLongArray("sources", sources.toRawProviders());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        rawSources = nbt.getLongArray("sources");
    }

    @Override
    public void markDirty() {
        chunk.setUnsaved(true);
    }

    @Override
    public void loadSources() {
        sources.initFromRawProviders(rawSources);
        rawSources = null;
        //spread all
        sources.getAllProviders().forEach(IMagnetProvider::spread);
    }
}
