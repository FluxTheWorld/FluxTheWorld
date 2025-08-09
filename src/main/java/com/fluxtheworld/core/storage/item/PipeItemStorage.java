package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import com.fluxtheworld.core.storage.PipeStackStorage;
import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.ItemStackAdapter;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class PipeItemStorage extends PipeStackStorage<ItemStack> {

  protected PipeItemStorage(StackStorage<ItemStack> storage, SlotAccessConfig<ItemStack> slotAccess,
      SideAccessConfig sideAccess, @Nullable Direction side) {
    super(ItemStackAdapter.INSTANCE, storage, slotAccess, sideAccess, side);
  }

}