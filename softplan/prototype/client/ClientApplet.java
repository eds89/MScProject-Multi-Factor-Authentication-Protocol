/*     */ package softplan.prototype.client;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JApplet;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import labsec.auth.biometric.event.EnrollmentEvent;
/*     */ import labsec.auth.biometric.event.EnrollmentListener;
/*     */ import labsec.auth.biometric.event.ProgressEvent;
/*     */ import labsec.auth.biometric.event.VerificationEvent;
/*     */ import labsec.auth.biometric.event.VerificationListener;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.Configuration;
/*     */ import softplan.prototype.common.EntityListener;
/*     */ import softplan.prototype.common.ExceptionHandler;
/*     */ import softplan.prototype.common.GUIUtils;
/*     */ import softplan.prototype.common.MessageEvent;
/*     */ import softplan.prototype.common.PINRequestHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClientApplet
/*     */   extends JApplet
/*     */   implements ActionListener, EnrollmentListener, VerificationListener, EntityListener, ExceptionHandler, PINRequestHandler
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(ClientApplet.class);
/*     */   
/*     */   private JLabel fingerprintImage;
/*     */   
/*     */   private JLabel statusLabel;
/*     */   
/*     */   private JButton connectButton;
/*     */   
/*     */   private JButton registerButton;
/*     */   
/*     */   private JButton authenticateButton;
/*     */   private JButton disconnectButton;
/*  53 */   private JButton[] allButtons = new JButton[] {
/*  54 */       this.connectButton, this.registerButton, this.authenticateButton, 
/*  55 */       this.disconnectButton
/*     */     };
/*     */   
/*     */   protected ClientEntity clientEntity;
/*     */   
/*     */   static {
/*  61 */     Configuration.configureClient();
/*     */   }
/*     */   
/*     */   public void initComponents() {
/*  65 */     Container c = getContentPane();
/*  66 */     c.setLayout(new BorderLayout());
/*     */     
/*  68 */     JPanel fingerPanel = new JPanel(new BorderLayout());
/*  69 */     this.fingerprintImage = new JLabel();
/*  70 */     fingerPanel.add(this.fingerprintImage, "Center");
/*     */     
/*  72 */     this.statusLabel = new JLabel();
/*  73 */     Border line = BorderFactory.createLineBorder(Color.BLACK, 1);
/*  74 */     Border empty = BorderFactory.createEmptyBorder(5, 5, 5, 5);
/*  75 */     Border beveled = BorderFactory.createBevelBorder(1);
/*  76 */     Border statusBorder = empty;
/*     */     
/*  78 */     this.statusLabel.setBorder(statusBorder);
/*  79 */     this.statusLabel.setSize(320, 480);
/*  80 */     fingerPanel.add(this.statusLabel, "South");
/*  81 */     c.add(fingerPanel, "Center");
/*     */     
/*  83 */     JPanel bottomPanel = new JPanel();
/*  84 */     bottomPanel.setLayout(new FlowLayout());
/*     */     
/*  86 */     this.connectButton = new JButton("Connect");
/*  87 */     this.connectButton.setEnabled(true);
/*  88 */     this.connectButton.addActionListener(this);
/*  89 */     bottomPanel.add(this.connectButton);
/*     */     
/*  91 */     this.registerButton = new JButton("Register");
/*  92 */     this.registerButton.setEnabled(true);
/*  93 */     this.registerButton.addActionListener(this);
/*  94 */     bottomPanel.add(this.registerButton);
/*     */     
/*  96 */     this.authenticateButton = new JButton("Authenticate");
/*  97 */     this.authenticateButton.setEnabled(false);
/*  98 */     this.authenticateButton.addActionListener(this);
/*  99 */     bottomPanel.add(this.authenticateButton);
/*     */     
/* 101 */     this.disconnectButton = new JButton("Disconnect");
/* 102 */     this.disconnectButton.setEnabled(false);
/* 103 */     this.disconnectButton.addActionListener(this);
/* 104 */     bottomPanel.add(this.disconnectButton);
/*     */     
/* 106 */     c.add(bottomPanel, "South");
/*     */   }
/*     */   
/*     */   protected void initClientEntity() {
/* 110 */     this.clientEntity = new ClientEntity();
/* 111 */     this.clientEntity.initBiometrics();
/* 112 */     this.clientEntity.setExceptionHandler(this);
/*     */     
/* 114 */     this.clientEntity.getFingerprintReader().setEnrollListener(this);
/* 115 */     this.clientEntity.getFingerprintReader().setVerifyListener(this);
/* 116 */     this.clientEntity.addEntityListener(this);
/* 117 */     this.clientEntity.setPinRequestHandler(this);
/*     */   }
/*     */   
/*     */   public void init() {
/* 121 */     logger.trace("init()");
/*     */     
/* 123 */     initClientEntity();
/* 124 */     initComponents();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 130 */     logger.trace("start()");
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 135 */     logger.trace("stop()");
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 140 */     logger.trace("destroy()");
/* 141 */     if (this.clientEntity.isConnected()) {
/* 142 */       this.clientEntity.disconnect();
/*     */     }
/*     */     
/* 145 */     this.clientEntity.getFingerprintReader().uninitialize();
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent event) {
/* 150 */     setFingerprintImage(null);
/*     */     try {
/* 152 */       if (event.getSource() == this.connectButton) {
/* 153 */         logger.trace("Applet User clicked on connect button");
/* 154 */         this.clientEntity.beginCommunicationToServer();
/*     */         
/* 156 */         enableButtons(true);
/* 157 */       } else if (event.getSource() == this.registerButton) {
/* 158 */         logger.trace("Applet User clicked on register button");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 165 */         String[] clients = this.clientEntity.getRcvMessageKeys();
/* 166 */         if (clients.length == 0) {
/* 167 */           GUIUtils.showAlertDialog(this, "No clients waiting for register. Try again later.");
/*     */           return;
/*     */         } 
/* 170 */         String selected = GUIUtils.showInputDialog(this, "These are the available clients to register. Select which one corresponds to yourself.\nThe possession of public key will be tested in order to guarantee security on this transaction.", 
/*     */             
/* 172 */             clients);
/* 173 */         if (selected == null) {
/*     */           return;
/*     */         }
/*     */         
/* 177 */         String codename = GUIUtils.showInputDialog(this, 
/* 178 */             "Type the username you want to register for\n'" + 
/* 179 */             selected + "'");
/* 180 */         if (codename == null) {
/*     */           return;
/*     */         }
/*     */         
/* 184 */         char[] pin = GUIUtils.showPINRequestDialog(this, 
/* 185 */             "Type the PIN for accessing the private key of certificate:\n'" + selected + "'." + 
/* 186 */             " private's key.");
/* 187 */         this.clientEntity.register(codename, selected, pin, true);
/*     */       }
/* 189 */       else if (event.getSource() == this.authenticateButton) {
/* 190 */         logger.trace("Applet User clicked on authentication button");
/*     */         
/* 192 */         char[] pin = GUIUtils.showPINRequestDialog(this);
/*     */         
/* 194 */         String[] availableClients = this.clientEntity.obtainCardData(pin);
/* 195 */         String selectedClient = GUIUtils.showInputDialog(this, 
/* 196 */             "The following certificates have been found in the smartcard.\nPlease, select the user you want to authenticate.", availableClients);
/* 197 */         if (selectedClient == null) {
/* 198 */           GUIUtils.showAlertDialog(this, "You must select a user to authenticate against the server.");
/*     */           
/*     */           return;
/*     */         } 
/* 202 */         String cpf = GUIUtils.showInputDialog(this, "Type the CPF for the user '" + 
/* 203 */             selectedClient + "'. ");
/* 204 */         if (cpf == null) {
/* 205 */           GUIUtils.showAlertDialog(this, "You must type the user's CPF to proceed.");
/*     */           return;
/*     */         } 
/* 208 */         this.clientEntity.beginuthentication(selectedClient, cpf, true);
/*     */       }
/* 210 */       else if (event.getSource() == this.disconnectButton) {
/* 211 */         logger.trace("Applet User clicked on disconnect button");
/* 212 */         this.clientEntity.disconnect();
/* 213 */         enableButtons(false);
/*     */       }
/*     */     
/* 216 */     } catch (Exception ex) {
/* 217 */       handleException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleException(Throwable ex) {
/* 223 */     logger.error("Exception while executing user command", ex);
/*     */     
/* 225 */     String message = "Exception thrown: " + 
/* 226 */       ex.getClass() + "\n\nReason: " + ex.getMessage();
/* 227 */     GUIUtils.showErrorDialog(this, message);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void enableButtons(final boolean enabled) {
/* 232 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 234 */             ClientApplet.this.connectButton.setEnabled(!enabled);
/* 235 */             ClientApplet.this.registerButton.setEnabled(enabled);
/* 236 */             ClientApplet.this.authenticateButton.setEnabled(enabled);
/* 237 */             ClientApplet.this.disconnectButton.setEnabled(enabled);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void disableAllButtons() {
/* 244 */     SwingUtilities.invokeLater(new Runnable() { public void run() { byte b;
/*     */             int i;
/*     */             JButton[] arrayOfJButton;
/* 247 */             for (i = (arrayOfJButton = ClientApplet.this.allButtons).length, b = 0; b < i; ) { JButton jButton = arrayOfJButton[b];
/* 248 */               jButton.setEnabled(false);
/*     */               b++; }
/*     */              }
/*     */            }
/*     */       );
/*     */   }
/*     */   
/*     */   public void setFingerprintImage(final Image img) {
/* 256 */     SwingUtilities.invokeLater(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/*     */             Icon icon;
/* 261 */             if (img != null) {
/* 262 */               icon = new ImageIcon(img);
/*     */             } else {
/* 264 */               icon = null;
/*     */             } 
/* 266 */             ClientApplet.this.fingerprintImage.setIcon(icon);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFingerprintLabel(final String s) {
/* 274 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/*     */             String newText;
/* 277 */             if ("".equals(s)) {
/* 278 */               newText = " ";
/*     */             } else {
/* 280 */               newText = s;
/*     */             } 
/* 282 */             ClientApplet.this.statusLabel.setText(newText);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putFingerOn(ProgressEvent progressEvent) {
/* 290 */     if (progressEvent.getCount() == 1) {
/* 291 */       setFingerprintImage(null);
/*     */     }
/* 293 */     logger.trace("putFingerOn() event=" + progressEvent);
/* 294 */     setFingerprintLabel("Put finger on reader for template " + 
/* 295 */         progressEvent.getCount() + "/" + 
/* 296 */         progressEvent.getTotal());
/*     */   }
/*     */ 
/*     */   
/*     */   public void takeFingerOff(ProgressEvent progressEvent) {
/* 301 */     logger.trace("takeFingerOff() event=" + progressEvent);
/* 302 */     setFingerprintLabel("Take finger off reader for template " + 
/* 303 */         progressEvent.getCount() + "/" + 
/* 304 */         progressEvent.getTotal());
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateImage(BufferedImage bitmapImage) {
/* 309 */     logger.trace("updateImage() event");
/* 310 */     String dimStr = String.valueOf(bitmapImage.getWidth()) + "x" + 
/* 311 */       bitmapImage.getHeight();
/* 312 */     logger.trace("bitmap image size= " + dimStr);
/* 313 */     setFingerprintImage(bitmapImage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void verificationComplete(VerificationEvent event) {
/* 319 */     boolean success = event.isSuccess();
/* 320 */     boolean matched = event.isMatched();
/* 321 */     logger.debug("Fingerprint toVerification complete: success=" + 
/* 322 */         success + ", matched=" + matched);
/* 323 */     if (success) {
/* 324 */       if (matched) {
/* 325 */         GUIUtils.showInfoDialog(this, "Fingerprint verified successfully.");
/*     */       } else {
/* 327 */         GUIUtils.showErrorDialog(this, "Fingerprints did not match.");
/*     */       } 
/*     */     } else {
/* 330 */       GUIUtils.showErrorDialog(this, "Fingerprint toVerification process did not complete.");
/*     */     } 
/* 332 */     setFingerprintLabel("");
/*     */   }
/*     */ 
/*     */   
/*     */   public void enrollmentComplete(EnrollmentEvent event) {
/* 337 */     setFingerprintLabel("");
/* 338 */     boolean successful = event.isSuccess();
/* 339 */     logger.debug("Fingerprint enrollment complete. Success? " + successful);
/* 340 */     if (successful) {
/* 341 */       GUIUtils.showInfoDialog(this, "Fingerprint captured successfully.");
/*     */     } else {
/* 343 */       GUIUtils.showErrorDialog(this, "Could not enroll fingerprint.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(MessageEvent m) {
/* 349 */     logger.trace("messageReceived(): " + m.getMessageKey());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void nonceReceived(MessageEvent m) {
/* 355 */     logger.trace("nonceReceived(): " + m.getMessageKey());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void entityConnected() {
/* 361 */     enableButtons(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void entityDisconnected() {
/* 367 */     enableButtons(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginProcessing() {
/* 373 */     disableAllButtons();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void endProcessing() {
/* 379 */     enableButtons(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void userAuthenticated() {
/* 385 */     GUIUtils.showInfoDialog(this, "User authenticated!");
/*     */   }
/*     */ 
/*     */   
/*     */   public void userNotAuthenticated() {
/* 390 */     GUIUtils.showErrorDialog(this, "User could not authenticated.\nThe applet will now be disconnected.");
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] requestPIN(String forUser) {
/* 395 */     String message = "Type the PIN for releasing the private key of user\n'" + 
/* 396 */       forUser + "'.";
/*     */     
/* 398 */     char[] pin = GUIUtils.showPINRequestDialog(this, message);
/* 399 */     return pin;
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\client\ClientApplet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */