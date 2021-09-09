/*    */ package labsec.auth.biometric.event;
/*    */ 
/*    */ public abstract class BaseEvent {
/*    */   private long eventTime;
/*    */   
/*    */   public BaseEvent() {
/*  7 */     this(System.currentTimeMillis());
/*    */   }
/*    */ 
/*    */   
/*    */   public BaseEvent(long eventTime) {
/* 12 */     this.eventTime = eventTime;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getEventTime() {
/* 18 */     return this.eventTime;
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\BaseEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */