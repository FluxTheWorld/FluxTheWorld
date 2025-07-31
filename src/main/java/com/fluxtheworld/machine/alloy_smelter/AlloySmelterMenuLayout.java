package com.fluxtheworld.machine.alloy_smelter;

import com.fluxtheworld.core.gui.component.EnergyBarWidget;
import com.fluxtheworld.core.gui.component.ProgressBarWidget;
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

    this.addItemSlot(storage, "input0", 32, 36);
    this.addItemSlot(storage, "input1", 54, 36);
    this.addItemSlot(storage, "output", 112, 36);

    this.addPlayerInventory(8, 84);
  }

  @Override
  public void initClient() {
    super.initClient();

    this.addRenderable(EnergyBarWidget.defaultSize(8, 14, () -> 0.5f));

    this.progress = this.addDataSlot(FloatDataSlot.standalone());
    this.addRenderable(ProgressBarWidget.arrowRight(80, 36, () -> this.progress.get()));
  }

  @Override
  public void initServer() {
    super.initServer();

    this.progress = this.addDataSlot(FloatDataSlot.readOnly(() -> this.blockEntity.getCurrentTask().getProgress()));
  }

}
