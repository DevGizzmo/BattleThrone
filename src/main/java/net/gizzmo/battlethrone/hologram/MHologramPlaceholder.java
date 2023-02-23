package net.gizzmo.battlethrone.hologram;

import me.filoghost.holographicdisplays.api.placeholder.GlobalPlaceholderReplaceFunction;
import net.gizzmo.battlethrone.throne.Throne;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.api.tools.StringTools;
import net.gizzmo.battlethrone.api.tools.TimeTools;
import net.gizzmo.battlethrone.config.Settings;

public class MHologramPlaceholder implements GlobalPlaceholderReplaceFunction {
    private final BattleThrone main;

    public MHologramPlaceholder(BattleThrone main) {
        this.main = main;
    }

    @Override
    public String getReplacement(String placeholder) {
        if (placeholder != null) {
            int underscoreIndex = placeholder.indexOf("_");
            if (underscoreIndex == -1) {
                return "§cCorrecte Usage: §b{arkathrone: <throne>_value}";
            }

            String throneName = placeholder.substring(0, underscoreIndex);
            String placeholderValue = placeholder.substring(underscoreIndex + 1);
            Throne throne = this.main.getThroneManager().getThrone(throneName);

            if (throne == null) {
                return "§cThrone inconnu !";
            }

            switch (placeholderValue) {
                case "id":
                    return this.getThroneId(throne);
                case "name":
                    return this.getThroneName(throne);
                case "money_earning":
                    return this.getMoneyEarning(throne);
                case "cooldown_period":
                    return this.getCooldownPeriod(throne);
                case "owner":
                    if (throne.getSettings().getPlayerInThrone().size() > 1)
                        return StringTools.fixColors(Settings.hologramMessageContest);
                    else
                        return this.getOwnerName(throne);
                case "capture_time":
                    return TimeTools.getTimeStringFormatTwo(Long.parseLong(this.getCaptureTime(throne)));
                case "counter_task":
                    return this.getCounterTask(throne);
                default:
                    return "&cPlaceholder Inconnu !";

            }
        }

        return "§cCorrecte Usage: §b{arkathrone: <throne>_value}";
    }

    private String getThroneId(final Throne throne) {
        return String.valueOf(throne.getId());
    }

    private String getThroneName(final Throne throne) {
        return throne.getName();
    }

    private String getMoneyEarning(final Throne throne) {
        return String.valueOf(throne.getSettings().getMoneyEarning());
    }

    private String getCooldownPeriod(final Throne throne) {
        return String.valueOf(throne.getSettings().getCooldownPeriod());
    }

    private String getOwnerName(final Throne throne)  {
        return throne.getSettings().getOwner() == null ? "Empty" : throne.getSettings().getOwner().getName();
    }

    private String getCaptureTime(final Throne throne) {
        return String.valueOf(throne.getSettings().getCaptureTime());
    }

    private String getCounterTask(final Throne throne) {
        return String.valueOf(throne.getSettings().getCounterTask());
    }
}
