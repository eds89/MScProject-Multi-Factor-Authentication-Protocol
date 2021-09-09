/*     */ package labsec.auth.biometric.Futronic;
/*     */ 
/*     */ import com.futronic.SDKHelper.FtrIdentifyRecord;
/*     */ import com.futronic.SDKHelper.FtrIdentifyResult;
/*     */ import com.futronic.SDKHelper.FutronicEnrollment;
/*     */ import com.futronic.SDKHelper.FutronicException;
/*     */ import com.futronic.SDKHelper.FutronicIdentification;
/*     */ import com.futronic.SDKHelper.FutronicSdkBase;
/*     */ import com.futronic.SDKHelper.FutronicVerification;
/*     */ import com.futronic.SDKHelper.IEnrollmentCallBack;
/*     */ import com.futronic.SDKHelper.IIdentificationCallBack;
/*     */ import com.futronic.SDKHelper.IVerificationCallBack;
/*     */ import labsec.auth.biometric.AbstractFingerprintReader;
/*     */ import labsec.auth.biometric.BiometricEnrollmentException;
/*     */ import labsec.auth.biometric.BiometricIdentificationException;
/*     */ import labsec.auth.biometric.BiometricVerificationException;
/*     */ import labsec.auth.biometric.FingerprintReader;
/*     */ import labsec.auth.biometric.UserRecord;
/*     */ import labsec.auth.biometric.event.EnrollmentListener;
/*     */ import labsec.auth.biometric.event.IdentificationListener;
/*     */ import labsec.auth.biometric.event.VerificationListener;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class FutronicReader
/*     */   extends AbstractFingerprintReader implements FingerprintReader {
/*  26 */   private static Logger logger = Logger.getLogger(FutronicReader.class);
/*     */ 
/*     */   
/*     */   public static final String LIBRARY_NAME_FOR_WINDOWS = "ftrJSDK.dll";
/*     */ 
/*     */   
/*     */   public static final String LIBRARY_NAME_FOR_LINUX = "";
/*     */ 
/*     */   
/*     */   public static final String NAME = "Futronic";
/*     */   
/*     */   public static final int MIN_QUALITY = 1;
/*     */   
/*     */   public static final int MIN_ACCEPTABLE_QUALITY = 6;
/*     */   
/*     */   public static final int MAX_QUALITY = 10;
/*     */   
/*     */   protected FutronicEnrollment enrollObject;
/*     */   
/*     */   protected FutronicIdentification identifyObject;
/*     */   
/*     */   protected FutronicVerification verifyObject;
/*     */   
/*  49 */   protected int maxModels = 5;
/*     */   
/*  51 */   protected int lastEnrolledQuality = 0;
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
/*     */ 
/*     */   
/*     */   protected FutronicVerification createVerification(byte[] baseTemplate) throws FutronicException {
/*  79 */     logger.debug("Creating toVerification object");
/*  80 */     FutronicVerification f = new NewFutronicVerification(baseTemplate);
/*  81 */     return f;
/*     */   }
/*     */   
/*     */   protected FutronicIdentification createIdentification() throws FutronicException {
/*  85 */     logger.debug("Creating identification object");
/*  86 */     FutronicIdentification f = new NewFutronicIdentification();
/*  87 */     return f;
/*     */   }
/*     */   
/*     */   protected FutronicEnrollment createEnrollmentObject() throws FutronicException {
/*  91 */     logger.debug("Creating enroll object");
/*  92 */     FutronicEnrollment f = new NewFutronicEnrollment();
/*  93 */     f.setMaxModels(getMaxModels());
/*  94 */     return f;
/*     */   }
/*     */   
/*     */   protected void dispose(FutronicSdkBase base) {
/*  98 */     if (base != null) {
/*     */       try {
/* 100 */         logger.debug("Disposing " + base.getClass().getName());
/* 101 */         base.Dispose();
/* 102 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void uninitialize() {
/* 112 */     logger.trace("uninitialize()");
/* 113 */     dispose((FutronicSdkBase)this.enrollObject);
/* 114 */     dispose((FutronicSdkBase)this.verifyObject);
/* 115 */     dispose((FutronicSdkBase)this.identifyObject);
/*     */ 
/*     */     
/* 118 */     this.enrollObject = null;
/* 119 */     this.verifyObject = null;
/* 120 */     this.identifyObject = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void abort() {
/* 126 */     logger.trace("abort()");
/* 127 */     this.enrollObject.OnCalcel();
/* 128 */     this.identifyObject.OnCalcel();
/* 129 */     this.verifyObject.OnCalcel();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxModels(int maxModels) {
/* 135 */     this.maxModels = maxModels;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxModels() {
/* 140 */     return this.maxModels;
/*     */   }
/*     */   
/*     */   public int getMinQuality() {
/* 144 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxQuality() {
/* 149 */     return 10;
/*     */   }
/*     */   
/*     */   public int getLastEnrolledQuality() {
/* 153 */     return this.lastEnrolledQuality;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGoodQuality(int quality) {
/* 158 */     return (quality >= 6 && quality <= 10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UserRecord enroll(String userKey) throws BiometricEnrollmentException {
/* 164 */     logger.trace("enroll()");
/*     */     try {
/* 166 */       logger.debug("Starting enrolling user " + userKey);
/* 167 */       this.enrollObject = createEnrollmentObject();
/*     */       
/* 169 */       this.enrollObject.Enrollment((IEnrollmentCallBack)this.enrollListener);
/*     */       
/* 171 */       synchronized (FutronicSdkBase.m_SyncRoot) {
/* 172 */         logger.trace("before wait() for enroll()");
/*     */ 
/*     */         
/*     */         try {
/* 176 */           FutronicSdkBase.m_SyncRoot.wait();
/* 177 */         } catch (InterruptedException e) {
/* 178 */           logger.trace("during wait() for enroll()", e);
/*     */         } 
/* 180 */         logger.trace("after wait() for enroll");
/*     */       } 
/*     */       
/* 183 */       byte[] template = this.enrollObject.getTemplate();
/* 184 */       this.lastEnrolledQuality = this.enrollObject.getQuality();
/*     */       
/* 186 */       logger.trace("Finishing enrolling user " + userKey + " templateLength=" + 
/* 187 */           template.length + ", quality=" + this.lastEnrolledQuality);
/* 188 */       return new FutronicUserRecord(userKey, template);
/* 189 */     } catch (Exception e) {
/* 190 */       logger.error("While enrolling user=" + userKey, e);
/* 191 */       throw new BiometricEnrollmentException(e);
/*     */     } finally {
/* 193 */       dispose((FutronicSdkBase)this.enrollObject);
/* 194 */       this.enrollObject = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public UserRecord identify(UserRecord[] records) throws BiometricIdentificationException {
/* 201 */     logger.trace("identify()");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 207 */       logger.debug("Obtaining base template for identification");
/* 208 */       this.identifyObject = createIdentification();
/* 209 */       this.identifyObject.GetBaseTemplate((IIdentificationCallBack)this.identifyListener);
/*     */       
/* 211 */       synchronized (FutronicSdkBase.m_SyncRoot) {
/* 212 */         logger.trace("before wait() for identify()");
/*     */ 
/*     */         
/*     */         try {
/* 216 */           FutronicSdkBase.m_SyncRoot.wait();
/* 217 */         } catch (InterruptedException e) {
/* 218 */           logger.trace("during wait() for identify()", e);
/*     */         } 
/* 220 */         logger.trace("after wait() for identify()");
/*     */       } 
/* 222 */       logger.debug("End of obtaining base templace");
/* 223 */       byte[] baseTemplate = this.identifyObject.getBaseTemplate();
/* 224 */       logger.debug("Base template obtained");
/*     */       
/* 226 */       UserRecord identified = identifyCommonTask(records);
/*     */       
/* 228 */       logger.trace("User identified as " + identified);
/* 229 */       return identified;
/* 230 */     } catch (Exception e) {
/* 231 */       logger.error("While identifying user agains many", e);
/* 232 */       throw new BiometricIdentificationException(e);
/*     */     } finally {
/* 234 */       dispose((FutronicSdkBase)this.identifyObject);
/* 235 */       this.identifyObject = null;
/*     */     } 
/*     */   }
/*     */   protected UserRecord identifyCommonTask(UserRecord[] records) {
/*     */     FutronicUserRecord[] futUsers;
/*     */     UserRecord identified;
/* 241 */     if (!(records instanceof FutronicUserRecord[])) {
/* 242 */       logger.debug("Adapting provided user records to Futronic-compatible's ones");
/* 243 */       futUsers = new FutronicUserRecord[records.length];
/* 244 */       for (int i = 0; i < futUsers.length; i++) {
/* 245 */         UserRecord u = records[i];
/* 246 */         futUsers[i] = new FutronicUserRecord(u.getKey(), 
/* 247 */             u.getTemplate());
/*     */       } 
/*     */     } else {
/* 250 */       futUsers = (FutronicUserRecord[])records;
/*     */     } 
/*     */     
/* 253 */     FtrIdentifyResult preResult = new FtrIdentifyResult();
/* 254 */     logger.debug("Beginning identification process");
/* 255 */     this.identifyObject.Identification((FtrIdentifyRecord[])futUsers, preResult);
/* 256 */     logger.debug("End of identification process");
/*     */ 
/*     */     
/* 259 */     int userIndex = preResult.m_Index;
/* 260 */     logger.trace("Index of identified user: " + userIndex);
/* 261 */     if (userIndex == -1) {
/* 262 */       identified = null;
/*     */     } else {
/* 264 */       identified = futUsers[userIndex];
/*     */     } 
/* 266 */     logger.trace("identified user: " + identified);
/* 267 */     return identified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verify(UserRecord user, byte[] template) throws BiometricVerificationException {
/* 275 */     logger.trace("verify() with template");
/*     */ 
/*     */     
/*     */     try {
/* 279 */       UserRecord identified = identify(new UserRecord[] { user
/* 280 */           }, template);
/* 281 */       boolean b = (identified != null);
/* 282 */       logger.trace("verify with template result: " + b);
/* 283 */       return b;
/* 284 */     } catch (BiometricIdentificationException e) {
/* 285 */       Exception cause = (Exception)e.getCause();
/* 286 */       throw new BiometricVerificationException(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UserRecord identify(UserRecord[] records, byte[] template) throws BiometricIdentificationException {
/* 295 */     logger.trace("identify() with template");
/*     */     try {
/* 297 */       this.identifyObject = createIdentification();
/*     */       
/* 299 */       this.identifyObject.setBaseTemplate(template);
/* 300 */       UserRecord identified = identifyCommonTask(records);
/*     */       
/* 302 */       return identified;
/* 303 */     } catch (Exception e) {
/* 304 */       throw new BiometricIdentificationException(e);
/*     */     } finally {
/* 306 */       dispose((FutronicSdkBase)this.identifyObject);
/* 307 */       this.identifyObject = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] obtainBaseTemplate() throws BiometricVerificationException {
/* 315 */     logger.trace("obtainBaseTemplate() ");
/* 316 */     int oldMaxModels = getMaxModels();
/*     */ 
/*     */     
/*     */     try {
/* 320 */       setMaxModels(1);
/* 321 */       UserRecord u = enroll("");
/* 322 */       return u.getTemplate();
/* 323 */     } catch (BiometricEnrollmentException e) {
/*     */       
/* 325 */       Throwable cause = e.getCause();
/* 326 */       throw new BiometricVerificationException();
/*     */     } finally {
/*     */       
/* 329 */       setMaxModels(oldMaxModels);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verify(UserRecord user) throws BiometricVerificationException {
/* 338 */     logger.trace("verify(");
/*     */     try {
/* 340 */       logger.debug("Creating Futronic Verification object");
/* 341 */       this.verifyObject = createVerification(user.getTemplate());
/*     */       
/* 343 */       logger.debug("Beginning toVerification process");
/* 344 */       this.verifyObject.Verification((IVerificationCallBack)this.verifyListener);
/*     */       
/* 346 */       synchronized (FutronicSdkBase.m_SyncRoot) {
/* 347 */         logger.trace("before wait() for verify()");
/*     */ 
/*     */         
/*     */         try {
/* 351 */           FutronicSdkBase.m_SyncRoot.wait();
/* 352 */         } catch (InterruptedException e) {
/* 353 */           logger.trace("duringwait() for verify()", e);
/*     */         } 
/* 355 */         logger.trace("after wait() for verify");
/*     */       } 
/*     */       
/* 358 */       logger.debug("End of toVerification process");
/* 359 */       boolean result = this.verifyObject.getResult();
/* 360 */       logger.debug(String.valueOf(user.getKey()) + " verified? " + result);
/* 361 */       return result;
/* 362 */     } catch (Exception e) {
/* 363 */       logger.error("While verifying user=" + user.getKey());
/* 364 */       throw new BiometricVerificationException(e);
/*     */     } finally {
/* 366 */       dispose((FutronicSdkBase)this.verifyObject);
/* 367 */       this.verifyObject = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 373 */     return "Futronic";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWindowsLibrary() {
/* 383 */     String absolute = "C:\\Users\\Eduardo\\workspace\\MultiFactorAuthProtocol\\dlls\\ftrJSDK.dll";
/* 384 */     return absolute;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLinuxLibrary() {
/* 389 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnrollListener(EnrollmentListener enrollListener) {
/* 400 */     if (!(enrollListener instanceof FutronicEnrollmentListener)) {
/* 401 */       enrollListener = new FutronicEnrollmentListener(enrollListener);
/*     */     }
/*     */     
/* 404 */     super.setEnrollListener(enrollListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVerifyListener(VerificationListener verifyListener) {
/* 409 */     if (!(verifyListener instanceof FutronicVerificationListener)) {
/* 410 */       verifyListener = new FutronicVerificationListener(verifyListener);
/*     */     }
/* 412 */     super.setVerifyListener(verifyListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIdentifyListener(IdentificationListener identifyListener) {
/* 417 */     if (!(identifyListener instanceof FutronicIdentificationListener)) {
/* 418 */       identifyListener = new FutronicIdentificationListener(identifyListener);
/*     */     }
/* 420 */     super.setIdentifyListener(identifyListener);
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\Futronic\FutronicReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */