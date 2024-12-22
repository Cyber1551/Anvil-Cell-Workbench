package com.cyber.advanced_cell_workbench.core.api.definitions;

import appeng.bootstrap.FeatureFactory;
import appeng.core.features.registries.PartModels;

public class ApiDefinitions {
    private final ApiBlocks blocks;

    private final FeatureFactory registry = new FeatureFactory();

    public ApiDefinitions(final PartModels partModels) {
        this.blocks = new ApiBlocks(this.registry, partModels);
    }

    public FeatureFactory getRegistry() {
        return this.registry;
    }

    public ApiBlocks getBlocks() {
        return this.blocks;
    }
}
