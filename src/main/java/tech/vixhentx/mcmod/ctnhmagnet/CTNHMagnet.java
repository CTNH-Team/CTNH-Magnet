package tech.vixhentx.mcmod.ctnhmagnet;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.vixhentx.mcmod.ctnhmagnet.common.CommonProxy;
import tech.vixhentx.mcmod.ctnhmagnet.client.ClientProxy;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(CTNHMagnet.MODID)
public class CTNHMagnet {
    public static final String MODID = "ctnhmagnet";
    public static final String NAME = "CTNH Magnet";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public static ResourceLocation ID (String location){
        return ResourceLocation.tryBuild(MODID,location);
    }

    @SuppressWarnings("noinspection InstantiationOfUtilityClass")
    public CTNHMagnet() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        CTNHMagnet.init();
        DistExecutor.unsafeRunForDist(() -> ()-> new ClientProxy(context), () -> ()-> new CommonProxy(context));
    }
    public static void init(){
        LOGGER.info("Initializing!");
    }

}
