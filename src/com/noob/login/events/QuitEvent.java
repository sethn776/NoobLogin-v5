package com.noob.login.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import com.noob.login.NoobLogin;

public class QuitEvent implements Listener {
	NoobLogin nl;

	@EventHandler
	public void onLoginLeave(PlayerQuitEvent e) {
		if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
			JoinEvent.notIn.add(e.getPlayer().getName());
		}
	}
}
