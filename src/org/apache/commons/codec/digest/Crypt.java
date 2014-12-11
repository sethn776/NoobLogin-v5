/*   1:    */ package org.apache.commons.codec.digest;
/*   2:    */ 
/*   3:    */ import org.apache.commons.codec.Charsets;
/*   4:    */ 
/*   5:    */ public class Crypt
/*   6:    */ {
/*   7:    */   public static String crypt(byte[] keyBytes)
/*   8:    */   {
/*   9: 46 */     return crypt(keyBytes, null);
/*  10:    */   }
/*  11:    */   
/*  12:    */   public static String crypt(byte[] keyBytes, String salt)
/*  13:    */   {
/*  14: 66 */     if (salt == null) {
/*  15: 67 */       return Sha2Crypt.sha512Crypt(keyBytes);
/*  16:    */     }
/*  17: 68 */     if (salt.startsWith("$6$")) {
/*  18: 69 */       return Sha2Crypt.sha512Crypt(keyBytes, salt);
/*  19:    */     }
/*  20: 70 */     if (salt.startsWith("$5$")) {
/*  21: 71 */       return Sha2Crypt.sha256Crypt(keyBytes, salt);
/*  22:    */     }
/*  23: 72 */     if (salt.startsWith("$1$")) {
/*  24: 73 */       return Md5Crypt.md5Crypt(keyBytes, salt);
/*  25:    */     }
/*  26: 75 */     return UnixCrypt.crypt(keyBytes, salt);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static String crypt(String key)
/*  30:    */   {
/*  31: 92 */     return crypt(key, null);
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static String crypt(String key, String salt)
/*  35:    */   {
/*  36:149 */     return crypt(key.getBytes(Charsets.UTF_8), salt);
/*  37:    */   }
/*  38:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.digest.Crypt
 * JD-Core Version:    0.7.0.1
 */