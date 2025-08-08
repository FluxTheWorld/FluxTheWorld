package com.fluxtheworld.core.storage.slot_access;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SlotAccessConfig<T> {
  protected final List<SlotAccessRule<T>> rules;
  protected final Map<String, Integer> namedSlots;
  protected final Map<SlotAccessTag, List<Integer>> taggedSlots;

  protected SlotAccessConfig(List<SlotAccessRule<T>> rules,
      Map<String, Integer> namedSlots, Map<SlotAccessTag, List<Integer>> taggedSlots) {
    this.rules = rules;
    this.namedSlots = namedSlots;
    this.taggedSlots = taggedSlots;
  }

  public int getSlotCount() {
    return rules.size();
  }

  public abstract boolean isValid(int slot, T stack);

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

  public int getSlotIndex(String name) {
    final var index = namedSlots.getOrDefault(name, -1);
    if (index == -1) {
      throw new IllegalArgumentException("Slot with name: '" + name + "' does not exist.");
    }
    return index;
  }

  public List<Integer> getTaggedSlots(SlotAccessTag tag) {
    return this.taggedSlots.getOrDefault(tag, Collections.emptyList());
  }

  public abstract static class Builder<T, B extends Builder<T, B, C>, C extends SlotAccessConfig<T>> {
    protected final List<SlotAccessRule<T>> rules;
    protected final Map<String, Integer> namedSlots;
    protected final Map<SlotAccessTag, List<Integer>> taggedSlots;

    protected Builder() {
      this.rules = new ArrayList<>();
      this.namedSlots = new HashMap<>();
      this.taggedSlots = new EnumMap<>(SlotAccessTag.class);
    }

    @SuppressWarnings("unchecked")
    protected B self() {
      return (B) this;
    }

    public B slot(String name, SlotAccessRule<T> rule) {
      this.rules.add(rule);
      this.namedSlots.put(name, this.rules.size() - 1);
      return self();
    }

    public B taggedSlot(String name, SlotAccessTag tag, SlotAccessRule<T> rule) {
      this.slot(name, rule);
      final var slots = taggedSlots.getOrDefault(tag, new ArrayList<>());
      slots.add(this.rules.size() - 1);
      taggedSlots.put(tag, slots);
      return self();
    }

    public B inputSlot(String name) {
      return this.taggedSlot(name,
          SlotAccessTag.INPUT,
          SlotAccessRule.<T>builder().menuCanInsert().menuCanExtract().pipeCanInsert().build());
    }

    public B outputSlot(String name) {
      return this.taggedSlot(name,
          SlotAccessTag.OUTPUT,
          SlotAccessRule.<T>builder().menuCanExtract().pipeCanExtract().build());
    }

    public abstract C build();
  }
}