/*    */ package test;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapTest
/*    */ {
/*    */   public static void main(String[] args) {
/* 13 */     Map<BigInteger, String> map = new HashMap<BigInteger, String>();
/* 14 */     map.put(BigInteger.ZERO, "teste");
/*    */     
/* 16 */     map.put(BigInteger.ONE, "teste-666");
/*    */     
/* 18 */     System.out.println(map.containsKey(new BigInteger("0")));
/* 19 */     System.out.println(map.containsKey(new BigInteger("1")));
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\test\MapTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */