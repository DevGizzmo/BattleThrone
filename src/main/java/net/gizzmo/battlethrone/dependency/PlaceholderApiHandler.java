package net.gizzmo.battlethrone.dependency;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.gizzmo.battlethrone.BattleThrone;
import net.gizzmo.battlethrone.throne.Throne;
import org.jetbrains.annotations.NotNull;

public class PlaceholderApiHandler extends PlaceholderExpansion {
    private BattleThrone main;

    public PlaceholderApiHandler(BattleThrone main) {
        this.main = main;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public @NotNull String getIdentifier() {
        return "battlethrone";
    }

    public @NotNull String getAuthor() {
        return this.main.getDescription().getAuthors().toString();
    }

    public @NotNull String getVersion() {
        return this.main.getDescription().getVersion();
    }

    public String onPlaceholderRequest(final Throne throne, final @NotNull String placeholder) {
        if (throne == null) {
            return "Error !";
        }

        switch (placeholder) {
            case "id":
                return this.getThroneId(throne);
            case "name":
                return this.getThroneName(throne);
            case "money_earning":
                return this.getMoneyEarning(throne);
            case "cooldown_period":
                return this.getCooldownPeriod(throne);
            case "owner":
                return this.getOwnerName(throne);
            case "capture_time":
                return this.getCaptureTime(throne);
            case "counter_task":
                return this.getCounterTask(throne);
            default:
                return "null";
        }
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
