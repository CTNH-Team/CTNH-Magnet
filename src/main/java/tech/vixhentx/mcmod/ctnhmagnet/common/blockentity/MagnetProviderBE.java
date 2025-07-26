package tech.vixhentx.mcmod.ctnhmagnet.common.blockentity;

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
import tech.vixhentx.mcmod.ctnhmagnet.common.datamodel.MagnetVector;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetCapabilities;

public class MagnetProviderBE extends BlockEntity implements IMagnetProvider {
    @Getter @Setter
    int OEt=10;
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
    public MagnetVector getMagnetField() {
        return new MagnetVector(OEt,Direction.NORTH);
    }

}
