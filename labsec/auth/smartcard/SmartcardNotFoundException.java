/*    */ package labsec.auth.smartcard;
/*    */ 
/*    */ 
/*    */ public class SmartcardNotFoundException
/*    */   extends SmartcardException
/*    */ {
/*    */   public SmartcardNotFoundException() {}
/*    */   
/*    */   public SmartcardNotFoundException(String message, Throwable cause) {
/* 10 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmartcardNotFoundException(String message) {
/* 15 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public SmartcardNotFoundException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\smartcard\SmartcardNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */