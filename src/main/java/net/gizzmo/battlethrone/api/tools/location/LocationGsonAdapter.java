package net.gizzmo.battlethrone.api.tools.location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.util.UUID;

public class LocationGsonAdapter {
    private static Gson gson;

    public LocationGsonAdapter() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = (new GsonBuilder()).registerTypeAdapter(Location.class, new LocationAdapter()).create();
        }

        return gson;
    }

    private static class LocationAdapter extends TypeAdapter<Location> {
        private LocationAdapter() {
        }

        public void write(JsonWriter var1, Location var2) throws IOException {
            var1.beginObject();
            var1.name("uuid").value(var2.getWorld().getUID().toString());
            var1.name("x").value(Double.toString(var2.getX()));
            var1.name("y").value(Double.toString(var2.getY()));
            var1.name("z").value(Double.toString(var2.getZ()));
            var1.name("yaw").value(Float.toString(var2.getYaw()));
            var1.name("pitch").value(Float.toString(var2.getPitch()));
            var1.endObject();
        }

        public Location read(JsonReader var1) throws IOException {
            DeserializedLocation var2 = new DeserializedLocation();
            var1.beginObject();

            while(var1.hasNext()) {
                switch (var1.nextName()) {
                    case "uuid":
                        var2.setWorld(Bukkit.getWorld(UUID.fromString(var1.nextString())));
                        break;
                    case "x":
                        var2.setX(var1.nextDouble());
                        break;
                    case "y":
                        var2.setY(var1.nextDouble());
                        break;
                    case "z":
                        var2.setZ(var1.nextDouble());
                        break;
                    case "yaw":
                        var2.setYaw(Float.parseFloat(var1.nextString()));
                        break;
                    case "pitch":
                        var2.setPitch(Float.parseFloat(var1.nextString()));
                }
            }

            var1.endObject();
            if (var2.isValid()) {
                return var2.getLocation();
            } else {
                return null;
            }
        }
    }

    private static class DeserializedLocation {
        World world = null;
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        float yaw = 0.0F;
        float pitch = 0.0F;

        public DeserializedLocation() {
        }

        public void setWorld(World var1) {
            this.world = var1;
        }

        public void setX(double var1) {
            this.x = var1;
        }

        public void setY(double var1) {
            this.y = var1;
        }

        public void setZ(double var1) {
            this.z = var1;
        }

        public void setYaw(float var1) {
            this.yaw = var1;
        }

        public void setPitch(float var1) {
            this.pitch = var1;
        }

        public boolean isValid() {
            return this.world != null;
        }

        public Location getLocation() {
            return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
        }
    }
}
