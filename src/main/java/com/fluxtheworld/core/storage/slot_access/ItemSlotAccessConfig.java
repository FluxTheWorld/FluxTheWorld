package com.fluxtheworld.core.storage.slot_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.item.ItemStack;

public class ItemSlotAccessConfig {
  private final List<SlotAccessRule<ItemStack>> rules;
  private final Map<String, Integer> namedSlots;

  private ItemSlotAccessConfig(List<SlotAccessRule<ItemStack>> rules, Map<String, Integer> namedSlots) {
    this.rules = rules;
    this.namedSlots = namedSlots;
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

  public int getSlotIndex(String name) {
    return namedSlots.getOrDefault(name, -1);
  }

  public static class Builder {
    private final List<SlotAccessRule<ItemStack>> rules;
    private final Map<String, Integer> namedSlots;

    public Builder() {
      this.rules = new ArrayList<>();
      this.namedSlots = new HashMap<>();
    }

    public Builder slot(String name, SlotAccessRule<ItemStack> rule) {
      this.rules.add(rule);
      this.namedSlots.put(name, this.rules.size() - 1);
      return this;
    }

    public Builder inputSlot(String name) {
      return this.slot(name, SlotAccessRule.<ItemStack>builder().menuCanInsert().menuCanExtract().pipeCanInsert().build());
    }

    public Builder outputSlot(String name) {
      return this.slot(name, SlotAccessRule.<ItemStack>builder().menuCanExtract().pipeCanExtract().build());
    }

    public ItemSlotAccessConfig build() {
      return new ItemSlotAccessConfig(this.rules, this.namedSlots);
    }
  }
}
