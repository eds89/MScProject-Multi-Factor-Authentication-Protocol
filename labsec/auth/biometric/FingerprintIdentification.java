package labsec.auth.biometric;

public interface FingerprintIdentification extends FingerprintBase {
  UserRecord identify(UserRecord[] paramArrayOfUserRecord) throws BiometricIdentificationException;
  
  UserRecord identify(UserRecord[] paramArrayOfUserRecord, byte[] paramArrayOfbyte) throws BiometricIdentificationException;
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\FingerprintIdentification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */