package com.fluxtheworld.core.storage.item;

import com.fluxtheworld.core.storage.MenuStackStorage;
import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.side_access.SideAccessConfig;
import com.fluxtheworld.core.storage.slot_access.SlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.ItemStackAdapter;

import net.minecraft.world.item.ItemStack;

public class MenuItemStorage extends MenuStackStorage<ItemStack> {

  protected MenuItemStorage(StackStorage<ItemStack> storage, SlotAccessConfig<ItemStack> slotAccess, SideAccessConfig sideAccess) {
    super(ItemStackAdapter.INSTANCE, storage, slotAccess, sideAccess);
  }

}