/*  1:   */ package com.noob.login.events;
/*  2:   */ 
/*  3:   */ /*  5:   */ import org.bukkit.event.EventHandler;
/*  6:   */ import org.bukkit.event.Listener;
/*  7:   */ import org.bukkit.event.player.PlayerInteractEvent;
/*  4:   */ 
/*  8:   */ 
/*  9:   */ public class ItemEvent
/* 10:   */   implements Listener
/* 11:   */ {
/* 12:   */   @EventHandler
/* 13:   */   public void onItemInteract(PlayerInteractEvent e)
/* 14:   */   {
/* 15:11 */     if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
/* 16:12 */       e.setCancelled(true);
/* 17:   */     }
/* 18:   */   }
/* 19:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     com.noob.login.events.ItemEvent
 * JD-Core Version:    0.7.0.1
 */