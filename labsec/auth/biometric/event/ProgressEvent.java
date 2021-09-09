/*    */ package labsec.auth.biometric.event;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProgressEvent
/*    */   extends BaseEvent
/*    */ {
/*    */   private int count;
/*    */   private int total;
/*    */   
/*    */   public ProgressEvent(int count, int total) {
/* 13 */     this.count = count;
/* 14 */     this.total = total;
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 18 */     return this.count;
/*    */   }
/*    */   
/*    */   public int getTotal() {
/* 22 */     return this.total;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 27 */     StringBuilder builder = new StringBuilder();
/* 28 */     builder.append("ProgressEvent [count=");
/* 29 */     builder.append(this.count);
/* 30 */     builder.append(", total=");
/* 31 */     builder.append(this.total);
/* 32 */     builder.append("]");
/* 33 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\ProgressEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */