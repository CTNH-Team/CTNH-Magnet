package tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetValues;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.manager.MagnetFieldManagerSelector;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MagnetFieldSpreader {
    private static final float deviationFactor = 0.5F;

    final IMagnetProvider source;
    //derivedFields with itself
    Long2ObjectMap<MagnetVector> derivedFields;

    public MagnetFieldSpreader(IMagnetProvider source) {
        this.source = source;
    }
    public Long2ObjectMap<MagnetVector> getDerivedFields() {
        if(derivedFields != null) return derivedFields;
         //calculate if absent
        derivedFields = new Long2ObjectOpenHashMap<>();
        derivedFields.put(source.getPos().asLong(), source.getMagnetField());

        //bfs
        Queue<BlockPos> q = new LinkedList<>();
        q.add(source.getPos());

        while (!q.isEmpty()){
            BlockPos pos = q.poll();
            for(var direction : Direction.values()){
                BlockPos neighbor = pos.relative(direction);
                if(derivedFields.containsKey(neighbor.asLong())) continue;

                Vector3f distanceVec = new Vector3f(neighbor.getX() - source.getPos().getX(),
                                                    neighbor.getY() - source.getPos().getY(),
                                                    neighbor.getZ() - source.getPos().getZ());
                float distance = distanceVec.length();

                float strength = source.getStrength() / (distance * distance * distance);
                if(strength < MagnetValues.MagMin) continue;

                Vector3f result = new MagnetVector(source.getMagnetField()).normalize()
                                        .add(new Vector3f(distanceVec).normalize(deviationFactor))
                                .normalize(strength);
                derivedFields.put(neighbor.asLong(), new MagnetVector(result));
                q.add(neighbor);
            }
        }

        return derivedFields;
    }
    public void spread() {
        MagnetFieldManagerSelector.getManager(source.getLevel()).accumulateMagnetFields(getDerivedFields());
    }
    public void unspread() {
        MagnetFieldManagerSelector.getManager(source.getLevel()).dispersalMagnetFields(getDerivedFields());
    }
}
