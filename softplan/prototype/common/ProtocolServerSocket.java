/*    */ package softplan.prototype.common;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.ServerSocket;
/*    */ 
/*    */ 
/*    */ public class ProtocolServerSocket
/*    */   extends ServerSocket
/*    */ {
/*    */   public ProtocolServerSocket() throws IOException {}
/*    */   
/*    */   public ProtocolServerSocket(int port) throws IOException {
/* 14 */     super(port);
/*    */   }
/*    */ 
/*    */   
/*    */   public ProtocolServerSocket(int port, int backlog) throws IOException {
/* 19 */     super(port, backlog);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ProtocolServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
/* 25 */     super(port, backlog, bindAddr);
/*    */   }
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\ProtocolServerSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */