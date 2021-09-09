package labsec.auth.biometric;

public interface FingerprintVerification extends FingerprintBase {
  boolean verify(UserRecord paramUserRecord) throws BiometricVerificationException;
  
  boolean verify(UserRecord paramUserRecord, byte[] paramArrayOfbyte) throws BiometricVerificationException;
  
  byte[] obtainBaseTemplate() throws BiometricVerificationException;
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\FingerprintVerification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */