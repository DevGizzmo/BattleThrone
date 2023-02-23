package net.gizzmo.battlethrone.api.tools.protocollib.title;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class TitleTools {
    public static void sendTitle(Player player, String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
        try {
            PacketContainer titlePacket = new PacketContainer(PacketType.Play.Server.SET_TITLE_TEXT);
            titlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(title));
            titlePacket.getIntegers().write(0, fadeInTime).write(1, stayTime).write(2, fadeOutTime);

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, titlePacket);

            if (subtitle != null) {
                PacketContainer subtitlePacket = new PacketContainer(PacketType.Play.Server.SET_SUBTITLE_TEXT);
                subtitlePacket.getChatComponents().write(0, WrappedChatComponent.fromText(subtitle));
                subtitlePacket.getIntegers().write(0, fadeInTime);
                subtitlePacket.getIntegers().write(1, stayTime);
                subtitlePacket.getIntegers().write(2, fadeOutTime);

                ProtocolLibrary.getProtocolManager().sendServerPacket(player, subtitlePacket);
            }
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}
