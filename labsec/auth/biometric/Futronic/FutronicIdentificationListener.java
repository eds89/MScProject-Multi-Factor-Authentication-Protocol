/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.IIdentificationCallBack;
/*    */ import labsec.auth.biometric.event.BaseListener;
/*    */ import labsec.auth.biometric.event.IdentificationAdapter;
/*    */ import labsec.auth.biometric.event.IdentificationEvent;
/*    */ import labsec.auth.biometric.event.IdentificationListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FutronicIdentificationListener
/*    */   extends FutronicBaseListener
/*    */   implements IdentificationListener, IIdentificationCallBack
/*    */ {
/*    */   public FutronicIdentificationListener() {
/* 18 */     this((IdentificationListener)new IdentificationAdapter());
/*    */   }
/*    */   
/*    */   public FutronicIdentificationListener(IdentificationListener identificationListener) {
/* 22 */     super((BaseListener)identificationListener);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void OnGetBaseTemplateComplete(boolean bSuccess, int nResult) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void identificationComplete(IdentificationEvent event) {
/* 33 */     ((IdentificationListener)this.delegateListener).identificationComplete(event);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicIdentificationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */