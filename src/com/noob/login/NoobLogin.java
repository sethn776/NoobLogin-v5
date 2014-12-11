package com.noob.login;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.noob.Server.mysql.MySQL;
import com.noob.login.ed.Cryptor;
import com.noob.login.events.BlockEvent;
import com.noob.login.events.ChatEvent;
import com.noob.login.events.CommandEvent;
import com.noob.login.events.DamageEvent;
import com.noob.login.events.ItemEvent;
import com.noob.login.events.JoinEvent;
import com.noob.login.events.MoveEvent;
import com.noob.login.events.ReloadEvent;
import com.noob.login.events.TimeoutTask;
import com.noob.updates.Updater;
import com.noob.updates.Updater.ReleaseType;

public class NoobLogin extends JavaPlugin {
	Cryptor cr = new Cryptor();
	File pwdConfig = new File(this.getDataFolder(), "passwords.yml");
	public boolean isLoggedIn = false;
	public FileConfiguration pwd = YamlConfiguration
			.loadConfiguration(pwdConfig);
	public HashMap<Player, Integer> pi = new HashMap<Player, Integer>();

	public MySQL MySQL = new MySQL(this, getConfig().getString("MySQL-Host"),
			getConfig().getString("MySQL-Port"), getConfig().getString(
					"MySQL-DBName"), getConfig().getString("MySQL-User"),
			getConfig().getString("MySQL-Pass"));
	public Connection c;

	private int fileId = 78816;
	public boolean update = false;
	public String name = "";
	public ReleaseType type = null;
	public String version = "";

	public void onEnable() {
		if (!this.pwdConfig.exists()) {
			try {
				this.pwdConfig.createNewFile();
				System.out.println("Created YAML file: passwords.yml");
			} catch (IOException e) {
				System.out.println("Cannot create YAML file: passwords.yml");
				e.printStackTrace();
			}
		}

		getConfig().addDefault("joinMsg", true);
		getConfig()
				.addDefault("reloadMsg",
						"&cA server reload has caused you to logout! Please login with /login <pass>");
		getConfig().addDefault("noPermission",
				"&cYou do not have permission to preform this command.");
		getConfig()
				.addDefault("notRegistered",
						"&4You are not registered. Please register to play! /register <pass> ");
		getConfig().addDefault("notLoggedIn",
				"&cYou are not logged in. Please login to play! /login <pass>");

		// MYSQL DEFAULTS
		getConfig().addDefault("MySQL-User", "username");
		getConfig().addDefault("MySQL-Pass", "password");
		getConfig().addDefault("MySQL-DBName", "database_name");
		getConfig().addDefault("MySQL-Port", 123);
		getConfig().addDefault("MySQL-Host", "host.name");
		getConfig().addDefault("Use-MySQL", false);
		getConfig()
				.options()
				.header("Configuration for NoobLoginPlugin! If you'd like to disable auto updating set Auto-Update to false!");
		getConfig().addDefault("Auto-Update", true);
		// END MD
		getConfig().options().copyDefaults(true);
		saveConfig();

		if (getConfig().getBoolean("Auto-Update") == false) {
			// Updater
			Updater updater = new Updater(this, this.fileId, this.getFile(),
					Updater.UpdateType.NO_DOWNLOAD, false);
			update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
			name = updater.getLatestName(); // Get the latest name
			version = updater.getLatestGameVersion(); // Get the latest game
														// version
			type = updater.getLatestType(); // Get the latest file's type
			// UpdaterEND
		} else {
			Updater auto = new Updater(this, this.fileId, this.getFile(),
					Updater.UpdateType.DEFAULT, true);
			Bukkit.getLogger().info(
					"Checking for updates..., Latest file is "
							+ auto.getLatestName());
			Bukkit.getLogger().info(
					"YOU CAN DISABLE AUTO UPDATE IN THE CONFIG!!!");
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new JoinEvent(this), this);
		pm.registerEvents(new MoveEvent(), this);
		pm.registerEvents(new BlockEvent(), this);
		pm.registerEvents(new ItemEvent(), this);
		pm.registerEvents(new ChatEvent(), this);
		pm.registerEvents(new CommandEvent(), this);
		pm.registerEvents(new ReloadEvent(this), this);
		pm.registerEvents(new DamageEvent(), this);

		if (getConfig().getBoolean("Use-MySQL") == true) {
			try {
				c = MySQL.openConnection();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

		if (l.equalsIgnoreCase("nooblogin") || l.equalsIgnoreCase("nl")) {
			if (args.length == 0) {
				s.sendMessage(ChatColor.RED
						+ "/nooblogin <check, update, mysql, disable, enable>");
				return false;
			}

			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("mysql")) {
					s.sendMessage(ChatColor.GOLD
							+ "Checking connection to the MySQL Server, Please wait...");
					if (getConfig().getBoolean("Use-MySQL") == true) {
						try {
							this.c = MySQL.openConnection();
							boolean connected = MySQL.checkConnection();
							if (connected) {
								s.sendMessage(ChatColor.GREEN
										+ "MySQL Connection successful!");
								PreparedStatement ps = this.c
										.prepareStatement("SHOW TABLES LIKE 'login_info'");
								ResultSet rs = ps.executeQuery();

								if (rs.next()) {
									s.sendMessage(ChatColor.GREEN
											+ "Found table, \"login_info\"!");
								} else {
									s.sendMessage(ChatColor.RED
											+ "Failed to find the table, \"login_info\"!");
								}

								if (!databaseContainsPlayer((Player) s)) {
									s.sendMessage(ChatColor.DARK_RED
											+ "Could not find you in the table, re-login then try again!");
									return false;
								}
								s.sendMessage(ChatColor.GOLD
										+ "Now, let's try to get some information! Please wait...");
								PreparedStatement k = this.c
										.prepareStatement("SELECT logins FROM `login_info` WHERE name=?");
								k.setString(1, s.getName());
								ResultSet r = k.executeQuery();
								r.next();
								s.sendMessage(ChatColor.GREEN
										+ "Found some info!!!");
								s.sendMessage(ChatColor.GOLD + "You have "
										+ r.getInt("logins") + " login(s)!");

								s.sendMessage(ChatColor.GOLD
										+ "MySQL Testing complete.");
							} else {
								s.sendMessage(ChatColor.RED
										+ "Problems connecting to the MySQL Server, check your details.");
							}
						} catch (SQLException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				} else if (args[0].equalsIgnoreCase("update")) {
					if (!(s.hasPermission("nooblogin.update"))) {
						s.sendMessage(ChatColor.RED
								+ "You do not have the required permissions to do this.");
						return false;
					}
					s.sendMessage(ChatColor.GOLD
							+ "Contacting update server...");
					Updater updater = new Updater(this, this.fileId,
							this.getFile(),
							Updater.UpdateType.NO_VERSION_CHECK, true);
					s.sendMessage(ChatColor.RED + "Latest file information: "
							+ updater.getLatestName() + "\nMC Version: "
							+ updater.getLatestGameVersion()
							+ "\n Check console for progress on download.");
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("check")) {

					String t = args[1];

					if (pwd.getString(t) == null) {
						s.sendMessage(ChatColor.GOLD + t
								+ " is not registered on this server.");
						return false;
					} else {
						s.sendMessage(ChatColor.GOLD + t
								+ " is registered on this server.");
						return true;
					}

				}
			}

		}

		TimeoutTask tt = new TimeoutTask(this, (Player) s);

		if (l.equalsIgnoreCase("register")) {
			if (s.hasPermission("nooblogin.register") || s.isOp()) {
				if ((s instanceof Player)) {
					Player user = (Player) s;
					if (!JoinEvent.notIn.contains(user.getName())) {
						user.sendMessage(ChatColor.GREEN
								+ "You are already logged in my good sir!");
						return false;
					}
					if (args.length == 0) {
						user.sendMessage(ChatColor.RED + "/register <PASS>");
						user.sendMessage(ChatColor.RED + "Ex. /register mypazz");
					} else if (args.length == 1) {
						if (this.pwd.getString(user.getName()) == null) {
							try {
								user.sendMessage(ChatColor.GREEN
										+ "Password set successfully!");
								user.sendMessage(ChatColor.GREEN
										+ "Your Password Is: §4" + args[0]);
								this.pwd.set(user.getName(),
										this.cr.encrypt(args[0]));
								saveConfig("passwords.yml", this.pwd);
								this.isLoggedIn = true;
								JoinEvent.notIn.remove(user.getName());
								user.sendMessage(ChatColor.RED
										+ "You are now Logged In!");
								tt.cancel();
								return false;
							} catch (Exception e) {
								System.out
										.println("An error registering a player has occurred!");
								user.sendMessage(ChatColor.RED
										+ "An error has occurred!");
								return false;
							}
						} else {
							user.sendMessage(ChatColor.RED
									+ "Your password is already set!");
							return false;
						}
					}
				}
			} else {
				s.sendMessage(getConfig().getString("noPermission").replace(
						"&", "§"));
			}
		} else {
			if ((l.equalsIgnoreCase("login")) || (l.equalsIgnoreCase("l"))) {
				if (s.hasPermission("nooblogin.login") || s.isOp()) {
					if (!(s instanceof Player)) {
						return false;
					}
					Player user = (Player) s;
					if (!JoinEvent.notIn.contains(user.getName())) {
						user.sendMessage(ChatColor.GREEN
								+ "You are already logged in my good sir!");
						return false;
					}
					if (args.length == 0) {
						user.sendMessage(ChatColor.GREEN + "/login <PASS>");
						user.sendMessage(ChatColor.GREEN + "Ex. /login mypazz");
						return false;
					}
					if (args.length != 1) {
						return false;
					}
					if (this.pwd.getString(user.getName()) == null) {
						user.sendMessage(ChatColor.RED
								+ "You are not registered");
						return false;
					}
					try {
						String es = this.cr.encrypt(args[0]);
						if (es.equals(this.pwd.getString(user.getName()))) {
							user.sendMessage(ChatColor.GREEN
									+ "You are now logged in!");
							this.isLoggedIn = true;
							JoinEvent.notIn.remove(user.getName());
							tt.cancel();
							return true;
						}
						user.sendMessage(ChatColor.RED + "Incorrect password!");
						return false;
					} catch (Exception e) {
						System.out.println("Error Logging In for user "
								+ user.getName());
						return false;
					}
				} else {
					s.sendMessage(getConfig().getString("noPermission")
							.replace("&", "§"));
				}
			}
			if ((l.equalsIgnoreCase("changepassword"))
					|| (l.equalsIgnoreCase("changepwd"))
					|| (l.equalsIgnoreCase("changepass"))) {
				if (!(s.hasPermission("nooblogin.changepwd"))) {
					s.sendMessage(getConfig().getString("noPermission")
							.replace("&", "§"));
					return false;
				}
				if ((s instanceof Player)) {
					Player user = (Player) s;
					if (isLoggedIn(user)) {
						if (args.length == 0) {
							user.sendMessage("/changepass <newpwd>");
							return false;
						}
						if (args.length == 1) {
							if (this.pwd.getString(user.getName()) != null) {
								try {
									String es = this.cr.encrypt(args[0]);
									if (!es.equals(this.pwd.getString(user
											.getName()))) {
										this.pwd.set(user.getName(), es);
										user.sendMessage(ChatColor.GREEN
												+ "Password has been changed to: §4"
												+ args[0]);
										saveConfig("passwords.yml", this.pwd);
										return false;
									}
									user.sendMessage(ChatColor.RED
											+ "You cannot change it to the same PASSWORD!");
									return false;
								} catch (Exception e) {
									System.out
											.println("Error changing password for user "
													+ user.getName());
									return false;
								}
							}
							user.sendMessage(ChatColor.RED
									+ "You are not registered!");
							return false;
						}
					} else {
						user.sendMessage(ChatColor.RED
								+ "You are not logged in!");
					}
				} else {
					if ((args.length == 0) || (args.length == 1)) {
						s.sendMessage("/changepass <player> <pass>");
						return false;
					}
					if (args.length == 2) {
						Player user = Bukkit.getServer().getPlayer(args[0]);
						String pass = args[1];
						if (this.pwd.getString(user.getName()) == null) {
							s.sendMessage(ChatColor.RED
									+ "Player not registered!");
							return false;
						}
						try {
							String es = this.cr.encrypt(pass);
							if (!es.equals(this.pwd.getString(user.getName()))) {
								this.pwd.set(user.getName(), es);
								user.sendMessage(ChatColor.GREEN + s.getName()
										+ " has changed your password to §4"
										+ pass);
								s.sendMessage(ChatColor.GREEN
										+ "Successfully changed "
										+ user.getName() + "'s password to: "
										+ pass);
								saveConfig("passwords.yml", this.pwd);
								return false;
							}
							s.sendMessage("Can't change " + user.getName()
									+ "'s password to the same thing!");
						} catch (Exception e) {
							System.out
									.println("Error changing password for user "
											+ user.getName());
							return false;
						}
					}
				}
			} else if ((l.equalsIgnoreCase("unregister"))
					&& ((s instanceof Player))) {
				if (!(s.hasPermission("nooblogin.unregister"))) {
					s.sendMessage(getConfig().getString("noPermission")
							.replace("&", "§"));
					return false;
				}
				Player user = (Player) s;
				if (args.length > 0) {
					user.sendMessage(ChatColor.RED + "/unregister");
					return false;
				}
				if (this.pwd.getString(user.getName()) == null) {
					user.sendMessage(ChatColor.RED + "You are not registered!");
					return false;
				}
				if (!isLoggedIn(user)) {
					user.sendMessage(ChatColor.RED + "You are not logged in!");
					return false;
				}
				unregister(user);
			}
		}
		return true;
	}

	public boolean isLoggedIn(Player z) {
		if (!JoinEvent.notIn.contains(z.getName())) {
			return true;
		} else {
			return false;
		}

	}

	public String Error(CommandSender user, String error) {
		return "Error " + error + " user " + user.getName();
	}

	public void register(Player user, String pass) {
		try {
			pass = this.cr.encrypt(pass);
			this.pwd.set(user.getName(), pass);
			saveConfig("passwords.yml", this.pwd);
			login(user);
			user.sendMessage(ChatColor.GREEN + "You have been registered!");
		} catch (Exception e) {
			Error(user, "registering");
			return;
		}
	}

	public void unregister(Player user) {
		try {
			this.pwd.set(user.getName(), null);
			saveConfig("passwords.yml", this.pwd);
			logout(user);
			user.sendMessage(ChatColor.RED + "You have been unregistered!");
		} catch (Exception e) {
			Error(user, "unregistering");
			return;
		}
	}

	public void login(Player user) {
		JoinEvent.notIn.remove(user.getName());
		user.sendMessage(ChatColor.GREEN + "You are now logged in!");
	}

	public void logout(Player user) {
		JoinEvent.notIn.add(user.getName());
		user.sendMessage(ChatColor.RED + "You have been logged out!");
	}

	public void saveConfig(String name, FileConfiguration config) {
		if (!name.endsWith(".yml")) {
			name = name + ".yml";
		}
		File file = new File(getDataFolder(), name);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized boolean databaseContainsPlayer(Player player) {
		try {
			PreparedStatement sql = c
					.prepareStatement("SELECT * FROM `login_info` WHERE name=?");
			sql.setString(1, player.getName());
			ResultSet rs = sql.executeQuery();
			boolean containsPlayer = rs.next();

			sql.close();
			rs.close();

			return containsPlayer;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
