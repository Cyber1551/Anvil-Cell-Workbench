package com.cyber.advanced_cell_workbench.core.api.definitions;

import appeng.api.IAppEngApi;
import appeng.api.definitions.IDefinitions;
import appeng.api.features.IRegistryContainer;
import appeng.api.networking.IGridHelper;
import appeng.api.parts.IPartHelper;
import appeng.api.storage.IStorageHelper;
import appeng.api.util.IClientHelper;
import appeng.core.features.registries.PartModels;
import appeng.core.features.registries.RegistryContainer;


public final class RegistrationApi {
    public static final RegistrationApi INSTANCE = new RegistrationApi();

    private final IRegistryContainer registryContainer;

    private final ApiDefinitions definitions;

    private RegistrationApi() {
        this.registryContainer = new RegistryContainer();
        this.definitions = new ApiDefinitions((PartModels) this.registryContainer.partModels());

    }

    public ApiDefinitions definitions() {
        return this.definitions;
    }

    public IRegistryContainer registries() {
        return this.registryContainer;
    }
}
