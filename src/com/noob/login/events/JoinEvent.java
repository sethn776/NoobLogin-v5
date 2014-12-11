package com.noob.login.events;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.noob.login.NoobLogin;

public class JoinEvent implements Listener {
	public NoobLogin nl;
	public static int taskID;

	public JoinEvent(NoobLogin nl) {
		this.nl = nl;
	}

	public static ArrayList<String> notIn = new ArrayList<String>();

	@EventHandler
	public void onJOIN(final PlayerJoinEvent e) {
		notIn.add(e.getPlayer().getName());
		if(nl.pi.containsKey(e.getPlayer())){
			nl.pi.remove(e.getPlayer());
			nl.pi.put(e.getPlayer(), 0);
		} else {
			nl.pi.put(e.getPlayer(), 0);
		}


		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(nl, 
				new TimeoutTask(nl, e.getPlayer()), 0, 2*20);
		
		if(e.getPlayer().hasPermission("nooblogin.update_notify") 
				|| e.getPlayer().isOp() && nl.update){
			e.getPlayer().sendMessage(ChatColor.GOLD + "An update for NoobLogin is available: " + 
		nl.name + ", a " + nl.type + " for " + nl.version);
			e.getPlayer().sendMessage(ChatColor.GOLD + "Type /nl update to update the plugin!");
		}

		if (isInAir(e.getPlayer())) {
			e.getPlayer().setAllowFlight(true);
			e.getPlayer().setFlying(true);
			e.getPlayer()
			.sendMessage(
					ChatColor.RED
					+ "Fly Mode Enabled Due to you logging out in AIR!");
			e.getPlayer().sendMessage(
					ChatColor.GREEN
					+ "Fly mode will be disabled in 10 seconds.");
			nl.getServer().getScheduler()
			.runTaskLaterAsynchronously(nl, new Runnable() {
				public void run() {
					e.getPlayer().setAllowFlight(false);
					e.getPlayer().setFlying(false);
					e.getPlayer().sendMessage(
							ChatColor.RED + "Fly mode disabled.");
				}
			}, 20 * 10);
		}
		if (nl.getConfig().getBoolean("joinMsg") == true) {
			if (nl.pwd.getString(e.getPlayer().getName()) == null) {
				e.getPlayer().sendMessage(
						nl.getConfig().getString("notRegistered")
						.replace("&", "§"));
			} else {
				e.getPlayer().sendMessage(
						nl.getConfig().getString("notLoggedIn")
						.replace("&", "§"));
			}
		}

		if (nl.getConfig().getBoolean("Use-MySQL") == true) {
			try {
				if(nl.databaseContainsPlayer(e.getPlayer())){
					
					int prevLog = 0;

					PreparedStatement p = nl.c.prepareStatement("SELECT logins from `login_info` WHERE name=?;");
					p.setString(1, e.getPlayer().getName());
					ResultSet prs = p.executeQuery();
					prs.next();

					prevLog = prs.getInt("logins");

					PreparedStatement lu = nl.c.prepareStatement("UPDATE `login_info` SET logins=? WHERE name=?;");
					lu.setInt(1, prevLog + 1);
					lu.setString(2, e.getPlayer().getName());
					lu.executeUpdate();

					lu.close();
					p.close();
					prs.close();
				}  else {
					PreparedStatement np = nl.c.prepareStatement("INSERT INTO `login_info` VALUES (?,1);");
					np.setString(1, e.getPlayer().getName());
					np.execute();
					np.close();
				}
			} catch (SQLException de) {
				de.printStackTrace();
			} finally {
				try {
					nl.MySQL.closeConnection();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public boolean isInAir(Player p) {
		return !p.isOnGround()
				&& p.getLocation().getBlock().getRelative(BlockFace.DOWN)
						.getType() == Material.AIR
				&& p.getGameMode() != GameMode.CREATIVE;
	}
}
