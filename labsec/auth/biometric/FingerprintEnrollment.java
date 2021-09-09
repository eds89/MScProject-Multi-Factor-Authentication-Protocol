package labsec.auth.biometric;

public interface FingerprintEnrollment extends FingerprintBase {
  void setMaxModels(int paramInt);
  
  int getMaxModels();
  
  int getMinQuality();
  
  int getMaxQuality();
  
  int getLastEnrolledQuality();
  
  boolean isGoodQuality(int paramInt);
  
  UserRecord enroll(String paramString) throws BiometricEnrollmentException;
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\FingerprintEnrollment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */