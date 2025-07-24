package com.fluxtheworld.core.storage.item;

import com.fluxtheworld.core.block_entity.MachineBlockEntity;
import com.fluxtheworld.core.storage.slot_access.ItemSlotAccessConfig;

public class MachineItemStorage extends ItemStorage {

  public MachineItemStorage(MachineBlockEntity machineBlockEntity, ItemSlotAccessConfig slotAccess) {
    super(slotAccess, (slot) -> machineBlockEntity.setChanged());
  }
  
}
