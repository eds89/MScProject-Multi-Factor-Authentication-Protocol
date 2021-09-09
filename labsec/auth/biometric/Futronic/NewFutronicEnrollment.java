/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.FutronicEnrollment;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NewFutronicEnrollment
/*    */   extends FutronicEnrollment
/*    */ {
/*    */   public void run() {
/* 14 */     synchronized (m_SyncRoot) {
/* 15 */       super.run();
/*    */       
/* 17 */       m_SyncRoot.notify();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\NewFutronicEnrollment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */