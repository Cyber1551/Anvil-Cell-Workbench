package com.cyber.anvil_cell_workbench.loader;

import com.cyber.anvil_cell_workbench.AnvilCellWorkbenchMod;
import com.cyber.anvil_cell_workbench.network.CPacketValueConfig;
import net.minecraftforge.fml.relauncher.Side;

public class ChannelLoader implements Runnable {
    @Override
    public void run() {
        AnvilCellWorkbenchMod.proxy.netHandler.registerMessage(new CPacketValueConfig.Handler(), CPacketValueConfig.class, 0, Side.SERVER);
    }
}
