package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread.MagnetFieldSpreader;

public interface IMagnetProvider {
    @Unmodifiable
    Long2ObjectMap<MagnetVector> getMagnetSources();
    @Getter
    MagnetFieldSpreader getSpreader();
    int getPriority();
    Level getLevel();
    BlockPos getPos();
    @NotNull @Unmodifiable
    default Long2ObjectMap<MagnetVector> spread(){
        return getSpreader().spread();
    }
    @NotNull @Unmodifiable
    default Long2ObjectMap<MagnetVector> unspread(){
        return getSpreader().unspread();
    }
}
