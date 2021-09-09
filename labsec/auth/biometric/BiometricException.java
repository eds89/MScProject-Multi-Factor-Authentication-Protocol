/*    */ package labsec.auth.biometric;
/*    */ 
/*    */ 
/*    */ public class BiometricException
/*    */   extends Exception
/*    */ {
/*    */   public BiometricException() {}
/*    */   
/*    */   public BiometricException(String message) {
/* 10 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public BiometricException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public BiometricException(String message, Throwable cause) {
/* 20 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\BiometricException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */