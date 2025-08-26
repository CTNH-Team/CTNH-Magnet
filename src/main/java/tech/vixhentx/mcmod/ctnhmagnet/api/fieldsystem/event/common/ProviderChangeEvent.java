package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.BlockEvent;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector.getServerManager;

public class ProviderChangeEvent {
    public static void onAddProvider(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        //TODO: impl spread logic
        if(level.getBlockEntity(pos) instanceof IMagnetProvider provider)
            getServerManager(level).setMagnetField(pos, provider.getMagnetField());
        else if(level.getBlockState(pos).getBlock() instanceof IMagnetProvider provider)
            getServerManager(level).setMagnetField(pos, provider.getMagnetField());
    }
    public static void onRemoveProvider(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level))return;

        BlockPos pos = event.getPos();

        //TODO: impl unspread logic
        if(level.getBlockEntity(pos) instanceof IMagnetProvider)
            getServerManager(level).setMagnetField(pos, MagnetVector.ZERO);
        else if(level.getBlockState(pos).getBlock() instanceof IMagnetProvider)
            getServerManager(level).setMagnetField(pos, MagnetVector.ZERO);
    }
}
