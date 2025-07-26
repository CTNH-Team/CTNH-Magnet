package tech.vixhentx.mcmod.ctnhmagnet.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.vixhentx.mcmod.ctnhmagnet.common.blockentity.MagnetProviderBE;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;

public class MagnetBlock extends Block implements EntityBlock {
    public MagnetBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MagnetProviderBE(MagnetBlockEntities.MAGNET_BLOCK_ENTITY.get(), pPos, pState);
    }

}

