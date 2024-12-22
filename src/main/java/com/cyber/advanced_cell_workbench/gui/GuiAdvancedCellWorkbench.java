package com.cyber.advanced_cell_workbench.gui;

import appeng.api.config.*;
import appeng.api.implementations.items.IUpgradeModule;
import appeng.client.gui.implementations.GuiUpgradeable;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiToggleButton;
import appeng.client.gui.widgets.MEGuiTextField;
import appeng.core.AELog;
import appeng.core.localization.GuiText;
import appeng.util.Platform;
import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.common.tile.TileAdvancedCellWorkbench;
import com.cyber.advanced_cell_workbench.gui.widget.GuiToggleButtonWithTooltip;
import com.cyber.advanced_cell_workbench.network.CPacketValueConfig;
import com.cyber.advanced_cell_workbench.network.PacketType;
import com.cyber.advanced_cell_workbench.util.TextUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class GuiAdvancedCellWorkbench extends GuiUpgradeable {

    private final ContainerAdvancedCellWorkbench workbench;

    private GuiImgButton clear;
    private GuiImgButton partition;
    private GuiToggleButton copyMode;
    private MEGuiTextField nameField;
    private GuiToggleButtonWithTooltip renameMode;

    public GuiAdvancedCellWorkbench(InventoryPlayer inventoryPlayer, TileAdvancedCellWorkbench tile) {
        super(new ContainerAdvancedCellWorkbench(inventoryPlayer, tile));
        this.workbench = (ContainerAdvancedCellWorkbench)this.inventorySlots;
        this.ySize = 251;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        super.initGui();

        //new GuiTextField(0, this.fontRenderer, this.guiLeft + 8, this.guiTop + 16, 140, this.fontRenderer.FONT_HEIGHT);
       // this.nameField = new MEGuiTextField(this.fontRenderer, this.guiLeft + 8, this.guiTop + 16, 140, this.fontRenderer.FONT_HEIGHT + 2);
        this.nameField = new MEGuiTextField(this.fontRenderer, this.guiLeft + 7, this.guiTop + 10, 140, 12);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(32);
        this.nameField.setTextColor(0xFFFFFF);
        this.nameField.setSelectionColor(0xFF008000);
        this.nameField.setVisible(true);

        if (this.workbench.getRenameMode() == RenameMode.AutoFocus) {
            nameField.setFocused(true);
        }
        //final RenameMode currentRenamemode = this.workbench.getRenameMode();
        //final boolean isAutoFocused = currentRenamemode == RenameMode.AutoFocus;
        //this.nameField.setFocused(isAutoFocused);
        //this.workbench.setIsNameFocused(this.workbench.getIsNameFocused());
        //final NameFieldMode searchModeSetting = AEConfig.instance().getConfigManager().getSetting(Settings.NAME_FIELD_MODE);

        //this.nameField.setFocused(true);
        //this.name.setText(this.workbench.getCellName());
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }


    @Override
    protected void addButtons() {
        this.clear = new GuiImgButton(this.guiLeft - 18, this.guiTop + 8, Settings.ACTIONS, ActionItems.CLOSE);
        this.partition = new GuiImgButton(this.guiLeft - 18, this.guiTop + 28, Settings.ACTIONS, ActionItems.WRENCH);
        this.copyMode = new GuiToggleButton(this.guiLeft - 18, this.guiTop + 48, 11 * 16 + 5, 12 * 16 + 5, GuiText.CopyMode.getLocal(), GuiText.CopyModeDesc.getLocal());
        this.fuzzyMode = new GuiImgButton(this.guiLeft - 18, this.guiTop + 88, Settings.FUZZY_MODE, FuzzyMode.IGNORE_ALL);
        this.renameMode = new GuiToggleButtonWithTooltip(this.guiLeft - 18, this.guiTop + 68, 2 * 16 + 4, 2 * 16 + 3,
                com.cyber.advanced_cell_workbench.gui.GuiText.RenameMode.getLocal(),
                com.cyber.advanced_cell_workbench.gui.GuiText.RenameModeDescA.getLocal(),
                com.cyber.advanced_cell_workbench.gui.GuiText.RenameModeDescB.getLocal());

        this.buttonList.add(this.fuzzyMode);
        this.buttonList.add(this.partition);
        this.buttonList.add(this.clear);
        this.buttonList.add(this.copyMode);
        this.buttonList.add(this.renameMode);
    }

    @Override
    public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        if (!this.nameField.getVisible()) {
            this.fontRenderer.drawString("Anvil Cell Workbench", 8, 6, 4210752);
        }
    }

    @Override
    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        this.handleButtonVisibility();
        this.handleNameTextFieldVisibility();

        this.bindTexture(this.getBackground());
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, 211 - 34, this.ySize);

        if (this.drawUpgrades()) {
            if (this.workbench.availableUpgrades() <= 8) {
                this.drawTexturedModalRect(offsetX + 177, offsetY, 177, 0, 35, 7 + this.workbench.availableUpgrades() * 18);
                this.drawTexturedModalRect(offsetX + 177, offsetY + (7 + (this.workbench.availableUpgrades() * 18)), 177, 151, 35, 7);
            } else if (this.workbench.availableUpgrades() <= 16) {
                this.drawTexturedModalRect(offsetX + 177, offsetY, 177, 0, 35, 7 + 8 * 18);
                this.drawTexturedModalRect(offsetX + 177, offsetY + (7 + (8) * 18), 177, 151, 35, 7);

                final int dx = this.workbench.availableUpgrades() - 8;
                this.drawTexturedModalRect(offsetX + 177 + 27, offsetY, 186, 0, 35 - 8, 7 + dx * 18);
                if (dx == 8) {
                    this.drawTexturedModalRect(offsetX + 177 + 27, offsetY + (7 + (dx) * 18), 186, 151, 35 - 8, 7);
                } else {
                    this.drawTexturedModalRect(offsetX + 177 + 27 + 4, offsetY + (7 + (dx) * 18), 186 + 4, 151, 35 - 8, 7);
                }
            } else {
                this.drawTexturedModalRect(offsetX + 177, offsetY, 177, 0, 35, 7 + 8 * 18);
                this.drawTexturedModalRect(offsetX + 177, offsetY + (7 + (8) * 18), 177, 151, 35, 7);

                this.drawTexturedModalRect(offsetX + 177 + 27, offsetY, 186, 0, 35 - 8, 7 + 8 * 18);
                this.drawTexturedModalRect(offsetX + 177 + 27, offsetY + (7 + (8) * 18), 186, 151, 35 - 8, 7);

                final int dx = this.workbench.availableUpgrades() - 16;
                this.drawTexturedModalRect(offsetX + 177 + 27 + 18, offsetY, 186, 0, 35 - 8, 7 + dx * 18);
                if (dx == 8) {
                    this.drawTexturedModalRect(offsetX + 177 + 27 + 18, offsetY + (7 + (dx) * 18), 186, 151, 35 - 8, 7);
                } else {
                    this.drawTexturedModalRect(offsetX + 177 + 27 + 18 + 4, offsetY + (7 + (dx) * 18), 186 + 4, 151, 35 - 8, 7);
                }
            }
        }

        if (this.hasToolbox()) {
            this.drawTexturedModalRect(offsetX + 178, offsetY + this.ySize - 90, 178, 161, 68, 68);
        }

        if (this.nameField != null) {
            this.nameField.drawTextBox();
        }

    }

    @Override
    protected void handleButtonVisibility() {
        this.copyMode.setState(this.workbench.getCopyMode() == CopyMode.CLEAR_ON_REMOVE);
        this.renameMode.setState(this.workbench.renameMode == RenameMode.Standard);

        boolean hasFuzzy = false;
        final IItemHandler inv = this.workbench.getCellUpgradeInventory();
        for (int x = 0; x < inv.getSlots(); x++) {
            final ItemStack is = inv.getStackInSlot(x);
            if (!is.isEmpty() && is.getItem() instanceof IUpgradeModule) {
                if (((IUpgradeModule)is.getItem()).getType(is) == Upgrades.FUZZY) {
                    hasFuzzy = true;
                }
            }
        }

        this.fuzzyMode.setVisibility(hasFuzzy);
    }

    private void handleNameTextFieldVisibility() {
        final CopyMode currentCopyMode = this.workbench.getCopyMode();
        final boolean isInCopyMode = currentCopyMode == CopyMode.KEEP_ON_REMOVE;
        boolean showNameTextbox = this.workbench.hasCell() || isInCopyMode;

        this.nameField.setVisible(showNameTextbox);
        this.nameField.setText(this.workbench.getCustomNameText());
    }

    @Override
    protected String getBackground() {
        if (this.nameField.getVisible()) {
            return "gui/cellworkbench_namefield.png";
        }

        return "gui/cellworkbench.png";
    }

    @Override
    protected boolean drawUpgrades() {
        return this.workbench.availableUpgrades() > 0;
    }

    @Override
    protected GuiText getName() {
        return GuiText.CellWorkbench;
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        try {
            if (btn == this.copyMode) {
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.CopyMode));
            } else if (btn == this.partition) {
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.Partition));
            } else if (btn == this.clear) {
                this.workbench.clear();
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.Clear));
            } else if (btn == this.fuzzyMode) {
                final boolean backwards = Mouse.isButtonDown(1);
                FuzzyMode fz = (FuzzyMode)this.fuzzyMode.getCurrentValue();
                fz = Platform.rotateEnum(fz, backwards, Settings.FUZZY_MODE.getPossibleValues());
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.Fuzzy, fz.name()));
            } else if (btn == this.renameMode) {
                RenameMode rnMode = Platform.nextEnum(this.workbench.renameMode);
                this.workbench.setRenameMode(rnMode);
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.RenameMode, rnMode.name()));
                this.reInitGui();
            } else {
                super.actionPerformed(btn);
            }
        } catch (final IOException ignored) {

        }
    }

    private void reInitGui() {
        this.buttonList.clear();
        this.initGui();
    }


    @Override
    protected void mouseClicked(int xCoord, int yCoord, int btn) throws IOException {
        if (workbench.getRenameMode() == RenameMode.Standard && !nameField.isMouseIn(xCoord, yCoord)) {
            nameField.setFocused(false);
        } else {
            nameField.mouseClicked(xCoord, yCoord, btn);
        }

        super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    protected void keyTyped(char character, int key) throws IOException {

        if (this.nameField.isFocused() && key == Keyboard.KEY_RETURN) {
            this.nameField.setFocused(false);
            return;
        }

        if (this.nameField.textboxKeyTyped(character, key)) {
            try {
                final String out = this.nameField.getText();
                final String cleaned = TextUtils.instance.cleanInputOfStyling(out);
                this.workbench.setCustomNameText(cleaned);
                AdvancedCellWorkbenchMod.proxy.netHandler.sendToServer(new CPacketValueConfig(PacketType.CellName, cleaned));
            } catch (final Exception e) {
                AELog.debug(e);
            }
        } else {
            super.keyTyped(character, key);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        // Auto-focus nameField if in AutoFocus mode and field isn't already focused
        if (this.workbench.getRenameMode() == RenameMode.AutoFocus && !nameField.isFocused() && shouldRefocus()) {
            nameField.setFocused(true);
        }
    }

    private boolean shouldRefocus() {
        // Add any additional conditions to control refocus behavior
        return true; /* conditions like GUI re-opened or cell changed */
    }
}
