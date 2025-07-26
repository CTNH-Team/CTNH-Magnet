package tech.vixhentx.mcmod.ctnhmagnet.api.utils;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.*;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.*;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

public class MagnetFieldManager {
    /// /////////////////////////////
    ///        FieldHelper        ///
    /// /////////////////////////////
    @NotNull
    @ParametersAreNonnullByDefault
    public static Optional<IMagnetChunkStorage> getMagnetFieldStorageOptional(BlockPos pos, Level level){
        if(level instanceof ServerLevel){
            LevelChunk chunk = level.getChunkAt(pos);
            return chunk.getCapability(MagnetCapabilities.CAPABILITY_MAGNET_CHUNK_STORAGE).resolve();
        }
        return Optional.empty();
    }
    public static MagnetVector getMagnetField(BlockPos pos, Level level){
        var optional = getMagnetFieldStorageOptional(pos, level);
        if(optional.isPresent()){
            return optional.get().getMagnetField(pos);
        }
        return new MagnetVector();
    }
    public static void setMagnetField(BlockPos pos, Level level, MagnetVector magnetVector){
        var optional = getMagnetFieldStorageOptional(pos, level);
        optional.ifPresent(magnetChunkStorage -> magnetChunkStorage.setMagnetField(pos, magnetVector));
    }
    public static Long2ObjectMap<MagnetVector> getMagnetFields(BlockPos pos,Level level){
        var optional = getMagnetFieldStorageOptional(pos, level);
        if(optional.isPresent()){
            return optional.get().getMagnetFields();
        }
        else return new Long2ObjectOpenHashMap<>();
    }
}
