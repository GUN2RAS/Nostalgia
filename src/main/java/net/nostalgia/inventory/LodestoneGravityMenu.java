package net.nostalgia.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class LodestoneGravityMenu extends AbstractContainerMenu {
    public final Container container;

    public LodestoneGravityMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(1));
    }

    public LodestoneGravityMenu(int syncId, Inventory playerInventory, Container container) {
        super(ModScreenHandlers.LODESTONE_GRAVITY_MENU, syncId);
        checkContainerSize(container, 1);
        this.container = container;
        container.startOpen(playerInventory.player);

        this.addSlot(new Slot(container, 0, 81, 63) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || stack.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 9 + col * 18, 148));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (invSlot < 1) {
                if (!this.moveItemStackTo(slotStack, 1, 10, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!(slotStack.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || slotStack.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST)) || !this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
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

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
        if (!player.level().isClientSide()) {
            this.clearContainer(player, this.container);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public void programAmethyst(int directionIdx) {
        Slot slot = this.getSlot(0);
        ItemStack stack = slot.getItem();
        if (!stack.isEmpty() && stack.is(Items.AMETHYST_SHARD)) {
            String dirStr = switch (directionIdx) {
                case 0 -> "up";
                case 1 -> "down";
                case 2 -> "left";
                case 3 -> "right";
                default -> "none";
            };
            if (!"none".equals(dirStr)) {
                ItemStack charged = new ItemStack(net.nostalgia.item.ModItems.CHARGED_AMETHYST, 1);
                net.nostalgia.item.ChargedAmethystItem.setDirection(charged, dirStr);
                slot.setByPlayer(charged);
                this.broadcastChanges();
            }
        }
    }
}

