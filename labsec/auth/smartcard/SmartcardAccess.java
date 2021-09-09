/*     */ package labsec.auth.smartcard;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.Key;
/*     */ import java.security.KeyStore;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.Security;
/*     */ import java.security.Signature;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.security.cert.X509Certificate;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.bouncycastle.util.encoders.Base64;
/*     */ import org.bouncycastle.util.encoders.Hex;
/*     */ import softplan.prototype.common.Configuration;
/*     */ 
/*     */ 
/*     */ public class SmartcardAccess
/*     */ {
/*  45 */   private static final Logger logger = Logger.getLogger(SmartcardAccess.class);
/*     */   
/*     */   public static final boolean PKCS11_UNIQUE_PROVIDER = false;
/*     */   
/*     */   public static final String PKCS11_PROVIDER_CLASS_NAME = "sun.security.pkcs11.SunPKCS11";
/*     */   
/*     */   public static final String PKCS11_KEYSTORE_NAME = "PKCS11";
/*     */   
/*  53 */   public static final String PKCS11_LIBRARY_PATH = Configuration.getProperty("pkcs11LibraryFile", "C:\\Windows\\System32\\aetpkss1.dll");
/*     */   
/*     */   public static final String PKCS11_LIBRARY_FILENAME = "aetpkss1.dll";
/*     */   
/*     */   public static final String DEFAULT_DIGEST_ALGORITHM = "SHA1";
/*     */   
/*     */   public static final String RSA_CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
/*     */   
/*     */   public static final String DESEDE_CIPHER_ALGORITHM = "DESede/CBC/PKCS5Padding";
/*     */   
/*     */   public static final String DEFAULT_SIGNATURE_ALGORITHM = "SHA1withRSA";
/*     */   
/*     */   public static final String DEFAULT_CIPHER_ALGORITHM = "DESede";
/*     */   
/*     */   public static final String DEFAULT_SSYMMETRIC_KEY_ALGORITHM = "RSA";
/*     */   
/*     */   public static final String DEFAULT_SYMMETRIC_KEY_ALGORITHM = "DESede";
/*     */   
/*     */   public static final String DEFAULT_SECURE_RANDOM_ALGORITHM = "PKCS11";
/*     */   
/*     */   public static final int DEFAULT_RANDOM_NUMBER_SIZE = 4;
/*     */   
/*     */   public static final int MIN_PIN_LENGTH = 4;
/*     */   
/*     */   public static final int MAX_PIN_LENGTH = 8;
/*     */   
/*  79 */   public static final char[] DEFAULT_PIN_FOR_TESTING = "123456".toCharArray();
/*     */   
/*     */   private Provider pkcs11Provider;
/*     */   
/*  83 */   private KeyStore keyStore = null;
/*     */   
/*     */   private boolean keyStoreInitialized = false;
/*     */   
/*     */   private boolean providerInitialized = false;
/*     */   
/*  89 */   private File pkcs11LibraryFile = null;
/*     */ 
/*     */   
/*     */   public SmartcardAccess() {
/*  93 */     this(new File(PKCS11_LIBRARY_PATH));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SmartcardAccess(Properties prop) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public SmartcardAccess(File pkcs11LibraryFile) {
/* 104 */     setPKCS11Library(pkcs11LibraryFile);
/*     */   }
/*     */   
/*     */   public void removeAllSecurityProviders() {
/* 108 */     Provider[] providers = Security.getProviders();
/* 109 */     for (int i = 0; i < providers.length; i++) {
/* 110 */       Security.removeProvider(providers[i].getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPKCS11Library(String libraryFile) {
/* 115 */     setPKCS11Library(new File(libraryFile));
/*     */   }
/*     */   
/*     */   public void setPKCS11Library(File libraryFile) {
/* 119 */     if (libraryFile == null || !libraryFile.isFile()) {
/* 120 */       throw new IllegalArgumentException("Invalid PKCS11 library filename: " + libraryFile);
/*     */     }
/* 122 */     this.pkcs11LibraryFile = libraryFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getPKCS11Library() {
/* 129 */     return this.pkcs11LibraryFile;
/*     */   }
/*     */ 
/*     */   
/*     */   public Provider getProvider() {
/* 134 */     return this.pkcs11Provider;
/*     */   }
/*     */   
/*     */   protected KeyStore getKeyStore() {
/* 138 */     return this.keyStore;
/*     */   }
/*     */   
/*     */   protected boolean isKeyStoreInitialized() {
/* 142 */     return this.keyStoreInitialized;
/*     */   }
/*     */   
/*     */   public boolean isProviderInitialized() {
/* 146 */     return this.providerInitialized;
/*     */   }
/*     */   
/*     */   public void initializeProvider() throws SmartcardInitializationError {
/* 150 */     String config = "name = Smartcard\nlibrary = " + 
/* 151 */       getPKCS11Library();
/*     */     try {
/* 153 */       Class<?> c = Class.forName("sun.security.pkcs11.SunPKCS11");
/* 154 */       Constructor<Provider> constr = (Constructor)c.getConstructor(new Class[] { InputStream.class });
/*     */       
/* 156 */       this.pkcs11Provider = constr.newInstance(new Object[] { new ByteArrayInputStream(config.getBytes()) });
/*     */       
/* 158 */       Security.addProvider(this.pkcs11Provider);
/* 159 */       this.providerInitialized = true;
/* 160 */     } catch (Exception e) {
/*     */       
/* 162 */       this.providerInitialized = false;
/* 163 */       throw new SmartcardInitializationError("Could not initialize PKCS#11 Provider. Cause: " + e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unintializeProvider() throws Exception {
/* 168 */     this.keyStore = null;
/* 169 */     this.keyStoreInitialized = false;
/* 170 */     this.providerInitialized = false;
/* 171 */     Security.removeProvider(getProvider().getName());
/*     */   }
/*     */   
/*     */   private static class StringComparator implements Comparator<Object> { private StringComparator() {}
/*     */     
/*     */     private boolean areStrings(Object o1, Object o2) {
/* 177 */       if (o1 instanceof String && 
/* 178 */         o2 instanceof String) return true; 
/*     */       return false;
/*     */     }
/*     */     public int compare(Object o1, Object o2) {
/* 182 */       if (!areStrings(o1, o2)) {
/* 183 */         return 0;
/*     */       }
/*     */       
/* 186 */       return ((String)o1).compareTo((String)o2);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/* 191 */   private static final StringComparator stringComparator = new StringComparator(null);
/*     */ 
/*     */   
/*     */   public String getProviderDefailedInfo() {
/* 195 */     List<Object> asList = Collections.list(getProvider().keys());
/* 196 */     Collections.sort(asList, stringComparator);
/* 197 */     Iterator<Object> iterator = asList.iterator();
/* 198 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 200 */     String cat = "";
/* 201 */     boolean first = true;
/* 202 */     while (iterator.hasNext()) {
/* 203 */       String keyStr = iterator.next().toString();
/*     */       
/* 205 */       String curr = keyStr.substring(0, keyStr.lastIndexOf('.'));
/*     */       
/* 207 */       if (curr.equals("Provider")) {
/*     */         continue;
/*     */       }
/* 210 */       if (!curr.equals(cat)) {
/* 211 */         if (!first) {
/* 212 */           buf.delete(buf.length() - 2, buf.length());
/* 213 */           buf.append(".\n");
/*     */         } else {
/* 215 */           first = false;
/*     */         } 
/* 217 */         cat = curr;
/* 218 */         buf.append(curr);
/* 219 */         buf.append(" = ");
/*     */       } 
/* 221 */       buf.append(keyStr.substring(keyStr.lastIndexOf('.') + 1, keyStr.length()));
/* 222 */       buf.append(", ");
/*     */     } 
/*     */     
/* 225 */     buf.delete(buf.length() - 2, buf.length());
/* 226 */     buf.append('.');
/* 227 */     buf.append("\n\nProvider information:\n Name = ");
/* 228 */     buf.append(getProvider().getName());
/* 229 */     buf.append("\nVersion = ");
/* 230 */     buf.append(getProvider().getVersion());
/* 231 */     buf.append("\nInfo = ");
/* 232 */     buf.append(getProvider().getInfo());
/*     */     
/* 234 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public void printAllProvidersInfo() {
/* 238 */     printProviderInfo(Security.getProviders());
/*     */   }
/*     */   
/*     */   protected void printProviderInfo(Provider p) {
/* 242 */     printProviderInfo(new Provider[] { p }); } protected void printProviderInfo(Provider[] providers) {
/*     */     byte b;
/*     */     int i;
/*     */     Provider[] arrayOfProvider;
/* 246 */     for (i = (arrayOfProvider = providers).length, b = 0; b < i; ) { Provider provider = arrayOfProvider[b];
/* 247 */       Set<Map.Entry<Object, Object>> entries = provider.entrySet();
/* 248 */       System.out.println("\nProvider: " + provider.getName());
/*     */       
/* 250 */       for (Map.Entry<Object, Object> entry : entries) {
/* 251 */         System.out.println((new StringBuilder()).append(entry.getKey()).append(" = ").append(entry.getValue()).toString());
/*     */       }
/* 253 */       System.out.println("===================");
/*     */       b++; }
/*     */   
/*     */   }
/*     */   protected void printAlgsInfo() {
/* 258 */     System.out.println("\n=== Printing list of all available algorithms ==\n");
/*     */     
/* 260 */     System.out.println("\n=== Signature ==");
/* 261 */     print(Security.getAlgorithms("Signature"));
/*     */     
/* 263 */     System.out.println("\n=== MessageDigest ==");
/* 264 */     print(Security.getAlgorithms("MessageDigest"));
/*     */     
/* 266 */     System.out.println("\n=== MAC ==");
/* 267 */     print(Security.getAlgorithms("Mac"));
/*     */     
/* 269 */     System.out.println("\n=== Cipher ==");
/* 270 */     print(Security.getAlgorithms("Cipher"));
/*     */     
/* 272 */     System.out.println("\n=== KeyStore ==");
/* 273 */     print(Security.getAlgorithms("KeyStore"));
/*     */     
/* 275 */     System.out.println("\n=== Key ==");
/* 276 */     print(Security.getAlgorithms("Key"));
/*     */   }
/*     */   
/*     */   protected void print(Set<?> aSet) {
/* 280 */     for (Object o : aSet) {
/* 281 */       System.out.println(o);
/*     */     }
/*     */   }
/*     */   
/*     */   private char[] toCharArray(byte[] b) {
/* 286 */     char[] c = new char[b.length];
/* 287 */     for (int i = 0; i < c.length; i++) {
/* 288 */       c[i] = (char)b[i];
/*     */     }
/* 290 */     return c;
/*     */   }
/*     */   
/*     */   public String toBase64(byte[] b) {
/* 294 */     return new String(toCharArray(Base64.encode(b)));
/*     */   }
/*     */   
/*     */   public String toHex(byte[] b) {
/* 298 */     return new String(toCharArray(Hex.encode(b)));
/*     */   }
/*     */   
/*     */   public static boolean isValidPIN(char[] pin) {
/* 302 */     return isValidPIN(String.valueOf(pin));
/*     */   }
/*     */   
/*     */   public static boolean isValidPIN(String pin) {
/* 306 */     if ("".equals(pin)) {
/* 307 */       return false;
/*     */     }
/*     */     
/* 310 */     boolean valid = false;
/*     */     try {
/* 312 */       int length = pin.length();
/* 313 */       if (length >= 4 && length <= 8) {
/*     */ 
/*     */ 
/*     */         
/* 317 */         int i = Integer.parseInt(pin);
/* 318 */         valid = true;
/*     */       } 
/* 320 */     } catch (NumberFormatException nfe) {
/* 321 */       valid = false;
/*     */     } 
/* 323 */     return valid;
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyStore loadPKCS11Keystore() throws SmartcardException {
/* 328 */     return loadPKCS11Keystore(DEFAULT_PIN_FOR_TESTING);
/*     */   }
/*     */ 
/*     */   
/*     */   public KeyStore loadPKCS11Keystore(char[] pin) throws SmartcardException {
/* 333 */     if (getKeyStore() != null && isKeyStoreInitialized()) {
/* 334 */       return getKeyStore();
/*     */     }
/*     */     
/*     */     try {
/* 338 */       this.keyStore = KeyStore.getInstance("PKCS11", getProvider());
/* 339 */       this.keyStore.load(null, pin);
/* 340 */       this.keyStoreInitialized = true;
/* 341 */     } catch (Exception e) {
/* 342 */       throw new SmartcardException("Problem while loading PKCS11 keystore", e);
/*     */     } 
/*     */     
/* 345 */     return this.keyStore;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey getPrivateKey(String alias, char[] pin) throws SmartcardKeyException {
/* 351 */     Key k = null;
/*     */     try {
/* 353 */       KeyStore ks = loadPKCS11Keystore(pin);
/* 354 */       k = ks.getKey(alias, pin);
/* 355 */     } catch (Exception e) {
/* 356 */       throw new SmartcardKeyException("Error while loading key from " + alias, e);
/*     */     } 
/* 358 */     if (!(k instanceof PrivateKey)) {
/* 359 */       throw new SmartcardKeyException("Alias '" + alias + "' does not have a private key");
/*     */     }
/* 361 */     return (PrivateKey)k;
/*     */   }
/*     */   
/*     */   public PublicKey getPublicKey(String alias) throws SmartcardKeyException {
/*     */     Certificate x509Cert;
/*     */     try {
/* 367 */       x509Cert = getCertificate(alias);
/* 368 */     } catch (SmartcardCertificateException e) {
/* 369 */       throw new SmartcardKeyException("Problem while obtaining the certificate of the alias: " + alias, e);
/*     */     } 
/* 371 */     return x509Cert.getPublicKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Certificate getCertificate(String alias) throws SmartcardCertificateException {
/* 378 */     if (!isKeyStoreInitialized()) {
/* 379 */       throw new IllegalStateException("KeyStore not initialized");
/*     */     }
/* 381 */     Certificate cert = null;
/*     */     try {
/* 383 */       cert = getKeyStore().getCertificate(alias);
/* 384 */     } catch (Exception e) {
/* 385 */       throw new SmartcardCertificateException("Error while loading certificate from " + alias, e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 390 */     return cert;
/*     */   }
/*     */   
/*     */   public void printKeyStoreContent(KeyStore ks) throws Exception {
/* 394 */     if (ks != null) {
/*     */       Enumeration<String> aliases;
/*     */       try {
/* 397 */         aliases = ks.aliases();
/* 398 */       } catch (Exception e) {
/* 399 */         throw new Exception("Could not obtain keystore's aliases: " + e.getMessage(), e);
/*     */       } 
/*     */       
/* 402 */       while (aliases.hasMoreElements()) {
/* 403 */         String alias = aliases.nextElement();
/* 404 */         System.out.println("Alias: " + alias);
/*     */ 
/*     */         
/*     */         try {
/* 408 */           X509Certificate cert = (X509Certificate)ks
/* 409 */             .getCertificate(alias);
/* 410 */           System.out.println("Certificate: " + cert);
/*     */           
/* 412 */           PrivateKey privKey = (PrivateKey)ks.getKey(alias, null);
/* 413 */           System.out.println("Private Key: " + privKey);
/* 414 */         } catch (Exception e) {
/* 415 */           throw new Exception("Exception while printing key store's contents", e);
/*     */         } 
/* 417 */         System.out.println("===========================");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAliases(char[] pin) throws SmartcardException {
/* 425 */     KeyStore ks = loadPKCS11Keystore(pin);
/* 426 */     return getAliases(ks);
/*     */   }
/*     */   
/*     */   public String[] getCPFs() throws SmartcardException {
/* 430 */     return new String[0];
/*     */   }
/*     */   
/*     */   public String getCPF(String alias) throws SmartcardException {
/* 434 */     return "";
/*     */   }
/*     */   
/*     */   public String getAlias(String cpf) throws SmartcardException {
/* 438 */     return "";
/*     */   }
/*     */   
/*     */   public String[] getAliases(KeyStore ks) throws SmartcardException {
/* 442 */     String[] aliasesArr = (String[])null;
/*     */     try {
/* 444 */       aliasesArr = new String[ks.size()];
/* 445 */       Enumeration<String> enumAlias = ks.aliases();
/* 446 */       int i = 0;
/* 447 */       while (enumAlias.hasMoreElements()) {
/* 448 */         aliasesArr[i++] = enumAlias.nextElement();
/*     */       }
/* 450 */     } catch (Exception e) {
/* 451 */       throw new SmartcardException("Exception while obtaining aliases' array", e);
/*     */     } 
/* 453 */     return aliasesArr;
/*     */   }
/*     */   
/*     */   public byte[] read(File f) throws IOException {
/* 457 */     if (f.length() > 2147483647L) {
/* 458 */       throw new IllegalArgumentException("file is too big");
/*     */     }
/* 460 */     ByteBuffer buf = ByteBuffer.allocate((int)f.length());
/* 461 */     byte[] readArr = new byte[2048];
/*     */     
/* 463 */     FileInputStream fis = null;
/*     */     try {
/* 465 */       fis = new FileInputStream(f);
/* 466 */       int amountRead = 0;
/* 467 */       while ((amountRead = fis.read(readArr)) != -1) {
/* 468 */         buf.put(readArr, 0, amountRead);
/*     */       }
/*     */       
/* 471 */       buf.position(0);
/*     */     } finally {
/*     */       try {
/* 474 */         if (fis != null) {
/* 475 */           fis.close();
/*     */         }
/* 477 */       } catch (IOException iOException) {}
/*     */     } 
/*     */ 
/*     */     
/* 481 */     return buf.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(File destinationFile, byte[] contents) throws IOException {
/* 486 */     write(destinationFile, ByteBuffer.wrap(contents));
/*     */   }
/*     */   
/*     */   public void write(File destinationFile, ByteBuffer contents) throws IOException {
/* 490 */     FileOutputStream fos = null;
/*     */     try {
/* 492 */       fos = new FileOutputStream(destinationFile);
/* 493 */       int size = contents.capacity();
/*     */       
/* 495 */       int i = 0;
/* 496 */       while (i <= size - 1) {
/* 497 */         fos.write(contents.get(i++));
/*     */       }
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 504 */         if (fos != null) {
/* 505 */           fos.close();
/*     */         }
/* 507 */       } catch (IOException iOException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] digest(byte[] inputBuffer) throws SmartcardDigestException {
/* 514 */     return digest("SHA1", inputBuffer);
/*     */   }
/*     */   
/*     */   public byte[] digest(String algName, byte[] inputBuffer) throws SmartcardDigestException {
/* 518 */     byte[] hash = (byte[])null;
/*     */     
/*     */     try {
/* 521 */       MessageDigest digest = MessageDigest.getInstance(algName, getProvider());
/* 522 */       digest.update(inputBuffer);
/* 523 */       hash = digest.digest();
/*     */     }
/* 525 */     catch (Exception e) {
/* 526 */       throw new SmartcardDigestException("Problem while calculating the hash " + algName, e);
/*     */     } 
/* 528 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] digest(File inputFile, String algName) throws IOException, SmartcardDigestException {
/* 533 */     byte[] inputContents = read(inputFile);
/* 534 */     byte[] hash = digest(algName, inputContents);
/* 535 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] digest(File inputFile) throws SmartcardDigestException, IOException {
/* 540 */     byte[] inputContents = read(inputFile);
/* 541 */     byte[] hash = digest("SHA1", inputContents);
/* 542 */     return hash;
/*     */   }
/*     */   
/*     */   public Key generateDESedeKey() throws SmartcardKeyException {
/* 546 */     return generateSymmetricKey("DESede");
/*     */   }
/*     */   
/*     */   public Key generateSymmetricKey(String algName) throws SmartcardKeyException {
/*     */     try {
/* 551 */       KeyGenerator keyGen = KeyGenerator.getInstance(algName, getProvider());
/* 552 */       SecretKey secretKey = keyGen.generateKey();
/* 553 */       return secretKey;
/* 554 */     } catch (Exception e) {
/* 555 */       throw new SmartcardKeyException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Key unwrapDESedeWithRSA(byte[] wrappedKey, Key unwrappingRSAKey) throws SmartcardCipherException {
/* 561 */     return unwrap("RSA/ECB/PKCS1Padding", wrappedKey, "DESede", 
/* 562 */         3, unwrappingRSAKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Key unwrap(String algName, byte[] wrappedKey, String wrappedKeyAlg, int unwrappedKeyType, Key unwrappingKey) throws SmartcardCipherException {
/* 567 */     Key unwrappedKey = null;
/*     */     try {
/* 569 */       Cipher c = Cipher.getInstance(algName, getProvider());
/* 570 */       c.init(4, unwrappingKey);
/*     */ 
/*     */       
/* 573 */       unwrappedKey = c.unwrap(wrappedKey, wrappedKeyAlg, unwrappedKeyType);
/* 574 */     } catch (Exception e) {
/* 575 */       throw new SmartcardCipherException(e);
/*     */     } 
/* 577 */     return unwrappedKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] wrapWithRSA(Key keyToBeWrappped, Key rsaKey) throws SmartcardCipherException {
/* 584 */     return wrap("RSA/ECB/PKCS1Padding", keyToBeWrappped, rsaKey);
/*     */   }
/*     */   
/*     */   public byte[] wrap(String algName, Key keyToBeWrappped, Key wrapperKey) throws SmartcardCipherException {
/* 588 */     byte[] wrappedKey = (byte[])null;
/*     */     try {
/* 590 */       Cipher c = Cipher.getInstance(algName, getProvider());
/* 591 */       c.init(3, wrapperKey);
/*     */       
/* 593 */       wrappedKey = c.wrap(keyToBeWrappped);
/* 594 */     } catch (Exception e) {
/* 595 */       throw new SmartcardCipherException(e);
/*     */     } 
/* 597 */     return wrappedKey;
/*     */   }
/*     */   
/*     */   public BigInteger generateRandomNumber() throws SmartcardRandomNumberException {
/* 601 */     return generateRandomNumber("PKCS11", 4);
/*     */   }
/*     */   
/*     */   public BigInteger generateRandomNumber(int numBytes) throws SmartcardRandomNumberException {
/* 605 */     return generateRandomNumber("PKCS11", numBytes);
/*     */   }
/*     */   
/*     */   public BigInteger generateRandomNumber(String algName, int numBytes) throws SmartcardRandomNumberException {
/*     */     try {
/* 610 */       SecureRandom secureRandom = SecureRandom.getInstance(algName, getProvider());
/*     */ 
/*     */       
/* 613 */       byte[] byteArr = new byte[numBytes];
/* 614 */       secureRandom.nextBytes(byteArr);
/*     */       
/* 616 */       return new BigInteger(byteArr);
/* 617 */     } catch (Exception e) {
/* 618 */       throw new SmartcardRandomNumberException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] cipherWithDESede(Key key, byte[] plainBuffer) throws SmartcardCipherException {
/* 624 */     return cipher("DESede", key, plainBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] cipher(String algName, Key key, byte[] plainBuffer) throws SmartcardCipherException {
/*     */     byte[] cipheredBuffer;
/*     */     try {
/* 631 */       Cipher cipher = Cipher.getInstance(algName, getProvider());
/* 632 */       cipher.init(1, key);
/*     */       
/* 634 */       cipheredBuffer = cipher.doFinal(plainBuffer);
/* 635 */     } catch (Exception e) {
/* 636 */       throw new SmartcardCipherException(e);
/*     */     } 
/* 638 */     return cipheredBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void cipher(File inputFile, File destinationFile, String alias) throws SmartcardCipherException, IOException, SmartcardKeyException {
/* 643 */     cipher(inputFile, destinationFile, alias, "DESede");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cipher(File inputFile, File destinationFile, String alias, String algName) throws SmartcardCipherException, IOException, SmartcardKeyException {
/* 649 */     byte[] plainContents = read(inputFile);
/* 650 */     Key key = getPublicKey(alias);
/* 651 */     byte[] cipheredContents = cipher(algName, key, plainContents);
/* 652 */     write(destinationFile, cipheredContents);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] decipherWithDESede(Key key, byte[] cipheredBuffer) throws SmartcardCipherException {
/* 657 */     return decipher("DESede", key, cipheredBuffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] decipher(String algName, Key key, byte[] cipheredBuffer) throws SmartcardCipherException {
/*     */     byte[] plainBuffer;
/*     */     try {
/* 664 */       Cipher decipher = Cipher.getInstance(algName, getProvider());
/* 665 */       decipher.init(2, key);
/*     */       
/* 667 */       plainBuffer = decipher.doFinal(cipheredBuffer);
/* 668 */     } catch (Exception e) {
/* 669 */       throw new SmartcardCipherException("A problem arose while deciphering the data with algorithm " + algName, e);
/*     */     } 
/* 671 */     return plainBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void decipher(File cipheredFile, File destinationFile, String alias, char[] pin) throws SmartcardCipherException, IOException, SmartcardKeyException {
/* 676 */     decipher(cipheredFile, destinationFile, alias, pin, "DESede");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void decipher(File cipheredFile, File destinationFile, String alias, char[] pin, String algName) throws SmartcardCipherException, IOException, SmartcardKeyException {
/* 682 */     byte[] cipheredContents = read(cipheredFile);
/* 683 */     PrivateKey privKey = getPrivateKey(alias, pin);
/* 684 */     byte[] plainContents = decipher(algName, privKey, cipheredContents);
/* 685 */     write(destinationFile, plainContents);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] sign(PrivateKey privKey, byte[] input) throws SmartcardSignatureException {
/* 690 */     return sign("SHA1withRSA", privKey, input);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] sign(String algName, PrivateKey privKey, byte[] input) throws SmartcardSignatureException {
/*     */     byte[] signature;
/*     */     try {
/* 697 */       Signature sig = Signature.getInstance(algName);
/* 698 */       sig.initSign(privKey);
/*     */       
/* 700 */       sig.update(input);
/*     */       
/* 702 */       signature = sig.sign();
/* 703 */     } catch (Exception e) {
/* 704 */       throw new SmartcardSignatureException("Problem while " + algName + "-signing the file", e);
/*     */     } 
/* 706 */     return signature;
/*     */   }
/*     */   
/*     */   public void sign(File inputFile, File destinationFile, String alias, char[] pin) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 710 */     sign(inputFile, destinationFile, alias, pin, "SHA1withRSA");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sign(File inputFile, File destinationFile, String alias, char[] pin, String algName) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 717 */     byte[] inputCBuffer = read(inputFile);
/* 718 */     PrivateKey privKey = getPrivateKey(alias, pin);
/* 719 */     byte[] signature = sign(algName, privKey, inputCBuffer);
/* 720 */     write(destinationFile, signature);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String sign(File inputFile, String alias, char[] pin) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 726 */     return sign(inputFile, alias, pin, "SHA1withRSA");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String sign(File inputFile, String alias, char[] pin, String algName) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 732 */     byte[] contents = read(inputFile);
/* 733 */     PrivateKey privKey = getPrivateKey(alias, pin);
/* 734 */     byte[] signature = sign(algName, privKey, contents);
/* 735 */     return toHex(signature);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verifySign(PublicKey pubKey, byte[] input, byte[] signature) throws SmartcardSignatureException {
/* 741 */     return verifySign("SHA1withRSA", pubKey, input, signature);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verifySign(String algName, PublicKey pubKey, byte[] input, byte[] signature) throws SmartcardSignatureException {
/*     */     boolean result;
/*     */     try {
/* 750 */       Signature ver = Signature.getInstance(algName, getProvider());
/*     */       
/* 752 */       ver.initVerify(pubKey);
/* 753 */       ver.update(input);
/*     */       
/* 755 */       result = ver.verify(signature);
/* 756 */     } catch (Exception e) {
/* 757 */       throw new SmartcardSignatureException("Exception thrown while verifying signature", e);
/*     */     } 
/*     */     
/* 760 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verifySign(File inputFile, File signatureFile, String alias) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 765 */     return verifySign(inputFile, signatureFile, alias, "SHA1withRSA");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean verifySign(File inputFile, File signatureFile, String alias, String algName) throws SmartcardSignatureException, IOException, SmartcardKeyException {
/* 771 */     byte[] inputContents = read(inputFile);
/* 772 */     byte[] signatureContents = read(signatureFile);
/* 773 */     PublicKey pubKey = getPublicKey(alias);
/* 774 */     boolean verified = verifySign(algName, pubKey, inputContents, signatureContents);
/* 775 */     return verified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 782 */     loadCertificate();
/*     */   }
/*     */   
/*     */   public static void loadCertificate() throws Exception {
/* 786 */     File file = new File("C:\\Dev\\certificates\\JAIR CIRICO.cer ");
/* 787 */     FileInputStream fis = new FileInputStream(file);
/* 788 */     X509Certificate certificado = X509Certificate.getInstance(fis);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void testCPFAccess() throws Exception {
/* 794 */     SmartcardAccess sm = new SmartcardAccess();
/* 795 */     sm.initializeProvider();
/* 796 */     KeyStore ks = sm.loadPKCS11Keystore();
/* 797 */     sm.printKeyStoreContent(ks);
/* 798 */     String[] a = sm.getAliases(DEFAULT_PIN_FOR_TESTING);
/* 799 */     for (int i = 0; i < a.length; i++) {
/* 800 */       System.out.println("Begin of Certificate: " + i);
/* 801 */       Certificate cert = sm.getCertificate(a[i]);
/* 802 */       X509Certificate x509 = (X509Certificate)cert;
/* 803 */       Collection<List<?>> ext = x509.getSubjectAlternativeNames();
/*     */       
/* 805 */       Iterator<List<?>> a1 = ext.iterator();
/* 806 */       while (a1.hasNext()) {
/* 807 */         List<?> list = a1.next();
/* 808 */         Iterator<?> j = list.iterator();
/* 809 */         while (j.hasNext()) {
/* 810 */           Object o = j.next();
/* 811 */           if (o instanceof Integer) {
/* 812 */             System.out.print(o);
/* 813 */             System.out.print("="); continue;
/* 814 */           }  if (o instanceof byte[]) {
/* 815 */             System.out.print(new BigInteger((byte[])o)); continue;
/* 816 */           }  if (o instanceof String) {
/* 817 */             System.out.print(o.toString());
/*     */           }
/*     */         } 
/* 820 */         System.out.println();
/*     */       } 
/*     */ 
/*     */       
/* 824 */       System.out.println("End of Certificate: " + i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void testMainFeatures(String[] args) throws Exception {
/* 830 */     SmartcardAccess test = new SmartcardAccess();
/* 831 */     test.setPKCS11Library("C:/Windows/System32/aetpkss1.dll");
/* 832 */     test.initializeProvider();
/*     */     
/* 834 */     KeyStore keyStore = test.loadPKCS11Keystore();
/* 835 */     if (keyStore == null) {
/* 836 */       System.out.println("The PKCS11 KeyStore was not loaded properly. Exiting...");
/*     */       return;
/*     */     } 
/* 839 */     test.printKeyStoreContent(keyStore);
/*     */     
/* 841 */     File f = new File("C:/Users/Eduardo/Documents/teste.txt");
/*     */     
/* 843 */     byte[] fileContents = test.read(f);
/*     */ 
/*     */     
/* 846 */     byte[] hash = test.digest("SHA1", fileContents);
/* 847 */     System.out.println("SHA1 hash of file " + f);
/* 848 */     System.out.println(Arrays.toString(hash));
/* 849 */     Hex.encode(hash, System.out);
/*     */ 
/*     */     
/* 852 */     String[] aliases = test.getAliases(keyStore);
/* 853 */     PrivateKey privKey = (PrivateKey)keyStore.getKey(aliases[0], null);
/* 854 */     byte[] signature = test.sign("SHA1withRSA", privKey, fileContents);
/*     */     
/* 856 */     System.out.println();
/* 857 */     System.out.println("SHA1withRSA signature of file " + f);
/* 858 */     System.out.println(Arrays.toString(signature));
/*     */ 
/*     */     
/* 861 */     PublicKey pubKey = test.getPublicKey(aliases[0]);
/* 862 */     boolean verifySignature = test.verifySign("SHA1withRSA", pubKey, fileContents, signature);
/* 863 */     System.out.println("does the signature match? " + verifySignature);
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\smartcard\SmartcardAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */