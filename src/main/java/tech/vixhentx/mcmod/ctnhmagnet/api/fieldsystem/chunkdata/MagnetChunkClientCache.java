package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MagnetChunkClientCache {
    //Map<ChunkPos, Map<BlockPos, MagnetVector>>
    private static final Long2ObjectMap<Long2ObjectMap<MagnetVector>> cache = new Long2ObjectOpenHashMap<>();

    /// //////////////////////////
    ///       Map Global       ///
    /// /////////////////////////
    public static void clear(){
        cache.clear();
    }

    /// //////////////////////////
    ///          Chunk        ///
    /// /////////////////////////

    public static void addChunk(ChunkPos chunkPos, Long2ObjectMap<MagnetVector> magnetFields){
        cache.put(chunkPos.toLong(), magnetFields);
    }
    @Nullable
    public static Long2ObjectMap<MagnetVector> getChunk(ChunkPos chunkPos){
        return cache.getOrDefault(chunkPos.toLong(),null);
    }
    public static void clearChunk(ChunkPos chunkPos){
        cache.remove(chunkPos.toLong());
    }

}
