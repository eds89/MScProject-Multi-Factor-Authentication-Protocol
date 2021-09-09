/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.FtrIdentifyRecord;
/*    */ import labsec.auth.biometric.UserRecord;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FutronicUserRecord
/*    */   extends FtrIdentifyRecord
/*    */   implements UserRecord
/*    */ {
/*    */   public FutronicUserRecord(String key, byte[] template) {
/* 13 */     setKey(key);
/* 14 */     this.m_Template = (byte[])template.clone();
/*    */   }
/*    */   
/*    */   private void setKey(String key) {
/* 18 */     byte[] b = new byte[key.length()];
/* 19 */     for (int i = 0; i < b.length; i++) {
/* 20 */       b[i] = (byte)key.charAt(i);
/*    */     }
/* 22 */     this.m_KeyValue = b;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 27 */     byte[] b = this.m_KeyValue;
/* 28 */     char[] c = new char[b.length];
/* 29 */     for (int i = 0; i < c.length; i++) {
/* 30 */       c[i] = (char)b[i];
/*    */     }
/* 32 */     return new String(c);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getTemplate() {
/* 37 */     return this.m_Template;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 46 */     StringBuilder builder = new StringBuilder();
/* 47 */     builder.append("FutronicUserRecord [key=");
/* 48 */     builder.append(getKey());
/* 49 */     builder.append(", template=[");
/* 50 */     builder.append((getTemplate()).length);
/* 51 */     builder.append("]]");
/* 52 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicUserRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */