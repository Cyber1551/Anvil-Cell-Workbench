package com.cyber.advanced_cell_workbench.gui;

import appeng.api.AEApi;
import appeng.api.config.CopyMode;
import appeng.api.config.FuzzyMode;
import appeng.api.config.Settings;
import appeng.api.implementations.items.IStorageCell;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEStack;
import appeng.api.storage.data.IItemList;
import appeng.container.guisync.GuiSync;
import appeng.container.implementations.ContainerUpgradeable;
import appeng.container.slot.OptionalSlotRestrictedInput;
import appeng.container.slot.SlotFakeTypeOnly;
import appeng.container.slot.SlotRestrictedInput;
import appeng.core.AELog;
import appeng.util.Platform;
import appeng.util.helpers.ItemHandlerUtil;
import appeng.util.inv.WrapperSupplierItemHandler;
import appeng.util.iterators.NullIterator;
import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.common.tile.TileAdvancedCellWorkbench;
import com.cyber.advanced_cell_workbench.network.CPacketValueConfig;
import com.cyber.advanced_cell_workbench.network.PacketType;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import java.util.EnumSet;
import java.util.Iterator;

public class ContainerAdvancedCellWorkbench extends ContainerUpgradeable {

    private final TileAdvancedCellWorkbench workbench;

    @GuiSync(2)
    public CopyMode copyMode = CopyMode.CLEAR_ON_REMOVE;
    private ItemStack prevStack = ItemStack.EMPTY;
    private int lastUpgrades = 0;

    @GuiSync(15)
    public RenameMode renameMode = RenameMode.Standard;


    public ContainerAdvancedCellWorkbench(final InventoryPlayer ip, final TileAdvancedCellWorkbench te) {
        super(ip, te);
        this.workbench = te;
    }

    public void setFuzzy(final FuzzyMode valueOf) {
        final ICellWorkbenchItem cwi = this.workbench.getCell();
        if (cwi != null) {
            cwi.setFuzzyMode(this.workbench.getInventoryByName("cell").getStackInSlot(0), valueOf);
        }
    }

    public void nextWorkBenchCopyMode() {
        CopyMode nextEnum = Platform.nextEnum(this.getWorkbenchCopyMode());
        this.workbench.getConfigManager().putSetting(Settings.COPY_MODE, nextEnum);
    }

    private CopyMode getWorkbenchCopyMode() {
        return (CopyMode)this.workbench.getConfigManager().getSetting(Settings.COPY_MODE);
    }

    @Override
    protected int getHeight() {
        return 251;
    }

    public String getCustomNameText() {
        return this.workbench.getCustomNameText();
    }

    public void setCustomNameText(String customNameText) {
        this.workbench.setCustomNameText(customNameText);
    }

    public void setRenameMode(RenameMode renameMode) {
        this.renameMode = renameMode;
        this.workbench.renameMode = renameMode;
    }

    public RenameMode getRenameMode() {
        return this.renameMode;
    }

    @Override
    protected void setupConfig() {
        final IItemHandler cell = this.getUpgradeable().getInventoryByName("cell");
        this.addSlotToContainer(new SlotRestrictedInput(SlotRestrictedInput.PlacableItemType.WORKBENCH_CELL, cell, 0, 152, 8, this.getPlayerInv()));

        final IItemHandler inv = this.getUpgradeable().getInventoryByName("config");
        final WrapperSupplierItemHandler upgradeInventory = new WrapperSupplierItemHandler(this::getCellUpgradeInventory);

        int offset = 0;
        final int y = 29;
        final int x = 8;

        for (int w = 0; w < 7; w++) {
            for (int z = 0; z < 9; z++) {
                this.addSlotToContainer(new SlotFakeTypeOnly(inv, offset, x + z * 18, y + w * 18));
                offset++;
            }
        }

        for (int zz = 0; zz < 3; zz++){
            for (int z = 0; z < 8; z++) {
                final int iSlot = zz * 8 + z;
                this.addSlotToContainer(new OptionalSlotRestrictedInput(SlotRestrictedInput.PlacableItemType.UPGRADES, upgradeInventory, this, iSlot, 187 + zz * 18, 8 + 18 * z, iSlot, this.getInventoryPlayer()));
            }
        }
    }

    @Override
    public int availableUpgrades() {
        final ItemStack is = this.workbench.getInventoryByName("cell").getStackInSlot(0);
        if (this.prevStack != is) {
            this.prevStack = is;
            this.lastUpgrades = this.getCellUpgradeInventory().getSlots();
        }

        return this.lastUpgrades;
    }

    public boolean hasCell() {
        final ItemStack is = this.workbench.getInventoryByName("cell").getStackInSlot(0);
        return !is.isEmpty();
    }

    @Override
    public void detectAndSendChanges() {
        final ItemStack is = this.workbench.getInventoryByName("cell").getStackInSlot(0);

        if (Platform.isServer()) {
            for (final IContainerListener listener : this.listeners) {
                if (this.prevStack != is) {
                    for (final Slot s : this.inventorySlots) {
                        if (s instanceof OptionalSlotRestrictedInput) {
                            final OptionalSlotRestrictedInput sri = (OptionalSlotRestrictedInput)s;
                            listener.sendSlotContents(this, sri.slotNumber, sri.getStack());
                        }
                    }

                    if (listener instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)listener).isChangingQuantityOnly = false;
                    }
                }
            }

            this.setCopyMode(this.getWorkbenchCopyMode());
            this.setFuzzyMode(this.getWorkbenchFuzzyMode());
            this.renameMode = this.workbench.renameMode;

            //this.workbench.setRenameMode(this.getRenameMode());
        }

        this.prevStack = is;
        this.standardDetectAndSendChanges();
    }

    @Override
    public boolean isSlotEnabled(int idx) {
        return idx < this.availableUpgrades();
    }

    public IItemHandler getCellUpgradeInventory() {
        final IItemHandler upgradeInventory = this.workbench.getCellUpgradeInventory();

        return upgradeInventory != null ? upgradeInventory : EmptyHandler.INSTANCE;
    }

    @Override
    public void onUpdate(String field, Object oldValue, Object newValue) {
        if (field.equals("copyMode")) {
            this.workbench.getConfigManager().putSetting(Settings.COPY_MODE, this.getCopyMode());
        }

        super.onUpdate(field, oldValue, newValue);
    }

    public void clear() {
        ItemHandlerUtil.clear(this.getUpgradeable().getInventoryByName("config"));
        this.workbench.clearCellName();
        if (this.copyMode == CopyMode.CLEAR_ON_REMOVE) {
            final ItemStack is = this.workbench.getCellItemStack();
            this.workbench.setCustomNameText(is.getDisplayName());
        }
        this.detectAndSendChanges();
    }

    private FuzzyMode getWorkbenchFuzzyMode() {
        final ICellWorkbenchItem cwi = this.workbench.getCell();
        if (cwi != null) {
            return cwi.getFuzzyMode(this.workbench.getInventoryByName("cell").getStackInSlot(0));
        }
        return FuzzyMode.IGNORE_ALL;
    }

    public void partition() {
        final IItemHandler inv = this.getUpgradeable().getInventoryByName("config");

        final ItemStack is = this.getUpgradeable().getInventoryByName("cell").getStackInSlot(0);
        final IStorageChannel channel = is.getItem() instanceof IStorageCell ? ((IStorageCell)is.getItem()).getChannel() : AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);

        final IMEInventory cellInv = AEApi.instance().registries().cell().getCellInventory(is, null, channel);

        Iterator<IAEStack> i = new NullIterator<>();
        if (cellInv != null) {
            final IItemList list = cellInv.getAvailableItems(channel.createList());
            i = list.iterator();
        }

        for (int x = 0; x < inv.getSlots(); x++) {
            if (i.hasNext()) {
                final ItemStack g = i.next().asItemStackRepresentation();
                ItemHandlerUtil.setStackInSlot(inv, x, g);
            }
            else {
                ItemHandlerUtil.setStackInSlot(inv, x, ItemStack.EMPTY);
            }
        }

        this.detectAndSendChanges();
    }

    @Override
    public void onSlotChange(Slot slot) {
        super.onSlotChange(slot);
        if (slot.getSlotIndex() != 0) return;

        final ItemStack itemStack = slot.getStack();
        if (itemStack.isEmpty()) return;

        if (this.getWorkbenchCopyMode() == CopyMode.KEEP_ON_REMOVE) {
            itemStack.setStackDisplayName(this.getCustomNameText());
            AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.CellName, this.getCustomNameText()));
        }
    }

    public CopyMode getCopyMode() {
        return this.copyMode;
    }

    public void setCopyMode(final CopyMode copyMode) {
        this.copyMode = copyMode;
    }

}
