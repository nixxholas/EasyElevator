/*    */ package io.github.easyelevator;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.Sign;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.block.Action;
/*    */ import org.bukkit.event.block.BlockRedstoneEvent;
/*    */ import org.bukkit.event.player.PlayerInteractEvent;
/*    */ 
/*    */ 
/*    */ public class EEPlayerListener
/*    */   implements Listener
/*    */ {
/*    */   EasyElevator plugin;
/*    */   
/*    */   public EEPlayerListener(EasyElevator pl)
/*    */   {
/* 21 */     this.plugin = pl;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerInteract(PlayerInteractEvent event)
/*    */   {
/* 27 */     if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
/*    */     {
/* 29 */       Player player = event.getPlayer();
/* 30 */       EEPermissionManager pm = new EEPermissionManager(player);
/* 31 */       Block clicked = event.getClickedBlock();
/* 32 */       if ((clicked.getState() instanceof Sign))
/*    */       {
/* 34 */         Sign sign = (Sign)clicked.getState();
/* 35 */         Elevator e = this.plugin.getElevator(sign);
/* 36 */         if (e != null)
/*    */         {
/* 38 */           if (((pm.has("easyelevator.call.sign")) || (pm.has("easyelevator.call.*"))) && 
/* 39 */             (e.isFloorSign(sign)))
/*    */           {
/* 41 */             e.Call(sign.getY());
/* 42 */             player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "The Elevator has been called");
/* 43 */             return;
/*    */           }
/*    */           
/* 46 */           if (e.isPlatformSign(sign))
/*    */           {
/* 48 */             e.changeFloor();
/* 49 */             return;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 54 */     if (event.getAction() == Action.LEFT_CLICK_BLOCK)
/*    */     {
/* 56 */       Player player = event.getPlayer();
/* 57 */       EEPermissionManager pm = new EEPermissionManager(player);
/* 58 */       Block clicked = event.getClickedBlock();
/* 59 */       if ((clicked.getState() instanceof Sign))
/*    */       {
/* 61 */         Sign sign = (Sign)clicked.getState();
/* 62 */         Elevator e = this.plugin.getElevator(sign);
/* 63 */         if (e != null) {
/* 64 */           if ((pm.has("easyelevator.stop.sign")) || (pm.has("easyelevator.stop.*")))
/*    */           {
/* 66 */             if (e.isPlatformSign(sign))
/*    */             {
/* 68 */               e.StopAt(Integer.parseInt(e.getPlatform().getSign().getLine(1)));
/* 69 */               player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "Stopping at floor " + Integer.parseInt(e.getPlatform().getSign().getLine(1)));
/*    */             }
/*    */           }
/*    */           else {
/* 73 */             player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "You don't have permission to do this");
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onBlockPlace(BlockRedstoneEvent event) {}
/*    */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\EEPlayerListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */