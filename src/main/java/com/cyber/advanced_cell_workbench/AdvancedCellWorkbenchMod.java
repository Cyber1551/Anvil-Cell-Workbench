package com.cyber.advanced_cell_workbench;

import com.cyber.advanced_cell_workbench.proxy.CommonProxy;

import com.cyber.advanced_cell_workbench.recipes.AdvancedCellWorkbenchRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = AdvancedCellWorkbenchMod.MODID, name = AdvancedCellWorkbenchMod.NAME, version = AdvancedCellWorkbenchMod.VERSION, dependencies = "required-after:appliedenergistics2")
public class AdvancedCellWorkbenchMod {
	public static final String MODID = "advanced_cell_workbench";
	public static final String NAME = "Advanced Cell Workbench";
	public static final String VERSION = "1.0.0";

	@Mod.Instance(MODID)
	public static AdvancedCellWorkbenchMod INSTANCE;

	private static Logger logger;

	@SidedProxy(modId = AdvancedCellWorkbenchMod.MODID, clientSide = "com.cyber.advanced_cell_workbench.proxy.ClientProxy", serverSide = "com.cyber.advanced_cell_workbench.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);

		//NetworkHandler.init("advanced_cell_workbench");
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			logger.info("Registering recipe for Advanced Cell Workbench");
			AdvancedCellWorkbenchRecipes.init();
		}
	}


//	@EventHandler
//	public void preInit(final FMLPreInitializationEvent event)
//	{
////		logger = event.getModLog();
////		logger.info("Registering item for Advanced Cell Workbench");
//
//		AELog.info("ADVANCED CELL WORKBENCH - Pre Initialization ( started )");
//		CreativeTab.init();
//
//		if (Platform.isClient()) {
//			AdvancedCellWorkbenchMod.proxy.preInit();
//		}
//
//		AELog.info("ADVANCED CELL WORKBENCH - Pre Initialization ( ended )");
//		//proxy.preInit(event);
//	}
//
//	@EventHandler
//	private void init(final FMLInitializationEvent event)
//	{
//		AELog.info("ADVANCED CELL WORKBENCH - Initialization ( started )");
//		AdvancedCellWorkbenchMod.proxy.init();
//		AELog.info("ADVANCED CELL WORKBENCH - Initialization ( ended )");
//	}
//
//	@EventHandler
//	private void postInit(final FMLPostInitializationEvent event)
//	{
//		AELog.info("ADVANCED CELL WORKBENCH - Post Initialization ( started )");
//		FMLCommonHandler.instance().registerCrashCallable(new IntegrationCrashEnhancement());
//		AdvancedCellWorkbenchMod.proxy.postInit();
//		NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiType.Gui_Handler);
//		//NetworkHandler.init("AE2");
//		AELog.info("ADVANCED CELL WORKBENCH - Post Initialization ( ended )");
//	}
//
//
//	@EventHandler
//	private void serverAboutToStart(final FMLServerAboutToStartEvent evt) {
//		WorldData.onServerAboutToStart(evt.getServer());
//	}
//
//	@EventHandler
//	private void serverStopping(final FMLServerStoppingEvent event) {
//		WorldData.instance().onServerStopping();
//	}
//
//	@EventHandler
//	private void serverStopped(final FMLServerStoppedEvent event) {
//		WorldData.instance().onServerStoppped();
//		TickHandler.INSTANCE.shutdown();
//	}
//
//	@EventHandler
//	private void serverStarting(final FMLServerStartingEvent evt) {
//		evt.registerServerCommand(new AECommand(evt.getServer()));
//	}

//	@EventHandler
//	public void onInit(FMLInitializationEvent event) {
//		proxy.init(event);
//	}
//
//	@EventHandler
//	public void onPostInit(FMLPostInitializationEvent event) {
//		proxy.postInit(event);
//	}
//
//	@Mod.EventBusSubscriber
//	public static class RegistrationHandler {
//		@SubscribeEvent
//		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//			logger.info("Registering recipe for Advanced Cell Workbench");
//			AdvancedCellWorkbenchRecipes.init();
//		}
//	}

	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MODID, path);
	}
}
