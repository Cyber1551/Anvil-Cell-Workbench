package com.cyber.advanced_cell_workbench.proxy;

import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.common.tile.TileAdvancedCellWorkbench;
import com.cyber.advanced_cell_workbench.handler.RegistryHandler;
import com.cyber.advanced_cell_workbench.inventory.InventoryHandler;
import com.cyber.advanced_cell_workbench.loader.ChannelLoader;
import com.cyber.advanced_cell_workbench.loader.ModBlocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy {
    public final RegistryHandler registryHandler = createRegistryHandler();
    public final SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel(AdvancedCellWorkbenchMod.MODID);

    public RegistryHandler createRegistryHandler() {
        return new RegistryHandler();
    }


    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(registryHandler);

        ModBlocks.init(registryHandler);
        //ModItems.init(registryHandler);

        GameRegistry.registerTileEntity(TileAdvancedCellWorkbench.class, "advanced_cell_workbench");

        (new ChannelLoader()).run();
    }

    public void init(FMLInitializationEvent event) {
        registryHandler.onInit();
    }

    public void postInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(AdvancedCellWorkbenchMod.INSTANCE, new InventoryHandler());
    }

}
