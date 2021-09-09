/*    */ package labsec.auth.biometric.event;
/*    */ 
/*    */ import labsec.auth.biometric.UserRecord;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdentificationEvent
/*    */   extends BaseEvent
/*    */ {
/*    */   private boolean success;
/*    */   private boolean matchFound;
/*    */   private UserRecord foundUser;
/*    */   
/*    */   public IdentificationEvent(boolean success, boolean matchFound, UserRecord foundUser) {
/* 17 */     this.success = success;
/* 18 */     this.matchFound = matchFound;
/* 19 */     this.foundUser = foundUser;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSuccess() {
/* 25 */     return this.success;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isMatchFound() {
/* 31 */     return this.matchFound;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public UserRecord getFoundUser() {
/* 37 */     return this.foundUser;
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\IdentificationEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */