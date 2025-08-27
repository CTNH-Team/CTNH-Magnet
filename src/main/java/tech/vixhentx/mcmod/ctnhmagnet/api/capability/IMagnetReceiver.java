package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector;

public interface IMagnetReceiver {
    default BlockEntity getHolder(){
        return (BlockEntity) this;
    }
    default BlockPos getPos(){
        return getHolder().getBlockPos();
    }
    default Level getLevel(){
        return getHolder().getLevel();
    }

    default MagnetVector getMagnetField(){
        return MagnetFieldManagerSelector.getManager(getLevel()).getMagnetField(getPos());
    }
    default float getStrength(){
        return getMagnetField().length();
    }
}
