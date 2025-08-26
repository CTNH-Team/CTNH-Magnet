package tech.vixhentx.mcmod.ctnhmagnet.networking.packet.C2S;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C.ChunkMagnetFieldResponsePacket;

import static tech.vixhentx.mcmod.ctnhmagnet.networking.MagnetNetworking.NETWORK;

//Client向Server请求某些区块的所有磁场数据
@NoArgsConstructor
public class ChunkMagnetFieldRequestPacket implements IPacket {
    //既然Handler里已经有了Level，这里就不传了
    public ChunkPos chunkToRequest;

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(chunkToRequest.toLong());
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        chunkToRequest = new ChunkPos(buf.readLong());
    }

    @Override
    public void execute(IHandlerContext handler) {
        //execute in server
        Level level = handler.getLevel();
        if(!handler.isClient() && level != null){
            NETWORK.sendToPlayer(new ChunkMagnetFieldResponsePacket(handler,this), handler.getPlayer());
        }
    }
}
