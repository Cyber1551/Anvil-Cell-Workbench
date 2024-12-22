package com.cyber.advanced_cell_workbench.core.registries;

import appeng.api.features.*;
import appeng.api.movable.IMovableRegistry;
import appeng.api.networking.IGridCacheRegistry;
import appeng.api.parts.IPartModels;
import appeng.api.storage.ICellRegistry;
import appeng.core.features.registries.PartModels;

public class RegistryContainer implements IRegistryContainer {

    private final IPartModels partModels = new PartModels();

    @Override
    public IMovableRegistry movable() {
        return null;
    }

    @Override
    public IGridCacheRegistry gridCache() {
        return null;
    }

    @Override
    public ISpecialComparisonRegistry specialComparison() {
        return null;
    }

    @Override
    public IWirelessTermRegistry wireless() {
        return null;
    }

    @Override
    public ICellRegistry cell() {
        return null;
    }

    @Override
    public IGrinderRegistry grinder() {
        return null;
    }

    @Override
    public IInscriberRegistry inscriber() {
        return null;
    }

    @Override
    public IChargerRegistry charger() {
        return null;
    }

    @Override
    public ILocatableRegistry locatable() {
        return null;
    }

    @Override
    public IP2PTunnelRegistry p2pTunnel() {
        return null;
    }

    @Override
    public IMatterCannonAmmoRegistry matterCannon() {
        return null;
    }

    @Override
    public IPlayerRegistry players() {
        return null;
    }

    @Override
    public IRecipeHandlerRegistry recipes() {
        return null;
    }

    @Override
    public IWorldGen worldgen() {
        return null;
    }

    @Override
    public IPartModels partModels() {
        return null;
    }
}
