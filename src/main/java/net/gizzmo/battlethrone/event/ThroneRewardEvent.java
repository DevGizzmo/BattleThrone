package net.gizzmo.battlethrone.event;

import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ThroneRewardEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Throne throne;
    private final Player owner;
    private boolean cancelled = false;

    public ThroneRewardEvent(Throne throne, Player owner) {
        this.throne = throne;
        this.owner = owner;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Throne getThrone() {
        return this.throne;
    }

    public Player getOwner() {
        return this.owner;
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
