/*    */ package labsec.auth.biometric;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FingerprintManagement
/*    */ {
/* 12 */   private static final Map<String, FingerprintReader> readersMap = new HashMap<String, FingerprintReader>();
/*    */   
/*    */   public static void addSupportedReader(FingerprintReader reader) {
/* 15 */     readersMap.put(reader.getName(), reader);
/*    */   }
/*    */ 
/*    */   
/*    */   public static FingerprintReader getInstance(String name) {
/* 20 */     return readersMap.get(name);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\FingerprintManagement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */