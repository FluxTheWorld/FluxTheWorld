package com.fluxtheworld.core.storage.slot_access;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.ItemStack;

public class ItemSlotAccessConfig {
  private final List<SlotAccessRule<ItemStack>> rules;

  private ItemSlotAccessConfig(List<SlotAccessRule<ItemStack>> rules) {
    this.rules = rules;
  }

  public int getSlotCount() {
    return rules.size();
  }

  public boolean isItemValid(int slot, ItemStack stack) {
    if (slot < 0 || slot >= this.getSlotCount()) {
      return false;
    }

    return this.rules.get(slot).isValid(stack);
  }

  public int getStackLimit(int slot) {
    if (slot < 0 || slot >= this.getSlotCount()) {
      return 0;
    }

    return this.rules.get(slot).getCapacity();
  }

  public boolean canMenuInsert(int slot) {
    return this.rules.get(slot).canMenuInsert();
  }

  public boolean canMenuExtract(int slot) {
    return this.rules.get(slot).canMenuExtract();
  }

  public boolean canPipeInsert(int slot) {
    return this.rules.get(slot).canPipeInsert();
  }

  public boolean canPipeExtract(int slot) {
    return this.rules.get(slot).canPipeExtract();
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final List<SlotAccessRule<ItemStack>> rules;

    public Builder() {
      this.rules = new ArrayList<>();
    }

    public Builder slot(SlotAccessRule<ItemStack> rule) {
      this.rules.add(rule);
      return this;
    }

    public Builder inputSlot() {
      return this.slot(SlotAccessRule.<ItemStack>builder().menuCanInsert().menuCanExtract().pipeCanInsert().build());
    }

    public Builder outputSlot() {
      return this.slot(SlotAccessRule.<ItemStack>builder().menuCanExtract().pipeCanExtract().build());
    }

    public ItemSlotAccessConfig build() {
      return new ItemSlotAccessConfig(this.rules);
    }
  }
}
