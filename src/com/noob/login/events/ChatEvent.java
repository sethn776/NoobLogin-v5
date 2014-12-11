/*  1:   */ package com.noob.login.events;
/*  2:   */ 
/*  3:   */ /*  4:   */ import org.bukkit.ChatColor;
/*  6:   */ import org.bukkit.event.EventHandler;
/*  7:   */ import org.bukkit.event.Listener;
/*  8:   */ import org.bukkit.event.player.AsyncPlayerChatEvent;
/*  5:   */ 
/*  9:   */ 
/* 10:   */ public class ChatEvent
/* 11:   */   implements Listener
/* 12:   */ {
/* 13:   */   @EventHandler
/* 14:   */   public void onUnLoginChat(AsyncPlayerChatEvent e)
/* 15:   */   {
/* 16:12 */     if (JoinEvent.notIn.contains(e.getPlayer().getName()))
/* 17:   */     {
/* 18:13 */       e.setCancelled(true);
/* 19:14 */       e.getPlayer().sendMessage(ChatColor.RED + "You are not logged in!");
/* 20:   */     }
/* 21:   */   }
/* 22:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     com.noob.login.events.ChatEvent
 * JD-Core Version:    0.7.0.1
 */