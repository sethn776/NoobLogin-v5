package com.noob.login.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
}
