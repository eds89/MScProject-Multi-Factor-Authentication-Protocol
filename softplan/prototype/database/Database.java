/*     */ package softplan.prototype.database;
/*     */ 
/*     */ import java.sql.Blob;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import org.apache.log4j.Logger;
/*     */ import softplan.prototype.common.ClientData;
/*     */ import softplan.prototype.common.Message;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Database
/*     */ {
/*  24 */   private static final Logger logger = Logger.getLogger(Database.class);
/*     */   
/*     */   private Connection conn;
/*     */   
/*     */   public Database() {
/*     */     try {
/*  30 */       Class.forName("com.mysql.jdbc.Driver");
/*     */       
/*  32 */       String connAddress = DatabaseConstants.getJDBCConnectionAddress();
/*  33 */       logger.debug("JDBC is trying to connect to database on address: " + connAddress);
/*  34 */       this.conn = DriverManager.getConnection(
/*  35 */           connAddress);
/*  36 */       logger.debug("JDBC connection obtained successfully!");
/*  37 */       printDatabase();
/*  38 */     } catch (Exception e) {
/*  39 */       this.conn = null;
/*  40 */       logger.fatal("Could not start database connection", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void printDatabase() {
/*     */     try {
/*  46 */       String query = DatabaseConstants.obtainSelectAllQuery();
/*     */       
/*  48 */       Statement stm = this.conn.createStatement();
/*  49 */       ResultSet results = stm.executeQuery(query);
/*  50 */       while (results.next()) {
/*  51 */         logger.trace("=== Begin Row ======");
/*  52 */         String code = results.getString("code");
/*  53 */         String cpf = results.getString("cpf");
/*  54 */         logger.trace(code);
/*  55 */         logger.trace(cpf);
/*  56 */         logger.trace("=== End Row ======");
/*     */       } 
/*  58 */       results.close();
/*  59 */     } catch (SQLException sqle) {
/*  60 */       logger.trace("While obtaining full client data");
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/*     */     boolean connected;
/*  66 */     if (this.conn != null) {
/*     */       try {
/*  68 */         connected = !this.conn.isClosed();
/*  69 */       } catch (SQLException sqle) {
/*  70 */         logger.debug("Exception while verifying connectivity", sqle);
/*  71 */         connected = false;
/*     */       } 
/*     */     } else {
/*  74 */       connected = false;
/*     */     } 
/*  76 */     logger.trace("isConnected() : " + connected);
/*  77 */     return connected;
/*     */   }
/*     */   
/*     */   public void disconnect() {
/*  81 */     if (this.conn == null) {
/*     */       return;
/*     */     }
/*  84 */     logger.trace("disconnect()");
/*     */     
/*     */     try {
/*  87 */       if (!this.conn.isClosed()) {
/*  88 */         logger.debug("Closing database connection");
/*  89 */         this.conn.close();
/*     */       } 
/*  91 */     } catch (SQLException e) {
/*  92 */       logger.debug("Exception while closing database connection", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkConnection() throws SQLException {
/*  97 */     if (!isConnected()) {
/*  98 */       throw new SQLException("Database not connected!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void insert(Message message) throws SQLException {
/* 103 */     logger.trace("insert(): " + message);
/* 104 */     checkConnection();
/* 105 */     PreparedStatement prepStm = this.conn.prepareStatement(
/* 106 */         DatabaseConstants.getInsertClientQuery());
/* 107 */     logger.debug("preparedStatement = " + prepStm);
/* 108 */     Message innerMsg = (Message)message.getFirst();
/*     */     
/* 110 */     ClientData clientData = (ClientData)innerMsg.getFirst();
/* 111 */     String code = clientData.getCode();
/* 112 */     String cpf = clientData.getCpf();
/* 113 */     byte[] template = clientData.getTemplate();
/*     */     
/* 115 */     byte[] innerSig = (byte[])innerMsg.get(1);
/*     */ 
/*     */     
/* 118 */     byte[] outerSig = (byte[])message.get(1);
/*     */ 
/*     */ 
/*     */     
/* 122 */     prepStm.setString(1, cpf);
/* 123 */     prepStm.setString(2, code);
/* 124 */     prepStm.setBytes(3, template);
/* 125 */     prepStm.setBytes(4, innerSig);
/* 126 */     prepStm.setBytes(5, outerSig);
/* 127 */     logger.debug("insertion statement = " + prepStm.toString());
/*     */     
/* 129 */     prepStm.executeUpdate();
/*     */     
/*     */     try {
/* 132 */       prepStm.close();
/* 133 */     } catch (SQLException e) {
/* 134 */       logger.trace("while closing prepared statement", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Message retrieveFromCPF(String clientCPF) throws SQLException {
/* 139 */     return retrieve(DatabaseConstants.obtainClientQueryFromCPF(clientCPF));
/*     */   }
/*     */   
/*     */   public Message retrieveFromCode(String clientCode) throws SQLException {
/* 143 */     return retrieve(DatabaseConstants.obtainSelectQueryFromCode(clientCode));
/*     */   }
/*     */   
/*     */   protected Message retrieve(String query) throws SQLException {
/* 147 */     logger.trace("select(): clientCode=" + query);
/* 148 */     checkConnection();
/*     */     
/* 150 */     Statement stm = this.conn.createStatement();
/* 151 */     String selectQuery = query;
/* 152 */     logger.debug("select query=" + selectQuery);
/*     */     
/* 154 */     ResultSet rs = stm.executeQuery(selectQuery);
/* 155 */     logger.debug("ResultSet obtained");
/*     */     
/* 157 */     boolean hasNext = rs.next();
/* 158 */     logger.trace("more than one result returned? " + hasNext);
/* 159 */     if (!hasNext) {
/* 160 */       return null;
/*     */     }
/*     */     
/* 163 */     String code = rs.getString("code");
/* 164 */     String cpf = rs.getString("cpf");
/* 165 */     byte[] template = getFullBlob(rs.getBlob("template"));
/* 166 */     ClientData clientData = new ClientData(cpf, code, template);
/*     */     
/* 168 */     byte[] innerSig = getFullBlob(rs.getBlob("inner_sig"));
/* 169 */     byte[] outerSig = getFullBlob(rs.getBlob("outer_sig"));
/*     */     
/* 171 */     Message retrieveInnerMsg = new Message();
/* 172 */     retrieveInnerMsg.add(clientData);
/* 173 */     retrieveInnerMsg.add(innerSig);
/*     */     
/* 175 */     Message retrieveOuterMsg = new Message();
/* 176 */     retrieveOuterMsg.add(retrieveInnerMsg);
/* 177 */     retrieveOuterMsg.add(outerSig);
/* 178 */     logger.trace("retrievedMessage=" + retrieveInnerMsg);
/*     */     try {
/* 180 */       rs.close();
/* 181 */     } catch (SQLException e) {
/* 182 */       logger.trace("while closing result set", e);
/*     */     } 
/* 184 */     return retrieveInnerMsg;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] getFullBlob(Blob blobField) throws SQLException {
/*     */     byte[] fullBlob;
/*     */     try {
/* 192 */       fullBlob = blobField.getBytes(0L, (int)blobField.length());
/* 193 */     } catch (NullPointerException npe) {
/* 194 */       logger.debug("received a null blob");
/* 195 */       fullBlob = (byte[])null;
/*     */     } 
/* 197 */     return fullBlob;
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\database\Database.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */