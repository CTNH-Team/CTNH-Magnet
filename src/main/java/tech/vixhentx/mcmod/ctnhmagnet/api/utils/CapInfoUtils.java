package tech.vixhentx.mcmod.ctnhmagnet.api.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetReceiver;

import java.util.Optional;

import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector.getServerManager;

public class CapInfoUtils {
    public static Optional<IMagnetProvider> getMagnetProvider(Level level, BlockPos pos) {
        if(level.getBlockEntity(pos) instanceof IMagnetProvider provider)
            return Optional.of(provider);
        else if(level.getBlockState(pos).getBlock() instanceof IMagnetProvider provider)
            return Optional.of(provider);

        return Optional.empty();
    }
    public static Optional<IMagnetReceiver> getMagnetReceiver(Level level, BlockPos pos) {
        if(level.getBlockEntity(pos) instanceof IMagnetReceiver receiver)
            return Optional.of(receiver);
        else if(level.getBlockState(pos).getBlock() instanceof IMagnetReceiver receiver)
            return Optional.of(receiver);

        return Optional.empty();
    }
}
