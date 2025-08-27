package tech.vixhentx.mcmod.ctnhmagnet.api.datamodel;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.joml.Vector3f;

public class MagnetVector extends Vector3f implements INBTSerializable<CompoundTag> {
    public MagnetVector() {
        super();
    }

    public MagnetVector(float x, float y, float z) {
        super(x, y, z);
    }

    public MagnetVector(Vector3f vec) {
        super(vec);
    }
    public MagnetVector(float strength,Direction direction){
        Vector3f v = direction.step();
        set(v.mul(strength));
    }
    @Unmodifiable
    public static final MagnetVector ZERO = new MagnetVector(0,0,0);
    public int strength(@NotNull Direction direction){
        Vector3f dir = direction.step();
        return (int) (dir.dot(this));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", x);
        tag.putFloat("y", y);
        tag.putFloat("z", z);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
         x = nbt.getFloat("x");
         y = nbt.getFloat("y");
         z = nbt.getFloat("z");
    }
    public static MagnetVector fromNBT(CompoundTag tag) {
        MagnetVector vec = new MagnetVector();
        vec.deserializeNBT(tag);
        return vec;
    }
}
