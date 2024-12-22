package com.cyber.advanced_cell_workbench.loader;

import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.items.ItemAdvancedCellWorkbench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
    public static final CreativeTabs TAB_ADVANCED_CELL_WORKBENCH = new CreativeTabs(AdvancedCellWorkbenchMod.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ADVANCED_CELL_WORKBENCH);
        }
    };

    @GameRegistry.ObjectHolder(AdvancedCellWorkbenchMod.MODID + ":advanced_cell_workbench")
    public static ItemAdvancedCellWorkbench ADVANCED_CELL_WORKBENCH;
}
