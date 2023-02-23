package net.gizzmo.battlethrone.event;

import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ThroneCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Throne throne;
    private final Player player;
    private boolean cancelled = false;

    public ThroneCreateEvent(Throne throne, Player player) {
        this.throne = throne;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Throne getThrone() {
        return this.throne;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean var1) {
        this.cancelled = var1;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
