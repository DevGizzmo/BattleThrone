package net.gizzmo.battlethrone.hologram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.gizzmo.battlethrone.api.tools.location.LocationGsonAdapter;
import org.bukkit.Location;

public class MHologramGsonAdapter {
    private static Gson gson;

    public MHologramGsonAdapter() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().registerTypeAdapter(MHologram.class, new MHologramAdapter()).create();
        }

        return gson;
    }

    private static class MHologramAdapter extends TypeAdapter<MHologram> {

        private MHologramAdapter() {
        }

        public void write(JsonWriter writer, MHologram hologram) throws IOException {
            writer.beginObject();
            writer.name("throne_name");
            writer.value(hologram.getThroneName());
            writer.name("location");
            LocationGsonAdapter.getGson().toJson(hologram.getLocation(), Location.class, writer);
            writer.name("lines");
            writer.beginArray();
            for (String line : hologram.getLines()) {
                writer.value(line);
            }
            writer.endArray();
            writer.endObject();
        }

        public MHologram read(JsonReader reader) throws IOException {
            DeserializedMHologram mHologram = new DeserializedMHologram();
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "throne_name":
                        mHologram.setThroneName(reader.nextString());
                        break;
                    case "location":
                        mHologram.setLocation(LocationGsonAdapter.getGson().fromJson(reader, Location.class));
                        break;
                    case "lines":
                        mHologram.setLines(readLinesArray(reader));
                        break;
                }
            }
            reader.endObject();
            return mHologram.getMHologram();
        }

        private List<String> readLinesArray(JsonReader reader) throws IOException {
            reader.beginArray();
            List<String> lines = new ArrayList<>();
            while (reader.hasNext()) {
                lines.add(reader.nextString());
            }
            reader.endArray();
            return lines;
        }
    }

    private static class DeserializedMHologram {
        private String throneName;
        private Location location;
        private List<String> lines;

        public DeserializedMHologram() {
        }

        public void setThroneName(String throneName) {
            this.throneName = throneName;
        }
        public void setLocation(Location location) {
            this.location = location;
        }
        public void setLines(List<String> lines) {
            this.lines = lines;
        }
        public MHologram getMHologram() {
            return new MHologram(throneName, location, lines);
        }
    }
}
