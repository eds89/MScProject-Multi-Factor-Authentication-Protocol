package softplan.prototype.common;

public interface EntityListener {
  void messageReceived(MessageEvent paramMessageEvent);
  
  void nonceReceived(MessageEvent paramMessageEvent);
  
  void beginProcessing();
  
  void endProcessing();
  
  void entityConnected();
  
  void entityDisconnected();
  
  void userAuthenticated();
  
  void userNotAuthenticated();
}


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\EntityListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */