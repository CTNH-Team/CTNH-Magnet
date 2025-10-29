package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector;

public abstract class MagnetFieldSpreader {
    final IMagnetProvider provider;
    //derivedFields with itself
    Long2ObjectMap<MagnetVector> derivedFields;

    public MagnetFieldSpreader(IMagnetProvider provider) {
        this.provider = provider;
    }
    public Long2ObjectMap<MagnetVector> getDerivedFields() {
        if(derivedFields != null) return derivedFields;
         //calculate if absent
        derivedFields = new Long2ObjectOpenHashMap<>();

        for(var entry : provider.getMagnetSources().long2ObjectEntrySet())
            deriveSingle(BlockPos.of(entry.getLongKey()), entry.getValue())
                    .forEach((l,v)->{
                        derivedFields.computeIfAbsent(l,__->new MagnetVector())
                                .add(v);
                    });

        return derivedFields;
    }
    public abstract Long2ObjectMap<MagnetVector> deriveSingle(BlockPos sourcePos, MagnetVector sourceField);
    @NotNull @Unmodifiable
    final public Long2ObjectMap<MagnetVector> spread() {
        MagnetFieldManagerSelector.getManager(provider.getLevel()).accumulateMagnetFields(getDerivedFields());
        return derivedFields;
    }
    @NotNull @Unmodifiable
    final public Long2ObjectMap<MagnetVector> unspread() {
        MagnetFieldManagerSelector.getManager(provider.getLevel()).dispersalMagnetFields(getDerivedFields());
        var ret =derivedFields;
        derivedFields = null;
        return ret;
    }
}
