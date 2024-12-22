package com.cyber.advanced_cell_workbench.gui;

import net.minecraft.util.text.translation.I18n;

public enum GuiText {
    RenameMode,
    RenameModeDescA,
    RenameModeDescB;

    private final String root;

    private GuiText() {
        this.root = "gui.advanced_cell_workbench";
    }

    private GuiText(String r) {
        this.root = r;
    }

    public String getLocal() {
        return I18n.translateToLocal(this.getUnlocalized());
    }

    public String getUnlocalized() {
        return this.root + '.' + this;
    }
}
