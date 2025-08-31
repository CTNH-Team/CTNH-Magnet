package tech.vixhentx.mcmod.ctnhmagnet.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vixhentx.mcmod.ctnhmagnet.common.blockentity.MagnetProviderBE;
import tech.vixhentx.mcmod.ctnhmagnet.registry.MagnetBlockEntities;

import javax.annotation.ParametersAreNonnullByDefault;

public class MagnetBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public MagnetBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @NotNull
    @ParametersAreNonnullByDefault
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MagnetProviderBE(MagnetBlockEntities.MAGNET_BLOCK_ENTITY.get(), pPos, pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }
}

