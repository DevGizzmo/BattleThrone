package net.gizzmo.battlethrone.throne.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettingsGsonAdapter {
    private static Gson gson;

    public SettingsGsonAdapter() {
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = (new GsonBuilder()).registerTypeAdapter(SettingsThrone.class, new SettingsThroneAdapter()).create();
        }

        return gson;
    }

    private static class SettingsThroneAdapter extends TypeAdapter<SettingsThrone> {
        private SettingsThroneAdapter() {
        }

        public void write(JsonWriter writer, SettingsThrone settingsThrone) {
            try {
                writer.beginObject();
                writer.name("moneyEarning");
                writer.value(settingsThrone.getMoneyEarning());
                writer.name("cooldownPeriod");
                writer.value(settingsThrone.getCooldownPeriod());
                writer.name("commands");
                writer.beginArray();
                for (Map.Entry<Integer, String> entry : settingsThrone.getCommands().entrySet()) {
                    writer.beginObject();
                    writer.name("id");
                    writer.value(entry.getKey());
                    writer.name("command");
                    writer.value(entry.getValue());
                    writer.endObject();
                }
                writer.endArray();
                writer.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public SettingsThrone read(JsonReader reader) {
            DeserializedSettingsThrone settingsThrone = new DeserializedSettingsThrone();
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "moneyEarning":
                            settingsThrone.setMoneyEarning(reader.nextInt());
                            break;
                        case "cooldownPeriod":
                            settingsThrone.setCooldownPeriod(reader.nextInt());
                            break;
                        case "commands":
                            reader.beginArray();
                            while (reader.hasNext()) {
                                reader.beginObject();
                                int id = -1;
                                String command = null;
                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case "id":
                                            id = reader.nextInt();
                                            break;
                                        case "command":
                                            command = reader.nextString();
                                            break;
                                    }
                                }
                                reader.endObject();
                                if (id != -1 && command != null) {
                                    settingsThrone.getCommands().put(id, command);
                                }
                            }
                            reader.endArray();
                            break;
                    }
                }
                reader.endObject();
                return settingsThrone.getSettingsThrone();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static class DeserializedSettingsThrone {
        private int moneyEarning;
        private int cooldownPeriod;
        private Map<Integer, String> commands;

        public DeserializedSettingsThrone() {
            this.commands = new HashMap<>();
        }

        public void setMoneyEarning(int moneyEarning) {
            this.moneyEarning = moneyEarning;
        }

        public void setCooldownPeriod(int cooldownPeriod) {
            this.cooldownPeriod = cooldownPeriod;
        }

        public Map<Integer, String> getCommands() {
            return this.commands;
        }

        public SettingsThrone getSettingsThrone() {
            return new SettingsThrone(moneyEarning, cooldownPeriod, commands);
        }
    }
}
