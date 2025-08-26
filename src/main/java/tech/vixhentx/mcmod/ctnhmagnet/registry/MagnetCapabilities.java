package tech.vixhentx.mcmod.ctnhmagnet.registry;

import net.minecraftforge.common.capabilities.*;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.*;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.IMagnetChunkStorage;

public class MagnetCapabilities {
    public static final Capability<IMagnetProvider> CAPABILITY_MAGNET_PROVIDER = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IMagnetReceiver> CAPABILITY_MAGNET_RECEIVER = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IMagnetChunkStorage> CAPABILITY_MAGNET_CHUNK_STORAGE = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event){
        event.register(IMagnetProvider.class);
        event.register(IMagnetReceiver.class);
        event.register(IMagnetChunkStorage.class);
    }
}
