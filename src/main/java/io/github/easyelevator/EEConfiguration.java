/*     */ package io.github.easyelevator;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ 
/*     */ public class EEConfiguration
/*     */ {
/*   9 */   private String folder = "plugins/EasyElevator";
/*  10 */   private File configFile = new File(this.folder + File.separator + "config.yml");
/*     */   
/*     */   private YamlConfiguration config;
/*     */   
/*     */ 
/*     */   private YamlConfiguration loadConfig()
/*     */   {
/*     */     try
/*     */     {
/*  19 */       YamlConfiguration config = new YamlConfiguration();
/*  20 */       config.load(this.configFile);
/*  21 */       return config;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  25 */       System.out.println("[EasyElevator] An error occured! Please delete your SkyDiver folder an reload this Plugin!");
/*     */     }
/*  27 */     return null;
/*     */   }
/*     */   
/*     */   public void createConfig()
/*     */   {
/*  32 */     new File(this.folder).mkdir();
/*  33 */     if (!this.configFile.exists())
/*     */     {
/*     */       try
/*     */       {
/*  37 */         System.out.println("[EasyElevator] Creating Config...");
/*  38 */         this.configFile.createNewFile();
/*  39 */         this.config = loadConfig();
/*  40 */         this.config.set("MaxPerimeter", Integer.valueOf(25));
/*  41 */         this.config.set("MaxFloors", Integer.valueOf(10));
/*  42 */         this.config.set("Arrival.Sound", Boolean.valueOf(true));
/*  43 */         this.config.set("Arrival.Message", Boolean.valueOf(true));
/*  44 */         this.config.set("Blocks.Border", "41");
/*  45 */         this.config.set("Blocks.Floor", "42");
/*  46 */         this.config.set("Blocks.OutputDoor", "35:14");
/*  47 */         this.config.set("Blocks.OutputFloor", "35:1");
/*  48 */         this.config.save(this.configFile);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*  52 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  57 */       this.config = loadConfig();
/*  58 */       addNewNodes();
/*  59 */       this.config = loadConfig();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getMaxPerimeter()
/*     */   {
/*  65 */     this.config = loadConfig();
/*  66 */     return this.config.getInt("MaxPerimeter");
/*     */   }
/*     */   
/*     */   public int getMaxFloors()
/*     */   {
/*  71 */     this.config = loadConfig();
/*  72 */     return this.config.getInt("MaxFloors");
/*     */   }
/*     */   
/*     */   public boolean getArrivalSound()
/*     */   {
/*  77 */     this.config = loadConfig();
/*  78 */     return this.config.getBoolean("Arrival.Sound");
/*     */   }
/*     */   
/*     */   public boolean getArrivalMessage()
/*     */   {
/*  83 */     this.config = loadConfig();
/*  84 */     return this.config.getBoolean("Arrival.Message");
/*     */   }
/*     */   
/*     */   public String getBlock(String Block)
/*     */   {
/*  89 */     this.config = loadConfig();
/*  90 */     if (this.config.contains("Blocks." + Block)) {
/*  91 */       return this.config.getString("Blocks." + Block);
/*     */     }
/*  93 */     System.out.println("[EasyElevator] An error occured in your config. Please check for errors! (" + Block + ")");
/*  94 */     return "ERROR";
/*     */   }
/*     */   
/*     */   public void addNewNodes()
/*     */   {
/*     */     try
/*     */     {
/* 101 */       if (this.config.contains("PlayElevatorSound"))
/*     */       {
/* 103 */         this.config.set("Arrival.Sound", this.config.get("PlayElevatorSound"));
/* 104 */         this.config.set("PlayElevatorSound", null);
/* 105 */         System.out.println("[EasyElevator] Added Arrival.Sound Node to Configuration");
/* 106 */         System.out.println("[EasyElevator] Removed PlayElevatorSound Node from Configuration");
/*     */       }
/* 108 */       if (!this.config.contains("Arrival.Sound"))
/*     */       {
/* 110 */         this.config.set("Arrival.Sound", Boolean.valueOf(true));
/* 111 */         System.out.println("[EasyElevator] Added Arrival.Sound Node to Configuration");
/*     */       }
/* 113 */       if (!this.config.contains("Arrival.Message"))
/*     */       {
/* 115 */         this.config.set("Arrival.Message", Boolean.valueOf(true));
/* 116 */         System.out.println("[EasyElevator] Added Arrival.Message Node to Configuration");
/*     */       }
/* 118 */       if (!this.config.contains("Blocks.Border"))
/*     */       {
/* 120 */         this.config.set("Blocks.Border", "41");
/* 121 */         System.out.println("[EasyElevator] Added Blocks.Border Node to Configuration");
/*     */       }
/* 123 */       if (!this.config.contains("Blocks.Floor"))
/*     */       {
/* 125 */         this.config.set("Blocks.Floor", "42");
/* 126 */         System.out.println("[EasyElevator] Added Blocks.Floor Node to Configuration");
/*     */       }
/* 128 */       if (!this.config.contains("Blocks.OutputDoor"))
/*     */       {
/* 130 */         this.config.set("Blocks.OutputDoor", "35:14");
/* 131 */         System.out.println("[EasyElevator] Added Blocks.OutputDoor Node to Configuration");
/*     */       }
/* 133 */       if (!this.config.contains("Blocks.OutputFloor"))
/*     */       {
/* 135 */         this.config.set("Blocks.OutputFloor", "35:1");
/* 136 */         System.out.println("[EasyElevator] Added Blocks.OutputFloor Node to Configuration");
/*     */       }
/* 138 */       this.config.save(this.configFile);
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\eirik\Desktop\Ny mappe (6)\plugins\EasyElevator-0.0.1-SNAPSHOT.jar!\io\github\easyelevator\EEConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */