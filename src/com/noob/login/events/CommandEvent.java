/*  1:   */ package com.noob.login.events;
/*  2:   */ 
/*  3:   */ /*  4:   */ import org.bukkit.ChatColor;
/*  6:   */ import org.bukkit.event.EventHandler;
/*  7:   */ import org.bukkit.event.Listener;
/*  8:   */ import org.bukkit.event.player.PlayerCommandPreprocessEvent;
/*  5:   */ 
/*  9:   */ 
/* 10:   */ public class CommandEvent
/* 11:   */   implements Listener
/* 12:   */ {
/* 13:   */   @EventHandler
/* 14:   */   public void onCommand(PlayerCommandPreprocessEvent e)
/* 15:   */   {
/* 16:12 */     if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
/* 17:13 */       if ((e.getMessage().startsWith("/login")) || 
/* 18:14 */         (e.getMessage().startsWith("/register")) || 
/* 19:15 */         (e.getMessage().startsWith("/l")) || 
/* 20:16 */         (e.getMessage().startsWith("/changepass")) || 
/* 21:17 */         (e.getMessage().startsWith("/spawn")) || 
/* 22:18 */         (e.getMessage().startsWith("/hub")))
/* 23:   */       {
/* 24:19 */         e.setCancelled(false);
/* 25:   */       }
/* 26:   */       else
/* 27:   */       {
/* 28:21 */         e.setCancelled(true);
/* 29:22 */         e.getPlayer()
/* 30:23 */           .sendMessage(
/* 31:24 */           ChatColor.RED + 
/* 32:25 */           "You cannot use other commands until you login or register!");
/* 33:   */       }
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     com.noob.login.events.CommandEvent
 * JD-Core Version:    0.7.0.1
 */