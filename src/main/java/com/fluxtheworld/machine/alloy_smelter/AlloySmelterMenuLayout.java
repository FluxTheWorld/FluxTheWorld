package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.FTW;
import com.fluxtheworld.core.gui.component.ProgressWidget;
import com.fluxtheworld.core.menu.MachineMenuLayout;
import com.fluxtheworld.core.slot.FloatDataSlot;
import com.fluxtheworld.core.storage.item.ItemStorage;

import net.minecraft.world.entity.player.Inventory;

public class AlloySmelterMenuLayout extends MachineMenuLayout<AlloySmelterBlockEntity> {

  public FloatDataSlot progress;

  public AlloySmelterMenuLayout(Inventory playerInventory, AlloySmelterBlockEntity blockEntity) {
    super(playerInventory, blockEntity);
  }

  @Override
  public void init() {
    ItemStorage storage = this.blockEntity.getItemStorage();

    this.addItemSlot(storage, "input1", 20, 36);
    this.addItemSlot(storage, "input2", 40, 36);
    this.addItemSlot(storage, "output", 60, 36);

    this.addPlayerInventory(8, 84);
  }

  @Override
  public void initClient() {
    super.initClient();

    this.progress = this.addDataSlot(FloatDataSlot.standalone());
    this.addRenderable(ProgressWidget.leftRight(80, 36, 32, 64, FTW.loc("progress"), () -> this.progress.get()));
  }

  @Override
  public void initServer() {
    super.initServer();

    this.progress = this.addDataSlot(FloatDataSlot.readOnly(() -> this.blockEntity.getCurrentTask().getProgress()));
  }

}
