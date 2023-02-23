package net.gizzmo.battlethrone.api.tools;

import net.gizzmo.battlethrone.api.tools.nms.NmsTools;
import net.gizzmo.battlethrone.api.tools.nms.NmsVersion;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {
	private static final Pattern HEX_COLOR_CODE_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");
	
    public StringTools() {
    }
    
    public static String fixColors(String chaine) {
        if (chaine == null) {
            return "";
        } else {
            if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_16)) {
                for(Matcher correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine); correspondance.find(); correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine)) {
                    String codeCouleurHexadecimal = chaine.substring(correspondance.start(), correspondance.end());
                    chaine = chaine.replace(codeCouleurHexadecimal, ChatColor.valueOf(codeCouleurHexadecimal) + "");
                }
            }

            return org.bukkit.ChatColor.translateAlternateColorCodes('&', chaine).replace("\\n", "\n");
        }
    }
    public static List<String> fixColorsList(List<String> chaines) {
    	List<String> fixColorChaines = new ArrayList<>();
        if (chaines != null) {
        	for(String chaine : chaines) {
	            if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_16)) {
		                for(Matcher correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine); correspondance.find(); correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine)) {
		                    String codeCouleurHexadecimal = chaine.substring(correspondance.start(), correspondance.end());
		                    chaine = chaine.replace(codeCouleurHexadecimal, ChatColor.valueOf(codeCouleurHexadecimal) + "");
		                }
	            }
	            fixColorChaines.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', chaine).replace("\\n", "\n"));
        	}
        }
        return fixColorChaines;
    }
    public static String[] fixColorsTableau(String[] chaines) {
        if (chaines == null) {
            return null;
        }
        String[] fixColorChaines = new String[chaines.length];
        for (int i = 0; i < chaines.length; i++) {
            String chaine = chaines[i];
            if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_16)) {
                for (Matcher correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine); correspondance.find(); correspondance = HEX_COLOR_CODE_PATTERN.matcher(chaine)) {
                    String codeCouleurHexadecimal = chaine.substring(correspondance.start(), correspondance.end());
                    chaine = chaine.replace(codeCouleurHexadecimal, ChatColor.valueOf(codeCouleurHexadecimal) + "");
                }
            }
            fixColorChaines[i] = org.bukkit.ChatColor.translateAlternateColorCodes('&', chaine).replace("\\n", "\n");
        }

        return fixColorChaines;
    }
    public static String stripColors(String chaine) {
        String stripChaine = org.bukkit.ChatColor.stripColor(fixColors(chaine));
        if (NmsTools.isNmsVersionAtLeast(NmsVersion.v1_16)) {
            stripChaine = ChatColor.stripColor(stripChaine);
        }

        return stripChaine;
    }
}
