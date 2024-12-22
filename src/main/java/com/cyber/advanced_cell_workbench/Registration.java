package com.cyber.advanced_cell_workbench;

import appeng.api.definitions.IBlocks;
import appeng.api.features.IRegistryContainer;
import appeng.bootstrap.ICriterionTriggerRegistry;
import appeng.bootstrap.IModelRegistry;
import appeng.bootstrap.components.*;
import appeng.core.AELog;
import appeng.core.Api;
import appeng.hooks.TickHandler;
import com.cyber.advanced_cell_workbench.core.api.definitions.ApiBlocks;
import com.cyber.advanced_cell_workbench.core.api.definitions.ApiDefinitions;
import com.cyber.advanced_cell_workbench.core.api.definitions.RegistrationApi;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class Registration {

    void preInitialize(final FMLPreInitializationEvent event) {
        final RegistrationApi api = RegistrationApi.INSTANCE;

        ApiDefinitions definitions = api.definitions();
        definitions.getRegistry().getBootstrapComponents(IPreInitComponent.class).forEachRemaining(component -> component.preInitialize(event.getSide()));
    }

    public void initialize(@Nonnull final FMLInitializationEvent event)
    {
        final RegistrationApi api = RegistrationApi.INSTANCE;

        final IRegistryContainer registries = api.registries();

        ApiDefinitions definitions = api.definitions();
        definitions.getRegistry().getBootstrapComponents(IInitComponent.class).forEachRemaining(component -> component.initialize(event.getSide()));

        //MinecraftForge.EVENT_BUS.register(TickHandler.INSTANCE);


    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void modelRegistryEvent(ModelRegistryEvent event) {
        final ApiDefinitions definitions = RegistrationApi.INSTANCE.definitions();
        final IModelRegistry registry = new Registration.ModelLoaderWrapper();
        final Side side = FMLCommonHandler.instance().getEffectiveSide();
        definitions.getRegistry().getBootstrapComponents(IModelRegistrationComponent.class).forEachRemaining(b -> b.modelRegistration(side, registry));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();
        final ApiDefinitions definitions = RegistrationApi.INSTANCE.definitions();
        final Side side = FMLCommonHandler.instance().getEffectiveSide();
        definitions.getRegistry().getBootstrapComponents(IBlockRegistrationComponent.class).forEachRemaining(b -> b.blockRegistration(side, registry));
    }

    @SubscribeEvent
    public void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        final IForgeRegistry<EntityEntry> registry = event.getRegistry();
        final ApiDefinitions definitions = RegistrationApi.INSTANCE.definitions();
        definitions.getRegistry().getBootstrapComponents(IEntityRegistrationComponent.class).forEachRemaining(b -> b.entityRegistration(registry));
    }

    void postInit(final FMLPostInitializationEvent event) {
        final IRegistryContainer registries = RegistrationApi.INSTANCE.registries();
        final ApiDefinitions definitions = RegistrationApi.INSTANCE.definitions();

        final ApiBlocks blocks = definitions.getBlocks();

        definitions.getRegistry().getBootstrapComponents(IPostInitComponent.class).forEachRemaining(component -> component.postInitialize(event.getSide()));
    }

    private static class ModelLoaderWrapper implements IModelRegistry {

        @Override
        public void registerItemVariants(Item item, ResourceLocation... names) {
            ModelLoader.registerItemVariants(item, names);
        }

        @Override
        public void setCustomModelResourceLocation(Item item, int metadata, ModelResourceLocation model) {
            ModelLoader.setCustomModelResourceLocation(item, metadata, model);
        }

        @Override
        public void setCustomMeshDefinition(Item item, ItemMeshDefinition meshDefinition) {
            ModelLoader.setCustomMeshDefinition(item, meshDefinition);
        }

        @Override
        public void setCustomStateMapper(Block block, IStateMapper mapper) {
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }

    private static class CriterionTriggerRegistry implements ICriterionTriggerRegistry {
        private final Method method;

        CriterionTriggerRegistry() {
            this.method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
            this.method.setAccessible(true);
        }

        @Override
        public void register(ICriterionTrigger<? extends ICriterionInstance> trigger) {
            try {
                this.method.invoke(null, trigger);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                AELog.debug(e);
            }
        }

    }

}
