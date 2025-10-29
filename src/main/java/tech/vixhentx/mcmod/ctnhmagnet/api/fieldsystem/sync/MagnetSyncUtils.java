package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.sync;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.level.chunk.LevelChunk;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.MagnetFieldSyncPacket;

import static tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking.NETWORK;

public class MagnetSyncUtils {
    public static void syncChanges(LevelChunk chunk, Long2ObjectMap<MagnetVector> changes){
        MagnetFieldSyncPacket packet = new MagnetFieldSyncPacket(changes);
        //TODO: send to players who has chunk data
        NETWORK.sendToAll(packet);
    }
}
