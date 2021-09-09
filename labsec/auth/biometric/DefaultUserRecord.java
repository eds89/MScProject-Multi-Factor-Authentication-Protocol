/*    */ package labsec.auth.biometric;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultUserRecord
/*    */   implements UserRecord
/*    */ {
/*    */   private final String key;
/*    */   private final byte[] template;
/*    */   
/*    */   public DefaultUserRecord(String key, byte[] template) {
/* 12 */     this.key = key;
/* 13 */     this.template = template;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 18 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getTemplate() {
/* 23 */     return this.template;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     StringBuilder builder = new StringBuilder();
/* 29 */     builder.append("DefaultUserRecord [key=");
/* 30 */     builder.append(this.key);
/* 31 */     builder.append(", template=[");
/* 32 */     builder.append(this.template.length);
/* 33 */     builder.append("]]");
/* 34 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\DefaultUserRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */