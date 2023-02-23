package net.gizzmo.battlethrone.event;

import net.gizzmo.battlethrone.throne.Throne;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCapturedThroneEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Throne throne;
    private final Player player;
    private boolean cancelled = false;

    public PlayerCapturedThroneEvent(Throne throne, Player player) {
        this.throne = throne;
        this.player = player;
    }

    public static HandlerList getHandlersList() {
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
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public HandlerList getHandlers() {
        return handlers;
    }
}
