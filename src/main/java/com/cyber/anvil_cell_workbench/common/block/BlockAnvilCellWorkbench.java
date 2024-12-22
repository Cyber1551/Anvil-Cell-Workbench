package com.cyber.anvil_cell_workbench.common.block;

import appeng.block.AEBaseTileBlock;
import com.cyber.anvil_cell_workbench.common.tile.TileAnvilCellWorkbench;
import com.cyber.anvil_cell_workbench.gui.GuiType;
import com.cyber.anvil_cell_workbench.inventory.InventoryHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAnvilCellWorkbench extends AEBaseTileBlock {

    public BlockAnvilCellWorkbench() {
        super(Material.IRON);
        setTileEntity(TileAnvilCellWorkbench.class);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
        }
        TileAnvilCellWorkbench tile = getTileEntity(world, pos);
        if (tile != null) {
            if (!world.isRemote) {
                InventoryHandler.openGui(player, world, pos, facing, GuiType.GUI_ANVIL_CELL_WORKBENCH);
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

}