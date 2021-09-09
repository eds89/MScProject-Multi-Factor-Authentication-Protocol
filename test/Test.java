/*    */ package test;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Arrays;
/*    */ import java.util.Properties;
/*    */ 
/*    */ public class Test {
/*    */   static {
/* 10 */     byte[] b = new byte[2];
/* 11 */     System.out.println(b instanceof byte[]);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 17 */     Properties allProps = System.getProperties();
/* 18 */     allProps.list(System.out);
/*    */     
/* 20 */     System.out.println(File.separator);
/* 21 */     System.out.println(File.pathSeparator);
/*    */ 
/*    */ 
/*    */     
/* 25 */     File f = new File("dlls" + File.separatorChar);
/* 26 */     System.out.println(f.getAbsolutePath());
/*    */     
/* 28 */     ByteBuffer bBuffer = ByteBuffer.allocate(4096);
/* 29 */     byte[] arr1 = new byte[5];
/* 30 */     byte[] arr2 = new byte[5];
/* 31 */     Arrays.fill(arr1, (byte)1);
/* 32 */     Arrays.fill(arr2, (byte)2);
/*    */     
/* 34 */     bBuffer.put(arr1);
/* 35 */     bBuffer.put(arr2);
/* 36 */     byte[] result = bBuffer.array();
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\test\Test.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */