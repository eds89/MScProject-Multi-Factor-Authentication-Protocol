/*    */ package softplan.prototype.common;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EncryptedMessage
/*    */   implements Serializable
/*    */ {
/*    */   private final byte[] encryptedMessage;
/*    */   private final String notation;
/*    */   
/*    */   public EncryptedMessage(byte[] encryptedMessage) {
/* 14 */     this.encryptedMessage = encryptedMessage;
/* 15 */     this.notation = "eK";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EncryptedMessage(byte[] encryptedMessage, String notation) {
/* 22 */     this.encryptedMessage = encryptedMessage;
/* 23 */     this.notation = notation;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getEncryptedMessage() {
/* 29 */     return this.encryptedMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 36 */     return "EncryptedMessage {|" + this.notation + "|}";
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\EncryptedMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */