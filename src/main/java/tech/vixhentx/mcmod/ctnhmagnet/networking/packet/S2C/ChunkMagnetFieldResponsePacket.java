package tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.chunkdata.MagnetChunkClientCache;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.networking.packet.C2S.ChunkMagnetFieldRequestPacket;

import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector.*;

//Server向Client发送所请求区块的所有磁场数据
@NoArgsConstructor
public class ChunkMagnetFieldResponsePacket implements IPacket {
    public Long2ObjectMap<MagnetVector> responseMagnetFields;
    public ChunkPos chunkFromRequest;
    //generate response in constructor
    public ChunkMagnetFieldResponsePacket(IHandlerContext context, ChunkMagnetFieldRequestPacket requestPacket) {
        Level level = context.getLevel();
        chunkFromRequest = requestPacket.chunkToRequest;
        responseMagnetFields = getServerManager(level).getChunkMagnetFields(chunkFromRequest);
    }
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(chunkFromRequest.toLong());
        buf.writeInt(responseMagnetFields.size());
        for(var entry : responseMagnetFields.long2ObjectEntrySet()){
            var pos = entry.getLongKey();
            var vec = entry.getValue();
            buf.writeLong(pos);
            buf.writeVector3f(vec);
        }
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        chunkFromRequest = new ChunkPos(buf.readLong());
        int size = buf.readInt();
        responseMagnetFields = new Long2ObjectOpenHashMap<>();
        for(int i = 0; i < size; i++){
            long pos = buf.readLong();
            MagnetVector vec = new MagnetVector(buf.readVector3f());
            responseMagnetFields.put(pos, vec);
        }
    }

    @Override
    public void execute(IHandlerContext handler) {
        //execute on client side
        //must directly set the client cache to avoid recursive call
        MagnetChunkClientCache.addChunk(chunkFromRequest,responseMagnetFields);
    }
}
