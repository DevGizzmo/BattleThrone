package net.gizzmo.battlethrone.api.tools.nms;

import net.gizzmo.battlethrone.api.exception.UnsupportedMinecraftVersionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NmsVersion {
    v1_7,
    v1_8,
    v1_9,
    v1_10,
    v1_11,
    v1_12,
    v1_13,
    v1_14,
    v1_15,
    v1_16,
    v1_17,
    v1_18,
    v1_19;

    private static final Pattern ONE_POINT_VERSION_NUMBER_PATTERN = Pattern.compile("v\\d_(\\d+)");

    private NmsVersion() {
    }

    public int extractVersionNumber() {
        Matcher correspondance = ONE_POINT_VERSION_NUMBER_PATTERN.matcher(this.name());
        correspondance.find();
        if (correspondance.groupCount() < 1) {
            throw new UnsupportedMinecraftVersionException();
        } else {
            return Integer.valueOf(correspondance.group(1));
        }
    }
}