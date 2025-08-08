package com.fluxtheworld.core.storage.fluid;

import com.fluxtheworld.core.storage.StorageChangeListener;

public interface FluidStorageChangeListener extends StorageChangeListener {
  
  /**
   * Called when the fluid storage contents have changed.
   *
   * @param slot The index of the slot that changed
   */
  void onFluidStorageChanged(int slot);
  
  /**
   * Default implementation that delegates to the specific fluid storage method.
   * This maintains backward compatibility while implementing the base interface.
   */
  @Override
  default void onStorageChanged(int slot) {
    onFluidStorageChanged(slot);
  }
}