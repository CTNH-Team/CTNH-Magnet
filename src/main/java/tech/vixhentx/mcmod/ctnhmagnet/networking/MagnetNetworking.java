package tech.vixhentx.mcmod.ctnhmagnet.networking;

import com.lowdragmc.lowdraglib.networking.INetworking;
import com.lowdragmc.lowdraglib.networking.forge.Networking;
import tech.vixhentx.mcmod.ctnhmagnet.CTNHMagnet;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.C2S.ChunkMagnetFieldRequestPacket;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.ChunkMagnetFieldResponsePacket;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.MagnetFieldSyncPacket;

public class MagnetNetworking {
    public static INetworking NETWORK = new Networking(CTNHMagnet.ID("networking"),"0.0.1");
    public static void init() {
        NETWORK.registerC2S(ChunkMagnetFieldRequestPacket.class);

        NETWORK.registerS2C(ChunkMagnetFieldResponsePacket.class);
        NETWORK.registerS2C(MagnetFieldSyncPacket.class);
    }
}
