/*     */ package io.github.easyelevator;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class Elevator implements Runnable
/*     */ {
/*     */   public EasyElevator plugin;
/*     */   private org.bukkit.block.Sign s;
/*     */   private Block attached;
/*     */   private World world;
/*     */   private int highestPoint;
/*     */   private int lowestPoint;
/*     */   private int xLow;
/*     */   private int xHigh;
/*     */   private int zLow;
/*     */   private int zHigh;
/*  27 */   private int maxFloors = -1;
/*  28 */   private int maxPerimeter = -1;
/*  29 */   private List<Integer> stops = new ArrayList();
/*  30 */   public Floor currentFloor = null;
/*  31 */   private String Direction = "";
/*  32 */   private boolean isMoving = false;
/*  33 */   private boolean hasOpenDoor = false;
/*  34 */   private boolean isInitialized = false;
/*  35 */   private List<Floor> floors = new ArrayList();
/*     */   private Platform platform;
/*     */   
/*     */   public Elevator(EasyElevator elev, org.bukkit.block.Sign s)
/*     */   {
/*  40 */     this.plugin = elev;
/*  41 */     this.world = s.getWorld();
/*  42 */     this.s = s;
/*  43 */     this.maxFloors = elev.getMaxFloors();
/*  44 */     this.maxPerimeter = elev.getMaxPerimeter();
/*     */     
/*  46 */     org.bukkit.material.Sign signData = (org.bukkit.material.Sign)s.getData();
/*  47 */     this.attached = s.getBlock().getRelative(signData.getAttachedFace());
/*     */     
/*  49 */     initializeLift();
/*     */   }
/*     */   
/*     */   private void initializeLift()
/*     */   {
/*  54 */     int count = 0;
/*     */     
/*  56 */     int low = -1;
/*  57 */     int high = -1;
/*  58 */     for (int i = this.s.getY(); i >= 0; i--)
/*     */     {
/*  60 */       Block b = this.world.getBlockAt(this.attached.getLocation().getBlockX(), i, this.attached.getLocation().getBlockZ());
/*  61 */       if (isBorder(b))
/*     */       {
/*  63 */         low = i;
/*  64 */         i = -1;
/*     */       }
/*     */     }
/*  67 */     for (int i = this.s.getY(); i < this.world.getMaxHeight(); i++)
/*     */     {
/*  69 */       Block b = this.world.getBlockAt(this.attached.getLocation().getBlockX(), i, this.attached.getLocation().getBlockZ());
/*  70 */       if (isBorder(b))
/*     */       {
/*  72 */         high = i;
/*  73 */         i = this.world.getMaxHeight();
/*     */       }
/*     */     }
/*     */     
/*  77 */     if ((low == -1) || (high == -1)) {
/*  78 */       return;
/*     */     }
/*     */     
/*  81 */     this.highestPoint = high;
/*  82 */     this.lowestPoint = low;
/*     */     
/*  84 */     Block b1 = null;
/*  85 */     Block b2 = null;
/*  86 */     for (int i = low; i < high; i++)
/*     */     {
/*  88 */       Location currLoc = new Location(this.world, this.attached.getLocation().getBlockX(), i, this.attached.getLocation().getBlockZ());
/*  89 */       Block target = this.world.getBlockAt(currLoc);
/*  90 */       if (isFloor(target))
/*     */       {
/*  92 */         int dirChange = 0;
/*     */         
/*  94 */         String dir = "";
/*     */         
/*  96 */         List<Block> blocks = new ArrayList();
/*  97 */         Block Start = target;
/*  98 */         Block t = null;
/*     */         
/* 100 */         b2 = null;
/* 101 */         b1 = null;
/*     */         do
/*     */         {
/* 104 */           Block temp = null;
/* 105 */           if (t == null)
/*     */           {
/* 107 */             if (temp == null)
/*     */             {
/* 109 */               temp = checkForIron(Start, Start.getRelative(0, 0, 1), blocks);
/* 110 */               if (temp != null)
/*     */               {
/* 112 */                 t = temp;
/* 113 */                 if (dirChanged(dir, "East")) {
/* 114 */                   dirChange++;
/*     */                 }
/* 116 */                 dir = "East";
/*     */               }
/*     */             }
/* 119 */             if (temp == null)
/*     */             {
/* 121 */               temp = checkForIron(Start, Start.getRelative(0, 0, -1), blocks);
/* 122 */               if (temp != null)
/*     */               {
/* 124 */                 t = temp;
/* 125 */                 if (dirChanged(dir, "West")) {
/* 126 */                   dirChange++;
/*     */                 }
/* 128 */                 dir = "West";
/*     */               }
/*     */             }
/* 131 */             if (temp == null)
/*     */             {
/* 133 */               temp = checkForIron(Start, Start.getRelative(1, 0, 0), blocks);
/* 134 */               if (temp != null)
/*     */               {
/* 136 */                 t = temp;
/* 137 */                 if (dirChanged(dir, "North")) {
/* 138 */                   dirChange++;
/*     */                 }
/* 140 */                 dir = "North";
/*     */               }
/*     */             }
/* 143 */             if (temp == null)
/*     */             {
/* 145 */               temp = checkForIron(Start, Start.getRelative(-1, 0, 0), blocks);
/* 146 */               if (temp != null)
/*     */               {
/* 148 */                 t = temp;
/* 149 */                 if (dirChanged(dir, "South")) {
/* 150 */                   dirChange++;
/*     */                 }
/* 152 */                 dir = "South";
/*     */               }
/*     */             }
/*     */           }
/* 156 */           else if (t != null)
/*     */           {
/* 158 */             if (temp == null)
/*     */             {
/* 160 */               temp = checkForIron(Start, t.getRelative(0, 0, 1), blocks);
/* 161 */               if (temp != null)
/*     */               {
/* 163 */                 t = temp;
/* 164 */                 if (dirChanged(dir, "East")) {
/* 165 */                   dirChange++;
/*     */                 }
/* 167 */                 dir = "East";
/*     */               }
/*     */             }
/* 170 */             if (temp == null)
/*     */             {
/* 172 */               temp = checkForIron(Start, t.getRelative(0, 0, -1), blocks);
/* 173 */               if (temp != null)
/*     */               {
/* 175 */                 t = temp;
/* 176 */                 if (dirChanged(dir, "West")) {
/* 177 */                   dirChange++;
/*     */                 }
/* 179 */                 dir = "West";
/*     */               }
/*     */             }
/* 182 */             if (temp == null)
/*     */             {
/* 184 */               temp = checkForIron(Start, t.getRelative(1, 0, 0), blocks);
/* 185 */               if (temp != null)
/*     */               {
/* 187 */                 t = temp;
/* 188 */                 if (dirChanged(dir, "North")) {
/* 189 */                   dirChange++;
/*     */                 }
/* 191 */                 dir = "North";
/*     */               }
/*     */             }
/* 194 */             if (temp == null)
/*     */             {
/* 196 */               temp = checkForIron(Start, t.getRelative(-1, 0, 0), blocks);
/* 197 */               if (temp != null)
/*     */               {
/* 199 */                 t = temp;
/* 200 */                 if (dirChanged(dir, "South")) {
/* 201 */                   dirChange++;
/*     */                 }
/* 203 */                 dir = "South";
/*     */               }
/*     */             }
/*     */           }
/* 207 */           if (temp == null) {
/* 208 */             return;
/*     */           }
/* 210 */           if ((dirChange == 1) && 
/* 211 */             (b1 == null)) {
/* 212 */             b1 = (Block)blocks.get(blocks.size() - 1);
/*     */           }
/*     */           
/* 215 */           if ((dirChange == 3) && 
/* 216 */             (b2 == null)) {
/* 217 */             b2 = (Block)blocks.get(blocks.size() - 1);
/*     */           }
/*     */           
/* 220 */           blocks.add(temp);
/* 221 */         } while (!Start.equals(t));
/* 222 */         if (blocks.size() > this.maxPerimeter) {
/* 223 */           return;
/*     */         }
/* 225 */         if (blocks.contains(target))
/*     */         {
/* 227 */           if ((b1 == null) || (b2 == null)) {
/* 228 */             return;
/*     */           }
/* 230 */           if ((dirChange != 4) && (dirChange != 3)) {
/* 231 */             return;
/*     */           }
/* 233 */           org.bukkit.block.Sign callSign = getCallSign(b1.getLocation(), b2.getLocation());
/* 234 */           if (callSign != null)
/*     */           {
/* 236 */             Floor floor = new Floor(this, b1.getLocation(), b2.getLocation(), callSign, count + 1);
/* 237 */             this.floors.add(floor);
/* 238 */             count++;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 243 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 247 */     if (this.floors.size() > this.maxFloors) {
/* 248 */       return;
/*     */     }
/* 250 */     this.platform = new Platform(this.plugin, b1.getLocation(), b2.getLocation(), ((Floor)this.floors.get(0)).getHeight(), ((Floor)this.floors.get(this.floors.size() - 1)).getHeight());
/* 251 */     if (!this.platform.isInitialized()) {
/* 252 */       return;
/*     */     }
/* 254 */     this.isInitialized = true;
/* 255 */     System.out.println("[EasyElevator] An elevator has been initialized");
/*     */   }
/*     */   
/*     */   private org.bukkit.block.Sign getCallSign(Location l1, Location l2)
/*     */   {
/* 260 */     BlockFace[] faces = new BlockFace[4];
/* 261 */     faces[0] = BlockFace.NORTH;
/* 262 */     faces[1] = BlockFace.EAST;
/* 263 */     faces[2] = BlockFace.SOUTH;
/* 264 */     faces[3] = BlockFace.WEST;
/*     */     
/* 266 */     int x1 = l1.getBlockX();
/* 267 */     int z1 = l1.getBlockZ();
/*     */     
/* 269 */     int x2 = l2.getBlockX();
/* 270 */     int z2 = l2.getBlockZ();
/*     */     
/* 272 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/* 273 */     if (x1 < x2)
/*     */     {
/* 275 */       xStart = x1;
/* 276 */       xEnd = x2;
/*     */     }
/* 278 */     if (x1 > x2)
/*     */     {
/* 280 */       xStart = x2;
/* 281 */       xEnd = x1;
/*     */     }
/* 283 */     if (x1 == x2)
/*     */     {
/* 285 */       xStart = x1;
/* 286 */       xEnd = x1;
/*     */     }
/* 288 */     if (z1 < z2)
/*     */     {
/* 290 */       zStart = z1;
/* 291 */       zEnd = z2;
/*     */     }
/* 293 */     if (z1 > z2)
/*     */     {
/* 295 */       zStart = z2;
/* 296 */       zEnd = z1;
/*     */     }
/* 298 */     if (z1 == z2)
/*     */     {
/* 300 */       zStart = z1;
/* 301 */       zEnd = z1;
/*     */     }
/* 303 */     this.xLow = xStart;
/* 304 */     this.xHigh = xEnd;
/* 305 */     this.zLow = zStart;
/* 306 */     this.zHigh = zEnd;
/*     */     
/* 308 */     xStart--;
/* 309 */     xEnd++;
/*     */     
/* 311 */     zStart--;
/* 312 */     zEnd++;
/* 313 */     for (int i = l1.getBlockY() + 2; i <= l1.getBlockY() + 3; i++) {
/* 314 */       for (int x = xStart; x <= xEnd; x++) {
/* 315 */         for (int z = zStart; z <= zEnd; z++)
/*     */         {
/* 317 */           Block tempBlock = this.world.getBlockAt(x, i, z);
/* 318 */           if ((x == xStart) || (x == xEnd))
/*     */           {
/* 320 */             if (tempBlock.getType().equals(Material.WALL_SIGN))
/*     */             {
/* 322 */               org.bukkit.block.Sign sign = (org.bukkit.block.Sign)tempBlock.getState();
/* 323 */               return sign;
/*     */             }
/*     */           }
/* 326 */           else if (((z == zStart) || (z == zEnd)) && 
/* 327 */             (tempBlock.getType().equals(Material.WALL_SIGN)))
/*     */           {
/* 329 */             org.bukkit.block.Sign sign = (org.bukkit.block.Sign)tempBlock.getState();
/* 330 */             return sign;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 336 */     return null;
/*     */   }
/*     */   
/*     */   private boolean dirChanged(String dir, String newDir)
/*     */   {
/* 341 */     if (dir.equals("")) {
/* 342 */       return false;
/*     */     }
/* 344 */     if (dir.equals(newDir)) {
/* 345 */       return false;
/*     */     }
/* 347 */     return true;
/*     */   }
/*     */   
/*     */   private Block checkForIron(Block Start, Block t, List<Block> blocks)
/*     */   {
/* 352 */     if ((isFloor(t)) || (isOutputDoor(t)) || (isOutputFloor(t)))
/*     */     {
/* 354 */       if ((Start.equals(t)) && (blocks.size() <= 4)) {
/* 355 */         return null;
/*     */       }
/* 357 */       if (!blocks.contains(t)) {
/* 358 */         return t;
/*     */       }
/*     */     }
/* 361 */     return null;
/*     */   }
/*     */   
/*     */   public void addStops(int Floor)
/*     */   {
/* 366 */     int height = -1;
/* 367 */     for (int i = 0; i < this.floors.size(); i++) {
/* 368 */       if (((Floor)this.floors.get(i)).getFloor() == Floor) {
/* 369 */         height = ((Floor)this.floors.get(i)).getHeight();
/*     */       }
/*     */     }
/* 372 */     addStopsFromHeight(height);
/*     */   }
/*     */   
/*     */   public void addStopsFromHeight(int height)
/*     */   {
/* 377 */     if ((height != -1) && 
/* 378 */       (!this.stops.contains(Integer.valueOf(height))))
/*     */     {
/* 380 */       this.stops.add(Integer.valueOf(height));
/* 381 */       if (!this.isMoving)
/*     */       {
/* 383 */         this.isMoving = true;
/* 384 */         run();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void Call(int height)
/*     */   {
/* 392 */     boolean hasHeight = false;
/* 393 */     Floor f = null;
/* 394 */     for (int i = 0; i < this.floors.size(); i++)
/*     */     {
/* 396 */       f = (Floor)this.floors.get(i);
/* 397 */       if (f.getSignHeight() == height)
/*     */       {
/* 399 */         hasHeight = true;
/* 400 */         f.setCalled(true);
/* 401 */         i = this.floors.size();
/*     */       }
/*     */     }
/* 404 */     if (hasHeight) {
/* 405 */       addStopsFromHeight(f.getHeight());
/*     */     }
/*     */   }
/*     */   
/*     */   public void StopAt(int floor)
/*     */   {
/* 411 */     for (Floor f : this.floors) {
/* 412 */       if (f.getFloor() == floor)
/*     */       {
/* 414 */         Call(f.getSignHeight());
/* 415 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 420 */   int lcount = 0;
/*     */   
/*     */   public void run()
/*     */   {
/* 424 */     if (this.lcount == 6) {
/* 425 */       this.lcount = 0;
/*     */     }
/* 427 */     updateDirection();
/* 428 */     updateFloorIndicator();
/* 429 */     if (!this.hasOpenDoor)
/*     */     {
/* 431 */       if (!this.platform.isStuck())
/*     */       {
/* 433 */         if (this.stops.contains(Integer.valueOf(this.platform.getHeight())))
/*     */         {
/* 435 */           for (Floor f : this.floors) {
/* 436 */             if (f.getHeight() == this.platform.getHeight()) {
/* 437 */               this.currentFloor = f;
/*     */             }
/*     */           }
/* 440 */           if (this.currentFloor != null)
/*     */           {
/* 442 */             if (this.plugin.getArrivalSound()) {
/* 443 */               this.currentFloor.playOpenSound();
/*     */             }
/* 445 */             this.currentFloor.switchRedstoneFloorOn(true);
/* 446 */             this.currentFloor.OpenDoor();
/* 447 */             this.hasOpenDoor = true;
/* 448 */             this.currentFloor.setCalled(false);
/* 449 */             this.platform.stopTeleport();
/* 450 */             this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 100L);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 455 */           if ((!this.Direction.equals("")) && 
/* 456 */             (this.currentFloor != null))
/*     */           {
/* 458 */             this.currentFloor.switchRedstoneFloorOn(false);
/* 459 */             this.currentFloor = null;
/*     */           }
/*     */           
/* 462 */           if (this.Direction.equals("DOWN"))
/*     */           {
/* 464 */             this.platform.moveDown(this.lcount);
/* 465 */             this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 1L);
/* 466 */             this.lcount += 1;
/*     */           }
/* 468 */           else if (!this.Direction.equals("UP"))
/*     */           {
/* 470 */             this.isMoving = false;
/* 471 */             return;
/*     */           }
/* 473 */           if (this.Direction.equals("UP"))
/*     */           {
/* 475 */             this.platform.moveUp(this.lcount);
/* 476 */             this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 1L);
/* 477 */             this.lcount += 1;
/*     */           }
/* 479 */           else if (!this.Direction.equals("DOWN"))
/*     */           {
/* 481 */             this.isMoving = false;
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 487 */         if (this.Direction.equals("UP")) {
/* 488 */           this.Direction = "DOWN";
/*     */         } else {
/* 490 */           this.Direction = "UP";
/*     */         }
/* 492 */         this.stops.clear();
/* 493 */         addStops(getFloorNumberFromHeight(getNextFloorHeight_2()));
/* 494 */         this.platform.isStuck(false);
/* 495 */         this.platform.sendMessage(org.bukkit.ChatColor.DARK_GRAY + "[EElevator] " + org.bukkit.ChatColor.GRAY + "The Elevator is stuck. Resetting...");
/* 496 */         this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 50L);
/*     */       }
/*     */     }
/* 499 */     else if (this.currentFloor != null)
/*     */     {
/* 501 */       this.currentFloor.CloseDoor();
/* 502 */       this.hasOpenDoor = false;
/* 503 */       removeCurrentFloor();
/* 504 */       this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 5L);
/*     */     }
/*     */   }
/*     */   
/*     */   public void changeFloor()
/*     */   {
/* 510 */     int curr = Integer.parseInt(this.platform.getSign().getLine(1));
/* 511 */     int next = curr + 1;
/* 512 */     if (next > this.floors.size()) {
/* 513 */       next = 1;
/*     */     }
/* 515 */     this.platform.writeSign(1, String.valueOf(next));
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFloorNumberFromHeight(int hight)
/*     */   {
/* 521 */     int floor = -1;
/* 522 */     for (Floor f : this.floors) {
/* 523 */       if (f.getHeight() == hight) {
/* 524 */         return f.getFloor();
/*     */       }
/*     */     }
/* 527 */     return floor;
/*     */   }
/*     */   
/*     */   public int getNextFloorHeight_2()
/*     */   {
/* 532 */     int next = -1;
/* 533 */     int current = this.platform.getHeight();
/* 534 */     if (this.Direction.equals("UP"))
/*     */     {
/* 536 */       for (int i = 0; i < this.floors.size(); i++)
/*     */       {
/* 538 */         int t = ((Floor)this.floors.get(i)).getHeight();
/* 539 */         if ((next == -1) && 
/* 540 */           (t > current)) {
/* 541 */           next = t;
/*     */         }
/* 543 */         if ((t > current) && (t < next)) {
/* 544 */           next = t;
/*     */         }
/*     */       }
/* 547 */       return next;
/*     */     }
/* 549 */     if (this.Direction.equals("DOWN"))
/*     */     {
/* 551 */       for (int i = 0; i < this.floors.size(); i++)
/*     */       {
/* 553 */         int t = ((Floor)this.floors.get(i)).getHeight();
/* 554 */         if ((next == -1) && 
/* 555 */           (t < current)) {
/* 556 */           next = t;
/*     */         }
/* 558 */         if ((t < current) && (t > next)) {
/* 559 */           next = t;
/*     */         }
/*     */       }
/* 562 */       return next;
/*     */     }
/* 564 */     if (this.Direction.equals("")) {
/* 565 */       return this.platform.getHeight();
/*     */     }
/* 567 */     return -1;
/*     */   }
/*     */   
/*     */   public int getNextFloorHeight()
/*     */   {
/* 572 */     if (this.currentFloor != null)
/*     */     {
/* 574 */       int next = -1;
/* 575 */       int current = this.currentFloor.getHeight();
/* 576 */       if (this.Direction.equals("UP")) {
/* 577 */         for (int i = 0; i < this.stops.size(); i++)
/*     */         {
/* 579 */           int t = ((Integer)this.stops.get(i)).intValue();
/* 580 */           if ((next == -1) && 
/* 581 */             (t > current)) {
/* 582 */             next = t;
/*     */           }
/* 584 */           if ((t > current) && (t < next)) {
/* 585 */             next = t;
/*     */           }
/*     */         }
/*     */       }
/* 589 */       if (this.Direction.equals("DOWN")) {
/* 590 */         for (int i = 0; i < this.stops.size(); i++)
/*     */         {
/* 592 */           int t = ((Integer)this.stops.get(i)).intValue();
/* 593 */           if ((next == -1) && 
/* 594 */             (t < current)) {
/* 595 */             next = t;
/*     */           }
/* 597 */           if ((t < current) && (t > next)) {
/* 598 */             next = t;
/*     */           }
/*     */         }
/*     */       }
/* 602 */       return next;
/*     */     }
/* 604 */     return -1;
/*     */   }
/*     */   
/*     */   public Platform getPlatform()
/*     */   {
/* 609 */     return this.platform;
/*     */   }
/*     */   
/*     */   public Floor getMainFloor()
/*     */   {
/* 614 */     return (Floor)this.floors.get(0);
/*     */   }
/*     */   
/*     */   public boolean isInitialized()
/*     */   {
/* 619 */     return this.isInitialized;
/*     */   }
/*     */   
/*     */   private void removeCurrentFloor()
/*     */   {
/* 624 */     for (int i = 0; i < this.stops.size(); i++) {
/* 625 */       if (((Integer)this.stops.get(i)).intValue() == this.platform.getHeight()) {
/* 626 */         this.stops.remove(i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateDirection()
/*     */   {
/* 633 */     int height = this.platform.getHeight();
/* 634 */     for (Iterator localIterator = this.stops.iterator(); localIterator.hasNext();)
/*     */     {
/* 636 */       int i = ((Integer)localIterator.next()).intValue();
/* 637 */       if ((this.Direction.equals("DOWN")) && 
/* 638 */         (i < height)) {
/* 639 */         return;
/*     */       }
/*     */       
/* 642 */       if ((this.Direction.equals("UP")) && 
/* 643 */         (i > height)) {
/* 644 */         return;
/*     */       }
/*     */       
/* 647 */       if (this.Direction.equals(""))
/*     */       {
/* 649 */         if (i > height) {
/* 650 */           this.Direction = "UP";
/*     */         } else {
/* 652 */           this.Direction = "DOWN";
/*     */         }
/* 654 */         return;
/*     */       }
/*     */     }
/* 657 */     if (this.stops.size() > 0)
/*     */     {
/* 659 */       if (this.Direction.equals("DOWN"))
/*     */       {
/* 661 */         this.Direction = "UP";
/* 662 */         return;
/*     */       }
/* 664 */       if (this.Direction.equals("UP")) {
/* 665 */         this.Direction = "DOWN";
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 670 */       this.Direction = "";
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateFloorIndicator()
/*     */   {
/* 676 */     int curr = getCurrentFloor();
/* 677 */     for (int i = 0; i < this.floors.size(); i++) {
/* 678 */       if (curr != -1)
/*     */       {
/* 680 */         ((Floor)this.floors.get(i)).writeSign(2, curr);
/*     */       }
/*     */       else
/*     */       {
/* 684 */         if (this.Direction.equals("UP")) {
/* 685 */           ((Floor)this.floors.get(i)).writeSign(2, "/\\");
/*     */         }
/* 687 */         if (this.Direction.equals("DOWN")) {
/* 688 */           ((Floor)this.floors.get(i)).writeSign(2, "\\/");
/*     */         }
/*     */       }
/*     */     }
/* 692 */     if (curr != -1)
/*     */     {
/* 694 */       this.platform.writeSign(2, curr);
/*     */     }
/*     */     else
/*     */     {
/* 698 */       if (this.Direction.equals("UP")) {
/* 699 */         this.platform.writeSign(2, "/\\");
/*     */       }
/* 701 */       if (this.Direction.equals("DOWN")) {
/* 702 */         this.platform.writeSign(2, "\\/");
/*     */       }
/*     */     }
/* 705 */     int next = getFloorNumberFromHeight(getNextFloorHeight());
/* 706 */     if (next != -1) {
/* 707 */       this.platform.writeSign(3, next);
/*     */     } else {
/* 709 */       this.platform.writeSign(3, "-");
/*     */     }
/*     */   }
/*     */   
/*     */   public int getCurrentFloor()
/*     */   {
/* 715 */     if (isFloor(this.platform.getHeight())) {
/* 716 */       for (int i = 0; i < this.floors.size(); i++) {
/* 717 */         if (this.platform.getHeight() == ((Floor)this.floors.get(i)).getHeight()) {
/* 718 */           return ((Floor)this.floors.get(i)).getFloor();
/*     */         }
/*     */       }
/*     */     }
/* 722 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean isPartOfElevator(Location loc)
/*     */   {
/* 727 */     int x = loc.getBlockX();
/* 728 */     int y = loc.getBlockY();
/* 729 */     int z = loc.getBlockZ();
/* 730 */     if ((y > this.lowestPoint) && (y < this.highestPoint) && (x >= this.xLow) && (x <= this.xHigh) && (z >= this.zLow) && (z <= this.zHigh)) {
/* 731 */       return true;
/*     */     }
/* 733 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFloorSign(org.bukkit.block.Sign sign)
/*     */   {
/* 738 */     for (int i = 0; i < this.floors.size(); i++)
/*     */     {
/* 740 */       Floor f = (Floor)this.floors.get(i);
/* 741 */       if (f.getSign().equals(sign)) {
/* 742 */         return true;
/*     */       }
/*     */     }
/* 745 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPlatformSign(org.bukkit.block.Sign sign)
/*     */   {
/* 750 */     if (this.platform.getSign().equals(sign)) {
/* 751 */       return true;
/*     */     }
/* 753 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isInElevator(Player player)
/*     */   {
/* 758 */     return this.platform.hasPlayer(player);
/*     */   }
/*     */   
/*     */   public boolean isFloor(int floorHeight)
/*     */   {
/* 763 */     for (int i = 0; i < this.floors.size(); i++) {
/* 764 */       if (floorHeight == ((Floor)this.floors.get(i)).getHeight()) {
/* 765 */         return true;
/*     */       }
/*     */     }
/* 768 */     return false;
/*     */   }
/*     */   
/*     */   public List<Floor> getFloors()
/*     */   {
/* 773 */     return this.floors;
/*     */   }
/*     */   
/*     */   public boolean isBorder(Block b)
/*     */   {
/*     */     try
/*     */     {
/* 780 */       String border = this.plugin.getBlockBorder();
/* 781 */       int id = -1;
/* 782 */       int data = -1;
/* 783 */       if (border.contains(":"))
/*     */       {
/* 785 */         id = Integer.parseInt(border.split(":")[0]);
/* 786 */         data = Integer.parseInt(border.split(":")[1]);
/*     */       }
/*     */       else
/*     */       {
/* 790 */         id = Integer.parseInt(border);
/*     */       }
/* 792 */       if (data != -1)
/*     */       {
/* 794 */         if ((data == b.getData()) && (id == b.getTypeId())) {
/* 795 */           return true;
/*     */         }
/*     */       }
/* 798 */       else if (id == b.getTypeId()) {
/* 799 */         return true;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/* 803 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFloor(Block b)
/*     */   {
/*     */     try
/*     */     {
/* 810 */       String border = this.plugin.getBlockFloor();
/* 811 */       int id = -1;
/* 812 */       int data = -1;
/* 813 */       if (border.contains(":"))
/*     */       {
/* 815 */         id = Integer.parseInt(border.split(":")[0]);
/* 816 */         data = Integer.parseInt(border.split(":")[1]);
/*     */       }
/*     */       else
/*     */       {
/* 820 */         id = Integer.parseInt(border);
/*     */       }
/* 822 */       if (data != -1)
/*     */       {
/* 824 */         if ((data == b.getData()) && (id == b.getTypeId())) {
/* 825 */           return true;
/*     */         }
/*     */       }
/* 828 */       else if (id == b.getTypeId()) {
/* 829 */         return true;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/* 833 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isOutputFloor(Block b)
/*     */   {
/*     */     try
/*     */     {
/* 840 */       String border = this.plugin.getBlockOutputFloor();
/* 841 */       int id = -1;
/* 842 */       int data = -1;
/* 843 */       if (border.contains(":"))
/*     */       {
/* 845 */         id = Integer.parseInt(border.split(":")[0]);
/* 846 */         data = Integer.parseInt(border.split(":")[1]);
/*     */       }
/*     */       else
/*     */       {
/* 850 */         id = Integer.parseInt(border);
/*     */       }
/* 852 */       if (data != -1)
/*     */       {
/* 854 */         if ((data == b.getData()) && (id == b.getTypeId())) {
/* 855 */           return true;
/*     */         }
/*     */       }
/* 858 */       else if (id == b.getTypeId()) {
/* 859 */         return true;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/* 863 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isOutputDoor(Block b)
/*     */   {
/*     */     try
/*     */     {
/* 870 */       String border = this.plugin.getBlockOutputDoor();
/* 871 */       int id = -1;
/* 872 */       int data = -1;
/* 873 */       if (border.contains(":"))
/*     */       {
/* 875 */         id = Integer.parseInt(border.split(":")[0]);
/* 876 */         data = Integer.parseInt(border.split(":")[1]);
/*     */       }
/*     */       else
/*     */       {
/* 880 */         id = Integer.parseInt(border);
/*     */       }
/* 882 */       if (data != -1)
/*     */       {
/* 884 */         if ((data == b.getData()) && (id == b.getTypeId())) {
/* 885 */           return true;
/*     */         }
/*     */       }
/* 888 */       else if (id == b.getTypeId()) {
/* 889 */         return true;
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/* 893 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\Elevator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */