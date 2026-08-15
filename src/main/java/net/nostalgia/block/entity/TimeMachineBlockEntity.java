package net.nostalgia.block.entity;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.nostalgia.item.ModItems;

public class TimeMachineBlockEntity extends BlockEntity implements Container {
  private NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
  private int energy = 0;
  protected final ContainerData data = new ContainerData() {
    {
      Objects.requireNonNull(TimeMachineBlockEntity.this);
    }

    public int get(int index) {
      return TimeMachineBlockEntity.this.energy;
    }

    public void set(int index, int value) {
      TimeMachineBlockEntity.this.energy = value;
    }

    public int getCount() {
      return 1;
    }
  };

  public TimeMachineBlockEntity(BlockPos pos, BlockState state) {
    super(ModBlockEntities.TIME_MACHINE_BE, pos, state);
  }

  public int getEnergy() {
    return this.energy;
  }

  public ContainerData getContainerData() {
    return this.data;
  }

  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    ContainerHelper.saveAllItems(output, this.items);
    output.putInt("Energy", this.energy);
  }

  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.items = NonNullList.withSize(1, ItemStack.EMPTY);
    ContainerHelper.loadAllItems(input, this.items);
    this.energy = input.getIntOr("Energy", 0);
  }

  public static void tick(Level level, BlockPos pos, BlockState state, TimeMachineBlockEntity blockEntity) {
    if (!level.isClientSide()) {
      ItemStack fuel = blockEntity.getItem(0);
      boolean isFuelPresent = fuel.is(Items.ECHO_SHARD) || fuel.is(Items.AMETHYST_SHARD) || fuel.is(ModItems.CHARGED_AMETHYST);
      if (isFuelPresent) {
        if (blockEntity.energy < 100) {
          blockEntity.energy = Math.min(100, blockEntity.energy + 2);
          blockEntity.setChanged();
        }
      } else if (blockEntity.energy > 0) {
        blockEntity.energy = Math.max(0, blockEntity.energy - 3);
        blockEntity.setChanged();
      }
    }
  }

  public int getContainerSize() {
    return 1;
  }

  public boolean isEmpty() {
    return ((ItemStack)this.items.get(0)).isEmpty();
  }

  public ItemStack getItem(int slot) {
    return (ItemStack)this.items.get(slot);
  }

  public ItemStack removeItem(int slot, int amount) {
    ItemStack result = ContainerHelper.removeItem(this.items, slot, amount);
    if (!result.isEmpty()) {
      this.setChanged();
    }

    return result;
  }

  public ItemStack removeItemNoUpdate(int slot) {
    return ContainerHelper.takeItem(this.items, slot);
  }

  public void setItem(int slot, ItemStack stack) {
    this.items.set(slot, stack);
    stack.limitSize(this.getMaxStackSize(stack));
    this.setChanged();
  }

  public boolean stillValid(Player player) {
    return Container.stillValidBlockEntity(this, player);
  }

  public void clearContent() {
    this.items.clear();
    this.setChanged();
  }
}
