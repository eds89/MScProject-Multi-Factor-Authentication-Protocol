/*    */ package labsec.auth.biometric.event;
/*    */ 
/*    */ public class EnrollmentEvent
/*    */   extends BaseEvent {
/*    */   private boolean success;
/*    */   
/*    */   public EnrollmentEvent(boolean success) {
/*  8 */     this.success = success;
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 12 */     return this.success;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 17 */     StringBuilder builder = new StringBuilder();
/* 18 */     builder.append("EnrollmentEvent [success=");
/* 19 */     builder.append(this.success);
/* 20 */     builder.append("]");
/* 21 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\EnrollmentEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */