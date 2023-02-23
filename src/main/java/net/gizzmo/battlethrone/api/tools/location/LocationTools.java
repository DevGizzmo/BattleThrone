package net.gizzmo.battlethrone.api.tools.location;

import net.gizzmo.battlethrone.api.exception.location.InvalidLocationException;
import net.gizzmo.battlethrone.api.exception.location.InvalidLocationWorldException;
import net.gizzmo.battlethrone.api.legacy.LegacyItemStackHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class LocationTools {
    public LocationTools() {
    }
    
    public static boolean isPassable(Material material) {
        Passable[] passables = Passable.values();

        for (Passable passable : passables) {
            if (passable.name().equals(material.name())) {
                return true;
            }
        }

        return false;
    }
    
    public static boolean isSafe(Location location) {
        int xCoordinate = location.getBlockX();
        int yCoordinate = location.getBlockY();
        int zCoordinate = location.getBlockZ();
        if (isPassable(location.getWorld().getBlockAt(xCoordinate, yCoordinate, zCoordinate).getType()) && isPassable(location.getWorld().getBlockAt(xCoordinate, yCoordinate + 1, zCoordinate).getType())) {
            while(true) {
                if (yCoordinate > 0) {
                    --yCoordinate;
                    Material blockMaterial = location.getWorld().getBlockAt(xCoordinate, yCoordinate, zCoordinate).getType();
                    if (!blockMaterial.isSolid()) {
                        if (!LegacyItemStackHandler.isLava(blockMaterial) && !LegacyItemStackHandler.isWater(blockMaterial) && !blockMaterial.equals(Material.FIRE)) {
                            continue;
                        }

                        return false;
                    }
                }

                return location.getBlockY() - yCoordinate <= 3 && yCoordinate != 0;
            }
        } else {
            return false;
        }
    }
    
    private static boolean isBetween(int value, int valueCheck1, int valueCheck2) {
        return value <= valueCheck1 && valueCheck1 <= valueCheck2 || value >= valueCheck1 && valueCheck1 >= valueCheck2;
    }

    public static boolean isLocationInside(Location location, Location corner1, Location corner2) {
        return isBetween(location.getBlockX(), corner2.getBlockX(), corner1.getBlockX()) && isBetween(location.getBlockY(), corner2.getBlockY(), corner1.getBlockY()) && isBetween(location.getBlockZ(), corner2.getBlockZ(), corner1.getBlockZ());
    }
    
    public static Location deserialize(String serializedLocation) {
        if (serializedLocation == null) {
            throw new InvalidLocationException();
        } else {
            String[] locationParts = serializedLocation.split(":");
            if (locationParts.length < 6) {
                throw new InvalidLocationException();
            } else {
                World world = Bukkit.getWorld(locationParts[0]);
                if (world == null) {
                    throw new InvalidLocationWorldException();
                } else {
                    return new Location(world, Double.parseDouble(locationParts[1]), Double.parseDouble(locationParts[2]), Double.parseDouble(locationParts[3]), Float.parseFloat(locationParts[4]), Float.parseFloat(locationParts[5]));
                }
            }
        }
    }
	
    public static String serialize(Location loc) {
        return loc == null ? "" : loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
    }
	
	private enum Passable {
        AIR,
        BROWN_MUSHROOM,
        CARPET,
        LONG_GRASS,
        LADDER,
        RED_MUSHROOM,
        YELLOW_FLOWER,
        RED_ROSE,
        DOUBLE_PLANT,
        ROSE,
        POWERED_RAIL,
        DETECTOR_RAIL,
        RAILS,
        ACTIVATOR_RAIL,
        WOOD_BUTTON,
        STONE_BUTTON,
        VINE,
        LEVER,
        BANNER,
        ARMOR_STAND,
        SNOW,
        REDSTONE_TORCH_OFF,
        REDSTONE_TORCH_ON,
        TORCH,
        WALL_SIGN,
        SIGN,
        BUTTON;

        Passable() {
        }
    }
}
