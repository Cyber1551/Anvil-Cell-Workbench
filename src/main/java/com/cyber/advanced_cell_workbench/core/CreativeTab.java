package com.cyber.advanced_cell_workbench.core;

import appeng.api.AEApi;
import appeng.api.definitions.*;
import com.cyber.advanced_cell_workbench.core.api.definitions.ApiBlocks;
import com.cyber.advanced_cell_workbench.core.api.definitions.ApiDefinitions;
import com.cyber.advanced_cell_workbench.core.api.definitions.RegistrationApi;
import com.cyber.advanced_cell_workbench.loader.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class CreativeTab extends CreativeTabs {
    public static appeng.core.CreativeTab instance = null;

    public CreativeTab() {
        super("advanced_cell_workbench");
    }

    public static void init() {
        instance = new appeng.core.CreativeTab();
    }

    @Override
    public ItemStack getIcon() {

        return this.createIcon();
    }

    @Override
    public ItemStack createIcon() {
        final ApiDefinitions definitions = RegistrationApi.INSTANCE.definitions();
        final ApiBlocks blocks = definitions.getBlocks();

        return this.findFirst(blocks.advancedCellWorkbench());
    }

    private ItemStack findFirst(final IItemDefinition... choices) {
        for (final IItemDefinition definition : choices) {
            Optional<ItemStack> maybeIs = definition.maybeStack(1);
            if (maybeIs.isPresent()) {
                return maybeIs.get();
            }
        }

        return new ItemStack(Blocks.CHEST);
    }
}
