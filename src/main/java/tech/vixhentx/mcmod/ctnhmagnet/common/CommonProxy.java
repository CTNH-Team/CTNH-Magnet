package tech.vixhentx.mcmod.ctnhmagnet.common;

import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.vixhentx.mcmod.ctnhmagnet.CTNHMagnet;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common.ChunkCapabilityAttacher;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common.ChunkInitialSpreadHandler;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.common.ProviderChangeEvent;
import tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking;
import tech.vixhentx.mcmod.ctnhmagnet.registry.*;

@Mod.EventBusSubscriber(modid = CTNHMagnet.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonProxy {
    public CommonProxy(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        eventBus.register(this);
        init();
    }
    public static void init() {
        MagnetNetworking.init();

        MagnetMachines.init();
        MagnetMultiblockMachines.init();
        MagnetBlocks.init();
        MagnetItems.init();
        MagnetBlockEntities.init();

        MagnetRegistration.REGISTRATE.registerRegistrate();
    }

    //event
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<LevelChunk> event) {
        ChunkCapabilityAttacher.onAttachChunkCapabilities(event);
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event){
        ProviderChangeEvent.onAddProvider(event);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){
        ProviderChangeEvent.onRemoveProvider(event);
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event){
        ChunkInitialSpreadHandler.onChunkLoad(event);
    }
}
