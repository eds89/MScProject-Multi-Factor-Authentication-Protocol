/*     */ package softplan.prototype.admin;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.EntityListener;
/*     */ import softplan.prototype.common.ExceptionHandler;
/*     */ import softplan.prototype.common.GUIUtils;
/*     */ import softplan.prototype.common.MessageEvent;
/*     */ import softplan.prototype.common.PINRequestHandler;
/*     */ 
/*     */ public class ManagementInterface
/*     */   extends JFrame
/*     */   implements ActionListener, ExceptionHandler, EntityListener, PINRequestHandler
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(ManagementInterface.class);
/*     */   
/*     */   private JList adminList;
/*     */   
/*     */   private JList clientList;
/*     */   
/*     */   JSplitPane splitPane;
/*     */   
/*     */   private JComboBox adminAlias;
/*     */   
/*     */   private JComboBox clientAlias;
/*     */   
/*     */   private JTextField cpfField;
/*     */   private JButton initializeButton;
/*     */   private JButton connectButton;
/*     */   private JButton registerButton;
/*     */   private JButton disconnectButton;
/*     */   private AdminEntity adminEntity;
/*     */   
/*     */   public ManagementInterface(AdminEntity adminEntity) {
/*  55 */     super("Management Interface");
/*     */     
/*  57 */     setAdminEntity(adminEntity);
/*  58 */     getAdminEntity().setExceptionHandler(this);
/*  59 */     getAdminEntity().setPinRequestHandler(this);
/*  60 */     getAdminEntity().addEntityListener(this);
/*     */     
/*  62 */     initComponents();
/*  63 */     initWindowListeners();
/*     */ 
/*     */     
/*  66 */     pack();
/*  67 */     adjustSplit();
/*  68 */     setTitle("Multifactor Protocol Management Interface");
/*     */     
/*  70 */     setLocationRelativeTo((Component)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AdminEntity getAdminEntity() {
/*  76 */     return this.adminEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdminEntity(AdminEntity adminEntity) {
/*  82 */     this.adminEntity = adminEntity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initComponents() {
/*  88 */     JPanel c = new JPanel();
/*  89 */     c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/*  90 */     c.setLayout(new BorderLayout());
/*  91 */     setContentPane(c);
/*     */ 
/*     */ 
/*     */     
/*  95 */     JPanel adminListPanel = new JPanel(new BorderLayout());
/*  96 */     this.adminList = new JList(new DefaultListModel());
/*  97 */     adminListPanel.add(new JLabel("Select a certificate to act as administrador in the protocol:"), "North");
/*  98 */     adminListPanel.add(new JScrollPane(this.adminList), "Center");
/*     */     
/* 100 */     JPanel clientListPanel = new JPanel(new BorderLayout());
/* 101 */     this.clientList = new JList(new DefaultListModel());
/* 102 */     clientListPanel.add(new JLabel("Select a certificate to act as client in the protocol:"), "North");
/* 103 */     clientListPanel.add(new JScrollPane(this.clientList), "Center");
/*     */     
/* 105 */     this.splitPane = new JSplitPane(1, 
/* 106 */         false, adminListPanel, clientListPanel);
/* 107 */     c.add(this.splitPane, "Center");
/*     */ 
/*     */     
/* 110 */     JPanel buttonsPanel = new JPanel(new FlowLayout());
/*     */     
/* 112 */     this.initializeButton = new JButton("Load Smartcard");
/* 113 */     this.initializeButton.addActionListener(this);
/* 114 */     buttonsPanel.add(this.initializeButton);
/*     */     
/* 116 */     this.connectButton = new JButton("Connect to Server");
/* 117 */     this.connectButton.addActionListener(this);
/* 118 */     buttonsPanel.add(this.connectButton);
/*     */     
/* 120 */     JLabel label = new JLabel("Client's CPF");
/* 121 */     buttonsPanel.add(label);
/*     */     
/* 123 */     this.cpfField = new JTextField("123.456.789-10", 8);
/* 124 */     Font cpfFont = this.cpfField.getFont();
/* 125 */     Font boldCpfFont = new Font(cpfFont.getName(), 
/* 126 */         1, cpfFont.getSize());
/* 127 */     this.cpfField.setFont(boldCpfFont);
/* 128 */     this.cpfField.setForeground(Color.BLUE);
/* 129 */     buttonsPanel.add(this.cpfField);
/*     */     
/* 131 */     this.registerButton = new JButton("Begin register");
/* 132 */     this.registerButton.addActionListener(this);
/* 133 */     this.registerButton.setEnabled(false);
/* 134 */     buttonsPanel.add(this.registerButton);
/*     */     
/* 136 */     this.disconnectButton = new JButton("Disconnect");
/* 137 */     this.disconnectButton.addActionListener(this);
/* 138 */     this.disconnectButton.setEnabled(false);
/* 139 */     buttonsPanel.add(this.disconnectButton);
/*     */ 
/*     */     
/* 142 */     c.add(buttonsPanel, "North");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initWindowListeners() {
/* 147 */     WindowAdapter windowAdapter = new WindowAdapter()
/*     */       {
/*     */         public void windowClosing(WindowEvent e)
/*     */         {
/* 151 */           ManagementInterface.logger.trace("windowClosing()");
/*     */           
/* 153 */           if (ManagementInterface.this.adminEntity.isConnected()) {
/* 154 */             ManagementInterface.this.adminEntity.disconnect();
/*     */           }
/* 156 */           System.exit(0);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 162 */     addWindowListener(windowAdapter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void adjustSplit() {
/* 168 */     this.splitPane.setDividerLocation((int)(getSize().getWidth() / 2.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     try {
/* 174 */       if (e.getSource() == this.initializeButton) {
/* 175 */         logger.trace("Admin User clicked on the initialize button");
/*     */         
/* 177 */         char[] pin = GUIUtils.showPINRequestDialog(this);
/* 178 */         String[] availableAliases = this.adminEntity.obtainCardData(pin);
/* 179 */         setListsElements(availableAliases);
/*     */       
/*     */       }
/* 182 */       else if (e.getSource() == this.connectButton) {
/* 183 */         logger.trace("Admin User clicked on the connect button");
/* 184 */         this.adminEntity.beginCommunicationToServer();
/*     */ 
/*     */       
/*     */       }
/* 188 */       else if (e.getSource() == this.registerButton) {
/* 189 */         logger.trace("Admin User clicked on the register button");
/* 190 */         if (this.adminList.getSelectedIndex() == -1 || 
/* 191 */           this.clientList.getSelectedIndex() == -1) {
/*     */           
/* 193 */           GUIUtils.showErrorDialog(this, 
/* 194 */               "You must to select both Admin and Client certificates!"); return;
/*     */         } 
/* 196 */         if (this.adminList.getSelectedIndex() == this.clientList.getSelectedIndex()) {
/* 197 */           GUIUtils.showErrorDialog(this, 
/* 198 */               "You must to select different Admin and Client certificates!");
/*     */           return;
/*     */         } 
/* 201 */         String clientCpf = this.cpfField.getText();
/*     */         
/* 203 */         String adminAlias = getAdminSelectedItem();
/* 204 */         String clientAlias = getClientSelectedItem();
/*     */         
/* 206 */         this.adminEntity.beginRegister(clientCpf, clientAlias, adminAlias);
/*     */       }
/* 208 */       else if (e.getSource() == this.disconnectButton) {
/* 209 */         logger.trace("Admin User clicked on the disconnect button");
/* 210 */         this.adminEntity.disconnect();
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 215 */     catch (Throwable e2) {
/* 216 */       handleException(e2);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleException(Throwable e) {
/* 221 */     logger.error("Exception while executing user command", e);
/* 222 */     String message = "Exception thrown!\n\n" + 
/* 223 */       e + ": message=" + e.getMessage();
/* 224 */     GUIUtils.showErrorDialog(this, message);
/*     */   }
/*     */   
/*     */   public void cleanLists() {
/* 228 */     ((DefaultListModel)this.adminList.getModel()).clear();
/* 229 */     ((DefaultListModel)this.clientList.getModel()).clear();
/*     */   }
/*     */   
/*     */   public String getClientListElementAt(int index) {
/* 233 */     return getListElementAt(this.clientList, index).toString();
/*     */   }
/*     */   
/*     */   public String getAdminListElementAt(int index) {
/* 237 */     return getListElementAt(this.adminList, index).toString();
/*     */   }
/*     */   
/*     */   private Object getListElementAt(JList list, int index) {
/* 241 */     Object o = ((DefaultListModel)list.getModel()).getElementAt(index);
/* 242 */     return o;
/*     */   }
/*     */   
/*     */   public void setListsElements(final String[] items) {
/* 246 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 248 */             ManagementInterface.this.cleanLists();
/* 249 */             DefaultListModel<String> adminListModel = (DefaultListModel)ManagementInterface.this.adminList.getModel();
/* 250 */             DefaultListModel<String> clientListModel = (DefaultListModel)ManagementInterface.this.clientList.getModel(); byte b; int i; String[] arrayOfString;
/* 251 */             for (i = (arrayOfString = items).length, b = 0; b < i; ) { String item = arrayOfString[b];
/* 252 */               adminListModel.addElement(item);
/* 253 */               clientListModel.addElement(item);
/*     */               b++; }
/*     */           
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public String getClientSelectedItem() {
/* 261 */     return getSelectedItem(this.clientList).toString();
/*     */   }
/*     */   
/*     */   public String getAdminSelectedItem() {
/* 265 */     return getSelectedItem(this.adminList).toString();
/*     */   }
/*     */   
/*     */   private Object getSelectedItem(JList list) {
/* 269 */     if (list.getSelectedIndex() == -1) {
/* 270 */       return null;
/*     */     }
/* 272 */     return list.getSelectedValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageReceived(MessageEvent m) {
/* 279 */     GUIUtils.showInfoDialog(this, "Management interface received a message from server.\nThe processing of such message will begin now.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nonceReceived(MessageEvent m) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void entityConnected() {
/* 296 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 298 */             ManagementInterface.this.connectButton.setEnabled(false);
/* 299 */             ManagementInterface.this.registerButton.setEnabled(true);
/* 300 */             ManagementInterface.this.disconnectButton.setEnabled(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void entityDisconnected() {
/* 309 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 311 */             ManagementInterface.this.connectButton.setEnabled(true);
/* 312 */             ManagementInterface.this.registerButton.setEnabled(false);
/* 313 */             ManagementInterface.this.disconnectButton.setEnabled(false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void beginProcessing() {
/* 324 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 326 */             ManagementInterface.this.initializeButton.setEnabled(false);
/* 327 */             ManagementInterface.this.connectButton.setEnabled(false);
/* 328 */             ManagementInterface.this.registerButton.setEnabled(false);
/* 329 */             ManagementInterface.this.disconnectButton.setEnabled(false);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endProcessing() {
/* 340 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 342 */             ManagementInterface.this.initializeButton.setEnabled(true);
/* 343 */             ManagementInterface.this.connectButton.setEnabled(false);
/* 344 */             ManagementInterface.this.registerButton.setEnabled(true);
/* 345 */             ManagementInterface.this.disconnectButton.setEnabled(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] requestPIN(String forUser) {
/* 357 */     logger.trace("requestPIN() for " + forUser);
/* 358 */     String message = "Type the PIN for releasing the private key of admin\n'" + 
/* 359 */       forUser + "'. ";
/* 360 */     char[] typedPin = GUIUtils.showPINRequestDialog(this, message);
/* 361 */     return typedPin;
/*     */   }
/*     */   
/*     */   public void userAuthenticated() {}
/*     */   
/*     */   public void userNotAuthenticated() {}
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\admin\ManagementInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */