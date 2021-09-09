/*    */ package softplan.prototype.common;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Signature
/*    */   implements Serializable
/*    */ {
/*    */   private final byte[] signature;
/*    */   private final String notation;
/*    */   
/*    */   public Signature(byte[] signature) {
/* 12 */     this.signature = signature;
/* 13 */     this.notation = "sKr";
/*    */   }
/*    */   
/*    */   public byte[] getSignature() {
/* 17 */     return this.signature;
/*    */   }
/*    */ 
/*    */   
/*    */   public Signature(byte[] signature, String notation) {
/* 22 */     this.signature = signature;
/* 23 */     this.notation = notation;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 28 */     return "Signature {|" + this.notation + "|}";
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\Signature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */