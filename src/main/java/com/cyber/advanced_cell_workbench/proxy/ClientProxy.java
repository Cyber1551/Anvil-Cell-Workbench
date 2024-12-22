package com.cyber.advanced_cell_workbench.proxy;

import appeng.api.util.AEColor;
import appeng.block.AEBaseBlock;
import appeng.client.gui.AEBaseGui;
import appeng.container.interfaces.IJEIGhostIngredients;
import appeng.core.AELog;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;
import appeng.helpers.HighlighterHandler;
import appeng.helpers.IMouseWheelItem;
import appeng.hooks.TickHandler;
import appeng.util.Platform;
import com.cyber.advanced_cell_workbench.handler.ClientRegistryHandler;
import com.cyber.advanced_cell_workbench.handler.RegistryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientProxy extends CommonProxy {

    @Override
    public RegistryHandler createRegistryHandler() {
        return new ClientRegistryHandler();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
