/*     */ package softplan.prototype.common;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import labsec.auth.smartcard.SmartcardAccess;
/*     */ import labsec.auth.smartcard.SmartcardException;
/*     */ import labsec.auth.smartcard.SmartcardInitializationError;
/*     */ import labsec.auth.smartcard.Utils;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseEntity
/*     */ {
/*  33 */   private static final Logger logger = Logger.getLogger(BaseEntity.class);
/*     */   
/*     */   protected SmartcardAccess smartcard;
/*     */   
/*     */   protected SSLSocket sslSocket;
/*     */   
/*     */   protected InputStream in;
/*     */   
/*     */   protected OutputStream out;
/*     */   
/*     */   protected String serverHostname;
/*     */   
/*     */   protected int serverPort;
/*     */   
/*     */   protected ExceptionHandler exceptionHandler;
/*     */   protected PINRequestHandler pinRequestHandler;
/*     */   protected String identifier;
/*  50 */   protected List<EntityListener> listeners = new ArrayList<EntityListener>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initSmartcardAccess() {
/*  59 */     this.smartcard = new SmartcardAccess();
/*     */     try {
/*  61 */       this.smartcard.initializeProvider();
/*  62 */     } catch (SmartcardInitializationError e) {
/*  63 */       logger.fatal(String.valueOf(this.identifier) + " could not init smartcard access object", (Throwable)e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEntityListener(EntityListener ml) {
/*  69 */     this.listeners.add(ml);
/*     */   }
/*     */   
/*     */   public void removeEntityListener(EntityListener ml) {
/*  73 */     this.listeners.remove(ml);
/*     */   }
/*     */   
/*     */   protected void fireReceivedMessage(String messageKey) {
/*  77 */     MessageEvent eventObject = new MessageEvent(messageKey);
/*  78 */     for (EntityListener ml : this.listeners) {
/*  79 */       ml.messageReceived(eventObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fireEntityConnected() {
/*  85 */     for (EntityListener each : this.listeners) {
/*  86 */       each.entityConnected();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireEntityDisconnected() {
/*  91 */     for (EntityListener each : this.listeners) {
/*  92 */       each.entityDisconnected();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireReceivedNonce(BigInteger nonce) {
/*  97 */     MessageEvent eventOEvent = new MessageEvent(nonce.toString());
/*  98 */     for (EntityListener ml : this.listeners) {
/*  99 */       ml.nonceReceived(eventOEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireBeginProcessing() {
/* 104 */     for (EntityListener ml : this.listeners) {
/* 105 */       ml.beginProcessing();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireEndProcessing() {
/* 110 */     for (EntityListener ml : this.listeners) {
/* 111 */       ml.endProcessing();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireUserAuthenticated() {
/* 116 */     for (EntityListener ml : this.listeners) {
/* 117 */       ml.userAuthenticated();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireUserNotAuthenticated() {
/* 122 */     for (EntityListener ml : this.listeners) {
/* 123 */       ml.userNotAuthenticated();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSmartcardInitialized() {
/* 128 */     return this.smartcard.isProviderInitialized();
/*     */   }
/*     */   
/*     */   public ExceptionHandler getExceptionHandler() {
/* 132 */     return this.exceptionHandler;
/*     */   }
/*     */   
/*     */   public void setExceptionHandler(ExceptionHandler exceptionHandler) {
/* 136 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */   
/*     */   public PINRequestHandler getPinRequestHandler() {
/* 140 */     return this.pinRequestHandler;
/*     */   }
/*     */   
/*     */   public void setPinRequestHandler(PINRequestHandler pinRequestHandler) {
/* 144 */     this.pinRequestHandler = pinRequestHandler;
/*     */   }
/*     */   
/*     */   public String getIdentifier() {
/* 148 */     return this.identifier;
/*     */   }
/*     */   
/*     */   public void setIdentifier(String identifier) {
/* 152 */     this.identifier = identifier;
/*     */   }
/*     */   
/*     */   public String getServerHostname() {
/* 156 */     return this.serverHostname;
/*     */   }
/*     */   
/*     */   public void setServerHostname(String serverHostname) {
/* 160 */     this.serverHostname = serverHostname;
/*     */   }
/*     */   
/*     */   public int getServerPort() {
/* 164 */     return this.serverPort;
/*     */   }
/*     */   
/*     */   public void setServerPort(int serverPort) {
/* 168 */     this.serverPort = serverPort;
/*     */   }
/*     */ 
/*     */   
/* 172 */   protected static SSLSocketFactory sslSocketFactory = null; protected byte[] inputArray;
/*     */   
/*     */   protected SSLSocketFactory getSSLSocketFactory() {
/* 175 */     if (sslSocketFactory == null) {
/*     */       try {
/* 177 */         logger.debug("Creating SSL Socket Factory for first-time use only");
/* 178 */         TrustManager[] tm = {
/* 179 */             new NaiveTrustManager() };
/* 180 */         SSLContext context = SSLContext.getInstance("SSL");
/* 181 */         context.init(new javax.net.ssl.KeyManager[0], tm, new SecureRandom());
/*     */         
/* 183 */         sslSocketFactory = context.getSocketFactory();
/* 184 */         logger.debug("SSL Socket Factory created successfully");
/* 185 */       } catch (Exception e) {
/* 186 */         logger.error("Could not create SSL Socket Factory", e);
/* 187 */         sslSocketFactory = null;
/*     */       } 
/*     */     }
/*     */     
/* 191 */     return sslSocketFactory;
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 195 */     boolean connected = (this.sslSocket != null) ? this.sslSocket.isConnected() : false;
/* 196 */     logger.debug(String.valueOf(this.identifier) + "'s SSLSocket is connected? " + connected);
/* 197 */     return connected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 204 */     logger.debug("Closing connection for " + this.identifier);
/* 205 */     if (this.sslSocket != null) {
/*     */       try {
/* 207 */         if (this.sslSocket.isConnected()) {
/* 208 */           writeObject("Bye!");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 214 */         this.sslSocket.close();
/* 215 */       } catch (Exception e) {
/*     */         
/* 217 */         logger.debug("Error while closing " + this.identifier + " socket", e);
/*     */       } 
/* 219 */       this.sslSocket = null;
/* 220 */       this.in = null;
/* 221 */       this.out = null;
/* 222 */       fireEntityDisconnected();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void beginCommunicationToServer() {
/* 227 */     Runnable r = new Runnable()
/*     */       {
/*     */         public void run() {
/* 230 */           BaseEntity.logger.debug("Beginning " + BaseEntity.this.identifier + " Entity thread.");
/*     */           try {
/* 232 */             BaseEntity.this.connect();
/* 233 */             BaseEntity.this.handleServerRequestss();
/* 234 */           } catch (SocketException se) {
/*     */             
/* 236 */             if (!"socket closed".equalsIgnoreCase(se.getMessage())) {
/* 237 */               BaseEntity.logger.error(String.valueOf(BaseEntity.this.identifier) + " Comm Exception", se);
/*     */             }
/* 239 */           } catch (Exception e) {
/* 240 */             BaseEntity.logger.error(String.valueOf(BaseEntity.this.identifier) + " Comm Exception", e);
/* 241 */             BaseEntity.this.exceptionHandler.handleException(e);
/*     */           } finally {
/*     */             
/* 244 */             BaseEntity.this.disconnect();
/*     */           } 
/* 246 */           BaseEntity.logger.debug("End of " + BaseEntity.this.identifier + " Entity thread.");
/*     */         }
/*     */       };
/* 249 */     Thread t = new Thread(r, this.identifier);
/* 250 */     t.start();
/*     */   }
/*     */   public BaseEntity() {
/* 253 */     this.inputArray = new byte[8192];
/*     */     initSmartcardAccess();
/*     */   } protected void cleanInputArray() {
/* 256 */     Arrays.fill(this.inputArray, (byte)0);
/*     */   }
/*     */   
/*     */   protected void handleServerRequestss() throws IOException {
/* 260 */     int amountRead = 0;
/* 261 */     cleanInputArray();
/* 262 */     logger.debug(String.valueOf(this.identifier) + " is handling server requests");
/* 263 */     while ((amountRead = this.in.read(this.inputArray)) != -1) {
/* 264 */       logger.trace("total bytes read: " + amountRead);
/* 265 */       Object objFromServer = Utils.transform(this.inputArray);
/* 266 */       logger.debug(String.valueOf(this.identifier) + " received an object: " + 
/* 267 */           objFromServer);
/* 268 */       if (objFromServer instanceof Message) {
/*     */         
/*     */         try {
/* 271 */           Message msgObj = (Message)objFromServer;
/*     */           
/* 273 */           String eventKey = removeIdentifierString(msgObj.getLast().toString());
/* 274 */           fireReceivedMessage(eventKey);
/*     */           
/* 276 */           processReceived(msgObj);
/* 277 */         } catch (Exception e) {
/*     */ 
/*     */           
/* 280 */           logger.error("Exception while processing received Message", e);
/* 281 */           this.exceptionHandler.handleException(e);
/*     */         } 
/* 283 */       } else if (objFromServer instanceof String) {
/* 284 */         String command = (String)objFromServer;
/* 285 */         if ("UserAuthenticated".equals(command))
/* 286 */         { fireUserAuthenticated(); }
/* 287 */         else { if ("UserNotAuthenticated".equals(command)) {
/* 288 */             fireUserNotAuthenticated(); break;
/*     */           } 
/* 290 */           if ("Bye!".equals(command))
/*     */             break;  }
/*     */       
/*     */       } 
/* 294 */       amountRead = 0;
/* 295 */       cleanInputArray();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected String removeIdentifierString(String original) {
/* 300 */     return original.replace(this.identifier, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect() throws IOException, UnknownHostException {
/* 314 */     SSLSocketFactory sslSocketFac = getSSLSocketFactory();
/* 315 */     logger.info(String.valueOf(this.identifier) + " trying to connect to server on host=" + 
/* 316 */         this.serverHostname + " and port=" + this.serverPort);
/* 317 */     this.sslSocket = (SSLSocket)sslSocketFac.createSocket(
/* 318 */         this.serverHostname, this.serverPort);
/* 319 */     logger.info(String.valueOf(this.identifier) + " connected to server on host=" + this.serverHostname + " and port=" + this.serverPort);
/* 320 */     this.sslSocket.startHandshake();
/*     */     
/* 322 */     InputStream origIs = this.sslSocket.getInputStream();
/* 323 */     this.in = origIs;
/* 324 */     logger.trace("in=" + this.in);
/*     */     
/* 326 */     OutputStream origOs = this.sslSocket.getOutputStream();
/* 327 */     this.out = origOs;
/* 328 */     logger.trace("out=" + this.out);
/* 329 */     fireEntityConnected();
/*     */   }
/*     */   
/*     */   public String[] obtainCardData(char[] pin) throws SmartcardException {
/* 333 */     logger.info(String.valueOf(this.identifier) + " is trying to obtain smartcard's data");
/* 334 */     String[] cardAliases = this.smartcard.getAliases(pin);
/* 335 */     logger.info(String.valueOf(this.identifier) + " obtained smartcard's data successfully");
/* 336 */     return cardAliases;
/*     */   }
/*     */   
/*     */   protected void writeObject(Object toWrite) throws IOException {
/* 340 */     logger.debug(String.valueOf(this.identifier) + " is transmitting object over stream: " + toWrite);
/* 341 */     this.out.write(Utils.transform(toWrite));
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void processReceived(Message paramMessage) throws IOException, ProtocolException, SmartcardException;
/*     */   
/*     */   protected abstract void processReceived(BigInteger paramBigInteger) throws IOException, ProtocolException, SmartcardException;
/*     */   
/*     */   protected static class NaiveTrustManager
/*     */     implements X509TrustManager
/*     */   {
/*     */     public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 357 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\BaseEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */