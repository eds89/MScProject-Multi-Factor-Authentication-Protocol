/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.FutronicIdentification;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NewFutronicIdentification
/*    */   extends FutronicIdentification
/*    */ {
/*    */   public void run() {
/* 14 */     synchronized (m_SyncRoot) {
/* 15 */       super.run();
/*    */       
/* 17 */       m_SyncRoot.notify();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\NewFutronicIdentification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */