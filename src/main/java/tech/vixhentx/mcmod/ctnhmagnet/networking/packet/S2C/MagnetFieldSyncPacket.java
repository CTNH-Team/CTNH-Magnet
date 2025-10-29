package tech.vixhentx.mcmod.ctnhmagnet.networking.packet.S2C;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.network.FriendlyByteBuf;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import static tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector.*;

//Server在磁场数据变更时向Client发送增量数据

@NoArgsConstructor @AllArgsConstructor
public class MagnetFieldSyncPacket implements IPacket {
    //0表示删除,非0表示替换
    public Long2ObjectMap<MagnetVector> changes;
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(changes.size());
        for (var entry : changes.long2ObjectEntrySet()){
            long pos = entry.getLongKey();
            MagnetVector vector = entry.getValue();
            buf.writeLong(pos);
            buf.writeVector3f(vector);
        }
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        changes = new Long2ObjectOpenHashMap<>(size);
        for (int i = 0; i < size; i++) {
            long pos = buf.readLong();
            MagnetVector vector = new MagnetVector(buf.readVector3f());
            changes.put(pos, vector);
        }
    }

    @Override
    public void execute(IHandlerContext handler) {
        //handle on client
        getClientManager(handler.getLevel()).accumulateMagnetFields(changes);
    }
}
