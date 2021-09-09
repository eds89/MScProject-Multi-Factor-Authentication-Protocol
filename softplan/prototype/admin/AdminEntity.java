/*     */ package softplan.prototype.admin;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.Key;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import labsec.auth.smartcard.SmartcardAccess;
/*     */ import labsec.auth.smartcard.SmartcardException;
/*     */ import labsec.auth.smartcard.Utils;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.BaseEntity;
/*     */ import softplan.prototype.common.ClientData;
/*     */ import softplan.prototype.common.Constants;
/*     */ import softplan.prototype.common.ExceptionHandler;
/*     */ import softplan.prototype.common.Message;
/*     */ import softplan.prototype.common.NoncesDoNotMatchException;
/*     */ import softplan.prototype.common.ProtocolException;
/*     */ import softplan.prototype.common.SignatureNotVerifiedException;
/*     */ 
/*     */ public class AdminEntity
/*     */   extends BaseEntity
/*     */ {
/*  26 */   private static final Logger logger = Logger.getLogger(AdminEntity.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   protected Map<BigInteger, String> noncesCache = new HashMap<BigInteger, String>();
/*     */ 
/*     */ 
/*     */   
/*     */   public AdminEntity() {
/*  38 */     this.serverHostname = Constants.ADMIN_SERVER_HOSTNAME;
/*  39 */     this.serverPort = Constants.ADMIN_SERVER_PORT;
/*  40 */     this.identifier = "Admin::";
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
/*     */   public void beginRegister(final String cpf, final String clientAlias, final String adminAlias) {
/*  54 */     Runnable run = new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/*     */           try {
/*  59 */             AdminEntity.logger.info("Beginning register process at the Admin side");
/*  60 */             AdminEntity.logger.trace("cpf = " + cpf);
/*  61 */             AdminEntity.logger.trace("client alias = " + clientAlias);
/*  62 */             AdminEntity.logger.trace("admin alias = " + adminAlias);
/*     */             
/*  64 */             BigInteger nA = AdminEntity.this.smartcard.generateRandomNumber();
/*  65 */             AdminEntity.logger.trace("generated Na = " + nA);
/*  66 */             ClientData client = new ClientData(cpf);
/*     */             
/*  68 */             Message message1 = new Message();
/*  69 */             message1.add(nA);
/*  70 */             message1.add(client);
/*  71 */             message1.add(adminAlias);
/*     */ 
/*     */ 
/*     */             
/*  75 */             byte[] bM1 = Utils.transform(message1);
/*     */             
/*  77 */             PublicKey clientPubKey = AdminEntity.this.smartcard.getPublicKey(clientAlias);
/*     */             
/*  79 */             Key symmetricKey = AdminEntity.this.smartcard.generateDESedeKey();
/*     */             
/*  81 */             byte[] encrMessage1 = AdminEntity.this.smartcard.cipherWithDESede(
/*  82 */                 symmetricKey, Utils.transform(message1));
/*     */             
/*  84 */             byte[] encrSymmetricKey = AdminEntity.this.smartcard.wrapWithRSA(symmetricKey, clientPubKey);
/*  85 */             symmetricKey = null;
/*     */ 
/*     */             
/*  88 */             Message outerMessage = new Message();
/*  89 */             outerMessage.add(encrSymmetricKey);
/*  90 */             outerMessage.add(encrMessage1);
/*  91 */             outerMessage.add("Client::" + clientAlias);
/*     */ 
/*     */ 
/*     */             
/*  95 */             AdminEntity.this.writeObject(outerMessage);
/*  96 */             AdminEntity.logger.debug("Storing nonce and client alias for further toVerification:");
/*  97 */             AdminEntity.this.noncesCache.put(nA, clientAlias);
/*     */           }
/*  99 */           catch (Throwable t) {
/* 100 */             AdminEntity.this.exceptionHandler.handleException(t);
/*     */           } 
/*     */         }
/*     */       };
/* 104 */     Thread admRegThread = new Thread(run, String.valueOf(this.identifier) + "_Register");
/* 105 */     admRegThread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processReceived(Message m) throws IOException, ProtocolException, SmartcardException {
/* 114 */     logger.debug("Beginning processing");
/* 115 */     String adminAlias = removeIdentifierString((String)m.getLast());
/* 116 */     char[] adminPIN = this.pinRequestHandler.requestPIN(adminAlias);
/*     */     
/* 118 */     PrivateKey admPrivKey = this.smartcard.getPrivateKey(adminAlias, 
/* 119 */         adminPIN);
/*     */     
/* 121 */     byte[] sessionKeyArr = (byte[])m.get(0);
/* 122 */     Key sessionKey = this.smartcard.unwrapDESedeWithRSA(sessionKeyArr, admPrivKey);
/*     */     
/* 124 */     byte[] encData = (byte[])m.get(1);
/* 125 */     byte[] decData = this.smartcard.decipherWithDESede(sessionKey, encData);
/*     */     
/* 127 */     Message inner = (Message)Utils.transform(decData);
/* 128 */     BigInteger rcvNa = (BigInteger)inner.get(0);
/* 129 */     logger.trace("nA = " + rcvNa);
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
/* 142 */     boolean contained = this.noncesCache.containsKey(rcvNa);
/* 143 */     logger.trace("nonces cache contains nonce = " + contained);
/*     */     
/* 145 */     if (!contained) {
/* 146 */       logger.debug("Received nonce " + rcvNa + "not found on nonces cache, " + 
/* 147 */           "therefore it was not generated by the admin.");
/* 148 */       logger.error("Nonces don't match: " + rcvNa + " and " + rcvNa);
/* 149 */       throw new NoncesDoNotMatchException();
/*     */     } 
/* 151 */     logger.info("Nonces verified successfully");
/* 152 */     ClientData storedClient = (ClientData)inner.get(1);
/*     */     
/* 154 */     String clientAlias = this.noncesCache.get(rcvNa);
/* 155 */     PublicKey clientPub = this.smartcard.getPublicKey(clientAlias);
/* 156 */     byte[] storedClientAsBinary = Utils.transform(storedClient);
/*     */     
/* 158 */     byte[] storedClientSignature = (byte[])inner.get(2);
/*     */     
/* 160 */     boolean signatureVerified = this.smartcard.verifySign(clientPub, 
/* 161 */         storedClientAsBinary, storedClientSignature);
/* 162 */     if (!signatureVerified) {
/* 163 */       logger.error("Signature of client data was not verified");
/* 164 */       throw new SignatureNotVerifiedException();
/*     */     } 
/* 166 */     logger.info("Client signature verified successfully");
/*     */     
/* 168 */     logger.debug("Creating message for storing into database");
/* 169 */     Message innerResultMsg = new Message();
/* 170 */     innerResultMsg.add(storedClient);
/* 171 */     innerResultMsg.add(storedClientSignature);
/*     */ 
/*     */     
/* 174 */     byte[] adminSignature = this.smartcard.sign(admPrivKey, 
/* 175 */         Utils.transform(innerResultMsg));
/*     */     
/* 177 */     Message outerResultMsg = new Message();
/* 178 */     outerResultMsg.add(innerResultMsg);
/* 179 */     outerResultMsg.add(adminSignature);
/* 180 */     outerResultMsg.add("Database::");
/*     */ 
/*     */     
/* 183 */     writeObject(outerResultMsg);
/* 184 */     this.noncesCache.remove(rcvNa);
/* 185 */     logger.debug("End of processing");
/*     */   }
/*     */   
/*     */   protected void processReceived(BigInteger nonce) throws IOException, ProtocolException, SmartcardException {}
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\admin\AdminEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */