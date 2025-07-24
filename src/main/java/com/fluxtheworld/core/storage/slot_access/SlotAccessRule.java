package com.fluxtheworld.core.storage.slot_access;

import java.util.function.Predicate;

public class SlotAccessRule<T> {

  private final boolean canMenuInsert;
  private final boolean canMenuExtract;

  private final boolean canPipeInsert;
  private final boolean canPipeExtract;

  private final int capacity;
  private final Predicate<T> filter;

  private SlotAccessRule(
      boolean canMenuInsert, boolean canMenuExtract,
      boolean canPipeInsert, boolean canPipeExtract,
      int capacity, Predicate<T> filter) {
    this.canMenuInsert = canMenuInsert;
    this.canMenuExtract = canMenuExtract;
    this.canPipeInsert = canPipeInsert;
    this.canPipeExtract = canPipeExtract;
    this.capacity = capacity;
    this.filter = filter;
  }

  public boolean canMenuInsert() {
    return canMenuInsert;
  }

  public boolean canMenuExtract() {
    return canMenuExtract;
  }

  public boolean canPipeInsert() {
    return canPipeInsert;
  }

  public boolean canPipeExtract() {
    return canPipeExtract;
  }

  public int getCapacity() {
    return capacity;
  }

  public boolean isValid(T stack) {
    return this.filter.test(stack);
  }

  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  public static class Builder<T> {

    private boolean canMenuInsert;
    private boolean canMenuExtract;

    private boolean canPipeInsert;
    private boolean canPipeExtract;

    private int capacity;
    private Predicate<T> filter;

    public Builder() {
      this.canMenuInsert = false;
      this.canMenuExtract = false;

      this.canPipeInsert = false;
      this.canPipeExtract = false;

      this.capacity = 64;
      this.filter = (it) -> true;
    }

    public Builder<T> menuCanExtract() {
      this.canMenuExtract = true;
      return this;
    }

    public Builder<T> menuCanInsert() {
      this.canMenuInsert = true;
      return this;
    }

    public Builder<T> pipeCanExtract() {
      this.canPipeExtract = true;
      return this;
    }

    public Builder<T> pipeCanInsert() {
      this.canPipeInsert = true;
      return this;
    }

    public Builder<T> capacity(int value) {
      this.capacity = value;
      return this;
    }

    public Builder<T> filter(Predicate<T> filter) {
      this.filter = filter;
      return this;
    }

    public SlotAccessRule<T> build() {
      return new SlotAccessRule<>(this.canMenuInsert, this.canMenuExtract, this.canPipeInsert, this.canPipeExtract, this.capacity, this.filter);
    }
  }
}
