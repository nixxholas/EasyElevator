/*     */ package io.github.easyelevator;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.material.MaterialData;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ public class Platform
/*     */ {
/*     */   private final EasyElevator plugin;
/*     */   private final Server server;
/*     */   private World world;
/*  22 */   private int level = -1;
/*  23 */   private int min = -1;
/*  24 */   private int max = -1;
/*     */   private int xmin;
/*     */   private int zmin;
/*     */   private int xmax;
/*     */   private int zmax;
/*  29 */   private boolean isInitialized = false;
/*  30 */   private boolean isStuck = false;
/*  31 */   private Sign platformSign = null;
/*     */   private Location l1;
/*     */   private Location l2;
/*  34 */   private List<Block> platform = new ArrayList();
/*     */   
/*     */   public Platform(EasyElevator plugin, Location l1, Location l2, int min, int max)
/*     */   {
/*  38 */     this.plugin = plugin;
/*  39 */     this.server = plugin.getServer();
/*  40 */     this.min = min;
/*  41 */     this.max = max;
/*  42 */     this.world = l1.getWorld();
/*     */     
/*     */ 
/*     */ 
/*  46 */     initializePlatform(l1, l2);
/*     */   }
/*     */   
/*     */   private void initializePlatform(Location l1, Location l2)
/*     */   {
/*  51 */     int x1 = l1.getBlockX();
/*  52 */     int z1 = l1.getBlockZ();
/*     */     
/*  54 */     int x2 = l2.getBlockX();
/*  55 */     int z2 = l2.getBlockZ();
/*     */     
/*  57 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/*  58 */     if (x1 < x2)
/*     */     {
/*  60 */       xStart = x1;
/*  61 */       xEnd = x2;
/*     */     }
/*  63 */     if (x1 > x2)
/*     */     {
/*  65 */       xStart = x2;
/*  66 */       xEnd = x1;
/*     */     }
/*  68 */     if (x1 == x2)
/*     */     {
/*  70 */       xStart = x1;
/*  71 */       xEnd = x1;
/*     */     }
/*  73 */     if (z1 < z2)
/*     */     {
/*  75 */       zStart = z1;
/*  76 */       zEnd = z2;
/*     */     }
/*  78 */     if (z1 > z2)
/*     */     {
/*  80 */       zStart = z2;
/*  81 */       zEnd = z1;
/*     */     }
/*  83 */     if (z1 == z2)
/*     */     {
/*  85 */       zStart = z1;
/*  86 */       zEnd = z1;
/*     */     }
/*  88 */     xStart++;
/*  89 */     xEnd--;
/*     */     
/*  91 */     zStart++;
/*  92 */     zEnd--;
/*     */     
/*  94 */     this.xmin = xStart;
/*  95 */     this.zmin = zStart;
/*  96 */     this.xmax = xEnd;
/*  97 */     this.zmax = zEnd;
/*     */     
/*  99 */     this.l1 = this.world.getBlockAt(xStart, l1.getBlockY(), zStart).getLocation();
/* 100 */     this.l2 = this.world.getBlockAt(xEnd, l1.getBlockY(), zEnd).getLocation();
/* 101 */     for (int i = this.min; i <= this.max; i++)
/*     */     {
/* 103 */       for (int x = xStart; x <= xEnd; x++) {
/* 104 */         for (int z = zStart; z <= zEnd; z++)
/*     */         {
/* 106 */           Block tempBlock = this.world.getBlockAt(x, i, z);
/* 107 */           Block signBlock = this.world.getBlockAt(x, i + 2, z);
/* 108 */           if (tempBlock.getTypeId() == 43)
/*     */           {
/* 110 */             this.platform.add(tempBlock);
/* 111 */             if ((signBlock.getState() instanceof Sign)) {
/* 112 */               this.platformSign = ((Sign)signBlock.getState());
/*     */             }
/* 114 */             this.l1.setY(i);
/* 115 */             this.l2.setY(i);
/*     */           }
/* 117 */           else if (this.platform.size() != 0)
/*     */           {
/* 119 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 123 */       if (this.platform.size() != 0) {
/* 124 */         i = this.max + 1;
/*     */       }
/*     */     }
/* 127 */     if (this.platform.size() == 0) {
/* 128 */       return;
/*     */     }
/* 130 */     if (this.platformSign == null) {
/* 131 */       return;
/*     */     }
/* 133 */     this.isInitialized = true;
/* 134 */     this.platformSign.setLine(0, ChatColor.DARK_GRAY + "[EElevator]");
/* 135 */     this.platformSign.setLine(1, "1");
/* 136 */     this.platformSign.update();
/*     */   }
/*     */   
/*     */   public void moveDown(int lcount)
/*     */   {
/* 141 */     if (canMove(this.l1.getBlockY() - 1))
/*     */     {
/* 143 */       this.isStuck = false;
/* 144 */       if (lcount == 5)
/*     */       {
/* 146 */         for (int i = 0; i < this.platform.size(); i++)
/*     */         {
/* 148 */           Block b = (Block)this.platform.get(i);
/* 149 */           b.setTypeId(0);
/* 150 */           b = this.world.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() - 1, b.getLocation().getBlockZ());
/* 151 */           b.setTypeId(43);
/* 152 */           b.setData((byte)0);
/* 153 */           this.platform.remove(i);
/* 154 */           this.platform.add(i, b);
/* 155 */           this.l1.setY(b.getLocation().getBlockY());
/* 156 */           this.l2.setY(b.getLocation().getBlockY());
/*     */         }
/* 158 */         updateSign(this.platformSign.getY() - 1);
/*     */       }
/* 160 */       List<Player> players = this.world.getPlayers();
/* 161 */       for (Player player : players) {
/* 162 */         if (hasPlayer(player))
/*     */         {
/* 164 */           player.setVelocity(new Vector(0.0D, -0.17D, 0.0D));
/* 165 */           player.setFallDistance(0.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 171 */       this.isStuck = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void moveUp(int lcount)
/*     */   {
/* 177 */     if (canMove(this.l1.getBlockY() + 3))
/*     */     {
/* 179 */       if (lcount == 5)
/*     */       {
/* 181 */         for (int i = 0; i < this.platform.size(); i++)
/*     */         {
/* 183 */           Block b = (Block)this.platform.get(i);
/* 184 */           b.setTypeId(0);
/* 185 */           b = this.world.getBlockAt(b.getLocation().getBlockX(), b.getLocation().getBlockY() + 1, b.getLocation().getBlockZ());
/* 186 */           b.setTypeId(43);
/* 187 */           b.setData((byte)0);
/* 188 */           this.platform.remove(i);
/* 189 */           this.platform.add(i, b);
/* 190 */           this.l1.setY(b.getLocation().getBlockY());
/* 191 */           this.l2.setY(b.getLocation().getBlockY());
/*     */         }
/* 193 */         updateSign(this.platformSign.getY() + 1);
/*     */       }
/* 195 */       List<Player> players = this.world.getPlayers();
/* 196 */       for (Player player : players) {
/* 197 */         if (hasPlayer(player))
/*     */         {
/* 199 */           player.setVelocity(new Vector(0.0D, 0.17D, 0.0D));
/* 200 */           player.setFallDistance(0.0F);
/* 201 */           moveUpCorrection(player);
/*     */         }
/*     */       }
/* 204 */       this.isStuck = false;
/*     */     }
/*     */     else
/*     */     {
/* 208 */       this.isStuck = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void moveUpCorrection(Player player)
/*     */   {
/* 214 */     Location pLoc = player.getLocation();
/* 215 */     if (pLoc.getBlockY() <= this.l1.getBlockY() - 0.5D)
/*     */     {
/* 217 */       pLoc.setY(pLoc.getBlockY() + 2);
/* 218 */       player.teleport(pLoc);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hasPlayer(Player player)
/*     */   {
/* 224 */     int x = player.getLocation().getBlockX();
/* 225 */     int y = player.getLocation().getBlockY();
/* 226 */     int z = player.getLocation().getBlockZ();
/* 227 */     if (((y >= this.min + 5) || (y <= this.max + 2)) && 
/* 228 */       (z >= this.zmin) && (z <= this.zmax) && (x >= this.xmin) && (x <= this.xmax)) {
/* 229 */       return true;
/*     */     }
/*     */     
/* 232 */     return false;
/*     */   }
/*     */   
/*     */   private void updateSign(int height)
/*     */   {
/* 237 */     Block signBlock = this.world.getBlockAt(this.platformSign.getX(), height, this.platformSign.getZ());
/*     */     
/* 239 */     signBlock.setType(Material.WALL_SIGN);
/* 240 */     Sign nSign = (Sign)signBlock.getState();
/* 241 */     nSign.getData().setData(this.platformSign.getData().getData());
/* 242 */     nSign.setLine(0, this.platformSign.getLine(0));
/* 243 */     nSign.setLine(1, this.platformSign.getLine(1));
/* 244 */     nSign.setLine(2, this.platformSign.getLine(2));
/* 245 */     nSign.setLine(3, this.platformSign.getLine(3));
/* 246 */     nSign.update();
/* 247 */     this.platformSign.getBlock().setTypeId(0);
/* 248 */     this.platformSign = nSign;
/*     */   }
/*     */   
/*     */   public boolean canMove(int height)
/*     */   {
/* 253 */     int x1 = this.l1.getBlockX();
/* 254 */     int z1 = this.l1.getBlockZ();
/*     */     
/* 256 */     int x2 = this.l2.getBlockX();
/* 257 */     int z2 = this.l2.getBlockZ();
/*     */     
/* 259 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/* 260 */     if (x1 < x2)
/*     */     {
/* 262 */       xStart = x1;
/* 263 */       xEnd = x2;
/*     */     }
/* 265 */     if (x1 > x2)
/*     */     {
/* 267 */       xStart = x2;
/* 268 */       xEnd = x1;
/*     */     }
/* 270 */     if (x1 == x2)
/*     */     {
/* 272 */       xStart = x1;
/* 273 */       xEnd = x1;
/*     */     }
/* 275 */     if (z1 < z2)
/*     */     {
/* 277 */       zStart = z1;
/* 278 */       zEnd = z2;
/*     */     }
/* 280 */     if (z1 > z2)
/*     */     {
/* 282 */       zStart = z2;
/* 283 */       zEnd = z1;
/*     */     }
/* 285 */     if (z1 == z2)
/*     */     {
/* 287 */       zStart = z1;
/* 288 */       zEnd = z1;
/*     */     }
/* 290 */     for (int x = xStart; x <= xEnd; x++) {
/* 291 */       for (int z = zStart; z <= zEnd; z++)
/*     */       {
/* 293 */         Block tempBlock = this.world.getBlockAt(x, height, z);
/* 294 */         if (!tempBlock.getType().equals(Material.AIR)) {
/* 295 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 299 */     return true;
/*     */   }
/*     */   
/*     */   public void sendMessage(String message)
/*     */   {
/* 304 */     List<Player> players = this.world.getPlayers();
/* 305 */     for (Player player : players) {
/* 306 */       if (hasPlayer(player)) {
/* 307 */         player.sendMessage(message);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopTeleport()
/*     */   {
/* 314 */     List<Player> players = this.world.getPlayers();
/* 315 */     for (Player player : players) {
/* 316 */       if (hasPlayer(player))
/*     */       {
/* 318 */         Location loc = player.getLocation();
/* 319 */         loc.setY(getHeight() + 1);
/* 320 */         player.teleport(loc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Sign getSign()
/*     */   {
/* 327 */     return this.platformSign;
/*     */   }
/*     */   
/*     */   public boolean isInitialized()
/*     */   {
/* 332 */     return this.isInitialized;
/*     */   }
/*     */   
/*     */   public boolean isStuck()
/*     */   {
/* 337 */     return this.isStuck;
/*     */   }
/*     */   
/*     */   public void isStuck(boolean b)
/*     */   {
/* 342 */     this.isStuck = b;
/*     */   }
/*     */   
/*     */   public int getHeight()
/*     */   {
/* 347 */     return this.l1.getBlockY();
/*     */   }
/*     */   
/*     */   public void writeSign(int line, String message)
/*     */   {
/* 352 */     this.platformSign.setLine(line, message);
/* 353 */     this.platformSign.update();
/*     */   }
/*     */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\Platform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */