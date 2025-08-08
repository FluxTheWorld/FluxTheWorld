package com.fluxtheworld.core.storage.item;

import com.fluxtheworld.core.storage.StorageChangeListener;

public interface ItemStorageChangeListener extends StorageChangeListener {
  
  /**
   * Called when the item storage contents have changed.
   *
   * @param slot The index of the slot that changed
   */
  void onItemStorageChanged(int slot);
  
  /**
   * Default implementation that delegates to the specific item storage method.
   * This maintains backward compatibility while implementing the base interface.
   */
  @Override
  default void onStorageChanged(int slot) {
    onItemStorageChanged(slot);
  }
}
