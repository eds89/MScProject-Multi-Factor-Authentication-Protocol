/*    */ package softplan.prototype.common;
/*    */ 
/*    */ 
/*    */ public class SignatureNotVerifiedException
/*    */   extends ProtocolException
/*    */ {
/*    */   public SignatureNotVerifiedException() {}
/*    */   
/*    */   public SignatureNotVerifiedException(String message, Throwable cause) {
/* 10 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   
/*    */   public SignatureNotVerifiedException(String message) {
/* 15 */     super(message);
/*    */   }
/*    */ 
/*    */   
/*    */   public SignatureNotVerifiedException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\SignatureNotVerifiedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */