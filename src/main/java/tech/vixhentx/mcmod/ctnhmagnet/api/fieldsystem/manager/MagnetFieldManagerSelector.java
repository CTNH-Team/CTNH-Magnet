package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagnetFieldManagerSelector {
    public static AbstractMagnetFieldManager getManager(Level level){
        return level.isClientSide ? new MagnetFieldManagerClient(level) : new MagnetFieldManagerServer(level);
    }
    public static AbstractMagnetFieldManager getServerManager(Level level){
        return new MagnetFieldManagerServer(level);
    }
    @OnlyIn(Dist.CLIENT)
    public static AbstractMagnetFieldManager getClientManager(Level level){
        return new MagnetFieldManagerClient(level);
    }
}
