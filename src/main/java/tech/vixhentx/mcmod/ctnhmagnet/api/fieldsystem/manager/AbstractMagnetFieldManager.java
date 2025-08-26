package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import it.unimi.dsi.fastutil.longs.AbstractLong2ObjectMap.BasicEntry;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;

import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Unmodifiable;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;


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
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(BlockPos pos){
        return getChunkMagnetFields(level.getChunkAt(pos));
    }
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(int x, int z){
        return getChunkMagnetFields(level.getChunk(x,z));
    }
    public Long2ObjectMap<MagnetVector> getChunkMagnetFields(ChunkPos chunkPos){
        return getChunkMagnetFields(level.getChunk(chunkPos.x,chunkPos.z));
    }

    /// /////////////////////////////
    ///           Multi           ///
    /// /////////////////////////////

    public abstract void setMagnetFields(List<Entry<MagnetVector>> magnetFields);
    @Unmodifiable
    public abstract List<Entry<MagnetVector>> getMagnetFields(LongList pos);

    /// /////////////////////////////
    ///           Single          ///
    /// /////////////////////////////

    @Unmodifiable
    public MagnetVector getMagnetField(BlockPos pos){
        return getMagnetFields(LongList.of(pos.asLong())).get(0).getValue();
    }
    public void setMagnetField(BlockPos pos, MagnetVector magnetVector){
        setMagnetFields(List.of(new BasicEntry<>(pos.asLong(), magnetVector)));
    }
}
