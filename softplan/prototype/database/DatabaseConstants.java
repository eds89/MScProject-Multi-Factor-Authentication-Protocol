/*     */ package softplan.prototype.database;
/*     */ 
/*     */ import softplan.prototype.common.Configuration;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatabaseConstants
/*     */ {
/*     */   public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
/*     */   public static final String DATABASE_TYPE = "mysql";
/*  11 */   public static final String SERVER_ADDRESS = Configuration.getProperty("databaseServerAddress", "localhost");
/*     */   
/*  13 */   public static final String DATABASE_NAME = Configuration.getProperty("databaseName", "esantos");
/*     */   
/*  15 */   public static final String USER_ACCOUNT = Configuration.getProperty("databaseUser", "root");
/*     */   
/*  17 */   public static final String USER_PASSWORD = Configuration.getProperty("databaseUserPassword", "seclab");
/*     */   
/*     */   public static final String TABLE_CLIENT = "client";
/*     */   
/*     */   public static final String FIELD_ID = "id";
/*     */   
/*     */   public static final int FIELD_ID_INDEX = 1;
/*     */   
/*     */   public static final String FIELD_CPF = "cpf";
/*     */   
/*     */   public static final int FIELD_CPF_INDEX = 2;
/*     */   
/*     */   public static final String FIELD_CODE = "code";
/*     */   
/*     */   public static final int FIELD_CODE_INDEX = 3;
/*     */   
/*     */   public static final String FIELD_TEMPLATE = "template";
/*     */   
/*     */   public static final int FIELD_TEMPLATE_INDEX = 4;
/*     */   
/*     */   public static final String FIELD_INNER_SIG = "inner_sig";
/*     */   
/*     */   public static final int FIELD_INNER_SIG_INDEX = 5;
/*     */   
/*     */   public static final String FIELD_OUTER_SIG = "outer_sig";
/*     */   
/*     */   public static final int FIELD_OUTER_SIG_INDEX = 6;
/*     */   
/*     */   public static String getJDBCConnectionAddress() {
/*  46 */     StringBuilder builder = new StringBuilder();
/*  47 */     builder.append("jdbc:");
/*  48 */     builder.append("mysql");
/*  49 */     builder.append("://");
/*  50 */     builder.append(SERVER_ADDRESS);
/*  51 */     builder.append("/");
/*  52 */     builder.append(DATABASE_NAME);
/*  53 */     builder.append("?user=");
/*  54 */     builder.append(USER_ACCOUNT);
/*  55 */     builder.append("&password=");
/*  56 */     builder.append(USER_PASSWORD);
/*     */     
/*  58 */     String builderStr = builder.toString();
/*     */     
/*  60 */     return builderStr;
/*     */   }
/*     */   
/*     */   public static String obtainSelectQueryFromCode(String code) {
/*  64 */     StringBuilder sb = new StringBuilder();
/*  65 */     sb.append("SELECT * FROM ");
/*  66 */     sb.append("client");
/*  67 */     sb.append(" WHERE ");
/*  68 */     sb.append("code");
/*  69 */     sb.append("  = '");
/*  70 */     sb.append(code);
/*  71 */     sb.append("'");
/*  72 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String obtainSelectAllQuery() {
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     sb.append("SELECT * FROM ");
/*  78 */     sb.append("client");
/*     */     
/*  80 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String obtainClientQueryFromCPF(String cpf) {
/*  84 */     StringBuilder sb = new StringBuilder();
/*  85 */     sb.append("SELECT * FROM ");
/*  86 */     sb.append("client");
/*  87 */     sb.append(" WHERE ");
/*  88 */     sb.append("cpf");
/*  89 */     sb.append("  = '");
/*  90 */     sb.append(cpf);
/*  91 */     sb.append("'");
/*  92 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getInsertClientQuery() {
/*  96 */     StringBuilder sb = new StringBuilder();
/*  97 */     sb.append("INSERT INTO ");
/*  98 */     sb.append("client");
/*  99 */     sb.append(" ( ");
/* 100 */     sb.append("cpf");
/* 101 */     sb.append(",");
/* 102 */     sb.append("code");
/* 103 */     sb.append(",");
/* 104 */     sb.append("template");
/* 105 */     sb.append(",");
/* 106 */     sb.append("inner_sig");
/* 107 */     sb.append(",");
/* 108 */     sb.append("outer_sig");
/* 109 */     sb.append(" )");
/* 110 */     sb.append(" VALUES ( ?, ?, ?, ?, ? )");
/* 111 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 115 */     System.out.println(obtainSelectQueryFromCode("Eduardo"));
/* 116 */     System.out.println(getInsertClientQuery());
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\database\DatabaseConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */