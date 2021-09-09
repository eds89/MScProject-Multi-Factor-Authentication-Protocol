/*     */ package softplan.prototype.common;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ public class ClientData
/*     */   implements Serializable
/*     */ {
/*     */   private String cpf;
/*     */   private String code;
/*     */   private byte[] template;
/*     */   
/*     */   public ClientData(String cpf) {
/*  15 */     this.cpf = cpf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientData(String cpf, String code) {
/*  22 */     this.cpf = cpf;
/*  23 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientData(String cpf, String code, byte[] template) {
/*  30 */     this.cpf = cpf;
/*  31 */     this.code = code;
/*  32 */     this.template = template;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCpf() {
/*  38 */     return this.cpf;
/*     */   }
/*     */   
/*     */   public String getCode() {
/*  42 */     return this.code;
/*     */   }
/*     */   
/*     */   public boolean hasCode() {
/*  46 */     return (this.code != null);
/*     */   }
/*     */   
/*     */   public byte[] getTemplate() {
/*  50 */     return this.template;
/*     */   }
/*     */   
/*     */   public boolean hasTemplate() {
/*  54 */     return (this.template != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  61 */     int prime = 31;
/*  62 */     int result = 1;
/*  63 */     result = 31 * result + ((this.code == null) ? 0 : this.code.hashCode());
/*  64 */     result = 31 * result + ((this.cpf == null) ? 0 : this.cpf.hashCode());
/*  65 */     result = 31 * result + Arrays.hashCode(this.template);
/*  66 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  73 */     if (this == obj)
/*  74 */       return true; 
/*  75 */     if (obj == null)
/*  76 */       return false; 
/*  77 */     if (getClass() != obj.getClass())
/*  78 */       return false; 
/*  79 */     ClientData other = (ClientData)obj;
/*  80 */     if (this.code == null) {
/*  81 */       if (other.code != null)
/*  82 */         return false; 
/*  83 */     } else if (!this.code.equals(other.code)) {
/*  84 */       return false;
/*  85 */     }  if (this.cpf == null) {
/*  86 */       if (other.cpf != null)
/*  87 */         return false; 
/*  88 */     } else if (!this.cpf.equals(other.cpf)) {
/*  89 */       return false;
/*  90 */     }  if (!Arrays.equals(this.template, other.template))
/*  91 */       return false; 
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  99 */     StringBuilder builder = new StringBuilder();
/* 100 */     builder.append("ClientData [");
/* 101 */     if (this.cpf != null) {
/* 102 */       builder.append("cpf=");
/* 103 */       builder.append(this.cpf);
/* 104 */       builder.append(", ");
/*     */     } 
/* 106 */     if (this.code != null) {
/* 107 */       builder.append("code=");
/* 108 */       builder.append(this.code);
/* 109 */       builder.append(", ");
/*     */     } 
/* 111 */     if (this.template != null) {
/* 112 */       builder.append("template=[");
/* 113 */       builder.append(this.template.length);
/* 114 */       builder.append("]]");
/*     */     } 
/* 116 */     builder.append("]");
/* 117 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\ClientData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */