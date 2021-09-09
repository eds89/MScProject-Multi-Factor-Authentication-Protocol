/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.IEnrollmentCallBack;
/*    */ import labsec.auth.biometric.event.BaseListener;
/*    */ import labsec.auth.biometric.event.EnrollmentAdapter;
/*    */ import labsec.auth.biometric.event.EnrollmentEvent;
/*    */ import labsec.auth.biometric.event.EnrollmentListener;
/*    */ 
/*    */ public class FutronicEnrollmentListener
/*    */   extends FutronicBaseListener
/*    */   implements EnrollmentListener, IEnrollmentCallBack
/*    */ {
/*    */   public FutronicEnrollmentListener() {
/* 14 */     this((EnrollmentListener)new EnrollmentAdapter());
/*    */   }
/*    */   
/*    */   public FutronicEnrollmentListener(EnrollmentListener enrollmentListener) {
/* 18 */     super((BaseListener)enrollmentListener);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void OnEnrollmentComplete(boolean bSuccess, int nResult) {
/* 24 */     enrollmentComplete(new EnrollmentEvent(bSuccess));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void enrollmentComplete(EnrollmentEvent event) {
/* 30 */     ((EnrollmentListener)this.delegateListener).enrollmentComplete(event);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicEnrollmentListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */