package com.cyber.anvil_cell_workbench.loader;

import com.cyber.anvil_cell_workbench.AnvilCellWorkbenchMod;
import com.cyber.anvil_cell_workbench.common.block.BlockAnvilCellWorkbench;
import com.cyber.anvil_cell_workbench.handler.RegistryHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    @GameRegistry.ObjectHolder(AnvilCellWorkbenchMod.MODID + ":anvil_cell_workbench")
    public static BlockAnvilCellWorkbench ANVIL_CELL_WORKBENCH;

    public static void init(RegistryHandler regHandler) {
        regHandler.block("anvil_cell_workbench", new BlockAnvilCellWorkbench());
    }
}
