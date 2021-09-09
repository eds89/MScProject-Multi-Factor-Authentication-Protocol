/*     */ package softplan.prototype.common;
/*     */ 
/*     */ public class Message implements Serializable {
/*     */   protected List<Object> data;
/*     */   private Type type;
/*     */   
/*     */   public enum Type {
/*   8 */     AUTH, REGISTER;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Message() {
/*  15 */     this(Type.REGISTER);
/*     */   }
/*     */   
/*     */   public Message(Type type) {
/*  19 */     this.type = type;
/*  20 */     this.data = new ArrayList();
/*     */   }
/*     */   
/*     */   public void add(Object o) {
/*  24 */     this.data.add(o);
/*     */   }
/*     */   
/*     */   public Object removeLast() {
/*  28 */     Object last = this.data.remove(this.data.size() - 1);
/*  29 */     return last;
/*     */   }
/*     */   
/*     */   public Object getFirst() {
/*  33 */     return this.data.get(0);
/*     */   }
/*     */   
/*     */   public Object getLast() {
/*  37 */     return this.data.get(this.data.size() - 1);
/*     */   }
/*     */   
/*     */   public Object get(int index) {
/*  41 */     return this.data.get(index);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  45 */     this.data.clear();
/*     */   }
/*     */   
/*     */   public boolean isAuth() {
/*  49 */     return (this.type == Type.AUTH);
/*     */   }
/*     */   
/*     */   public boolean isRegister() {
/*  53 */     return (this.type == Type.REGISTER);
/*     */   }
/*     */   
/*     */   public boolean isNonceOnlyMessage() {
/*  57 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isClassAt(Class c, int index) {
/*  61 */     if (this.data.size() <= index) {
/*  62 */       return false;
/*     */     }
/*  64 */     return this.data.get(index).getClass().equals(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  69 */     StringBuilder builder = new StringBuilder();
/*  70 */     builder.append("Message [type=");
/*  71 */     builder.append(this.type);
/*  72 */     builder.append(", data=");
/*  73 */     for (Object eachElement : this.data) {
/*  74 */       if (eachElement instanceof byte[]) {
/*  75 */         int l; byte[] eachElementArr = (byte[])eachElement;
/*     */         
/*  77 */         int elemsToPrint = 5;
/*     */         
/*  79 */         if (eachElementArr.length < 5) {
/*  80 */           l = eachElementArr.length;
/*     */         } else {
/*  82 */           l = 5;
/*     */         } 
/*  84 */         builder.append("byte[length=");
/*  85 */         builder.append(eachElementArr.length);
/*  86 */         builder.append(", [");
/*  87 */         for (int i = 0; i < l; i++) {
/*  88 */           builder.append(eachElementArr[i]);
/*  89 */           builder.append(", ");
/*     */         } 
/*     */         
/*  92 */         builder.append("...]]");
/*     */       } else {
/*  94 */         builder.append(eachElement.toString());
/*     */       } 
/*  96 */       builder.append(',');
/*     */     } 
/*  98 */     builder.append("]");
/*  99 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 103 */     Message m = new Message();
/* 104 */     m.add(new byte[666]);
/* 105 */     System.out.println(m);
/*     */   }
/*     */ }


/* Location:              D:\Projects\MScInComputerScience\Thesis\Backup\msc_thesis\notes\protocolo_mfap\prototipo_softplan\MultifactorAuthProtocol-1.0-beta.jar!\softplan\prototype\common\Message.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */