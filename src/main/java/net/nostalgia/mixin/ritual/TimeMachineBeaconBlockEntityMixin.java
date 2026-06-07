package net.nostalgia.mixin.ritual;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.nostalgia.alphalogic.ritual.TimeMachineStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconBlockEntity.class)
public abstract class TimeMachineBeaconBlockEntityMixin extends BlockEntity implements TimeMachineStorage {

    public TimeMachineBeaconBlockEntityMixin() {
        super(null, null, null);
    }

    @Unique
    private ItemStack nostalgia$shard = ItemStack.EMPTY;

    @Unique
    private int nostalgia$energy = 0;

    @Unique
    private final ContainerData nostalgia$energyData = new ContainerData() {
        @Override
        public int get(int index) {
            return TimeMachineBeaconBlockEntityMixin.this.nostalgia$energy;
        }

        @Override
        public void set(int index, int value) {
            TimeMachineBeaconBlockEntityMixin.this.nostalgia$energy = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    @Override
    public ItemStack nostalgia$getShard() {
        return this.nostalgia$shard;
    }

    @Override
    public void nostalgia$setShard(ItemStack shard) {
        this.nostalgia$shard = shard;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public int nostalgia$getEnergy() {
        return this.nostalgia$energy;
    }

    @Override
    public void nostalgia$setEnergy(int energy) {
        this.nostalgia$energy = energy;
        this.setChanged();
        if (this.level != null && !this.level.isClientSide()) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public ContainerData nostalgia$getEnergyData() {
        return this.nostalgia$energyData;
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void nostalgia$saveAdditional(ValueOutput output, CallbackInfo ci) {
        output.putInt("TimeMachineEnergy", this.nostalgia$energy);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void nostalgia$loadAdditional(ValueInput input, CallbackInfo ci) {
        this.nostalgia$energy = input.getIntOr("TimeMachineEnergy", 0);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private static void nostalgia$tick(Level level, BlockPos pos, BlockState selfState, BeaconBlockEntity entity, CallbackInfo ci) {
        if (level.isClientSide()) {
            return;
        }
        if (entity instanceof TimeMachineStorage storage) {
            ItemStack fuel = storage.nostalgia$getShard();
            boolean isFuelPresent = fuel.is(net.minecraft.world.item.Items.ECHO_SHARD) || fuel.is(net.minecraft.world.item.Items.AMETHYST_SHARD) || fuel.is(net.nostalgia.item.ModItems.CHARGED_AMETHYST);
            int energy = storage.nostalgia$getEnergy();
            if (isFuelPresent) {
                if (energy < 100) {
                    storage.nostalgia$setEnergy(Math.min(100, energy + 2));
                }
            } else {
                if (energy > 0) {
                    storage.nostalgia$setEnergy(Math.max(0, energy - 3));
                }
            }
        }
    }

    @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
    private void nostalgia$collectComponents(DataComponentMap.Builder components, CallbackInfo ci) {
        if (!this.nostalgia$shard.isEmpty()) {
            NonNullList<ItemStack> list = NonNullList.withSize(1, this.nostalgia$shard);
            components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
        }
    }

    @Inject(method = "applyImplicitComponents", at = @At("TAIL"))
    private void nostalgia$applyComponents(DataComponentGetter components, CallbackInfo ci) {
        ItemContainerContents container = components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        if (container != ItemContainerContents.EMPTY) {
            NonNullList<ItemStack> list = NonNullList.withSize(1, ItemStack.EMPTY);
            container.copyInto(list);
            this.nostalgia$shard = list.get(0);
        } else {
            this.nostalgia$shard = ItemStack.EMPTY;
        }
    }

    @Override
    public void preRemoveSideEffects(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this.level != null && !this.level.isClientSide()) {
            if (!this.nostalgia$shard.isEmpty()) {
                net.minecraft.world.Containers.dropItemStack(this.level, pos.getX(), pos.getY(), pos.getZ(), this.nostalgia$shard);
                this.nostalgia$shard = ItemStack.EMPTY;
                net.nostalgia.alphalogic.ritual.EchoRitualManager.clearSelection(null);
            }
        }
    }
}

