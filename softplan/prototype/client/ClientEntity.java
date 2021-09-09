/*     */ package softplan.prototype.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.Key;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import labsec.auth.biometric.AbstractFingerprintReader;
/*     */ import labsec.auth.biometric.BiometricEnrollmentException;
/*     */ import labsec.auth.biometric.Futronic.FutronicReader;
/*     */ import labsec.auth.biometric.UserRecord;
/*     */ import labsec.auth.smartcard.SmartcardAccess;
/*     */ import labsec.auth.smartcard.SmartcardException;
/*     */ import labsec.auth.smartcard.Utils;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.BaseEntity;
/*     */ import softplan.prototype.common.ClientData;
/*     */ import softplan.prototype.common.Constants;
/*     */ import softplan.prototype.common.ExceptionHandler;
/*     */ import softplan.prototype.common.Message;
/*     */ import softplan.prototype.common.PINRequestHandler;
/*     */ import softplan.prototype.common.ProtocolException;
/*     */ 
/*     */ public class ClientEntity
/*     */   extends BaseEntity
/*     */   implements Runnable
/*     */ {
/*  32 */   private static final Logger logger = Logger.getLogger(ClientEntity.class);
/*     */   
/*     */   protected Map<String, Message> rcvdRegMsgs;
/*     */   
/*     */   protected AbstractFingerprintReader fingerprintReader;
/*     */   
/*     */   protected Message authMessage;
/*     */   
/*  40 */   protected Object authSyncObj = new Object();
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientEntity() {
/*  45 */     this.serverHostname = "localhost";
/*  46 */     this.serverPort = 15668;
/*  47 */     this.identifier = "Client::";
/*     */     
/*  49 */     this.rcvdRegMsgs = new HashMap<String, Message>();
/*     */   }
/*     */   
/*     */   protected void initBiometrics() {
/*     */     try {
/*  54 */       logger.trace("initBiometrics()");
/*     */       
/*  56 */       this.fingerprintReader = (AbstractFingerprintReader)new FutronicReader();
/*  57 */       this.fingerprintReader.initialize();
/*  58 */       this.fingerprintReader.setMaxModels(Constants.NUMBER_OF_FINGER_MODELS);
/*     */     }
/*  60 */     catch (Throwable e) {
/*  61 */       logger.fatal(String.valueOf(this.identifier) + " could not initialize biometric reader", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AbstractFingerprintReader getFingerprintReader() {
/*  67 */     return this.fingerprintReader;
/*     */   }
/*     */   
/*     */   public void setFingerprintReader(AbstractFingerprintReader fingerprintReader) {
/*  71 */     this.fingerprintReader = fingerprintReader;
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
/*     */   protected void processReceived(Message m) throws IOException, ProtocolException, SmartcardException {
/*  83 */     Message rcvMessage = m;
/*  84 */     if (rcvMessage.isRegister()) {
/*  85 */       logger.debug("Begin processing of register message: " + rcvMessage);
/*     */ 
/*     */ 
/*     */       
/*  89 */       String clientAlias = removeIdentifierString(rcvMessage.getLast().toString());
/*  90 */       logger.trace("clientAliasReceived=" + clientAlias);
/*  91 */       rcvMessage.removeLast();
/*     */       
/*  93 */       this.rcvdRegMsgs.put(clientAlias, rcvMessage);
/*  94 */       logger.debug("End processing of register message: " + rcvMessage);
/*  95 */     } else if (rcvMessage.isAuth()) {
/*  96 */       logger.debug("Begin processing of auth message: " + rcvMessage);
/*  97 */       this.authMessage = rcvMessage;
/*     */       
/*  99 */       synchronized (this.authSyncObj) {
/* 100 */         this.authSyncObj.notify();
/* 101 */         logger.trace("auth sync lock released");
/*     */       } 
/*     */       
/* 104 */       logger.debug("End processing of auth message: " + rcvMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processReceived(BigInteger nonce) throws IOException, ProtocolException, SmartcardException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getRcvMessageKeys() {
/* 117 */     Set<String> keys = this.rcvdRegMsgs.keySet();
/* 118 */     logger.trace(keys.toString());
/* 119 */     int size = keys.size();
/* 120 */     String[] arr = new String[size];
/*     */     
/* 122 */     return keys.<String>toArray(arr);
/*     */   }
/*     */ 
/*     */   
/*     */   public void register() throws IOException, SmartcardException, BiometricEnrollmentException {
/* 127 */     register("eduardosantos3011", (String)null, (char[])null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(final String clientCode, final String selectedClient, final char[] selectedClientPIN, final boolean withBiometric) {
/* 134 */     Runnable registerRunnable = new Runnable() {
/*     */         public void run() {
/*     */           try {
/*     */             byte[] bioTemplate;
/* 138 */             ClientEntity.logger.debug("Beginning register operation. With biometrics? " + withBiometric);
/* 139 */             ClientEntity.logger.trace("code=" + clientCode);
/* 140 */             ClientEntity.logger.trace("selectedClient=" + selectedClient);
/* 141 */             ClientEntity.logger.trace("withBiometrics=" + withBiometric);
/*     */ 
/*     */ 
/*     */             
/* 145 */             if (withBiometric) {
/* 146 */               UserRecord record = ClientEntity.this.fingerprintReader.enroll(clientCode);
/* 147 */               ClientEntity.logger.trace("userRecord=" + record);
/* 148 */               bioTemplate = record.getTemplate();
/*     */             } else {
/* 150 */               bioTemplate = (byte[])null;
/*     */             } 
/*     */             
/* 153 */             Message sentMessage = ClientEntity.this.rcvdRegMsgs.get(selectedClient);
/*     */             
/* 155 */             PrivateKey clientPriv = ClientEntity.this.smartcard.getPrivateKey(selectedClient, selectedClientPIN);
/* 156 */             byte[] encSessionKey = (byte[])sentMessage.getFirst();
/* 157 */             Key sessionKey = ClientEntity.this.smartcard.unwrapDESedeWithRSA(encSessionKey, clientPriv);
/*     */             
/* 159 */             byte[] encInnerMsg = (byte[])sentMessage.get(1);
/* 160 */             byte[] decInnerMsg = ClientEntity.this.smartcard.decipherWithDESede(sessionKey, encInnerMsg);
/* 161 */             Message innerMsg = (Message)Utils.transform(decInnerMsg);
/* 162 */             ClientEntity.logger.trace("innerMessage=" + innerMsg);
/*     */             
/* 164 */             BigInteger nA = (BigInteger)innerMsg.getFirst();
/* 165 */             ClientEntity.logger.trace("nA=" + nA);
/* 166 */             ClientData innerClientData = (ClientData)innerMsg.get(1);
/* 167 */             String cpf = innerClientData.getCpf();
/* 168 */             ClientEntity.logger.trace("cpf=" + cpf);
/* 169 */             String admAlias = (String)innerMsg.get(2);
/* 170 */             ClientEntity.logger.trace("A=" + admAlias);
/*     */             
/* 172 */             ClientData clientData = new ClientData(cpf, clientCode, bioTemplate);
/* 173 */             ClientEntity.logger.trace("new data=" + clientData);
/* 174 */             byte[] dataArr = Utils.transform(clientData);
/* 175 */             byte[] sigDataArr = ClientEntity.this.smartcard.sign(clientPriv, dataArr);
/*     */ 
/*     */             
/* 178 */             Message resultInner = new Message();
/* 179 */             resultInner.add(nA);
/* 180 */             resultInner.add(clientData);
/* 181 */             resultInner.add(sigDataArr);
/* 182 */             ClientEntity.logger.trace("resultInner=" + resultInner);
/*     */             
/* 184 */             byte[] resultInnerArr = Utils.transform(resultInner);
/*     */             
/* 186 */             Key newSessionKey = ClientEntity.this.smartcard.generateDESedeKey();
/* 187 */             byte[] encResultInner = ClientEntity.this.smartcard.cipherWithDESede(newSessionKey, resultInnerArr);
/*     */             
/* 189 */             PublicKey adminPub = ClientEntity.this.smartcard.getPublicKey(admAlias);
/* 190 */             byte[] encNewSessionKey = ClientEntity.this.smartcard.wrapWithRSA(newSessionKey, adminPub);
/*     */             
/* 192 */             Message resultOuter = new Message();
/* 193 */             resultOuter.add(encNewSessionKey);
/* 194 */             resultOuter.add(encResultInner);
/* 195 */             resultOuter.add("Admin::" + admAlias);
/* 196 */             ClientEntity.logger.trace("resultOuter=" + resultOuter);
/*     */             
/* 198 */             ClientEntity.this.writeObject(resultOuter);
/*     */             
/* 200 */             ClientEntity.this.rcvdRegMsgs.remove(selectedClient);
/* 201 */             ClientEntity.logger.debug("End of register operation");
/*     */           }
/* 203 */           catch (Throwable t) {
/*     */             
/* 205 */             ClientEntity.this.exceptionHandler.handleException(t);
/*     */           } 
/*     */         }
/*     */       };
/* 209 */     Thread registerThread = new Thread(registerRunnable, 
/* 210 */         "Register-Thread");
/* 211 */     registerThread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void beginuthentication(final String clientAlias, final String clientCpf, final boolean withBiometrics) {
/* 216 */     Runnable authRunnable = new Runnable() { public void run() {
/*     */           try {
/*     */             String authReq;
/*     */             byte[] bioTemplate;
/* 220 */             ClientEntity.logger.debug("Beginning authentication");
/*     */ 
/*     */             
/* 223 */             if (withBiometrics) {
/* 224 */               authReq = "AuthRequestWithBiometrics";
/*     */             } else {
/* 226 */               authReq = "AuthRequest";
/*     */             } 
/* 228 */             String authReqSent = authReq.concat(clientAlias);
/* 229 */             ClientEntity.logger.trace("authRequestSent = " + authReqSent);
/* 230 */             ClientEntity.this.writeObject(authReqSent);
/*     */ 
/*     */             
/* 233 */             synchronized (ClientEntity.this.authSyncObj) {
/*     */               try {
/* 235 */                 ClientEntity.logger.trace("before authSynch wait()");
/* 236 */                 ClientEntity.this.authSyncObj.wait();
/* 237 */                 ClientEntity.logger.trace("after auth wait()");
/* 238 */               } catch (InterruptedException e) {
/* 239 */                 ClientEntity.logger.trace("while wait() for auth lock", e);
/*     */               } 
/*     */             } 
/*     */             
/* 243 */             if (withBiometrics) {
/* 244 */               ClientEntity.logger.debug("obtaining biometric only once");
/* 245 */               bioTemplate = ClientEntity.this.fingerprintReader.obtainBaseTemplate();
/*     */             } else {
/* 247 */               bioTemplate = (byte[])null;
/*     */             } 
/* 249 */             ClientEntity.logger.debug("Decrypting session key");
/* 250 */             byte[] encSessionKey = (byte[])ClientEntity.this.authMessage.getFirst();
/* 251 */             char[] clientPin = ClientEntity.this.pinRequestHandler.requestPIN(clientAlias);
/* 252 */             PrivateKey clientPriv = ClientEntity.this.smartcard.getPrivateKey(clientAlias, clientPin);
/* 253 */             Key sessionKey = ClientEntity.this.smartcard.unwrapDESedeWithRSA(encSessionKey, clientPriv);
/*     */             
/* 255 */             ClientEntity.logger.debug("Decrypting nonce");
/* 256 */             byte[] encNonce = (byte[])ClientEntity.this.authMessage.get(1);
/* 257 */             byte[] decNonceArr = ClientEntity.this.smartcard.decipherWithDESede(sessionKey, encNonce);
/* 258 */             BigInteger nonce = new BigInteger(decNonceArr);
/* 259 */             ClientEntity.logger.trace("auth nonce = " + nonce);
/*     */             
/* 261 */             ClientEntity.logger.debug("Decrypting inner message");
/* 262 */             Message innerMsg = new Message(Message.Type.AUTH);
/* 263 */             ClientData clientData = new ClientData(clientCpf, null, bioTemplate);
/* 264 */             innerMsg.add(clientData);
/* 265 */             innerMsg.add("Server::");
/* 266 */             byte[] innerMsgArr = Utils.transform(innerMsg);
/*     */             
/* 268 */             ClientEntity.logger.debug("Encrypting inner message with nonce");
/* 269 */             SecretKeySpec nonceAsKey = new SecretKeySpec(nonce.toByteArray(), 
/* 270 */                 "DESede");
/* 271 */             byte[] encInnerMsg = ClientEntity.this.smartcard.cipherWithDESede(nonceAsKey, innerMsgArr);
/*     */             
/* 273 */             Message outerMsg = new Message(Message.Type.AUTH);
/* 274 */             outerMsg.add(encInnerMsg);
/* 275 */             outerMsg.add(clientAlias);
/* 276 */             ClientEntity.this.writeObject(outerMsg);
/*     */             
/* 278 */             ClientEntity.logger.debug("End of authentication");
/* 279 */           } catch (Throwable e) {
/* 280 */             ClientEntity.this.exceptionHandler.handleException(e);
/*     */           } 
/*     */         } }
/*     */       ;
/*     */     
/* 285 */     Thread authThread = new Thread(authRunnable, 
/* 286 */         "Auth-Thread");
/* 287 */     authThread.start();
/*     */   }
/*     */   
/*     */   public void run() {}
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\client\ClientEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */