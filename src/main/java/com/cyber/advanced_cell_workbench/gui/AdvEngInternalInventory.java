package com.cyber.advanced_cell_workbench.gui;

import appeng.core.AELog;
import appeng.helpers.ItemStackHelper;
import appeng.util.Platform;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import appeng.util.inv.filter.IAEItemFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.ItemStackHandler;

public class AdvEngInternalInventory extends ItemStackHandler implements Iterable<ItemStack> {
    protected boolean enableClientEvents;
    protected IAEAppEngInventory te;
    protected final int[] maxStack;
    protected ItemStack previousStack;
    protected IAEItemFilter filter;
    protected boolean dirtyFlag;

    public AdvEngInternalInventory(IAEAppEngInventory inventory, int size, int maxStack, IAEItemFilter filter) {
        super(size);
        this.enableClientEvents = false;
        this.previousStack = ItemStack.EMPTY;
        this.dirtyFlag = false;
        this.setTileEntity(inventory);
        this.setFilter(filter);
        this.maxStack = new int[size];
        Arrays.fill(this.maxStack, maxStack);
    }

    public AdvEngInternalInventory(IAEAppEngInventory inventory, int size, int maxStack) {
        this(inventory, size, maxStack, (IAEItemFilter)null);
    }

    public AdvEngInternalInventory(IAEAppEngInventory inventory, int size) {
        this(inventory, size, 64);
    }

    public void setFilter(IAEItemFilter filter) {
        this.filter = filter;
    }

    public int getSlotLimit(int slot) {
        return this.maxStack[slot];
    }

    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack != this.getStackInSlot(slot)) {
            this.previousStack = this.getStackInSlot(slot).copy();
        }

        super.setStackInSlot(slot, stack);
    }

    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.filter != null && !this.filter.allowInsert(this, slot, stack)) {
            return stack;
        } else {
            if (!simulate) {
                this.previousStack = this.getStackInSlot(slot).copy();
            }

            return super.insertItem(slot, stack, simulate);
        }
    }

    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.filter != null && !this.filter.allowExtract(this, slot, amount)) {
            return ItemStack.EMPTY;
        } else {
            if (!simulate) {
                this.previousStack = this.getStackInSlot(slot).copy();
            }

            return super.extractItem(slot, amount, simulate);
        }
    }

    protected void onContentsChanged(int slot) {
        if (this.getTileEntity() != null && this.eventsEnabled() && !this.dirtyFlag) {
            this.dirtyFlag = true;
            ItemStack newStack = this.getStackInSlot(slot);
            ItemStack oldStack = this.previousStack;
            InvOperation op = InvOperation.SET;
            if (newStack.isEmpty() || oldStack.isEmpty() || oldStack.getCount() != newStack.getCount() && ItemStack.areItemsEqual(newStack, oldStack)) {
                if (newStack.getCount() > oldStack.getCount()) {
                    newStack = newStack.copy();
                    newStack.shrink(oldStack.getCount());
                    oldStack = ItemStack.EMPTY;
                    op = InvOperation.INSERT;
                } else {
                    oldStack.shrink(newStack.getCount());
                    newStack = ItemStack.EMPTY;
                    op = InvOperation.EXTRACT;
                }
            }

            this.getTileEntity().onChangeInventory(this, slot, op, oldStack, newStack);
            this.getTileEntity().saveChanges();
            this.previousStack = ItemStack.EMPTY;
            this.dirtyFlag = false;
        }

        super.onContentsChanged(slot);
    }

    protected boolean eventsEnabled() {
        return Platform.isServer() || this.isEnableClientEvents();
    }

    public void setMaxStackSize(int slot, int size) {
        this.maxStack[slot] = size;
    }

    public boolean isItemValid(int slot, ItemStack stack) {
        if (this.maxStack[slot] == 0) {
            return false;
        } else {
            return this.filter != null ? this.filter.allowInsert(this, slot, stack) : true;
        }
    }

    public void writeToNBT(NBTTagCompound data, String name) {
        data.setTag(name, this.serializeNBT());
    }

    public NBTTagCompound serializeNBT() {
        NBTTagList nbtTagList = new NBTTagList();

        for(int i = 0; i < this.stacks.size(); ++i) {
            ItemStack is = (ItemStack)this.stacks.get(i);
            if (!is.isEmpty()) {
                NBTTagCompound itemTag = ItemStackHelper.stackToNBT(is);
                itemTag.setInteger("Slot", i);
                nbtTagList.appendTag(itemTag);
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", this.stacks.size());
        return nbt;
    }

    public void readFromNBT(NBTTagCompound data, String name) {
        NBTTagCompound c = data.getCompoundTag(name);
        if (c != null) {
            this.readFromNBT(c);
        }

    }

    public void readFromNBT(NBTTagCompound data) {
        this.deserializeNBT(data);
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        this.setSize(nbt.hasKey("Size", 3) ? nbt.getInteger("Size") : this.stacks.size());
        NBTTagList tagList = nbt.getTagList("Items", 10);

        for(int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");
            if (slot >= 0 && slot < this.stacks.size()) {
                this.stacks.set(slot, ItemStackHelper.stackFromNBT(itemTags));
            }
        }

        this.onLoad();
    }

    public Iterator<ItemStack> iterator() {
        return Collections.unmodifiableList(super.stacks).iterator();
    }

    private boolean isEnableClientEvents() {
        return this.enableClientEvents;
    }

    public void setEnableClientEvents(boolean enableClientEvents) {
        this.enableClientEvents = enableClientEvents;
    }

    public IAEAppEngInventory getTileEntity() {
        return this.te;
    }

    public void setTileEntity(IAEAppEngInventory te) {
        this.te = te;
    }
}
