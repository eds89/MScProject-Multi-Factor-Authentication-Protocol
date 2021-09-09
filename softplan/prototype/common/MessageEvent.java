/*    */ package softplan.prototype.common;
/*    */ 
/*    */ 
/*    */ public class MessageEvent
/*    */ {
/*    */   private long time;
/*    */   private String messageKey;
/*    */   
/*    */   public MessageEvent(String messageKey) {
/* 10 */     this.messageKey = messageKey;
/* 11 */     this.time = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public long getTime() {
/* 15 */     return this.time;
/*    */   }
/*    */   
/*    */   public String getMessageKey() {
/* 19 */     return this.messageKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     StringBuilder builder = new StringBuilder();
/* 25 */     builder.append("MessageEvent [messageKey=");
/* 26 */     builder.append(this.messageKey);
/* 27 */     builder.append("]");
/* 28 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\MessageEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */