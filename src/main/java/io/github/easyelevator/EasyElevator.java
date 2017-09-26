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
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ 
/*     */ public class EasyElevator extends org.bukkit.plugin.java.JavaPlugin
/*     */ {
/*  18 */   private EEPlayerListener pl = new EEPlayerListener(this);
/*  19 */   private EEConfiguration config = new EEConfiguration();
/*  20 */   public List<Elevator> elevators = new ArrayList();
/*  21 */   private int MaxPerimeter = 24;
/*  22 */   private int MaxFloors = 10;
/*  23 */   private boolean ArrivalSound = true;
/*  24 */   private boolean ArrivalMessage = true;
/*  25 */   private String BlockBorder = "41";
/*  26 */   private String BlockFloor = "42";
/*  27 */   private String BlockOutputDoor = "35:14";
/*  28 */   private String BlockOutputFloor = "35:1";
/*     */   
/*     */ 
/*     */ 
/*     */   public void onEnable()
/*     */   {
/*  34 */     PluginManager pm = getServer().getPluginManager();
/*  35 */     pm.registerEvents(this.pl, this);
/*  36 */     this.config.createConfig();
/*  37 */     this.MaxPerimeter = this.config.getMaxPerimeter();
/*  38 */     this.MaxFloors = this.config.getMaxFloors();
/*  39 */     this.ArrivalSound = this.config.getArrivalSound();
/*  40 */     this.ArrivalMessage = this.config.getArrivalMessage();
/*  41 */     this.BlockBorder = this.config.getBlock("Border");
/*  42 */     this.BlockFloor = this.config.getBlock("Floor");
/*  43 */     this.BlockOutputFloor = this.config.getBlock("OutputFloor");
/*  44 */     this.BlockOutputDoor = this.config.getBlock("OutputDoor");
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/*  49 */     for (Elevator e : this.elevators) {
/*  50 */       if (e.currentFloor != null) {
/*  51 */         e.currentFloor.switchRedstoneFloorOn(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel1, String[] args)
/*     */   {
/*  58 */     if (((commandLabel1.equals("elv")) || (commandLabel1.equals("eelevator"))) && 
/*  59 */       ((sender instanceof Player)))
/*     */     {
/*  61 */       Player player = (Player)sender;
/*  62 */       EEPermissionManager pm = new EEPermissionManager(player);
/*  63 */       if (args.length == 1)
/*     */       {
/*  65 */         if (args[0].equals("reload")) {
/*  66 */           if (pm.has("easyelevator.reload"))
/*     */           {
/*  68 */             this.MaxPerimeter = this.config.getMaxPerimeter();
/*  69 */             this.MaxFloors = this.config.getMaxFloors();
/*  70 */             this.ArrivalSound = this.config.getArrivalSound();
/*  71 */             this.ArrivalMessage = this.config.getArrivalMessage();
/*  72 */             this.BlockBorder = this.config.getBlock("Border");
/*  73 */             this.BlockFloor = this.config.getBlock("Floor");
/*  74 */             this.BlockOutputFloor = this.config.getBlock("OutputFloor");
/*  75 */             this.BlockOutputDoor = this.config.getBlock("OutputDoor");
/*  76 */             for (Elevator e : this.elevators) {
/*  77 */               if (e.currentFloor != null) {
/*  78 */                 e.currentFloor.switchRedstoneFloorOn(false);
/*     */               }
/*     */             }
/*  81 */             this.elevators.clear();
/*  82 */             player.sendMessage(ChatColor.DARK_GRAY + 
/*  83 */               "[EElevator] " + ChatColor.GRAY + 
/*  84 */               "The plugin has been reloaded");
/*     */           }
/*     */           else
/*     */           {
/*  88 */             player.sendMessage(ChatColor.DARK_GRAY + 
/*  89 */               "[EElevator] " + ChatColor.GRAY + 
/*  90 */               "You don't have permission to do this");
/*     */           }
/*     */         }
/*  93 */         if (args[0].equals("call")) {
/*  94 */           if ((pm.has("easyelevator.call.cmd")) || (pm.has("easyelevator.call.*")))
/*     */           {
/*  96 */             boolean success = Call(player);
/*  97 */             if (success) {
/*  98 */               player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "The Elevator has been called");
/*     */             } else {
/* 100 */               player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "No Elevator in range");
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 105 */             player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "You don't have permission to do this");
/*     */           }
/*     */         }
/* 108 */         if (args[0].equals("stop")) {
/* 109 */           if ((pm.has("easyelevator.stop.cmd")) || (pm.has("easyelevator.stop.*"))) {
/* 110 */             for (int i = 0; i < this.elevators.size(); i++)
/*     */             {
/* 112 */               Elevator e = (Elevator)this.elevators.get(i);
/* 113 */               if (e.isInElevator(player))
/*     */               {
/* 115 */                 int target = e.getFloorNumberFromHeight(e.getNextFloorHeight_2());
/* 116 */                 if (target != -1)
/*     */                 {
/* 118 */                   e.addStops(target);
/* 119 */                   player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "Stopping at floor " + target);
/* 120 */                   return true;
/*     */                 }
/*     */               }
/*     */             }
/*     */           } else {
/* 125 */             player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "You don't have permission to do this");
/*     */           }
/*     */         }
/*     */       }
/* 129 */       if (args.length == 2) {
/* 130 */         if (args[0].equals("stop"))
/*     */         {
/* 132 */           if ((pm.has("easyelevator.stop.cmd")) || (pm.has("easyelevator.stop.*"))) {
/*     */             try
/*     */             {
/* 135 */               int target = Integer.parseInt(args[1]);
/* 136 */               for (int i = 0; i < this.elevators.size(); i++)
/*     */               {
/* 138 */                 Elevator e = (Elevator)this.elevators.get(i);
/* 139 */                 if (e.isInElevator(player))
/*     */                 {
/* 141 */                   if ((target > e.getFloors().size()) || (target < 1))
/*     */                   {
/* 143 */                     player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "Floor '" + target + "' is not in range");
/* 144 */                     return true;
/*     */                   }
/* 146 */                   e.addStops(target);
/* 147 */                   player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "Stopping at floor " + target);
/* 148 */                   i = this.elevators.size();
/*     */                 }
/*     */               }
/*     */             }
/*     */             catch (Exception ex)
/*     */             {
/* 154 */               player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "Floor '" + args[1] + "' is not a valid value");
/* 155 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 160 */           player.sendMessage(ChatColor.DARK_GRAY + "[EElevator] " + ChatColor.GRAY + "You don't have permission to do this");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 165 */     return true;
/*     */   }
/*     */   
/*     */   private boolean Call(Player player)
/*     */   {
/* 170 */     org.bukkit.block.Sign sign = getSurroundingElevatorSign(player);
/* 171 */     if (sign != null)
/*     */     {
/* 173 */       Elevator e = getElevator(sign);
/* 174 */       if (e != null)
/*     */       {
/* 176 */         e.Call(sign.getY());
/* 177 */         return true;
/*     */       }
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */   
/*     */   private org.bukkit.block.Sign getSurroundingElevatorSign(Player player)
/*     */   {
/* 185 */     Block tempBlock = null;
/* 186 */     World world = player.getWorld();
/* 187 */     Location loc = player.getLocation();
/*     */     
/* 189 */     Location l1 = null;Location l2 = null;
/* 190 */     l1 = loc;
/* 191 */     l2 = loc;
/*     */     
/* 193 */     int z = 0;
/*     */     
/* 195 */     int x1 = l1.getBlockX();
/* 196 */     int y1 = l1.getBlockY();
/* 197 */     int z1 = l1.getBlockZ();
/*     */     
/* 199 */     int x2 = l2.getBlockX();
/* 200 */     int y2 = l2.getBlockY();
/* 201 */     int z2 = l2.getBlockZ();
/*     */     
/* 203 */     int xStart = 0;int xEnd = 0;int yStart = 0;int yEnd = 0;int zStart = 0;int zEnd = 0;
/* 204 */     if (x1 < x2)
/*     */     {
/* 206 */       xStart = x1;
/* 207 */       xEnd = x2;
/*     */     }
/* 209 */     if (x1 > x2)
/*     */     {
/* 211 */       xStart = x2;
/* 212 */       xEnd = x1;
/*     */     }
/* 214 */     if (x1 == x2)
/*     */     {
/* 216 */       xStart = x1;
/* 217 */       xEnd = x1;
/*     */     }
/* 219 */     if (z1 < z2)
/*     */     {
/* 221 */       zStart = z1;
/* 222 */       zEnd = z2;
/*     */     }
/* 224 */     if (z1 > z2)
/*     */     {
/* 226 */       zStart = z2;
/* 227 */       zEnd = z1;
/*     */     }
/* 229 */     if (z1 == z2)
/*     */     {
/* 231 */       zStart = z1;
/* 232 */       zEnd = z1;
/*     */     }
/* 234 */     if (y1 < y2)
/*     */     {
/* 236 */       yStart = y1;
/* 237 */       yEnd = y2;
/*     */     }
/* 239 */     if (y1 > y2)
/*     */     {
/* 241 */       yStart = y2;
/* 242 */       yEnd = y1;
/*     */     }
/* 244 */     if (y1 == y2)
/*     */     {
/* 246 */       yStart = y1;
/* 247 */       yEnd = y1;
/*     */     }
/* 249 */     xStart -= 5;yStart += 0;zStart -= 5;xEnd += 5;yEnd += 2;zEnd += 5;
/* 250 */     for (int i = xStart; i <= xEnd; i++)
/*     */     {
/* 252 */       int x = i;
/* 253 */       for (int j = yStart; j <= yEnd; j++)
/*     */       {
/* 255 */         int y = j;
/* 256 */         for (int k = zStart; k <= zEnd; k++)
/*     */         {
/* 258 */           z = k;
/*     */           
/* 260 */           tempBlock = world.getBlockAt(x, y, z);
/* 261 */           if ((tempBlock.getType().equals(Material.WALL_SIGN)) || (tempBlock.getType().equals(Material.SIGN)) || (tempBlock.getType().equals(Material.SIGN_POST)))
/*     */           {
/* 263 */             org.bukkit.block.Sign sign = (org.bukkit.block.Sign)tempBlock.getState();
/* 264 */             if (sign.getLine(0).equals(ChatColor.DARK_GRAY + "[EElevator]"))
/*     */             {
/* 266 */               boolean isPS = false;
/* 267 */               for (Elevator e : this.elevators)
/*     */               {
/* 269 */                 if (e.getPlatform().getSign().equals(sign))
/*     */                 {
/* 271 */                   isPS = true;
/*     */                 }
/*     */               }
/* 274 */               if (!isPS)
/*     */               {
/* 276 */                 return (org.bukkit.block.Sign)tempBlock.getState();
/*     */               }
/*     */             }
/*     */           }
/* 280 */           tempBlock = null;
/*     */         }
/*     */       }
/*     */     }
/* 284 */     return null;
/*     */   }
/*     */   
/*     */   public Elevator getElevator(org.bukkit.block.Sign sign)
/*     */   {
/* 289 */     if ((sign.getLine(0).equals("[EElevator]")) || (sign.getLine(0).equals(ChatColor.DARK_GRAY + "[EElevator]")))
/*     */     {
/* 291 */       Elevator e = null;
/* 292 */       for (int i = 0; i < this.elevators.size(); i++)
/*     */       {
/* 294 */         org.bukkit.material.Sign signData = (org.bukkit.material.Sign)sign.getData();
/* 295 */         Block attached = sign.getBlock().getRelative(signData.getAttachedFace());
/* 296 */         if ((((Elevator)this.elevators.get(i)).isPartOfElevator(attached.getLocation())) && (
/* 297 */           (((Elevator)this.elevators.get(i)).isFloorSign(sign)) || (((Elevator)this.elevators.get(i)).isPlatformSign(sign))))
/*     */         {
/* 299 */           e = (Elevator)this.elevators.get(i);
/* 300 */           i = this.elevators.size();
/*     */         }
/*     */       }
/*     */       
/* 304 */       if (e == null) {
/* 305 */         e = new Elevator(this, sign);
/*     */       }
/* 307 */       if ((e != null) && 
/* 308 */         (e.isInitialized()))
/*     */       {
/* 310 */         if (!this.elevators.contains(e)) {
/* 311 */           this.elevators.add(e);
/*     */         }
/* 313 */         return e;
/*     */       }
/*     */     }
/*     */     
/* 317 */     return null;
/*     */   }
/*     */   
/*     */   public int getMaxPerimeter()
/*     */   {
/* 322 */     return this.MaxPerimeter;
/*     */   }
/*     */   
/*     */   public int getMaxFloors()
/*     */   {
/* 327 */     return this.MaxFloors;
/*     */   }
/*     */   
/*     */   public boolean getArrivalSound()
/*     */   {
/* 332 */     return this.ArrivalSound;
/*     */   }
/*     */   
/*     */   public boolean getArrivalMessage()
/*     */   {
/* 337 */     return this.ArrivalMessage;
/*     */   }
/*     */   
/*     */   public String getBlockBorder()
/*     */   {
/* 342 */     return this.BlockBorder;
/*     */   }
/*     */   
/*     */   public String getBlockFloor()
/*     */   {
/* 347 */     return this.BlockFloor;
/*     */   }
/*     */   
/*     */   public String getBlockOutputDoor()
/*     */   {
/* 352 */     return this.BlockOutputDoor;
/*     */   }
/*     */   
/*     */   public String getBlockOutputFloor()
/*     */   {
/* 357 */     return this.BlockOutputFloor;
/*     */   }
/*     */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\EasyElevator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */