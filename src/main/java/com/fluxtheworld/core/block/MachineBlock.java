package com.fluxtheworld.core.block;

import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.fluxtheworld.FTWMod;
import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.machine.alloy_smelter.AlloySmelterRegistry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
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
      InteractionResult result = this.openMenu(state, level, pos, player, hitResult, menuProvider);
      if (result.consumesAction()) {
        return result;
      }
    }

    return super.useWithoutItem(state, level, pos, player, hitResult);
  }

  protected InteractionResult openMenu(BlockState state, Level level, BlockPos pos, Player player,
      BlockHitResult hitResult, MenuProvider menuProvider) {
    if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
      serverPlayer.openMenu(menuProvider, pos);
    }

    return InteractionResult.SUCCESS;
  }

  // endregion
}
