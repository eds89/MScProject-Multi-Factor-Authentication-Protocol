package labsec.auth.biometric;

public interface FingerprintBase {
  void initialize();
  
  void uninitialize();
  
  void abort();
  
  String getWindowsLibrary();
  
  String getLinuxLibrary();
  
  String getName();
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\FingerprintBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */