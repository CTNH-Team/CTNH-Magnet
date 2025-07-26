package tech.vixhentx.mcmod.ctnhmagnet.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tech.vixhentx.mcmod.ctnhmagnet.client.event.MagnetFieldRenderEvent;
import tech.vixhentx.mcmod.ctnhmagnet.common.CommonProxy;

public class ClientProxy extends CommonProxy {

    public ClientProxy(FMLJavaModLoadingContext context) {
        super(context);
        init();
    }
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(MagnetFieldRenderEvent::tick);
    }
}
