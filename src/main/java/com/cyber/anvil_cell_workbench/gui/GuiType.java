package com.cyber.anvil_cell_workbench.gui;

import appeng.api.util.AEPartLocation;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import com.cyber.anvil_cell_workbench.common.container.ContainerAnvilCellWorkbench;
import com.cyber.anvil_cell_workbench.common.tile.TileAnvilCellWorkbench;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public enum GuiType {

    GUI_ANVIL_CELL_WORKBENCH(new TileGuiFactory<TileAnvilCellWorkbench>(TileAnvilCellWorkbench.class) {
        @Override
        protected Object createServerGui(EntityPlayer player, TileAnvilCellWorkbench inv) {
            return new ContainerAnvilCellWorkbench(player.inventory, inv);
        }

        @Override
        protected Object createClientGui(EntityPlayer player, TileAnvilCellWorkbench inv) {
            return new GuiAnvilCellWorkbench(player.inventory, inv);
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
