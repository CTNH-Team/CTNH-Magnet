package tech.vixhentx.mcmod.ctnhmagnet.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IMagnetReceiver {
    default void setProvider(IMagnetProvider provider){}
    default IMagnetProvider getProvider(){return null;}
    default int getOEt(){
        return getProvider().getOEt();
    }
    default BlockEntity getHolder(){
        return (BlockEntity) this;
    }
    default BlockPos getPos(){
        return getHolder().getBlockPos();
    }
    default Level getLevel(){
        return getHolder().getLevel();
    }
}
