/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.FutronicException;
/*    */ import com.futronic.SDKHelper.FutronicVerification;
/*    */ 
/*    */ public class NewFutronicVerification
/*    */   extends FutronicVerification
/*    */ {
/*    */   public NewFutronicVerification(byte[] Template) throws FutronicException, NullPointerException {
/* 10 */     super(Template);
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 15 */     synchronized (m_SyncRoot) {
/* 16 */       super.run();
/*    */       
/* 18 */       m_SyncRoot.notify();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\NewFutronicVerification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */