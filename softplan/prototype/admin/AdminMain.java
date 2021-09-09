/*    */ package softplan.prototype.admin;
/*    */ 
/*    */ import softplan.prototype.common.Configuration;
/*    */ import softplan.prototype.common.GUIUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdminMain
/*    */ {
/*    */   static {
/* 11 */     Configuration.configureAdmin();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 18 */     AdminEntity adminEntity = new AdminEntity();
/* 19 */     if (!adminEntity.isSmartcardInitialized()) {
/* 20 */       GUIUtils.showErrorDialog(null, "Could not initialize smartcard PKCS11 provider.Check the log messages for details.\nMake sure the all prerequirements have been set up correctly.\n\nThe management interface cannot be initialized.");
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */ 
/*    */     
/* 27 */     ManagementInterface mf = new ManagementInterface(adminEntity);
/* 28 */     adminEntity.setExceptionHandler(mf);
/* 29 */     mf.setVisible(true);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\admin\AdminMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */