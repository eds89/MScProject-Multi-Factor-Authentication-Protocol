/*    */ package labsec.auth.smartcard;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import softplan.prototype.common.ClientData;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/*    */   public static byte[] transform(Object o) throws IOException {
/* 19 */     ByteArrayOutputStream bos = null;
/* 20 */     ObjectOutputStream oos = null;
/*    */     try {
/* 22 */       bos = new ByteArrayOutputStream();
/* 23 */       oos = new ObjectOutputStream(bos);
/*    */ 
/*    */       
/* 26 */       oos.writeObject(o);
/* 27 */       byte[] writtenArray = bos.toByteArray();
/* 28 */       return writtenArray;
/*    */     } finally {
/* 30 */       if (oos != null) {
/*    */         try {
/* 32 */           oos.close();
/* 33 */         } catch (IOException iOException) {}
/*    */       }
/*    */       
/* 36 */       if (bos != null) {
/*    */         try {
/* 38 */           bos.close();
/* 39 */         } catch (IOException iOException) {}
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object transform(byte[] b) throws IOException {
/* 47 */     ObjectInputStream ois = null;
/*    */     try {
/* 49 */       ois = new ObjectInputStream(new ByteArrayInputStream(b));
/*    */       
/* 51 */       Object readObject = ois.readObject();
/* 52 */       ois.close();
/* 53 */       return readObject;
/* 54 */     } catch (ClassNotFoundException cnfe) {
/*    */       
/* 56 */       throw new IOException(cnfe);
/*    */     } finally {
/* 58 */       if (ois != null) {
/*    */         try {
/* 60 */           ois.close();
/* 61 */         } catch (IOException iOException) {}
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 70 */     ClientData clData = new ClientData("666", "Name");
/*    */     
/* 72 */     Serializable empty = new Serializable() {  };
/* 73 */     byte[] emptyArr = transform(empty);
/* 74 */     byte[] b = transform(clData);
/*    */     
/* 76 */     Object o = transform(b);
/* 77 */     ClientData clData2 = clData;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isNull(Object o) {
/* 83 */     return (o == null);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\smartcard\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */