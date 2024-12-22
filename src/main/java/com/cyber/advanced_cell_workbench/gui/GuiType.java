package com.cyber.advanced_cell_workbench.gui;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.exceptions.AppEngException;
import appeng.api.features.IWirelessTermHandler;
import appeng.api.implementations.guiobjects.IGuiItem;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.client.gui.AEBaseGui;
import appeng.client.gui.GuiNull;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerNull;
import appeng.container.ContainerOpenContext;
import appeng.container.implementations.ContainerCellWorkbench;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.core.sync.GuiWrapper;
import appeng.helpers.WirelessTerminalGuiObject;
import appeng.tile.misc.TileCellWorkbench;
import appeng.util.Platform;
import com.cyber.advanced_cell_workbench.common.tile.TileAdvancedCellWorkbench;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Supplier;

public enum GuiType {

    GUI_ADVANCED_CELL_WORKBENCH(new TileGuiFactory<TileAdvancedCellWorkbench>(TileAdvancedCellWorkbench.class) {
        @Override
        protected Object createServerGui(EntityPlayer player, TileAdvancedCellWorkbench inv) {
            return new ContainerAdvancedCellWorkbench(player.inventory, inv);
        }

        @Override
        protected Object createClientGui(EntityPlayer player, TileAdvancedCellWorkbench inv) {
            return new GuiAdvancedCellWorkbench(player.inventory, inv);
        }
    });

    public static final List<GuiType> VALUES = ImmutableList.copyOf(values());

    @Nullable
    public static GuiType getByOrdinal(int ordinal) {
        return ordinal < 0 || ordinal >= VALUES.size() ? null : VALUES.get(ordinal);
    }

    private GuiFactory guiFactory;
    private Supplier<GuiFactory> supplier;

    GuiType(GuiFactory guiFactory) {
        this.guiFactory = guiFactory;
    }

    GuiType(Supplier<GuiFactory> lazyFactory) {
        this.supplier = lazyFactory;
    }

    public GuiFactory getFactory() {
        if (this.guiFactory == null) {
            this.guiFactory = supplier.get();
        }
        return this.guiFactory;
    }

    public interface GuiFactory {

        @Nullable
        Object createServerGui(EntityPlayer player, World world, int x, int y, int z, EnumFacing face);

        @SideOnly(Side.CLIENT)
        @Nullable
        Object createClientGui(EntityPlayer player, World world, int x, int y, int z, EnumFacing face);

    }

    public static abstract class TileGuiFactory<T> implements GuiFactory {

        protected final Class<T> invClass;

        public TileGuiFactory(Class<T> invClass) {
            this.invClass = invClass;
        }

        @Nullable
        protected T getInventory(@Nullable TileEntity tile, EntityPlayer player, EnumFacing face, BlockPos pos) {
            return invClass.isInstance(tile) ? invClass.cast(tile) : null;
        }

        @Nullable
        @Override
        public Object createServerGui(EntityPlayer player, World world, int x, int y, int z, EnumFacing face) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            T inv = getInventory(tile, player, face, new BlockPos(x, y, z));
            if (inv == null) {
                return null;
            }
            Object gui = createServerGui(player, inv);
            if (gui instanceof AEBaseContainer) {
                ContainerOpenContext ctx = new ContainerOpenContext(inv);
                ctx.setWorld(world);
                ctx.setX(x);
                ctx.setY(y);
                ctx.setZ(z);
                ctx.setSide(AEPartLocation.fromFacing(face));
                ((AEBaseContainer)gui).setOpenContext(ctx);
            }
            return gui;
        }

        @Nullable
        protected abstract Object createServerGui(EntityPlayer player, T inv);

        @Nullable
        @Override
        public Object createClientGui(EntityPlayer player, World world, int x, int y, int z, EnumFacing face) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            T inv = getInventory(tile, player, face, new BlockPos(x, y, z));
            if (inv == null) {
                return null;
            }
            return createClientGui(player, inv);
        }

        @Nullable
        protected abstract Object createClientGui(EntityPlayer player, T inv);

    }

}
