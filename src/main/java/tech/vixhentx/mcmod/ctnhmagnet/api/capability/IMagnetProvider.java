package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Unmodifiable;
import org.openjdk.nashorn.internal.objects.annotations.Getter;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread.MagnetFieldSpreader;

public interface IMagnetProvider {
    @Unmodifiable
    Long2ObjectMap<MagnetVector> getMagnetSources();
    @Getter
    MagnetFieldSpreader getSpreader();
    Level getLevel();
    default void spread(){
        getSpreader().spread();
    }
    default void unspread(){
        getSpreader().unspread();
    }
}
