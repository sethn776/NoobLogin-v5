/*   1:    */ package org.apache.commons.codec.digest;
/*   2:    */ 
/*   3:    */ import java.security.MessageDigest;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.regex.Matcher;
/*   6:    */ import java.util.regex.Pattern;
/*   7:    */ import org.apache.commons.codec.Charsets;
/*   8:    */ 
/*   9:    */ public class Md5Crypt
/*  10:    */ {
/*  11:    */   static final String APR1_PREFIX = "$apr1$";
/*  12:    */   private static final int BLOCKSIZE = 16;
/*  13:    */   static final String MD5_PREFIX = "$1$";
/*  14:    */   private static final int ROUNDS = 1000;
/*  15:    */   
/*  16:    */   public static String apr1Crypt(byte[] keyBytes)
/*  17:    */   {
/*  18: 72 */     return apr1Crypt(keyBytes, "$apr1$" + B64.getRandomSalt(8));
/*  19:    */   }
/*  20:    */   
/*  21:    */   public static String apr1Crypt(byte[] keyBytes, String salt)
/*  22:    */   {
/*  23: 89 */     if ((salt != null) && (!salt.startsWith("$apr1$"))) {
/*  24: 90 */       salt = "$apr1$" + salt;
/*  25:    */     }
/*  26: 92 */     return md5Crypt(keyBytes, salt, "$apr1$");
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static String apr1Crypt(String keyBytes)
/*  30:    */   {
/*  31:105 */     return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8));
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static String apr1Crypt(String keyBytes, String salt)
/*  35:    */   {
/*  36:126 */     return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8), salt);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public static String md5Crypt(byte[] keyBytes)
/*  40:    */   {
/*  41:141 */     return md5Crypt(keyBytes, "$1$" + B64.getRandomSalt(8));
/*  42:    */   }
/*  43:    */   
/*  44:    */   public static String md5Crypt(byte[] keyBytes, String salt)
/*  45:    */   {
/*  46:161 */     return md5Crypt(keyBytes, salt, "$1$");
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static String md5Crypt(byte[] keyBytes, String salt, String prefix)
/*  50:    */   {
/*  51:180 */     int keyLen = keyBytes.length;
/*  52:    */     String saltString;
/*  54:184 */     if (salt == null)
/*  55:    */     {
/*  56:185 */       saltString = B64.getRandomSalt(8);
/*  57:    */     }
/*  58:    */     else
/*  59:    */     {
/*  60:187 */       Pattern p = Pattern.compile("^" + prefix.replace("$", "\\$") + "([\\.\\/a-zA-Z0-9]{1,8}).*");
/*  61:188 */       Matcher m = p.matcher(salt);
/*  62:189 */       if ((m == null) || (!m.find())) {
/*  63:190 */         throw new IllegalArgumentException("Invalid salt value: " + salt);
/*  64:    */       }
/*  65:192 */       saltString = m.group(1);
/*  66:    */     }
/*  67:194 */     byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
/*  68:    */     
/*  69:196 */     MessageDigest ctx = DigestUtils.getMd5Digest();
/*  70:    */     
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:201 */     ctx.update(keyBytes);
/*  75:    */     
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:206 */     ctx.update(prefix.getBytes(Charsets.UTF_8));
/*  80:    */     
/*  81:    */ 
/*  82:    */ 
/*  83:    */ 
/*  84:211 */     ctx.update(saltBytes);
/*  85:    */     
/*  86:    */ 
/*  87:    */ 
/*  88:    */ 
/*  89:216 */     MessageDigest ctx1 = DigestUtils.getMd5Digest();
/*  90:217 */     ctx1.update(keyBytes);
/*  91:218 */     ctx1.update(saltBytes);
/*  92:219 */     ctx1.update(keyBytes);
/*  93:220 */     byte[] finalb = ctx1.digest();
/*  94:221 */     int ii = keyLen;
/*  95:222 */     while (ii > 0)
/*  96:    */     {
/*  97:223 */       ctx.update(finalb, 0, ii > 16 ? 16 : ii);
/*  98:224 */       ii -= 16;
/*  99:    */     }
/* 100:230 */     Arrays.fill(finalb, (byte)0);
/* 101:    */     
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:235 */     ii = keyLen;
/* 106:236 */     int j = 0;
/* 107:237 */     while (ii > 0)
/* 108:    */     {
/* 109:238 */       if ((ii & 0x1) == 1) {
/* 110:239 */         ctx.update(finalb[0]);
/* 111:    */       } else {
/* 112:241 */         ctx.update(keyBytes[0]);
/* 113:    */       }
/* 114:243 */       ii >>= 1;
/* 115:    */     }
/* 116:249 */     StringBuilder passwd = new StringBuilder(prefix + saltString + "$");
/* 117:250 */     finalb = ctx.digest();
/* 118:256 */     for (int i = 0; i < 1000; i++)
/* 119:    */     {
/* 120:257 */       ctx1 = DigestUtils.getMd5Digest();
/* 121:258 */       if ((i & 0x1) != 0) {
/* 122:259 */         ctx1.update(keyBytes);
/* 123:    */       } else {
/* 124:261 */         ctx1.update(finalb, 0, 16);
/* 125:    */       }
/* 126:264 */       if (i % 3 != 0) {
/* 127:265 */         ctx1.update(saltBytes);
/* 128:    */       }
/* 129:268 */       if (i % 7 != 0) {
/* 130:269 */         ctx1.update(keyBytes);
/* 131:    */       }
/* 132:272 */       if ((i & 0x1) != 0) {
/* 133:273 */         ctx1.update(finalb, 0, 16);
/* 134:    */       } else {
/* 135:275 */         ctx1.update(keyBytes);
/* 136:    */       }
/* 137:277 */       finalb = ctx1.digest();
/* 138:    */     }
/* 139:283 */     B64.b64from24bit(finalb[0], finalb[6], finalb[12], 4, passwd);
/* 140:284 */     B64.b64from24bit(finalb[1], finalb[7], finalb[13], 4, passwd);
/* 141:285 */     B64.b64from24bit(finalb[2], finalb[8], finalb[14], 4, passwd);
/* 142:286 */     B64.b64from24bit(finalb[3], finalb[9], finalb[15], 4, passwd);
/* 143:287 */     B64.b64from24bit(finalb[4], finalb[10], finalb[5], 4, passwd);
/* 144:288 */     B64.b64from24bit((byte)0, (byte)0, finalb[11], 2, passwd);
/* 145:    */     
/* 146:    */ 
/* 147:    */ 
/* 148:    */ 
/* 149:    */ 
/* 150:294 */     ctx.reset();
/* 151:295 */     ctx1.reset();
/* 152:296 */     Arrays.fill(keyBytes, (byte)0);
/* 153:297 */     Arrays.fill(saltBytes, (byte)0);
/* 154:298 */     Arrays.fill(finalb, (byte)0);
/* 155:    */     
/* 156:300 */     return passwd.toString();
/* 157:    */   }
/* 158:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.digest.Md5Crypt
 * JD-Core Version:    0.7.0.1
 */