package net.gizzmo.battlethrone.api.legacy;

import net.gizzmo.battlethrone.api.tools.nms.NmsTools;
import net.gizzmo.battlethrone.api.tools.nms.NmsVersion;
import org.bukkit.Material;

public class LegacyItemStackHandler {
    public LegacyItemStackHandler() {
    }

    public static boolean isLava(Material material) {
        if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_13)) {
            return material.name().equals("LAVA");
        } else {
            return material.name().equals("LAVA") || material.name().equals("STATIONARY_LAVA");
        }
    }

    public static boolean isWater(Material material) {
        if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_13)) {
            return material.name().equals("WATER");
        } else {
            return material.name().equals("WATER") || material.name().equals("STATIONARY_WATER");
        }
    }
}
