package net.nostalgia.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TimeMachineMenu extends AbstractContainerMenu {
  public final Container container;
  private final ContainerData data;

  public TimeMachineMenu(int syncId, Inventory playerInventory) {
    this(syncId, playerInventory, new SimpleContainer(1), new TimeMachineMenu.SyncedContainerData(2));
  }

  public TimeMachineMenu(int syncId, Inventory playerInventory, Container container) {
    this(syncId, playerInventory, container, new TimeMachineMenu.SyncedContainerData(2));
  }

  public TimeMachineMenu(int syncId, Inventory playerInventory, Container container, ContainerData data) {
    super(ModScreenHandlers.TIME_MACHINE_MENU, syncId);
    checkContainerSize(container, 1);
    this.container = container;
    this.data = data;
    this.addDataSlots(data);
    container.startOpen(playerInventory.player);
    this.addSlot(new Slot(container, 0, 80, 116));

    for (int col = 0; col < 9; col++) {
      this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 147));
    }
  }

  public boolean isDataSynced() {
    return this.data instanceof TimeMachineMenu.SyncedContainerData syncedData ? syncedData.isSynced() : true;
  }

  public int getEnergy() {
    return this.data.get(0);
  }

  public int getWasBooted() {
    return this.data.get(1);
  }

  public void setWasBooted(int value) {
    this.data.set(1, value);
  }

  public ItemStack quickMoveStack(Player player, int invSlot) {
    ItemStack itemStack = ItemStack.EMPTY;
    Slot slot = (Slot)this.slots.get(invSlot);
    if (slot != null && slot.hasItem()) {
      ItemStack slotStack = slot.getItem();
      itemStack = slotStack.copy();
      if (invSlot < 1) {
        if (!this.moveItemStackTo(slotStack, 1, 10, true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
        return ItemStack.EMPTY;
      }

      if (slotStack.isEmpty()) {
        slot.setByPlayer(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }

      if (slotStack.getCount() == itemStack.getCount()) {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, slotStack);
    }

    return itemStack;
  }

  public void removed(Player player) {
    super.removed(player);
    this.container.stopOpen(player);
  }

  public boolean stillValid(Player player) {
    return this.container.stillValid(player);
  }

  private static class SyncedContainerData extends SimpleContainerData {
    private boolean synced = false;

    public SyncedContainerData(int size) {
      super(size);
    }

    public void set(int index, int value) {
      super.set(index, value);
      this.synced = true;
    }

    public boolean isSynced() {
      return this.synced;
    }
  }
}
