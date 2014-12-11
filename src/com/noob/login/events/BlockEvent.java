/*  1:   */ package com.noob.login.events;
/*  2:   */ 
/*  3:   */ /*  5:   */ import org.bukkit.event.EventHandler;
/*  6:   */ import org.bukkit.event.Listener;
/*  7:   */ import org.bukkit.event.block.BlockBreakEvent;
/*  8:   */ import org.bukkit.event.block.BlockPlaceEvent;
/*  4:   */ 
/*  9:   */ 
/* 10:   */ public class BlockEvent
/* 11:   */   implements Listener
/* 12:   */ {
/* 13:   */   @EventHandler
/* 14:   */   public void onBreakBlock(BlockBreakEvent e)
/* 15:   */   {
/* 16:12 */     if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
/* 17:13 */       e.setCancelled(true);
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   @EventHandler
/* 22:   */   public void onPlaceBlock(BlockPlaceEvent e)
/* 23:   */   {
/* 24:19 */     if (JoinEvent.notIn.contains(e.getPlayer().getName())) {
/* 25:20 */       e.setCancelled(true);
/* 26:   */     }
/* 27:   */   }
/* 28:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     com.noob.login.events.BlockEvent
 * JD-Core Version:    0.7.0.1
 */