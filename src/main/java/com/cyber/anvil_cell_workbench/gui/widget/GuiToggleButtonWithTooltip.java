package com.cyber.anvil_cell_workbench.gui.widget;

import appeng.client.gui.widgets.ITooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.util.regex.Pattern;

public class GuiToggleButtonWithTooltip extends GuiButton implements ITooltip {
    private static final Pattern PATTERN_NEW_LINE = Pattern.compile("\\n", Pattern.LITERAL);
    private final int iconIdxOn;
    private final int iconIdxOff;

    private final String displayName;
    private final String descriptionA;
    private final String descriptionB;

    private boolean isActive;

    public GuiToggleButtonWithTooltip(final int x, final int y, final int on, final int off, final String displayName, final String descriptionA, final String descriptionB) {
        super(0, 0, 16, "");
        this.iconIdxOn = on;
        this.iconIdxOff = off;
        this.displayName = displayName;
        this.descriptionA = descriptionA;
        this.descriptionB = descriptionB;
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
    }

    public void setState(final boolean isOn) {
        this.isActive = isOn;
    }

    @Override
    public void drawButton(final Minecraft par1Minecraft, final int par2, final int par3, final float partial) {
        if (this.visible) {
            final int iconIndex = this.getIconIndex();

            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            par1Minecraft.renderEngine.bindTexture(new ResourceLocation("appliedenergistics2", "textures/guis/states.png"));
            this.hovered = par2 >= this.x && par3 >= this.y && par2 < this.x + this.width && par3 < this.y + this.height;

            final int uv_y = (int) Math.floor(iconIndex / 16);
            final int uv_x = iconIndex - uv_y * 16;

            this.drawTexturedModalRect(this.x, this.y, 256 - 16, 256 - 16, 16, 16);
            this.drawTexturedModalRect(this.x, this.y, uv_x * 16, uv_y * 16, 16, 16);
            this.mouseDragged(par1Minecraft, par2, par3);
        }
    }

    private int getIconIndex() {
        return this.isActive ? this.iconIdxOn : this.iconIdxOff;
    }

    @Override
    public String getMessage() {
        if (this.displayName != null) {
            String name = I18n.translateToLocal(this.displayName);
            if (name == null || name.isEmpty()) {
                name = this.displayName;
            }

            final String value = this.isActive ? this.getValueA() : this.getValueB();

            final StringBuilder sb = new StringBuilder(value);

            int i = sb.lastIndexOf("\n");
            if (i <= 0) {
                i = 0;
            }
            while (i + 30 < sb.length() && (i = sb.lastIndexOf(" ", i + 30)) != -1) {
                sb.replace(i, i + 1, "\n");
            }

            return name + '\n' + sb;
        }
        return null;
    }

    private String getValueA() {
        String valueA = I18n.translateToLocal(this.descriptionA);
        if (valueA == null || valueA.isEmpty()) {
            valueA = this.descriptionA;
        }

        return PATTERN_NEW_LINE.matcher(valueA).replaceAll("\n");
    }

    private String getValueB() {
        String valueB = I18n.translateToLocal(this.descriptionB);
        if (valueB == null || valueB.isEmpty()) {
            valueB = this.descriptionB;
        }

        return PATTERN_NEW_LINE.matcher(valueB).replaceAll("\n");
    }

    @Override
    public int xPos() {
        return this.x;
    }

    @Override
    public int yPos() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }
}
