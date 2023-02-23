package net.gizzmo.battlethrone.api.tools.protocollib.particle;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.gizzmo.battlethrone.api.tools.area.AreaTools;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ParticleTools {
    public static void playRegionParticles(Player player, AreaTools region, EnumWrappers.Particle particle, float speed, int amount) {
        World world = player.getWorld();
        int minX = region.getMin().getBlockX();
        int minY = region.getMin().getBlockY();
        int minZ = region.getMin().getBlockZ();
        int maxX = region.getMax().getBlockX() + 1;
        int maxY = region.getMax().getBlockY() + 1;
        int maxZ = region.getMax().getBlockZ() + 1;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ) {
                        Location loc = new Location(world, x, y, z);
                        sendParticlePacket(player, loc, particle, speed, amount);
                    }
                }
            }
        }
    }

    public static void sendParticlePacket(Player player, Location location, EnumWrappers.Particle particle, float speed, int amount) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.WORLD_PARTICLES);

        packet.getModifier().writeDefaults();
        packet.getParticles().write(0, particle);
        packet.getFloat().write(0, (float) location.getBlockX());
        packet.getFloat().write(1, (float) location.getBlockY());
        packet.getFloat().write(2, (float) location.getBlockZ());
        packet.getFloat().write(6, speed);
        packet.getIntegers().write(0, amount); // Number of particles
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
