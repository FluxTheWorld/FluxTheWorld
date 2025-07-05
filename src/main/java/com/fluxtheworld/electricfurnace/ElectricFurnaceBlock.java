package com.fluxtheworld.electricfurnace;

import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.SimpleMenuProvider;

import com.fluxtheworld.electricfurnace.ElectricFurnaceBlockEntity;
import com.fluxtheworld.electricfurnace.ElectricFurnaceContainer;

public class ElectricFurnaceBlock extends BaseEntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public static final BooleanProperty LIT = BlockStateProperties.LIT;

  public ElectricFurnaceBlock(BlockBehaviour.Properties properties) {
    super(properties);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return simpleCodec(ElectricFurnaceBlock::new);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, LIT);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ElectricFurnaceBlockEntity(pos, state);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
      BlockEntityType<T> type) {
    return createFurnaceTicker(level, type, ElectricFurnaceBlockEntity.TYPE);
  }

  protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level,
      BlockEntityType<T> serverType, BlockEntityType<? extends ElectricFurnaceBlockEntity> clientType) {
    return level.isClientSide ? null
        : createTickerHelper(serverType, clientType, ElectricFurnaceBlockEntity::serverTick);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
      BlockHitResult hitResult) {
    if (level.isClientSide) {
      return InteractionResult.SUCCESS;
    }
    BlockEntity blockentity = level.getBlockEntity(pos);
    if (blockentity instanceof ElectricFurnaceBlockEntity) {
      MenuConstructor menuConstructor = (containerId, playerInventory, _player) -> new ElectricFurnaceContainer(
          containerId, playerInventory, (ElectricFurnaceBlockEntity) blockentity);
      MenuProvider menuProvider = new SimpleMenuProvider(menuConstructor,
          Component.translatable("container.electric_furnace"));

      if (player instanceof ServerPlayer serverPlayer) {
        serverPlayer.openMenu(menuProvider, pos);
      }
    }

    return InteractionResult.CONSUME;
  }
}