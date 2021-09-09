/*    */ package softplan.prototype.common;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JOptionPane;
/*    */ import labsec.auth.smartcard.SmartcardAccess;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GUIUtils
/*    */ {
/*    */   public static void showErrorDialog(Component parent, Object errorMsg) {
/* 15 */     JOptionPane.showMessageDialog(parent, errorMsg, "Error", 0);
/*    */   }
/*    */   
/*    */   public static void showInfoDialog(Component parent, Object infoMsg) {
/* 19 */     JOptionPane.showMessageDialog(parent, infoMsg, "Information", 1);
/*    */   }
/*    */   
/*    */   public static void showAlertDialog(Component parent, Object warnMsg) {
/* 23 */     JOptionPane.showMessageDialog(parent, warnMsg, "Warning", 2);
/*    */   }
/*    */   
/*    */   public static String showInputDialog(Component parent, Object questionMsg, String[] values) {
/* 27 */     return showInputDialog(parent, questionMsg, values, values[0]);
/*    */   }
/*    */   
/*    */   public static String showInputDialog(Component parent, Object questionMsg, String[] values, String initialValue) {
/* 31 */     Object selected = JOptionPane.showInputDialog(parent, questionMsg, "Input required", 3, null, (Object[])values, initialValue);
/* 32 */     if (selected == null) {
/* 33 */       return null;
/*    */     }
/* 35 */     return selected.toString();
/*    */   }
/*    */   
/*    */   public static String showInputDialog(Component parent, Object questionMsg) {
/* 39 */     String s = null;
/*    */     do {
/* 41 */       s = JOptionPane.showInputDialog(parent, questionMsg, "Input required", 3);
/* 42 */     } while (s == null || s.equals(""));
/* 43 */     return s;
/*    */   }
/*    */   
/*    */   public static char[] showPINRequestDialog(Component parent) {
/* 47 */     return showPINRequestDialog(parent, "Enter THE PIN to access smartcard's content.");
/*    */   }
/*    */   
/*    */   public static char[] showPINRequestDialog(Component parent, String message) {
/* 51 */     String pin = null;
/*    */     
/* 53 */     boolean correct = false;
/* 54 */     while (!correct) {
/* 55 */       pin = showInputDialog(parent, message);
/*    */       
/* 57 */       if (!SmartcardAccess.isValidPIN(pin)) {
/* 58 */         showErrorDialog(parent, "The PIN must be between 4 and 8 digits.");
/*    */         continue;
/*    */       } 
/* 61 */       correct = true;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 66 */     return pin.toCharArray();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\GUIUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */