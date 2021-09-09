/*    */ package softplan.prototype.common;
/*    */ 
/*    */ public class Constants {
/*  4 */   public static final int NUMBER_OF_FINGER_MODELS = Configuration.getProperty("numberFingerModels", 5);
/*    */ 
/*    */ 
/*    */   
/*  8 */   public static final String ADMIN_SERVER_HOSTNAME = Configuration.getProperty("adminServerHostname", "localhost");
/*    */   
/* 10 */   public static final int ADMIN_SERVER_PORT = Configuration.getProperty("adminServerPort", 15666);
/*    */   
/* 12 */   public static final String DATABASE_SERVER_HOSTNAME = Configuration.getProperty("clientServerHostname", "localhost");
/*    */   
/* 14 */   public static final int DATABASE_SERVER_PORT = Configuration.getProperty("clientServerPort", 15667);
/*    */   public static final String CLIENT_SERVER_HOSTNAME = "localhost";
/*    */   public static final int CLIENT_SERVER_PORT = 15668;
/*    */   public static final String ADMIN_KEY = "Admin::";
/*    */   public static final String CLIENT_KEY = "Client::";
/*    */   public static final String SERVER_KEY = "Server::";
/*    */   public static final String DATABASE_KEY = "Database::";
/*    */   public static final String REQUEST_CLIENT_CACHED_DATA = "Client's Cached Data";
/*    */   public static final String REQUEST_ADMIN_CACHED_DATA = "Admin's Cached Data";
/*    */   public static final String REQUEST_DATABASE_CACHED_DATA = "Database's Cached Data!";
/*    */   public static final String AUTH_REQUEST_WITH_BIOMETRICS = "AuthRequestWithBiometrics";
/*    */   public static final String AUTH_REQUEST = "AuthRequest";
/*    */   public static final String END_TRANSMISSION = "Bye!";
/*    */   public static final String USER_AUTHENTICATED = "UserAuthenticated";
/*    */   public static final String USER_NOT_AUTHENTICATED = "UserNotAuthenticated";
/*    */   public static final int AUTHENTICATION_WITH_BIOMETRICS = 4;
/*    */   public static final int REGISTER_WITH_BIOMETRICS = 8;
/*    */   public static final int INPUT_ARRAY_LENGTH = 8192;
/*    */   public static final int SERVER_NONCE_SIZE = 4;
/*    */   public static final String DATABASE_FILE = "Database_File";
/*    */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */