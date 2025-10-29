package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Unmodifiable;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public sealed abstract class AbstractMagnetFieldManager permits MagnetFieldManagerClient, MagnetFieldManagerServer {
    final Level level;

    protected AbstractMagnetFieldManager(Level level) {
        this.level = level;
    }

    /// /////////////////////////////
    ///           Chunk           ///
    /// /////////////////////////////
    public abstract Long2ObjectMap<MagnetVector> getChunkMagnetFields(LevelChunk chunk);
    public abstract Long2ObjectMap<MagnetVector> getChunkMagnetFields(BlockPos pos);
    public abstract Long2ObjectMap<MagnetVector> getChunkMagnetFields(int x, int z);
    public abstract Long2ObjectMap<MagnetVector> getChunkMagnetFields(ChunkPos chunkPos);

    /// /////////////////////////////
    ///           Multi           ///
    /// /////////////////////////////

    public abstract void setMagnetFields(Long2ObjectMap<MagnetVector> toSets);
    public abstract Long2ObjectMap<MagnetVector> getMagnetFields(LongList pos);
    public abstract void accumulateMagnetFields(Long2ObjectMap<MagnetVector> toAdds);
    public abstract void dispersalMagnetFields(Long2ObjectMap<MagnetVector> toSubs);

    /// /////////////////////////////
    ///           Single          ///
    /// /////////////////////////////

    public abstract MagnetVector getMagnetField(BlockPos pos);
    public abstract void setMagnetField(BlockPos pos, MagnetVector magnetVector);
    public abstract void accumulateMagnetField(BlockPos pos, MagnetVector toAdd);
    public abstract void dispersalMagnetField(BlockPos pos, MagnetVector toSub);


    /// //////////////////////////////
    ///           Utils           ///
    /// /////////////////////////////

    @ParametersAreNonnullByDefault
    public static void accumulate(@Unmodifiable Long2ObjectMap<MagnetVector> in, Long2ObjectMap<MagnetVector> dest){
        for(Long2ObjectMap.Entry<MagnetVector> entry : in.long2ObjectEntrySet()){
            dest.computeIfAbsent(entry.getLongKey(), __->new MagnetVector())
                    .add(entry.getValue());
        }
    }
    @ParametersAreNonnullByDefault
    public static void dispersal(@Unmodifiable Long2ObjectMap<MagnetVector> in, Long2ObjectMap<MagnetVector> dest){
        for(Long2ObjectMap.Entry<MagnetVector> entry : in.long2ObjectEntrySet()){
            dest.computeIfAbsent(entry.getLongKey(), __->new MagnetVector())
                    .sub(entry.getValue());
        }
    }
}
