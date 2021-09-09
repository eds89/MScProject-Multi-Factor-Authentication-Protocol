/*     */ package softplan.prototype.common;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.ConsoleAppender;
/*     */ import org.apache.log4j.FileAppender;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PatternLayout;
/*     */ 
/*     */ public class Configuration {
/*  16 */   private static final Logger logger = Logger.getLogger(Configuration.class);
/*     */   
/*     */   private static final String SERVER_LOG_FILENAME = "log-server.txt";
/*     */   
/*     */   private static final String CLIENT_LOG_FILENAME = "log-client.txt";
/*     */   
/*     */   private static final String ADMIN_LOG_FILENAME = "log-admin.txt";
/*     */   
/*  24 */   private static final Level LOGGER_LEVEL = Level.ALL;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String CONFIG_FILENAME = "config.properties";
/*     */ 
/*     */   
/*  31 */   private static final Properties configs = new Properties();
/*     */   static {
/*     */     try {
/*  34 */       File configFile = new File("config.properties");
/*  35 */       if (!configFile.exists()) {
/*     */         
/*  37 */         File newTry = new File("../config.properties");
/*  38 */         configFile = newTry;
/*     */       } 
/*     */       
/*  41 */       File absoluteFile = configFile.getAbsoluteFile();
/*  42 */       configs.load(new FileInputStream(absoluteFile));
/*  43 */     } catch (IOException ioe) {
/*  44 */       System.err.println("Cannot load config file config.properties");
/*  45 */       ioe.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getProperty(String key, String defaultValue) {
/*  50 */     String foundValue = configs.getProperty(key, defaultValue);
/*  51 */     logger.trace("key = " + key + " / value = " + foundValue);
/*     */     
/*  53 */     return foundValue;
/*     */   }
/*     */   public static int getProperty(String key, int defaultValue) {
/*     */     int intValue;
/*  57 */     String value = configs.getProperty(key);
/*     */     
/*     */     try {
/*  60 */       intValue = Integer.parseInt(value);
/*  61 */     } catch (NumberFormatException e) {
/*  62 */       intValue = defaultValue;
/*     */     } 
/*  64 */     logger.trace("key = " + key + " / value = " + intValue);
/*  65 */     return intValue;
/*     */   }
/*     */   
/*     */   public static void configureServer() {
/*  69 */     configureLog4J("log-server.txt");
/*  70 */     configureSmartcard();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     System.setProperty("javax.net.ssl.keyStore", "SSLKeyStore");
/*  76 */     System.setProperty("javax.net.ssl.keyStorePassword", "123456");
/*     */   }
/*     */   
/*     */   public static void configureClient() {
/*  80 */     configureLog4J("log-client.txt");
/*  81 */     configureSmartcard();
/*  82 */     configureBiometricReader();
/*     */   }
/*     */   
/*     */   public static void configureAdmin() {
/*  86 */     configureLog4J("log-admin.txt");
/*  87 */     configureSmartcard();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final PatternLayout logLayout = new PatternLayout("%d{HH:mm:ss,SSS} %p [%t] %c{1} - %m%n");
/*     */ 
/*     */ 
/*     */   
/*     */   private static void configureLog4J(String filename) {
/*  97 */     Logger root = Logger.getRootLogger();
/*     */ 
/*     */     
/* 100 */     root.addAppender((Appender)new ConsoleAppender(
/* 101 */           (Layout)logLayout));
/* 102 */     if (filename.equals("log-client.txt")) {
/*     */       return;
/*     */     }
/*     */     try {
/* 106 */       FileAppender fileAppender = new FileAppender((Layout)logLayout, filename);
/* 107 */       Logger.getRootLogger().addAppender((Appender)fileAppender);
/* 108 */     } catch (IOException ioe) {
/* 109 */       logger.debug("Could not init file appender for " + filename, ioe);
/*     */     } 
/* 111 */     Logger.getRootLogger().setLevel(LOGGER_LEVEL);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void configureFileAppender(String filename) {}
/*     */   
/*     */   private static void configureSmartcard() {
/* 118 */     System.setProperty("java.security.debug", 
/* 119 */         "sun.security.pkcs11.SunPKCS11");
/*     */   }
/*     */   
/*     */   private static void configureBiometricReader() {
/* 123 */     String libraryPath = System.getProperty("java.library.path");
/* 124 */     logger.trace("java.library.path = " + libraryPath);
/* 125 */     String bioLibraryPath = configs.getProperty("biometricDriverPath", "c:\\Drivers");
/*     */     
/* 127 */     if (bioLibraryPath != null && !libraryPath.contains(bioLibraryPath)) {
/* 128 */       String finalLibPath = String.valueOf(libraryPath) + File.pathSeparator + 
/* 129 */         bioLibraryPath;
/* 130 */       logger.debug("final java.library.path = " + finalLibPath);
/* 131 */       System.setProperty("java.library.path", finalLibPath);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 136 */     System.out.println(configs.toString());
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\Configuration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */