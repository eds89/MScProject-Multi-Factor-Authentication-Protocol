package labsec.auth.biometric.event;

import java.awt.image.BufferedImage;

public class BaseAdapter implements BaseListener {
  public void putFingerOn(ProgressEvent progressEvent) {}
  
  public void takeFingerOff(ProgressEvent progressEvent) {}
  
  public void updateImage(BufferedImage bitmapImage) {}
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\labsec\auth\biometric\event\BaseAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */