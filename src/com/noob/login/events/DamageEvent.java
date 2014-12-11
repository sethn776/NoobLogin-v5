package com.noob.login.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {

	@EventHandler
	public void onEntityDamagePlayer(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (JoinEvent.notIn.contains(((Player) e.getEntity()).getName())) {
				if (e.getDamager() instanceof Player) {
					e.setCancelled(true);
				} else {
					e.setCancelled(true);
				}
			}
		}

	}

}
