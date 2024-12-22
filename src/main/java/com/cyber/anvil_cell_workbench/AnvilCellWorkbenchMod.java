package com.cyber.anvil_cell_workbench;

import com.cyber.anvil_cell_workbench.proxy.CommonProxy;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = AnvilCellWorkbenchMod.MODID, name = AnvilCellWorkbenchMod.NAME, version = AnvilCellWorkbenchMod.VERSION, dependencies = "required-after:appliedenergistics2")
public class AnvilCellWorkbenchMod {
	public static final String MODID = "anvil_cell_workbench";
	public static final String NAME = "Anvil Cell Workbench";
	public static final String VERSION = "1.0.0";

	@Mod.Instance(MODID)
	public static AnvilCellWorkbenchMod INSTANCE;

	private static Logger logger;

	@SidedProxy(modId = AnvilCellWorkbenchMod.MODID, clientSide = "com.cyber.anvil_cell_workbench.proxy.ClientProxy", serverSide = "com.cyber.anvil_cell_workbench.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		logger.info("Anvil Cell Workbench - Pre Initialization ( started )");
		proxy.preInit(event);
		logger.info("Anvil Cell Workbench - Pre Initialization ( ended )");
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		logger.info("Anvil Cell Workbench - Initialization ( started )");
		proxy.init(event);
		logger.info("Anvil Cell Workbench - Initialization ( ended )");
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		logger.info("Anvil Cell Workbench - Post Initialization ( started )");
		proxy.postInit(event);
		logger.info("Anvil Cell Workbench - Post Initialization ( ended )");
	}

//	@Mod.EventBusSubscriber
//	public static class RegistrationHandler {
//		@SubscribeEvent
//		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//			logger.info("Registering recipes for Anvil Cell Workbench");
//			FluidicArmRecipes.init();
//		}
//	}

	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MODID, path);
	}
}
