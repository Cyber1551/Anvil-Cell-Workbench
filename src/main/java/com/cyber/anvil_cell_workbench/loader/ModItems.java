package com.cyber.anvil_cell_workbench.loader;

import com.cyber.anvil_cell_workbench.AnvilCellWorkbenchMod;
import com.cyber.anvil_cell_workbench.items.ItemAdvancedCellWorkbench;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class ModItems {
    public static final CreativeTabs TAB_ANVIL_CELL_WORKBENCH = new CreativeTabs(AnvilCellWorkbenchMod.MODID) {
        @Nonnull
        @Override
        public ItemStack createIcon() {
            return new ItemStack(new ItemAdvancedCellWorkbench());
        }
    };

//    @GameRegistry.ObjectHolder(AnvilCellWorkbenchMod.MODID + ":anvil_cell_workbench")
//    public static ItemAdvancedCellWorkbench ANVIL_CELL_WORKBENCH;
}
