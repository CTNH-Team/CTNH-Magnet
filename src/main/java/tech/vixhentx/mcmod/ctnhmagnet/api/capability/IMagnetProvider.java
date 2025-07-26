package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.common.field.MagnetFieldImpl;

public interface IMagnetProvider {
    int getOEt();
    void setOEt(int OEt);
    MagnetVector getMagnetField();
    default BlockEntity getHolder(){
        return (BlockEntity) this;
    }
    default BlockPos getPos(){
        return getHolder().getBlockPos();
    }
    default Level getLevel(){
        return getHolder().getLevel();
    }
    default void updateField(){
        MagnetFieldImpl.updateField(this);
    }
}
