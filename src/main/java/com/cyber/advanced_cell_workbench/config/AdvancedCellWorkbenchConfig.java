package com.cyber.advanced_cell_workbench.config;

import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;

import net.minecraftforge.common.config.Config;

@Config(modid = AdvancedCellWorkbenchMod.MODID)
public class AdvancedCellWorkbenchConfig {
    @Config.Comment("Recipe Type. Options: none (no generated recipes), easy (2x2 crafting), normal (3x3 crafting). Default: normal")
    @Config.RequiresMcRestart
    public static String recipeType = "normal";
}
