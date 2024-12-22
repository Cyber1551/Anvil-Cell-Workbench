package com.cyber.advanced_cell_workbench.loader;

import com.cyber.advanced_cell_workbench.AdvancedCellWorkbenchMod;
import com.cyber.advanced_cell_workbench.network.CPacketValueConfig;
import net.minecraftforge.fml.relauncher.Side;

public class ChannelLoader implements Runnable {
    @Override
    public void run() {
        int id = 0;
        //AdvancedCellWorkbenchMod.proxy.netHandler.registerMessage(new CPacketRenameCell.Handler(), CPacketRenameCell.class, id++, Side.SERVER);
        AdvancedCellWorkbenchMod.proxy.netHandler.registerMessage(new CPacketValueConfig.Handler(), CPacketValueConfig.class, id++, Side.SERVER);
    }
}
