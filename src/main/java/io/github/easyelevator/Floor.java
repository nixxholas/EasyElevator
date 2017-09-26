/*     */ package io.github.easyelevator;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Sound;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Sign;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Floor
/*     */ {
/*     */   private Elevator elevator;
/*     */   private Location l1;
/*     */   private Location l2;
/*     */   private World world;
/*     */   private Sign callSign;
/*     */   private int floor;
/*     */   private int height;
/*  25 */   private boolean isCalled = false;
/*  26 */   private boolean hasOpenDoors = false;
/*  27 */   private Material OutputDoorMat = null;
/*  28 */   private byte OutputDoorData = 0;
/*  29 */   private Material OutputFloorMat = null;
/*  30 */   private byte OutputFloorData = 0;
/*  31 */   private List<Block> doorOpenBlock = new ArrayList();
/*  32 */   private List<Block> redstoneOutDoorBlock = new ArrayList();
/*  33 */   private List<Block> redstoneOutFloorBlock = new ArrayList();
/*     */   
/*     */   public Floor(Elevator elv, Location l1, Location l2, Sign callSign, int floor)
/*     */   {
/*  37 */     this.elevator = elv;
/*  38 */     this.l1 = l1;
/*  39 */     this.l2 = l2;
/*  40 */     this.world = l1.getWorld();
/*  41 */     this.callSign = callSign;
/*  42 */     this.floor = floor;
/*  43 */     this.height = l1.getBlockY();
/*     */     
/*  45 */     updateSign("0");
/*  46 */     initializeSign();
/*  47 */     initializeDoor();
/*     */   }
/*     */   
/*     */   private void initializeSign()
/*     */   {
/*  52 */     this.callSign.setLine(0, ChatColor.DARK_GRAY + "[EElevator]");
/*  53 */     this.callSign.setLine(1, this.floor);
/*  54 */     this.callSign.update();
/*     */   }
/*     */   
/*     */   private void initializeDoor()
/*     */   {
/*  59 */     int x1 = this.l1.getBlockX();
/*  60 */     int z1 = this.l1.getBlockZ();
/*     */     
/*  62 */     int x2 = this.l2.getBlockX();
/*  63 */     int z2 = this.l2.getBlockZ();
/*     */     
/*  65 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/*  66 */     if (x1 < x2)
/*     */     {
/*  68 */       xStart = x1;
/*  69 */       xEnd = x2;
/*     */     }
/*  71 */     if (x1 > x2)
/*     */     {
/*  73 */       xStart = x2;
/*  74 */       xEnd = x1;
/*     */     }
/*  76 */     if (x1 == x2)
/*     */     {
/*  78 */       xStart = x1;
/*  79 */       xEnd = x1;
/*     */     }
/*  81 */     if (z1 < z2)
/*     */     {
/*  83 */       zStart = z1;
/*  84 */       zEnd = z2;
/*     */     }
/*  86 */     if (z1 > z2)
/*     */     {
/*  88 */       zStart = z2;
/*  89 */       zEnd = z1;
/*     */     }
/*  91 */     if (z1 == z2)
/*     */     {
/*  93 */       zStart = z1;
/*  94 */       zEnd = z1;
/*     */     }
/*  96 */     for (int x = xStart; x <= xEnd; x++) {
/*  97 */       for (int z = zStart; z <= zEnd; z++)
/*     */       {
/*  99 */         Block tempBlock = this.world.getBlockAt(x, this.l1.getBlockY() + 1, z);
/* 100 */         if (((x == xStart) || (x == xEnd) || (z == zStart) || (z == zEnd)) && (
/* 101 */           (tempBlock.getType().equals(Material.WOODEN_DOOR)) || (tempBlock.getType().equals(Material.IRON_DOOR_BLOCK)))) {
/* 102 */           this.doorOpenBlock.add(tempBlock);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void switchRedstoneDoorOn(boolean b)
/*     */   {
/* 111 */     int x1 = this.l1.getBlockX();
/* 112 */     int z1 = this.l1.getBlockZ();
/*     */     
/* 114 */     int x2 = this.l2.getBlockX();
/* 115 */     int z2 = this.l2.getBlockZ();
/*     */     
/* 117 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/* 118 */     if (x1 < x2)
/*     */     {
/* 120 */       xStart = x1;
/* 121 */       xEnd = x2;
/*     */     }
/* 123 */     if (x1 > x2)
/*     */     {
/* 125 */       xStart = x2;
/* 126 */       xEnd = x1;
/*     */     }
/* 128 */     if (x1 == x2)
/*     */     {
/* 130 */       xStart = x1;
/* 131 */       xEnd = x1;
/*     */     }
/* 133 */     if (z1 < z2)
/*     */     {
/* 135 */       zStart = z1;
/* 136 */       zEnd = z2;
/*     */     }
/* 138 */     if (z1 > z2)
/*     */     {
/* 140 */       zStart = z2;
/* 141 */       zEnd = z1;
/*     */     }
/* 143 */     if (z1 == z2)
/*     */     {
/* 145 */       zStart = z1;
/* 146 */       zEnd = z1;
/*     */     }
/* 148 */     for (int x = xStart; x <= xEnd; x++) {
/* 149 */       for (int z = zStart; z <= zEnd; z++)
/*     */       {
/* 151 */         Block tempBlock = this.world.getBlockAt(x, this.l1.getBlockY(), z);
/* 152 */         if ((x == xStart) || (x == xEnd) || (z == zStart) || (z == zEnd)) {
/* 153 */           if (b)
/*     */           {
/* 155 */             if (this.elevator.isOutputDoor(tempBlock))
/*     */             {
/* 157 */               this.OutputDoorMat = tempBlock.getType();
/* 158 */               this.OutputDoorData = tempBlock.getData();
/* 159 */               tempBlock.setType(Material.REDSTONE_TORCH_ON);
/* 160 */               tempBlock.setData((byte)5);
/* 161 */               this.redstoneOutDoorBlock.add(tempBlock);
/*     */             }
/*     */           }
/* 164 */           else if (((this.elevator.isOutputDoor(tempBlock)) || (tempBlock.getType().equals(Material.REDSTONE_TORCH_ON))) && 
/* 165 */             (this.redstoneOutDoorBlock.contains(tempBlock)))
/*     */           {
/* 167 */             tempBlock.setType(this.OutputDoorMat);
/* 168 */             tempBlock.setData(this.OutputDoorData);
/* 169 */             this.redstoneOutDoorBlock.remove(tempBlock);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 175 */     if (!b) {
/* 176 */       this.redstoneOutDoorBlock = new ArrayList();
/*     */     }
/*     */   }
/*     */   
/*     */   public void switchRedstoneFloorOn(boolean b)
/*     */   {
/* 182 */     int x1 = this.l1.getBlockX();
/* 183 */     int z1 = this.l1.getBlockZ();
/*     */     
/* 185 */     int x2 = this.l2.getBlockX();
/* 186 */     int z2 = this.l2.getBlockZ();
/*     */     
/* 188 */     int xStart = 0;int xEnd = 0;int zStart = 0;int zEnd = 0;
/* 189 */     if (x1 < x2)
/*     */     {
/* 191 */       xStart = x1;
/* 192 */       xEnd = x2;
/*     */     }
/* 194 */     if (x1 > x2)
/*     */     {
/* 196 */       xStart = x2;
/* 197 */       xEnd = x1;
/*     */     }
/* 199 */     if (x1 == x2)
/*     */     {
/* 201 */       xStart = x1;
/* 202 */       xEnd = x1;
/*     */     }
/* 204 */     if (z1 < z2)
/*     */     {
/* 206 */       zStart = z1;
/* 207 */       zEnd = z2;
/*     */     }
/* 209 */     if (z1 > z2)
/*     */     {
/* 211 */       zStart = z2;
/* 212 */       zEnd = z1;
/*     */     }
/* 214 */     if (z1 == z2)
/*     */     {
/* 216 */       zStart = z1;
/* 217 */       zEnd = z1;
/*     */     }
/* 219 */     for (int x = xStart; x <= xEnd; x++) {
/* 220 */       for (int z = zStart; z <= zEnd; z++)
/*     */       {
/* 222 */         Block tempBlock = this.world.getBlockAt(x, this.l1.getBlockY(), z);
/* 223 */         if ((x == xStart) || (x == xEnd) || (z == zStart) || (z == zEnd)) {
/* 224 */           if (b)
/*     */           {
/* 226 */             if (this.elevator.isOutputFloor(tempBlock))
/*     */             {
/* 228 */               this.OutputFloorMat = tempBlock.getType();
/* 229 */               this.OutputFloorData = tempBlock.getData();
/* 230 */               tempBlock.setType(Material.REDSTONE_TORCH_ON);
/* 231 */               tempBlock.setData((byte)5);
/* 232 */               this.redstoneOutFloorBlock.add(tempBlock);
/*     */             }
/*     */           }
/* 235 */           else if (((this.elevator.isOutputFloor(tempBlock)) || (tempBlock.getType().equals(Material.REDSTONE_TORCH_ON))) && 
/* 236 */             (this.redstoneOutFloorBlock.contains(tempBlock)))
/*     */           {
/* 238 */             tempBlock.setType(this.OutputFloorMat);
/* 239 */             tempBlock.setData(this.OutputFloorData);
/* 240 */             this.redstoneOutFloorBlock.remove(tempBlock);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 246 */     if (!b) {
/* 247 */       this.redstoneOutFloorBlock = new ArrayList();
/*     */     }
/*     */   }
/*     */   
/*     */   public void OpenDoor()
/*     */   {
/* 253 */     switchRedstoneDoorOn(true);
/* 254 */     for (Block block : this.doorOpenBlock) {
/* 255 */       if (block.getData() <= 3) {
/* 256 */         block.setData((byte)(block.getData() + 4));
/*     */       }
/*     */     }
/* 259 */     this.hasOpenDoors = true;
/*     */   }
/*     */   
/*     */   public void CloseDoor()
/*     */   {
/* 264 */     for (Block block : this.doorOpenBlock) {
/* 265 */       if (block.getData() >= 4) {
/* 266 */         block.setData((byte)(block.getData() - 4));
/*     */       }
/*     */     }
/* 269 */     this.hasOpenDoors = false;
/* 270 */     switchRedstoneDoorOn(false);
/*     */   }
/*     */   
/*     */   public void updateSign(String platformFloor)
/*     */   {
/* 275 */     this.callSign.setLine(2, platformFloor);
/* 276 */     this.callSign.update();
/*     */   }
/*     */   
/*     */   public void setCalled(boolean b)
/*     */   {
/* 281 */     if (b)
/*     */     {
/* 283 */       if (!this.hasOpenDoors)
/*     */       {
/* 285 */         this.callSign.setLine(3, "Called");
/* 286 */         this.callSign.update();
/* 287 */         this.isCalled = true;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 292 */       this.callSign.setLine(3, "");
/* 293 */       this.callSign.update();
/* 294 */       this.isCalled = false;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCalled()
/*     */   {
/* 300 */     return this.isCalled;
/*     */   }
/*     */   
/*     */   public int getHeight()
/*     */   {
/* 305 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getFloor()
/*     */   {
/* 310 */     return this.floor;
/*     */   }
/*     */   
/*     */   public int getSignHeight()
/*     */   {
/* 315 */     return this.callSign.getY();
/*     */   }
/*     */   
/*     */   public void writeSign(int line, String message)
/*     */   {
/* 320 */     this.callSign.setLine(line, message);
/* 321 */     this.callSign.update();
/*     */   }
/*     */   
/*     */   public String getIdentifyLocation()
/*     */   {
/* 326 */     return this.callSign.getBlock().getLocation().getBlockX() + " " + this.callSign.getBlock().getLocation().getBlockZ();
/*     */   }
/*     */   
/*     */   public Sign getSign()
/*     */   {
/* 331 */     return this.callSign;
/*     */   }
/*     */   
/*     */   public void playOpenSound()
/*     */   {
/* 336 */     for (Block block : this.doorOpenBlock)
/*     */     {
/* 338 */       Location loc = block.getLocation();
/* 339 */       loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_PLING, 1.0F, 0.0F);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\Floor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */