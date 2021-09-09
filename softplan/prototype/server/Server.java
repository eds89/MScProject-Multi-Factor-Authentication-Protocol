/*     */ package softplan.prototype.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.BindException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.security.Key;
/*     */ import java.security.PublicKey;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import labsec.auth.biometric.AbstractFingerprintReader;
/*     */ import labsec.auth.biometric.DefaultUserRecord;
/*     */ import labsec.auth.biometric.Futronic.FutronicReader;
/*     */ import labsec.auth.biometric.UserRecord;
/*     */ import labsec.auth.smartcard.SmartcardAccess;
/*     */ import labsec.auth.smartcard.SmartcardInitializationError;
/*     */ import labsec.auth.smartcard.Utils;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.ClientData;
/*     */ import softplan.prototype.common.Constants;
/*     */ import softplan.prototype.common.GUIUtils;
/*     */ import softplan.prototype.common.Message;
/*     */ import softplan.prototype.database.Database;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Server
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(Server.class);
/*     */   
/*     */   protected ServerSocket adminServerSocket;
/*     */   
/*     */   protected ServerSocket clientServerSocket;
/*     */   
/*     */   protected ServerSocket databaseServerSocket;
/*     */   
/*     */   protected Socket socAdmin;
/*     */   
/*     */   protected InputStream inAdmin;
/*     */   
/*     */   protected OutputStream outAdmin;
/*     */   
/*     */   protected Socket socClient;
/*     */   protected InputStream inClient;
/*     */   protected OutputStream outClient;
/*     */   protected InputStream inDatabase;
/*     */   protected OutputStream outDatabase;
/*     */   protected Database database;
/*  65 */   protected Map<String, BigInteger> authCache = new HashMap<String, BigInteger>();
/*     */ 
/*     */ 
/*     */   
/*     */   protected SmartcardAccess smartcard;
/*     */ 
/*     */ 
/*     */   
/*  73 */   protected Set<CacheEntry> cacheAsSet = new HashSet<CacheEntry>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractFingerprintReader toVerification;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final byte[][] inputArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initializeSmartcard() {
/*  91 */     this.smartcard = new SmartcardAccess();
/*     */     try {
/*  93 */       this.smartcard.initializeProvider();
/*  94 */     } catch (SmartcardInitializationError e) {
/*  95 */       logger.fatal("Server could not init smartcard access object", (Throwable)e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void initializeBiometrics() {
/*     */     try {
/* 101 */       this.toVerification = (AbstractFingerprintReader)new FutronicReader();
/* 102 */       this.toVerification.initialize();
/* 103 */     } catch (Throwable t) {
/* 104 */       logger.fatal("Server could not initialize fingerprint reader for verification.", t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void populateWithMock() {
/* 109 */     Message m = new Message();
/* 110 */     m.add(new byte[4]);
/* 111 */     m.add(new byte[666]);
/* 112 */     m.add("Client::AAAAAAAAAAAA");
/* 113 */     this.cacheAsSet.add(new CacheEntry("Client::", m));
/*     */   }
/*     */   
/*     */   protected ServerSocket createSSLServerSocket(int portNumber) throws IOException {
/* 117 */     logger.info("Creating SSL Server Socket on port " + portNumber);
/* 118 */     ServerSocketFactory ssFactory = SSLServerSocketFactory.getDefault();
/* 119 */     ServerSocket sSocket = ssFactory.createServerSocket(
/* 120 */         portNumber);
/* 121 */     return sSocket;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initAdminServer() throws IOException {
/* 126 */     logger.info("Initializing Admin Server");
/* 127 */     this.adminServerSocket = createSSLServerSocket(
/* 128 */         Constants.ADMIN_SERVER_PORT);
/*     */   }
/*     */   
/*     */   public void initClientServer() throws IOException {
/* 132 */     logger.info("Initializing Client Server");
/* 133 */     this.clientServerSocket = createSSLServerSocket(
/* 134 */         15668);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeAdmin() {
/* 139 */     logger.info("Closing Admin");
/* 140 */     close(this.socAdmin);
/* 141 */     this.socAdmin = null;
/* 142 */     this.inAdmin = null;
/* 143 */     this.outAdmin = null;
/*     */   }
/*     */   
/*     */   public void closeClient() {
/* 147 */     logger.info("Closing Client");
/* 148 */     close(this.socClient);
/* 149 */     this.socClient = null;
/* 150 */     this.inClient = null;
/* 151 */     this.outClient = null;
/*     */   }
/*     */   
/*     */   private void close(Socket s) {
/*     */     try {
/* 156 */       if (s != null)
/*     */       {
/*     */         
/* 159 */         s.close();
/*     */       }
/* 161 */     } catch (IOException e) {
/* 162 */       logger.debug("Error while closing socket and streams", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAdminConnected() {
/* 167 */     return isConnected(this.socAdmin);
/*     */   }
/*     */   
/*     */   public boolean isClientConnected() {
/* 171 */     return isConnected(this.socClient);
/*     */   }
/*     */   
/*     */   private boolean isConnected(Socket s) {
/* 175 */     return (s != null && !s.isClosed());
/*     */   }
/*     */   
/*     */   public void waitForAdmin() throws IOException {
/* 179 */     logger.info("Waiting for Admin");
/* 180 */     this.socAdmin = this.adminServerSocket.accept();
/* 181 */     logger.info("Admin connected. " + this.socAdmin);
/*     */     
/* 183 */     InputStream in = this.socAdmin.getInputStream();
/* 184 */     this.inAdmin = in;
/*     */     
/* 186 */     OutputStream os = this.socAdmin.getOutputStream();
/* 187 */     this.outAdmin = os;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void waitForClient() throws IOException {
/* 197 */     logger.info("Waiting for Client");
/* 198 */     this.socClient = this.clientServerSocket.accept();
/* 199 */     logger.info("Client connected. " + this.socClient);
/*     */     
/* 201 */     InputStream is = this.socClient.getInputStream();
/* 202 */     this.inClient = is;
/*     */ 
/*     */     
/* 205 */     OutputStream os = this.socClient.getOutputStream();
/* 206 */     this.outClient = os;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginAdminCommunication() {
/* 211 */     Runnable r = new Runnable()
/*     */       {
/*     */         public void run() {
/* 214 */           Server.logger.info("Beginning Admin's Server Thread");
/*     */           try {
/* 216 */             Server.this.initAdminServer();
/* 217 */           } catch (BindException be) {
/* 218 */             Server.logger.debug("", be);
/* 219 */             Server.this.handleBindException("Admin", Constants.ADMIN_SERVER_PORT);
/*     */             return;
/* 221 */           } catch (IOException ioe) {
/* 222 */             Server.logger.fatal("Error while initializing Admin server", ioe);
/*     */             return;
/*     */           } 
/*     */           while (true) {
/*     */             try {
/* 227 */               Server.this.waitForAdmin();
/* 228 */               Server.this.sendAdminCachedMessages();
/* 229 */               Server.this.handleAdminRequests();
/*     */             }
/* 231 */             catch (Exception e) {
/* 232 */               Server.logger.error("Admin Server", e); continue;
/*     */             } finally {
/* 234 */               Server.this.closeAdmin();
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 241 */     Thread adminThread = new Thread(r, "Admin_Server");
/* 242 */     adminThread.start();
/*     */   }
/*     */   
/*     */   public void beginClientCommunication() {
/* 246 */     Runnable r = new Runnable()
/*     */       {
/*     */         public void run() {
/* 249 */           Server.logger.info("Beginning Client's Server Thread");
/*     */           
/*     */           try {
/* 252 */             Server.this.initClientServer();
/* 253 */           } catch (BindException be) {
/* 254 */             Server.logger.debug("", be);
/* 255 */             Server.this.handleBindException("Client", 15668);
/*     */             return;
/* 257 */           } catch (IOException e) {
/* 258 */             Server.logger.fatal("Error while initializing Client server", e);
/*     */             return;
/*     */           } 
/*     */           while (true) {
/*     */             try {
/* 263 */               Server.this.waitForClient();
/*     */ 
/*     */               
/* 266 */               Server.this.sendClientCachedMessages();
/*     */               
/* 268 */               Server.this.handleClientRequests();
/*     */ 
/*     */             
/*     */             }
/* 272 */             catch (BindException be) {
/* 273 */               Server.this.handleBindException("Client", 15668); continue;
/* 274 */             } catch (Exception e) {
/* 275 */               Server.logger.error("Client Server", e); continue;
/*     */             } finally {
/* 277 */               Server.this.closeClient();
/*     */             } 
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 284 */     Thread clientThread = new Thread(r, "Client_Server");
/* 285 */     clientThread.start();
/*     */   }
/*     */   
/*     */   protected void handleBindException(String user, int port) {
/* 289 */     String message = "Cannot start server socket for " + 
/* 290 */       user + " on port " + port + ".\nChange the corresponded port number in the configuration file and try again.";
/* 291 */     GUIUtils.showErrorDialog(null, message);
/*     */   }
/*     */   
/*     */   protected void sendAdminCachedMessages() throws IOException {
/* 295 */     logger.debug("Sending Admin's cached data");
/* 296 */     sendCacheMessages("Admin::", this.outAdmin);
/*     */   }
/*     */   
/*     */   protected void sendClientCachedMessages() throws IOException {
/* 300 */     logger.debug("Sending Client's cached data");
/* 301 */     sendCacheMessages("Client::", this.outClient);
/*     */   }
/*     */   
/*     */   protected void sendDatabaseCachedMessages() throws IOException {
/* 305 */     logger.debug("Sending Database's cached data");
/* 306 */     sendCacheMessages("Database::", this.outDatabase);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendCacheMessages(String key, OutputStream outputStreamEntity) throws IOException {
/* 311 */     logger.debug("Sending cached messages to " + key);
/* 312 */     logger.trace("total of cached messages: " + this.cacheAsSet.size());
/* 313 */     Set<CacheEntry> set = this.cacheAsSet;
/* 314 */     int sentCount = 0;
/* 315 */     Iterator<CacheEntry> iterator = set.iterator();
/* 316 */     while (iterator.hasNext()) {
/* 317 */       CacheEntry entry = iterator.next();
/* 318 */       if (key.equals(entry.getKey())) {
/* 319 */         Message cachedMessage = entry.getMessage();
/* 320 */         logger.debug("Found cached message: " + ++sentCount);
/* 321 */         byte[] bCachedMessage = Utils.transform(cachedMessage);
/* 322 */         outputStreamEntity.write(bCachedMessage);
/* 323 */         iterator.remove();
/*     */       } 
/*     */     } 
/* 326 */     logger.debug("End of sending cached data to " + key);
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
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void handleAdminRequests() throws IOException {
/* 354 */     logger.info("Handling Admin's requests");
/* 355 */     handleRequests(this.inAdmin, this.outAdmin);
/*     */   }
/*     */   
/*     */   public void handleClientRequests() throws IOException {
/* 359 */     logger.info("Handling Client's requests");
/* 360 */     handleRequests(this.inClient, this.outClient);
/*     */   }
/*     */   
/*     */   public void handleDatabaseRequests() throws IOException {
/* 364 */     logger.info("Handling Database's requests");
/* 365 */     handleRequests(this.inDatabase, this.outDatabase);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Server() {
/* 371 */     this.inputArray = new byte[3][8192];
/*     */     this.database = new Database();
/*     */     initializeSmartcard(); } private byte[] getInputArrayFor(InputStream is) {
/*     */     int index;
/* 375 */     if (is == this.inAdmin) {
/* 376 */       index = 0;
/* 377 */     } else if (is == this.inClient) {
/* 378 */       index = 1;
/*     */     } else {
/*     */       
/* 381 */       index = 2;
/*     */     } 
/*     */     
/* 384 */     return this.inputArray[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRequests(InputStream inStream, OutputStream outStream) throws IOException {
/* 389 */     int amountRead = 0;
/* 390 */     byte[] inputArray = getInputArrayFor(inStream);
/* 391 */     Arrays.fill(inputArray, (byte)0);
/*     */     
/* 393 */     while ((amountRead = inStream.read(inputArray)) != -1) {
/* 394 */       logger.trace("total bytes read: " + amountRead);
/* 395 */       Object o = Utils.transform(inputArray);
/* 396 */       logger.debug("Read object from server stream: " + o);
/* 397 */       if (o instanceof Message) {
/* 398 */         Message rcvMessage = (Message)o;
/* 399 */         if (rcvMessage.isAuth()) {
/* 400 */           String comm = authProccess(rcvMessage);
/* 401 */           outStream.write(Utils.transform(comm));
/* 402 */           if (comm.equals("UserNotAuthenticated")) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 407 */         String destEntity = (String)rcvMessage.getLast();
/* 408 */         redirectMessage(destEntity, rcvMessage);
/*     */       }
/* 410 */       else if (o instanceof String) {
/* 411 */         String rcvComm = (String)o;
/* 412 */         logger.debug("Received command: " + rcvComm);
/*     */         
/* 414 */         if ("Client's Cached Data".equals(rcvComm)) {
/* 415 */           logger.debug("Server will send client's cached data");
/* 416 */           sendClientCachedMessages();
/*     */         }
/* 418 */         else if (rcvComm.startsWith("AuthRequestWithBiometrics")) {
/* 419 */           String clientAlias = rcvComm.replace(
/* 420 */               "AuthRequestWithBiometrics", "");
/*     */           
/* 422 */           Message firstAuthMsg = beginAuthProcess(clientAlias);
/* 423 */           byte[] firstAuthArr = Utils.transform(firstAuthMsg);
/*     */           
/* 425 */           logger.debug("Sending message for authentication: " + firstAuthMsg);
/* 426 */           outStream.write(firstAuthArr);
/*     */         }
/* 428 */         else if ("Bye!".equals(rcvComm)) {
/* 429 */           logger.debug("End of transmission.");
/*     */           break;
/*     */         } 
/*     */       } 
/* 433 */       Arrays.fill(inputArray, (byte)0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String authProccess(Message message) {
/*     */     try {
/* 440 */       byte[] encMessage = Utils.transform(message.get(0));
/* 441 */       String clientAlias = message.getLast().toString().replace(
/* 442 */           "Client::", "");
/* 443 */       BigInteger authNonce = this.authCache.get(clientAlias);
/*     */ 
/*     */       
/* 446 */       SecretKeySpec nonceKey = new SecretKeySpec(authNonce.toByteArray(), 
/* 447 */           "DESede");
/*     */       
/* 449 */       byte[] decMessage = this.smartcard.decipherWithDESede(nonceKey, encMessage);
/* 450 */       Message inner = (Message)Utils.transform(decMessage);
/*     */       
/* 452 */       ClientData clientData = (ClientData)inner.get(0);
/* 453 */       String cpf = clientData.getCpf();
/* 454 */       byte[] template = clientData.getTemplate();
/*     */ 
/*     */       
/* 457 */       Message dbData = this.database.retrieveFromCPF(cpf);
/*     */       
/* 459 */       Message innerDbData = (Message)dbData.getFirst();
/*     */       
/* 461 */       ClientData dbClient = (ClientData)innerDbData.get(0);
/* 462 */       byte[] dbClientArr = Utils.transform(dbClient);
/* 463 */       byte[] innerSig = (byte[])dbData.get(1);
/*     */       
/* 465 */       PublicKey clientPub = this.smartcard.getPublicKey(clientAlias);
/* 466 */       boolean outerVer = this.smartcard.verifySign(clientPub, dbClientArr, innerSig);
/* 467 */       if (!outerVer) {
/* 468 */         logger.error("innerSig from database not verified");
/* 469 */         return "UserNotAuthenticated";
/*     */       } 
/*     */ 
/*     */       
/* 473 */       byte[] innerDbDataArr = Utils.transform(innerDbData);
/* 474 */       byte[] outerSig = (byte[])dbData.get(1);
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
/* 486 */       byte[] dbTemplate = dbClient.getTemplate();
/* 487 */       if (dbTemplate == null)
/*     */       {
/*     */         
/* 490 */         return "UserAuthenticated";
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 495 */       DefaultUserRecord defaultUserRecord = new DefaultUserRecord(
/* 496 */           dbClient.getCode(), dbTemplate);
/* 497 */       logger.trace("dbTemplate = " + dbTemplate);
/*     */       
/* 499 */       boolean templatesVerified = this.toVerification.verify((UserRecord)defaultUserRecord, 
/* 500 */           template);
/* 501 */       logger.trace("templatesVerified = " + templatesVerified);
/* 502 */       if (!templatesVerified) {
/* 503 */         return "UserNotAuthenticated";
/*     */       }
/*     */     }
/* 506 */     catch (Exception e) {
/* 507 */       logger.error("Could not complete authentication process", e);
/*     */       
/* 509 */       return "UserNotAuthenticated";
/*     */     } 
/*     */     
/* 512 */     return "UserAuthenticated";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Message beginAuthProcess(String clientAlias) {
/* 518 */     logger.debug("Beginning authentication process of " + clientAlias);
/* 519 */     Message authMessage = new Message(Message.Type.AUTH);
/*     */     try {
/* 521 */       Key sessionKey = this.smartcard.generateDESedeKey();
/*     */ 
/*     */       
/* 524 */       BigInteger nonce = this.smartcard.generateRandomNumber(24);
/* 525 */       logger.trace("generated nonce for authentication: " + nonce);
/*     */       
/* 527 */       logger.debug("Encrypting nonce");
/* 528 */       byte[] nonceArr = Utils.transform(nonce);
/* 529 */       byte[] encNonce = this.smartcard.cipherWithDESede(sessionKey, nonceArr);
/*     */       
/* 531 */       logger.debug("Encrypting session key");
/* 532 */       PublicKey clientPub = this.smartcard.getPublicKey(clientAlias);
/* 533 */       byte[] encSessionKey = this.smartcard.wrapWithRSA(sessionKey, clientPub);
/*     */       
/* 535 */       authMessage.add(encSessionKey);
/* 536 */       authMessage.add(encNonce);
/*     */ 
/*     */       
/* 539 */       this.authCache.put(clientAlias, nonce);
/* 540 */     } catch (Exception e) {
/* 541 */       logger.error("Could not begin authentication proccess", e);
/*     */     } 
/* 543 */     logger.debug("End of the authentication process of " + clientAlias);
/* 544 */     return authMessage;
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void redirectMessage(String dest, Message message) throws IOException {
/* 561 */     logger.info("Redirecting message to " + dest);
/* 562 */     byte[] bMess = Utils.transform(message);
/* 563 */     String cacheKey = null;
/*     */     
/* 565 */     if (dest.startsWith("Admin::")) {
/* 566 */       if (isAdminConnected()) {
/* 567 */         this.outAdmin.write(bMess);
/*     */       } else {
/* 569 */         cacheKey = "Admin::";
/*     */       } 
/* 571 */     } else if (dest.startsWith("Client::")) {
/* 572 */       if (isClientConnected()) {
/* 573 */         this.outClient.write(bMess);
/*     */       } else {
/* 575 */         cacheKey = "Client::";
/*     */       } 
/* 577 */     } else if (dest.startsWith("Database::")) {
/* 578 */       if (this.database.isConnected()) {
/*     */         try {
/* 580 */           this.database.insert(message);
/* 581 */         } catch (SQLException sqle) {
/* 582 */           logger.error("Could not insert message in the database", sqle);
/* 583 */           cacheKey = "Database::";
/*     */         } 
/*     */       } else {
/* 586 */         cacheKey = "Database::";
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 591 */     if (cacheKey != null) {
/* 592 */       logger.info("Storing message into cache for further redirection to " + cacheKey);
/* 593 */       this.cacheAsSet.add(new CacheEntry(cacheKey, message));
/*     */     } 
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
/*     */   protected static class CacheEntry
/*     */   {
/*     */     private final String key;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Message message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CacheEntry(String key, Message message) {
/* 625 */       this.key = key;
/* 626 */       this.message = message;
/*     */     }
/*     */     
/*     */     public String getKey() {
/* 630 */       return this.key;
/*     */     }
/*     */     
/*     */     public Message getMessage() {
/* 634 */       return this.message;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 641 */       int prime = 31;
/* 642 */       int result = 1;
/* 643 */       result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
/* 644 */       result = 31 * result + (
/* 645 */         (this.message == null) ? 0 : this.message.hashCode());
/* 646 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 651 */       if (this == obj)
/* 652 */         return true; 
/* 653 */       if (obj == null)
/* 654 */         return false; 
/* 655 */       if (getClass() != obj.getClass())
/* 656 */         return false; 
/* 657 */       CacheEntry other = (CacheEntry)obj;
/* 658 */       if (this.key == null) {
/* 659 */         if (other.key != null)
/* 660 */           return false; 
/* 661 */       } else if (!this.key.equals(other.key)) {
/* 662 */         return false;
/* 663 */       }  if (this.message == null) {
/* 664 */         if (other.message != null)
/* 665 */           return false; 
/* 666 */       } else if (!this.message.equals(other.message)) {
/* 667 */         return false;
/* 668 */       }  return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 673 */       StringBuilder builder = new StringBuilder();
/* 674 */       builder.append("CacheEntry [key=");
/* 675 */       builder.append(this.key);
/* 676 */       builder.append(", message=");
/* 677 */       builder.append(this.message);
/* 678 */       builder.append("]");
/* 679 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\server\Server.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */