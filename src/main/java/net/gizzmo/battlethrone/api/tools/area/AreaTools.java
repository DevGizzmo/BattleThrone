package net.gizzmo.battlethrone.api.tools.area;

import net.arkamc.arkathrone.ArkaThrone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class AreaTools {
	private Location min;
	private Location max;
	
    /**
     * Constructeur
     *
     * @param first Premiere position
     * @param second Seconde position
     */
	public AreaTools(Location first, Location second) {
        if (first == null || second == null) {
            return;
        }

        this.min = first;
        this.max = second;
	}
	
	 /**
     * Recupere la taille de l'area sur l'axe X.
     * 
     * @return Size
     */
	public int getSizeX() {
        return this.max.getBlockX() - this.min.getBlockX();
    }

    /**
     * Recupere la taille de l'area sur l'axe Y.
     * 
     * @return Size
     */
    public int getSizeY() {
        return this.max.getBlockY() - this.min.getBlockY();
    }

    /**
     * Recupere la taille de l'area sur l'axe Z.
     * 
     * @return Size
     */
    public int getSizeZ() {
        return this.max.getBlockZ() - this.min.getBlockZ();
    }

    /**
     * Recupere la position minimum.
     * 
     * @return Location
     */
    public Location getMin() {
        return new Location(this.min.getWorld(), Math.min(this.max.getX(), this.min.getX()), Math.min(this.max.getY(), this.min.getY()), Math.min(this.max.getZ(), this.min.getZ()));
    }

    /**
     * Recupere la position maximum.
     *
     * @return Location
     */
    public Location getMax() {
        return new Location(this.max.getWorld(), Math.max(this.max.getX() , this.min.getX()), Math.max(this.max.getY(), this.min.getY()), Math.max(this.max.getZ(), this.min.getZ()));
    }

    /**
     * Verifier si une position se trouve dans l'area.
     *
     * @param loc Location
     *
     * @return {@code true} si a l'interieur
     */
    public boolean isInArea(Location loc) {
        if (loc == null) {
            return false;
        }

        return loc.getX() >= this.getMin().getX() && loc.getX() <= this.getMax().getX() + 1
                && loc.getY() >= this.getMin().getY() && loc.getY() <= this.getMax().getY() + 1
                && loc.getZ() >= this.getMin().getZ() && loc.getZ() <= this.getMax().getZ() + 1;
    }

    /**
     * Verifie si une position se trouve à l'intérieur de l'area
     * a partir d'une certain distance.
     *
     * @param loc Location
     * @param range Range radius
     *
     * @return {@code true} si a l'interieur
     */
    public boolean isInLimit(Location loc, int range) {
        if (loc == null) {
            return false;
        }
        else if (loc.getX() > this.getMax().getX() + range || this.getMin().getX() - range > loc.getX()) {
            return false;
        }
        else if (loc.getY() > this.getMax().getY() + range || this.getMin().getY() - range > loc.getY()) {
            return false; 
        }
        else return !(loc.getZ() > this.getMax().getZ() + range) && !(this.getMin().getZ() - range > loc.getZ());
    }

	public static AreaTools strToArea(String loc) {
        if (loc == null) {
            return null;
        }

        String[] location = loc.split(", ");

        if(location.length == 7) {
            World world = Bukkit.getWorld(location[0]);
            Location first = new Location(world, Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]));
            Location second = new Location(world, Double.parseDouble(location[4]), Double.parseDouble(location[5]), Double.parseDouble(location[6]));
            return new AreaTools(first, second);
        }
        
        return null;
	}
	
    public static String areaTostr(AreaTools areaTools) {
        return areaTools.getMin().getWorld().getName() + ", " + areaTools.getMin().getX() + ", " + areaTools.getMin().getY() + ", " + areaTools.getMin().getZ() + ", " + areaTools.getMax().getX() + ", " + areaTools.getMax().getY() + ", " + areaTools.getMax().getZ();
    }
}

