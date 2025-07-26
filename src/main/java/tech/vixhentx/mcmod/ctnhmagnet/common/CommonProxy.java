package tech.vixhentx.mcmod.ctnhmagnet.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.vixhentx.mcmod.ctnhmagnet.common.event.BlockEventHandler;
import tech.vixhentx.mcmod.ctnhmagnet.common.event.ChunkCapabilityAttacher;
import tech.vixhentx.mcmod.ctnhmagnet.registry.*;

public class CommonProxy {
    public CommonProxy(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        eventBus.register(this);
        init();
    }
    public static void init() {
        MagnetMachines.init();
        MagnetMultiblockMachines.init();
        MagnetBlocks.init();
        MagnetItems.init();
        MagnetBlockEntities.init();

        MagnetRegistration.REGISTRATE.registerRegistrate();

        MinecraftForge.EVENT_BUS.addListener(BlockEventHandler::onBlockPlace);
        MinecraftForge.EVENT_BUS.addListener(BlockEventHandler::onBlockBreak);
    }

}
