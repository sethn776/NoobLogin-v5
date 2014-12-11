package com.noob.login.events;

import org.bukkit.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.noob.login.NoobLogin;

public class TimeoutTask implements Runnable {
	private NoobLogin nl;
	private Player p;

	public TimeoutTask(NoobLogin noob, Player player) {
		this.nl = noob;
		this.p = player;
	}

	public void run() {
		if (!nl.pi.containsKey(p)) {
			nl.pi.put(p, 1);
		} else {
			int i = nl.pi.get(p) + 1;
			nl.pi.remove(p);
			nl.pi.put(p, i);
		}

		if (nl.pi.containsKey(p)) {
			int i = nl.pi.get(p);
			if (i == 5) {
				p.sendMessage(ChatColor.RED + "You need to login soon, you're burning up!");
			} else if (i == 7){
				p.sendMessage(ChatColor.DARK_RED + "You have one last chance, hurry! Login!!!");
			} else if (i == 10){
				Bukkit.getLogger().info("Kicked, " + p.getName() + " for login timeout.");
				p.kickPlayer("Login timeout.");
			}
		}
	}
	
	public void cancel(){
		Bukkit.getScheduler().cancelTask(JoinEvent.taskID);
	}

}
