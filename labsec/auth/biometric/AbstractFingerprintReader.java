/*     */ package labsec.auth.biometric;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import labsec.auth.biometric.event.EnrollmentListener;
/*     */ import labsec.auth.biometric.event.IdentificationListener;
/*     */ import labsec.auth.biometric.event.VerificationListener;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractFingerprintReader
/*     */   implements FingerprintReader
/*     */ {
/*  15 */   private static final Logger logger = Logger.getLogger(AbstractFingerprintReader.class);
/*     */ 
/*     */   
/*     */   protected EnrollmentListener enrollListener;
/*     */ 
/*     */   
/*     */   protected VerificationListener verifyListener;
/*     */ 
/*     */   
/*     */   protected IdentificationListener identifyListener;
/*     */ 
/*     */ 
/*     */   
/*     */   public void initialize() {
/*  29 */     logger.trace("initialize()");
/*     */     
/*     */     try {
/*  32 */       SecurityManager securityManager = System.getSecurityManager();
/*  33 */       RuntimePermission loadLibPerm = new RuntimePermission("loadLibrary");
/*  34 */       logger.debug("Checking for loadLibrary permission");
/*  35 */       securityManager.checkPermission(loadLibPerm);
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
/*  55 */       LibraryLoader libLoader = new LibraryLoader();
/*  56 */       Boolean wasLibLoaded = Boolean.FALSE;
/*     */       try {
/*  58 */         wasLibLoaded = AccessController.<Boolean>doPrivileged(libLoader);
/*  59 */       } catch (PrivilegedActionException pae) {
/*     */         
/*  61 */         throw new BiometricInitializationError(pae.getException().getCause());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  66 */       if (!wasLibLoaded.booleanValue()) {
/*  67 */         throw new BiometricInitializationError("Fingerprint reader library loaded unsuccessfully");
/*     */       }
/*     */     }
/*  70 */     catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       if (!(t instanceof BiometricInitializationError)) {
/*  76 */         throw new BiometricInitializationError(t);
/*     */       }
/*  78 */       throw (BiometricInitializationError)t;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnrollListener(EnrollmentListener enrollListener) {
/*  86 */     this.enrollListener = enrollListener;
/*     */   }
/*     */   
/*     */   public EnrollmentListener getEnrollListener() {
/*  90 */     return this.enrollListener;
/*     */   }
/*     */   
/*     */   public VerificationListener getVerifyListener() {
/*  94 */     return this.verifyListener;
/*     */   }
/*     */   
/*     */   public void setVerifyListener(VerificationListener verifyListener) {
/*  98 */     this.verifyListener = verifyListener;
/*     */   }
/*     */   
/*     */   public IdentificationListener getIdentifyListener() {
/* 102 */     return this.identifyListener;
/*     */   }
/*     */   
/*     */   public void setIdentifyListener(IdentificationListener identifyListener) {
/* 106 */     this.identifyListener = identifyListener;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 110 */     return String.valueOf(getName()) + " Fingerprint Reader";
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\AbstractFingerprintReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */