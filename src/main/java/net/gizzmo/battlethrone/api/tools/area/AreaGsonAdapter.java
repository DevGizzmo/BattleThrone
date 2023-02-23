package net.gizzmo.battlethrone.api.tools.area;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.gizzmo.battlethrone.api.tools.location.LocationGsonAdapter;
import org.bukkit.Location;

import java.io.IOException;

public class AreaGsonAdapter {
    private static Gson gson;

    public AreaGsonAdapter() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = (new GsonBuilder()).registerTypeAdapter(AreaTools.class, new AreaToolsAdapter()).create();
        }

        return gson;
    }

    private static class AreaToolsAdapter extends TypeAdapter<AreaTools> {
        private AreaToolsAdapter() {
        }

        public void write(JsonWriter writer, AreaTools areaTools) {
            try {
                writer.beginObject();
                writer.name("min");
                LocationGsonAdapter.getGson().toJson(areaTools.getMin(), Location.class, writer);
                writer.name("max");
                LocationGsonAdapter.getGson().toJson(areaTools.getMax(), Location.class, writer);
                writer.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public AreaTools read(JsonReader reader) throws  IOException {
            DeserializedAreaTools areaTools = new DeserializedAreaTools();
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "min":
                        areaTools.setMin(LocationGsonAdapter.getGson().fromJson(reader, Location.class));
                        break;
                    case "max":
                        areaTools.setMax(LocationGsonAdapter.getGson().fromJson(reader, Location.class));
                        break;
                }
            }
            reader.endObject();
            return areaTools.getAreaTools();
        }
    }

    private static class DeserializedAreaTools {
        private Location min;
        private Location max;

        public DeserializedAreaTools() {
        }

        public void setMin(Location min) {
            this.min = min;
        }

        public void setMax(Location max) {
            this.max = max;
        }

        public AreaTools getAreaTools() {
            return new AreaTools(min, max);
        }
    }
}
