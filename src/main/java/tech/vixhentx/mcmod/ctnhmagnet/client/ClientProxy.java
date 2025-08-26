package tech.vixhentx.mcmod.ctnhmagnet.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.vixhentx.mcmod.ctnhmagnet.CTNHMagnet;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.client.ChunkCacheEventHandler;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.event.client.MagnetFieldRenderEvent;
import tech.vixhentx.mcmod.ctnhmagnet.common.CommonProxy;

@Mod.EventBusSubscriber(modid = CTNHMagnet.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public ClientProxy(FMLJavaModLoadingContext context) {
        super(context);
        init();
    }
    public static void init() {
    }

    //event
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event){
        MagnetFieldRenderEvent.tick(event);
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event){
        ChunkCacheEventHandler.onChunkUnload(event);
    }
}
