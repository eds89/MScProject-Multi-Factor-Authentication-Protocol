/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.IVerificationCallBack;
/*    */ import labsec.auth.biometric.event.BaseListener;
/*    */ import labsec.auth.biometric.event.VerificationAdapter;
/*    */ import labsec.auth.biometric.event.VerificationEvent;
/*    */ import labsec.auth.biometric.event.VerificationListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FutronicVerificationListener
/*    */   extends FutronicBaseListener
/*    */   implements IVerificationCallBack, VerificationListener
/*    */ {
/*    */   public FutronicVerificationListener() {
/* 17 */     this((VerificationListener)new VerificationAdapter());
/*    */   }
/*    */   
/*    */   public FutronicVerificationListener(VerificationListener verificationListener) {
/* 21 */     super((BaseListener)verificationListener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void verificationComplete(VerificationEvent event) {
/* 26 */     ((VerificationListener)this.delegateListener).verificationComplete(event);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void OnVerificationComplete(boolean bSuccess, int nResult, boolean bVerificationSuccess) {
/* 33 */     verificationComplete(new VerificationEvent(bSuccess, bVerificationSuccess));
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicVerificationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */