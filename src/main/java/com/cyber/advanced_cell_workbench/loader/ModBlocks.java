package com.cyber.advanced_cell_workbench.loader;

import appeng.api.definitions.ITileDefinition;
import appeng.block.misc.BlockCellWorkbench;
import appeng.bootstrap.FeatureFactory;
import appeng.bootstrap.definitions.TileEntityDefinition;
import appeng.core.features.AEFeature;
import appeng.items.parts.PartModels;
import appeng.tile.misc.TileCellWorkbench;
import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.common.block.BlockAdvancedCellWorkbench;
import com.cyber.advanced_cell_workbench.handler.RegistryHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    @GameRegistry.ObjectHolder(AdvancedCellWorkbenchMod.MODID + ":advanced_cell_workbench")
    public static BlockAdvancedCellWorkbench ADVANCED_CELL_WORKBENCH;

    public static void init(RegistryHandler regHandler) {
        regHandler.block("advanced_cell_workbench", new BlockAdvancedCellWorkbench());
    }

}
