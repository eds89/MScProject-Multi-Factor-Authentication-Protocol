/*    */ package labsec.auth.biometric.event;
/*    */ 
/*    */ 
/*    */ public class VerificationEvent
/*    */   extends BaseEvent
/*    */ {
/*    */   private boolean success;
/*    */   private boolean matched;
/*    */   
/*    */   public VerificationEvent(boolean success, boolean matched) {
/* 11 */     this.success = success;
/* 12 */     this.matched = matched;
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 16 */     return this.success;
/*    */   }
/*    */   
/*    */   public boolean isMatched() {
/* 20 */     return this.matched;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     StringBuilder builder = new StringBuilder();
/* 26 */     builder.append("VerificationEvent [success=");
/* 27 */     builder.append(this.success);
/* 28 */     builder.append(", matched=");
/* 29 */     builder.append(this.matched);
/* 30 */     builder.append("]");
/* 31 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\VerificationEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */