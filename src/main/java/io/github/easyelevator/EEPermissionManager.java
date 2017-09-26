/*    */ package io.github.easyelevator;
/*    */ 
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ 
/*    */ public class EEPermissionManager
/*    */ {
/*    */   Player player;
/*  9 */   String admin = "easyelevator.admin";
/*    */   
/*    */   public EEPermissionManager(Player p)
/*    */   {
/* 13 */     this.player = p;
/*    */   }
/*    */   
/*    */   public boolean has(String permission)
/*    */   {
/* 18 */     if (isAdmin()) {
/* 19 */       return true;
/*    */     }
/* 21 */     return this.player.hasPermission(permission);
/*    */   }
/*    */   
/*    */   private boolean isAdmin()
/*    */   {
/* 26 */     if (this.player.isOp()) {
/* 27 */       return true;
/*    */     }
/* 29 */     return this.player.hasPermission(this.admin);
/*    */   }
/*    */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\EEPermissionManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */