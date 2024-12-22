package com.cyber.anvil_cell_workbench.common.tile;

import appeng.api.config.CopyMode;
import appeng.api.config.Settings;
import appeng.api.config.Upgrades;
import appeng.api.implementations.IUpgradeableHost;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.util.IConfigManager;
import appeng.tile.AEBaseTile;
import appeng.tile.inventory.AppEngInternalAEInventory;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.ConfigManager;
import appeng.util.IConfigManagerHost;
import appeng.util.Platform;
import appeng.util.helpers.ItemHandlerUtil;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.InvOperation;
import com.cyber.anvil_cell_workbench.gui.RenameMode;
import com.cyber.anvil_cell_workbench.util.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class TileAnvilCellWorkbench extends AEBaseTile implements IUpgradeableHost, IAEAppEngInventory, IConfigManagerHost {

    private static final String INVENTORY_NAME_CONFIG = "config";
    private static final String INVENTORY_NAME_CELL = "cell";

    private final AppEngInternalInventory cell = new AppEngInternalInventory(this, 1);
    private final AppEngInternalAEInventory config = new AppEngInternalAEInventory(this, 63);
    private final ConfigManager manager = new ConfigManager(this);

    private String customNameText = "";
    private IItemHandler cacheUpgrades = null;
    private IItemHandler cacheConfig = null;
    private boolean locked = false;

    public RenameMode renameMode = RenameMode.Standard;

    public TileAnvilCellWorkbench() {
        this.manager.registerSetting(Settings.COPY_MODE, CopyMode.CLEAR_ON_REMOVE);
        this.cell.setEnableClientEvents(true);
    }

    public String getCustomNameText() {
        return this.customNameText;
    }

    public void setCustomNameText(String customNameText) {
        this.customNameText = TextUtils.instance.cleanInputOfStyling(customNameText);
        final ItemStack cell = this.getCellItemStack();
        if (this.customNameText.trim().isEmpty()) {
            clearCellName();
        }
        else {
            setCellName(cell, customNameText);
        }

        this.saveChanges();
    }

    public void setCellName(ItemStack itemStack, String cellCustomName) {
        if (itemStack.isEmpty()) return;
        itemStack.setStackDisplayName(cellCustomName);
    }

    public void clearCellName() {
        final ItemStack cell = this.getCellItemStack();
        if (cell.isEmpty()) return;
        cell.clearCustomName();
    }

    public IItemHandler getCellUpgradeInventory() {
        if (this.cacheUpgrades == null) {
            this.cacheUpgrades = getOrInitializeUpgradeCache();
        }
        return this.cacheUpgrades;
    }

    private IItemHandler getOrInitializeUpgradeCache() {
        final ICellWorkbenchItem cellWorkbenchItem = this.getCell();
        if (cellWorkbenchItem != null) {
            final ItemStack cellItemStackInstance = this.cell.getStackInSlot(0);
            if (!cellItemStackInstance.isEmpty()) {
                return cellWorkbenchItem.getUpgradesInventory(cellItemStackInstance);
            }
        }
        return null;
    }

    public ICellWorkbenchItem getCell() {
        if (this.cell.getStackInSlot(0).isEmpty()) {
            return null;
        }

        if (this.cell.getStackInSlot(0).getItem() instanceof ICellWorkbenchItem) {
            return (ICellWorkbenchItem) this.cell.getStackInSlot(0).getItem();
        }

        return null;
    }

    public ItemStack getCellItemStack() {
        return this.cell.getStackInSlot(0);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.cell.writeToNBT(data, "cell");
        this.config.writeToNBT(data, "config");
        this.manager.writeToNBT(data);
        data.setString("customNameText", this.customNameText);
        data.setInteger("renameMode", this.renameMode.ordinal());
        return data;
    }

    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.cell.readFromNBT(data, "cell");
        this.config.readFromNBT(data, "config");
        this.manager.readFromNBT(data);
        this.customNameText = data.getString("customNameText");
        this.renameMode = RenameMode.values()[data.getInteger("renameMode")];
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        if (INVENTORY_NAME_CONFIG.equals(name)) {
            return this.config;
        }
        if (INVENTORY_NAME_CELL.equals(name)) {
            return this.cell;
        }
        return null;
    }

    @Override
    public int getInstalledUpgrades(final Upgrades upgrades) {
        return 0;
    }


    @Override
    public void onChangeInventory(final IItemHandler inventory, final int slot, final InvOperation operation, final ItemStack removedStack, final ItemStack newStack) {
        if (isCellInventoryChange(inventory)) {
            handleCellInventoryChange(newStack, removedStack);
            return;
        }

        if (isConfigInventoryChange(inventory)) {
            handleConfigInventoryChange();
        }
    }

    private boolean isCellInventoryChange(final IItemHandler inventory) {
        return inventory == this.cell && !this.locked;
    }

    private boolean isConfigInventoryChange(final IItemHandler inventory) {
        return inventory == this.config && !this.locked;
    }

    private void handleCellInventoryChange(final ItemStack newStack, final ItemStack removedStack) {
        this.locked = true;
        this.cacheUpgrades = null;
        this.cacheConfig = null;

        final IItemHandler configInventory = this.getCellConfigInventory();
        if (configInventory != null) {
            syncConfiguration(configInventory);
        } else {
            clearOrResetConfiguration();
        }

        updateCellName(newStack, removedStack);
        this.locked = false;
    }

    private void syncConfiguration(final IItemHandler configInventory) {
        if (isCellConfigured(configInventory)) {
            for (int i = 0; i < this.config.getSlots(); i++) {
                this.config.setStackInSlot(i, configInventory.getStackInSlot(i));
            }
        } else {
            ItemHandlerUtil.copy(this.config, configInventory, false);
        }
    }

    private boolean isCellConfigured(final IItemHandler configInventory) {
        for (int i = 0; i < configInventory.getSlots(); i++) {
            if (!configInventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void clearOrResetConfiguration() {
        if (this.manager.getSetting(Settings.COPY_MODE) == CopyMode.CLEAR_ON_REMOVE) {
            clearAllConfigSlots();
        }
    }

    private void clearAllConfigSlots() {
        for (int i = 0; i < this.config.getSlots(); i++) {
            this.config.setStackInSlot(i, ItemStack.EMPTY);
        }
        this.saveChanges();
    }

    private void updateCellName(final ItemStack newStack, final ItemStack removedStack) {
        if (Platform.isClient() && !newStack.isItemEqual(removedStack) && !newStack.isEmpty()) {
            this.setCustomNameText(newStack.getDisplayName());
        }
    }

    private void handleConfigInventoryChange() {
        this.locked = true;
        final IItemHandler configInventory = this.getCellConfigInventory();
        if (configInventory != null) {
            ItemHandlerUtil.copy(this.config, configInventory, false);
            ItemHandlerUtil.copy(configInventory, this.config, false);
        }
        this.locked = false;
    }

    private IItemHandler getCellConfigInventory() {
        if (this.cacheConfig == null) {
            this.cacheConfig = getOrInitializeConfigCache();
        }
        return this.cacheConfig;
    }

    private IItemHandler getOrInitializeConfigCache() {
        final ICellWorkbenchItem cellWorkbenchItem = this.getCell();
        if (cellWorkbenchItem != null) {
            final ItemStack cellItemStackInstance = this.cell.getStackInSlot(0);
            if (!cellItemStackInstance.isEmpty()) {
                return cellWorkbenchItem.getConfigInventory(cellItemStackInstance);
            }
        }
        return null;
    }

    @Override
    public void getDrops(World w, BlockPos pos, List<ItemStack> drops) {
        super.getDrops(w, pos, drops);
        final ItemStack itemStack = this.cell.getStackInSlot(0);
        if (!itemStack.isEmpty()) {
            drops.add(itemStack);
        }
    }

    @Override
    public IConfigManager getConfigManager() {
        return this.manager;
    }

    @Override
    public void updateSetting(IConfigManager iConfigManager, Enum anEnum, Enum anEnum1) {

    }
}
