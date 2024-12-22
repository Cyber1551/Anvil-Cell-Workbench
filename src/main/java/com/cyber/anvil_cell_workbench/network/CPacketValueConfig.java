package com.cyber.anvil_cell_workbench.network;

import appeng.api.config.FuzzyMode;
import com.cyber.anvil_cell_workbench.common.container.ContainerAnvilCellWorkbench;
import com.cyber.anvil_cell_workbench.gui.RenameMode;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class CPacketValueConfig implements IMessage {

    private PacketType type;
    private String stringValue;

    public CPacketValueConfig() {
    }

    public CPacketValueConfig(final PacketType type){
        this.type = type;
        this.stringValue = "";
    }

    public CPacketValueConfig(final PacketType type, final String value) {
        this.type = type;
        this.stringValue = value;
    }


    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.type = PacketType.values()[byteBuf.readInt()];

        int valueLength = byteBuf.readInt(); // Read the length of the string
        byte[] valueBytes = new byte[valueLength];
        byteBuf.readBytes(valueBytes);       // Read the bytes into the array
        this.stringValue = new String(valueBytes, java.nio.charset.StandardCharsets.UTF_8);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.type.ordinal());

        byte[] valueBytes = this.stringValue.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byteBuf.writeInt(valueBytes.length);
        byteBuf.writeBytes(valueBytes);
    }

    public static class Handler implements IMessageHandler<CPacketValueConfig, IMessage> {

        @Nullable
        @Override
        public IMessage onMessage(CPacketValueConfig message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() -> {
                if (player.openContainer instanceof ContainerAnvilCellWorkbench) {
                    ContainerAnvilCellWorkbench te = (ContainerAnvilCellWorkbench)player.openContainer;

                    switch (message.type) {
                        case Clear:
                            te.clear();
                            break;
                        case CopyMode:
                            te.nextWorkBenchCopyMode();
                            break;
                        case Partition:
                            te.partition();
                            break;
                        case Fuzzy:
                            te.setFuzzy(FuzzyMode.valueOf(message.stringValue));
                            break;
                        case CellName:
                            te.setCustomNameText(message.stringValue);
                            break;
                        case RenameMode:
                            te.setRenameMode(RenameMode.valueOf(message.stringValue));
                            break;
//                        case CopyCellName:
//                            te.setCellName(message.copyStack, message.value);
//                            break;

                    }

                    te.detectAndSendChanges();
                }
            });
            return null;
        }

    }
}
