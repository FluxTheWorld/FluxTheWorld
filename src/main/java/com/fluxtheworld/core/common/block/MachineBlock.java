package com.fluxtheworld.core.common.block;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.core.common.block_entity.MachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.fml.LogicalSide;

public class MachineBlock<BE extends MachineBlockEntity> extends GenericEntityBlock<BE> {

  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  public MachineBlock(Supplier<BlockEntityType<BE>> typeSupplier, Properties properties) {
    super(typeSupplier, properties);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING).add(LIT);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  // region MenuProvider

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
      BlockHitResult hitResult) {
    if (level.getBlockEntity(pos) instanceof MenuProvider menuProvider) {
      LogicalSide side = level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
      InteractionResult result = this.openMenu(side, player, menuProvider);
      if (result.consumesAction()) {
        return result;
      }
    }

    return super.useWithoutItem(state, level, pos, player, hitResult);
  }

  protected InteractionResult openMenu(LogicalSide side, Player player, MenuProvider menu) {
    if (side == LogicalSide.CLIENT) {
      return InteractionResult.SUCCESS;
    }

    player.openMenu(menu);
    return InteractionResult.CONSUME;
  }

  // endregion
}
