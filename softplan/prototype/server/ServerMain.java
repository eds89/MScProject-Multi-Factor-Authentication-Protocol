/*    */ package softplan.prototype.server;
/*    */ 
/*    */ import org.apache.log4j.Logger;
/*    */ import softplan.prototype.common.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ServerMain
/*    */ {
/*    */   static {
/* 12 */     Configuration.configureServer();
/*    */   }
/* 14 */   private static final Logger logger = Logger.getLogger(ServerMain.class);
/*    */ 
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 18 */     Server server = new Server();
/*    */ 
/*    */ 
/*    */     
/* 22 */     server.beginAdminCommunication();
/* 23 */     server.beginClientCommunication();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void oldMain(String[] args) throws Exception {
/* 30 */     final Server server = new Server();
/* 31 */     Runnable r = new Runnable() {
/*    */         public void run() {
/*    */           try {
/* 34 */             server.initAdminServer();
/*    */             
/* 36 */             server.waitForAdmin();
/* 37 */             server.handleAdminRequests();
/*    */             
/* 39 */             server.waitForClient();
/* 40 */             server.handleClientRequests();
/*    */           }
/* 42 */           catch (Exception e) {
/* 43 */             e.printStackTrace();
/*    */           } 
/*    */         }
/*    */       };
/*    */     
/* 48 */     Thread t = new Thread(r);
/* 49 */     t.start();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\server\ServerMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */