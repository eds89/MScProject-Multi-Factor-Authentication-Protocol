package labsec.auth.biometric.event;

import java.awt.image.BufferedImage;

public interface BaseListener {
  void putFingerOn(ProgressEvent paramProgressEvent);
  
  void takeFingerOff(ProgressEvent paramProgressEvent);
  
  void updateImage(BufferedImage paramBufferedImage);
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\BaseListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */