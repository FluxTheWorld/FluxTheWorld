package com.fluxtheworld.core.storage.item;

import com.fluxtheworld.core.storage.side_access.SideAccessConfig;

public interface ItemStorageProvider {
  ItemStorage getItemStorage();

  SideAccessConfig getItemSideAccess();
}
