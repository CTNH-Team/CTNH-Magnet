package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetValues;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;

import java.util.LinkedList;
import java.util.Queue;

public class RoundDeviantSpreader extends MagnetFieldSpreader{
    private static final float deviationFactor = 0.5F;

    public RoundDeviantSpreader(IMagnetProvider source) {
        super(source);
    }

    @Override
    public Long2ObjectMap<MagnetVector> deriveSingle(BlockPos sourcePos, MagnetVector sourceField){
        Long2ObjectMap<MagnetVector> ret = new Long2ObjectOpenHashMap<>();
        ret.put(sourcePos.asLong(), sourceField);
        float sourceStrength = sourceField.length();

        //bfs
        Queue<BlockPos> q = new LinkedList<>();
        q.add(sourcePos);

        while (!q.isEmpty()){
            BlockPos pos = q.poll();
            for(var direction : Direction.values()){
                BlockPos neighbor = pos.relative(direction);
                if(ret.containsKey(neighbor.asLong())) continue;

                Vector3f distanceVec = new Vector3f(neighbor.getX() - sourcePos.getX(),
                        neighbor.getY() - sourcePos.getY(),
                        neighbor.getZ() - sourcePos.getZ());
                float distance = distanceVec.length();

                float strength = sourceStrength / (distance * distance * distance);
                if(strength < MagnetValues.MagMin) continue;

                Vector3f result = new MagnetVector(sourceField).normalize()
                        .add(new Vector3f(distanceVec).normalize(deviationFactor))
                        .normalize(strength);
                ret.put(neighbor.asLong(), new MagnetVector(result));
                q.add(neighbor);
            }
        }

        return ret;
    }
}
