package com.fluxtheworld.core.storage.item;

import javax.annotation.Nullable;

import org.jetbrains.annotations.UnknownNullability;

import com.fluxtheworld.core.storage.StackStorage;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;
import com.fluxtheworld.core.storage.stack_adapter.ItemStackAdapter;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class ItemStorage extends StackStorage<ItemStack> {

  private final @Nullable ItemStorageChangeListener changeListener;

  public ItemStorage(ItemSlotAccessConfig slotAccess) {
    this(slotAccess, null);
  }

  public ItemStorage(ItemSlotAccessConfig slotAccess, @Nullable ItemStorageChangeListener changeListener) {
    super(slotAccess, ItemStackAdapter.INSTANCE);
    this.changeListener = changeListener;
  }

  @Override
  public ItemStack getStackInSlot(int slot) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getStackInSlot'");
  }

  @Override
  public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'insert'");
  }

  @Override
  public ItemStack extract(int slot, int amount, boolean simulate) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'extract'");
  }

  @Override
  public @UnknownNullability CompoundTag serializeNBT(Provider provider) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'serializeNBT'");
  }

  @Override
  public void deserializeNBT(Provider provider, CompoundTag nbt) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deserializeNBT'");
  }

}