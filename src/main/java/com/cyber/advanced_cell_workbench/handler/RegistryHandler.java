package com.cyber.advanced_cell_workbench.handler;

import appeng.block.AEBaseItemBlock;
import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;
import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.loader.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class RegistryHandler {

    protected final List<Pair<String, Block>> blocks = new ArrayList<>();
    protected final List<Pair<String, Item>> items = new ArrayList<>();

    public void block(String name, Block block) {
        blocks.add(Pair.of(name, block));
    }

    public void item(String name, Item item) {
        items.add(Pair.of(name, item));
    }

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> event) {
        for (Pair<String, Block> entry : blocks) {
            String key = entry.getLeft();
            Block block = entry.getRight();
            block.setRegistryName(key);
            block.setTranslationKey(AdvancedCellWorkbenchMod.MODID + ":" + key);
            block.setCreativeTab(ModItems.TAB_ADVANCED_CELL_WORKBENCH);
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> event) {
        // TODO some way to handle blocks with custom ItemBlock
        for (Pair<String, Block> entry : blocks) {
            event.getRegistry().register(initItem(entry.getLeft(), new AEBaseItemBlock(entry.getRight())));
        }
        for (Pair<String, Item> entry : items) {
            event.getRegistry().register(initItem(entry.getLeft(), entry.getRight()));
        }
    }

    private static Item initItem(String key, Item item) {
        item.setRegistryName(key);
        item.setTranslationKey(AdvancedCellWorkbenchMod.MODID + ":" + key);
        item.setCreativeTab(ModItems.TAB_ADVANCED_CELL_WORKBENCH);
        return item;
    }

    public void onInit() {
        for (Pair<String, Block> entry : blocks) {
            // respects registry overrides, i guess
            Block block = ForgeRegistries.BLOCKS.getValue(AdvancedCellWorkbenchMod.resource(entry.getKey()));
            if (block instanceof AEBaseTileBlock) {
                AEBaseTile.registerTileItem(((AEBaseTileBlock)block).getTileEntityClass(),
                        new BlockStackSrc(block, 0, ActivityState.Enabled));
            }
        }
    }

}