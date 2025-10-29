package tech.vixhentx.mcmod.ctnhmagnet.common.blockentity;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import lombok.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhmagnet.api.capability.IMagnetProvider;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetPriority;
import tech.vixhentx.mcmod.ctnhmagnet.api.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread.MagnetFieldSpreader;
import tech.vixhentx.mcmod.ctnhmagnet.api.fieldsystem.spread.RoundDeviantSpreader;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

public class MagnetProviderBE extends BlockEntity implements IMagnetProvider {
    private final float strength = 32.0f;
    @Getter
    private final MagnetFieldSpreader spreader = new RoundDeviantSpreader(this);

    public MagnetProviderBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap== MagnetCapabilities.CAPABILITY_MAGNET_PROVIDER)
            return MagnetCapabilities.CAPABILITY_MAGNET_PROVIDER.orEmpty(cap, LazyOptional.of(() -> this));
        return LazyOptional.empty();
    }

    @Override
    public Long2ObjectMap<MagnetVector> getMagnetSources() {
        var map = new Long2ObjectArrayMap<MagnetVector>(2);
        map.put(getBlockPos().relative(Direction.NORTH).asLong(), new MagnetVector(strength, Direction.NORTH));
        map.put(getBlockPos().relative(Direction.SOUTH).asLong(), new MagnetVector(strength, Direction.NORTH));
        return map;
    }

    @Override
    public int getPriority() {
        return MagnetPriority.STATIC.priority();
    }

    @Override
    public BlockPos getPos() {
        return worldPosition;
    }
}
