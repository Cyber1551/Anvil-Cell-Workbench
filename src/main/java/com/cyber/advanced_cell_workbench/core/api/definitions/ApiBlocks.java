package com.cyber.advanced_cell_workbench.core.api.definitions;

import appeng.api.definitions.IBlockDefinition;
import appeng.api.definitions.IBlocks;
import appeng.api.definitions.ITileDefinition;
import appeng.block.misc.BlockCellWorkbench;
import appeng.bootstrap.FeatureFactory;
import appeng.bootstrap.definitions.TileEntityDefinition;
import appeng.core.features.AEFeature;
import appeng.core.features.registries.PartModels;
import appeng.tile.misc.TileCellWorkbench;
import com.cyber.advanced_cell_workbench.common.block.BlockAdvancedCellWorkbench;
import com.cyber.advanced_cell_workbench.common.tile.TileAdvancedCellWorkbench;

public class ApiBlocks {

    private final IBlockDefinition advancedCellWorkbench;

    public ApiBlocks(FeatureFactory registry, PartModels partModels){
        this.advancedCellWorkbench = registry.block("advanced_cell_workbench", BlockAdvancedCellWorkbench::new)
                .features(AEFeature.STORAGE_CELLS)
                .tileEntity(new TileEntityDefinition(TileAdvancedCellWorkbench.class))
                .build();
    }

    public IBlockDefinition advancedCellWorkbench() {
        return this.advancedCellWorkbench;
    }
}
