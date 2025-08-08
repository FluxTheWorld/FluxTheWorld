package com.fluxtheworld.core.storage;

/**
 * Base interface for storage change listeners.
 * This interface provides a unified way to handle storage content changes
 * across different storage types (items, fluids, etc.).
 */
public interface StorageChangeListener {
  
  /**
   * Called when the contents of a storage slot have changed.
   * 
   * @param slot The index of the slot that changed
   */
  void onStorageChanged(int slot);
}