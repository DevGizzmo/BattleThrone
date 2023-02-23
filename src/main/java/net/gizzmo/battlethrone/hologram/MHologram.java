package net.gizzmo.battlethrone.hologram;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.PlaceholderSetting;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.StringTools;
import net.gizzmo.battlethrone.config.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MHologram {
    private final BattleThrone main;
    private String throneName;
    private Hologram hologram;
    private Location location;
    private List<String> lines;

    public MHologram(String throneName, Location location, List<String> lines) {
        this.main = BattleThrone.getInstance();
        this.throneName = throneName;
        this.lines = lines;
        this.location = location;
    }

    public String getThroneName() {
        return this.throneName;
    }
    public Hologram getHologram() {
        return this.hologram;
    }
    public List<String> getLines() {
        return this.lines;
    }
    public Location getLocation() {
        return this.location;
    }
    public void setThroneName(String throneName) {
        this.throneName = throneName;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setLines(List<String> lines) {
        this.lines = StringTools.fixColorsList(lines);
    }
    public void init() {
        if (this.hologram != null) {
            this.hologram.delete();
        }

        this.hologram = HolographicDisplaysAPI.get(this.main).createHologram(this.location);
        this.hologram.setPlaceholderSetting(PlaceholderSetting.ENABLE_ALL);

        for (int i = 0; i < this.getLines().size(); i++) {
            if (!this.getLines().get(i).equals(Settings.hologramLines.get(i).replace("<throne>", this.throneName))) {
                List<String> lineList = Settings.hologramLines;
                lineList.replaceAll(s -> s.replaceAll("<throne>", this.throneName));

                this.lines = lineList;
                break;
            }
        }

        for (String line : this.lines) {
            LineType lineType = LineType.fromString(line);
            switch (lineType) {
                case TEXT:
                    String value = lineType.getValue(line);
                    this.hologram.getLines().appendText(value);
                    break;
                case ITEM:
                    Material material = Material.getMaterial(lineType.getValue(line).toUpperCase());
                    this.hologram.getLines().appendItem(new ItemStack(material));
                    break;
                case BLANK:
                    this.hologram.getLines().appendText("");
                    break;
            }
        }


    }

    public void delete() {
        if (this.hologram != null) {
            this.hologram.delete();
        }
    }

    public enum LineType {
        TEXT("text"),
        ITEM("item"),
        BLANK("");

        private final String key;

        LineType(String key) {
            this.key = key;
        }

        public static LineType fromString(String line) {
            if (line == null || line.isEmpty()) {
                return BLANK;
            }
            String[] parts = line.split(":", 2);
            String key = parts[0].toLowerCase();
            for (LineType lineType : values()) {
                if (lineType.key.equals(key)) {
                    return lineType;
                }
            }
            return BLANK;
        }

        public String getValue(String line) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                return parts[1];
            }
            return "";
        }
    }
}
