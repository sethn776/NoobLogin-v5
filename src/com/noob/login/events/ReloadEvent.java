package com.noob.login.events;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import com.noob.login.NoobLogin;

public class ReloadEvent
implements Listener
{

	private NoobLogin nl;

	public ReloadEvent(NoobLogin nl) {
		this.nl = nl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onServerReload1(PluginEnableEvent e)
	{
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!JoinEvent.notIn.contains(p.getName()))
				JoinEvent.notIn.add(p.getName());
			p.sendMessage(nl.getConfig()
					.getString("reloadMsg").replace("&", "§"));
		}
	}
}
