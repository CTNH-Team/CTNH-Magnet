package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread.MagnetFieldSpreader;

public interface IMagnetProvider {
    float getStrength();
    MagnetVector getMagnetField();
    MagnetFieldSpreader getSpreader();
    default BlockEntity getHolder(){
        return (BlockEntity) this;
    }
    default Level getLevel(){
        return getHolder().getLevel();
    }
    default BlockPos getPos(){
        return getHolder().getBlockPos();
    }
    default void spread(){
        getSpreader().spread();
    }
    default void unspread(){
        getSpreader().unspread();
    }
}
