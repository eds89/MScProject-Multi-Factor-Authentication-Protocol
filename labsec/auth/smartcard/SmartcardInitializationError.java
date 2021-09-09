/*    */ package labsec.auth.smartcard;
/*    */ 
/*    */ 
/*    */ public class SmartcardInitializationError
/*    */   extends Error
/*    */ {
/*    */   public SmartcardInitializationError() {}
/*    */   
/*    */   public SmartcardInitializationError(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmartcardInitializationError(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmartcardInitializationError(String message, Throwable cause) {
/* 20 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\smartcard\SmartcardInitializationError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */