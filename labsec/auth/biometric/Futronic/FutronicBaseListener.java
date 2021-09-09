/*    */ package labsec.auth.biometric.Futronic;
/*    */ 
/*    */ import com.futronic.SDKHelper.FTR_PROGRESS;
/*    */ import com.futronic.SDKHelper.ICallBack;
/*    */ import java.awt.image.BufferedImage;
/*    */ import labsec.auth.biometric.event.BaseAdapter;
/*    */ import labsec.auth.biometric.event.BaseListener;
/*    */ import labsec.auth.biometric.event.ProgressEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FutronicBaseListener
/*    */   implements BaseListener, ICallBack
/*    */ {
/*    */   protected BaseListener delegateListener;
/*    */   
/*    */   public FutronicBaseListener() {
/* 18 */     this.delegateListener = (BaseListener)new BaseAdapter();
/*    */   }
/*    */   public FutronicBaseListener(BaseListener baseListener) {
/*    */     BaseAdapter baseAdapter;
/* 22 */     if (baseListener == null) {
/* 23 */       baseAdapter = new BaseAdapter();
/*    */     }
/* 25 */     this.delegateListener = (BaseListener)baseAdapter;
/*    */   }
/*    */   
/*    */   private ProgressEvent createEvent(FTR_PROGRESS futProg) {
/* 29 */     ProgressEvent e = new ProgressEvent(futProg.m_Count, futProg.m_Total);
/* 30 */     return e;
/*    */   }
/*    */ 
/*    */   
/*    */   public void OnPutOn(FTR_PROGRESS Progress) {
/* 35 */     putFingerOn(createEvent(Progress));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void OnTakeOff(FTR_PROGRESS Progress) {
/* 41 */     takeFingerOff(createEvent(Progress));
/*    */   }
/*    */ 
/*    */   
/*    */   public void UpdateScreenImage(BufferedImage Bitmap) {
/* 46 */     updateImage(Bitmap);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean OnFakeSource(FTR_PROGRESS Progress) {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void putFingerOn(ProgressEvent progressEvent) {
/* 56 */     this.delegateListener.putFingerOn(progressEvent);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void takeFingerOff(ProgressEvent progressEvent) {
/* 62 */     this.delegateListener.takeFingerOff(progressEvent);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateImage(BufferedImage bitmapImage) {
/* 67 */     this.delegateListener.updateImage(bitmapImage);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicBaseListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */