/*   1:    */ package org.apache.commons.codec.digest;
/*   2:    */ 
/*   3:    */ import java.security.MessageDigest;
/*   4:    */ import java.util.Arrays;
/*   5:    */ import java.util.regex.Matcher;
/*   6:    */ import java.util.regex.Pattern;
/*   7:    */ import org.apache.commons.codec.Charsets;
/*   8:    */ 
/*   9:    */ public class Sha2Crypt
/*  10:    */ {
/*  11:    */   private static final int ROUNDS_DEFAULT = 5000;
/*  12:    */   private static final int ROUNDS_MAX = 999999999;
/*  13:    */   private static final int ROUNDS_MIN = 1000;
/*  14:    */   private static final String ROUNDS_PREFIX = "rounds=";
/*  15:    */   private static final int SHA256_BLOCKSIZE = 32;
/*  16:    */   static final String SHA256_PREFIX = "$5$";
/*  17:    */   private static final int SHA512_BLOCKSIZE = 64;
/*  18:    */   static final String SHA512_PREFIX = "$6$";
/*  19: 69 */   private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");
/*  20:    */   
/*  21:    */   public static String sha256Crypt(byte[] keyBytes)
/*  22:    */   {
/*  23: 83 */     return sha256Crypt(keyBytes, null);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public static String sha256Crypt(byte[] keyBytes, String salt)
/*  27:    */   {
/*  28:102 */     if (salt == null) {
/*  29:103 */       salt = "$5$" + B64.getRandomSalt(8);
/*  30:    */     }
/*  31:105 */     return sha2Crypt(keyBytes, salt, "$5$", 32, "SHA-256");
/*  32:    */   }
/*  33:    */   
/*  34:    */   private static String sha2Crypt(byte[] keyBytes, String salt, String saltPrefix, int blocksize, String algorithm)
/*  35:    */   {
/*  36:136 */     int keyLen = keyBytes.length;
/*  37:    */     
/*  38:    */ 
/*  39:139 */     int rounds = 5000;
/*  40:140 */     boolean roundsCustom = false;
/*  41:141 */     if (salt == null) {
/*  42:142 */       throw new IllegalArgumentException("Salt must not be null");
/*  43:    */     }
/*  44:145 */     Matcher m = SALT_PATTERN.matcher(salt);
/*  45:146 */     if ((m == null) || (!m.find())) {
/*  46:147 */       throw new IllegalArgumentException("Invalid salt value: " + salt);
/*  47:    */     }
/*  48:149 */     if (m.group(3) != null)
/*  49:    */     {
/*  50:150 */       rounds = Integer.parseInt(m.group(3));
/*  51:151 */       rounds = Math.max(1000, Math.min(999999999, rounds));
/*  52:152 */       roundsCustom = true;
/*  53:    */     }
/*  54:154 */     String saltString = m.group(4);
/*  55:155 */     byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
/*  56:156 */     int saltLen = saltBytes.length;
/*  57:    */     
/*  58:    */ 
/*  59:    */ 
/*  60:160 */     MessageDigest ctx = DigestUtils.getDigest(algorithm);
/*  61:    */     
/*  62:    */ 
/*  63:    */ 
/*  64:    */ 
/*  65:    */ 
/*  66:166 */     ctx.update(keyBytes);
/*  67:    */     
/*  68:    */ 
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:    */ 
/*  79:    */ 
/*  80:    */ 
/*  81:181 */     ctx.update(saltBytes);
/*  82:    */     
/*  83:    */ 
/*  84:    */ 
/*  85:    */ 
/*  86:    */ 
/*  87:    */ 
/*  88:188 */     MessageDigest altCtx = DigestUtils.getDigest(algorithm);
/*  89:    */     
/*  90:    */ 
/*  91:    */ 
/*  92:    */ 
/*  93:    */ 
/*  94:194 */     altCtx.update(keyBytes);
/*  95:    */     
/*  96:    */ 
/*  97:    */ 
/*  98:    */ 
/*  99:    */ 
/* 100:200 */     altCtx.update(saltBytes);
/* 101:    */     
/* 102:    */ 
/* 103:    */ 
/* 104:    */ 
/* 105:    */ 
/* 106:206 */     altCtx.update(keyBytes);
/* 107:    */     
/* 108:    */ 
/* 109:    */ 
/* 110:    */ 
/* 111:    */ 
/* 112:212 */     byte[] altResult = altCtx.digest();
/* 113:    */     
/* 114:    */ 
/* 115:    */ 
/* 116:    */ 
/* 117:    */ 
/* 118:    */ 
/* 119:    */ 
/* 120:    */ 
/* 121:    */ 
/* 122:222 */     int cnt = keyBytes.length;
/* 123:223 */     while (cnt > blocksize)
/* 124:    */     {
/* 125:224 */       ctx.update(altResult, 0, blocksize);
/* 126:225 */       cnt -= blocksize;
/* 127:    */     }
/* 128:230 */     ctx.update(altResult, 0, cnt);
/* 129:    */     
/* 130:    */ 
/* 131:    */ 
/* 132:    */ 
/* 133:    */ 
/* 134:    */ 
/* 135:    */ 
/* 136:    */ 
/* 137:    */ 
/* 138:    */ 
/* 139:    */ 
/* 140:    */ 
/* 141:    */ 
/* 142:    */ 
/* 143:    */ 
/* 144:246 */     cnt = keyBytes.length;
/* 145:247 */     while (cnt > 0)
/* 146:    */     {
/* 147:248 */       if ((cnt & 0x1) != 0) {
/* 148:249 */         ctx.update(altResult, 0, blocksize);
/* 149:    */       } else {
/* 150:251 */         ctx.update(keyBytes);
/* 151:    */       }
/* 152:253 */       cnt >>= 1;
/* 153:    */     }
/* 154:260 */     altResult = ctx.digest();
/* 155:    */     
/* 156:    */ 
/* 157:    */ 
/* 158:    */ 
/* 159:    */ 
/* 160:266 */     altCtx = DigestUtils.getDigest(algorithm);
/* 161:275 */     for (int i = 1; i <= keyLen; i++) {
/* 162:276 */       altCtx.update(keyBytes);
/* 163:    */     }
/* 164:283 */     byte[] tempResult = altCtx.digest();
/* 165:    */     
/* 166:    */ 
/* 167:    */ 
/* 168:    */ 
/* 169:    */ 
/* 170:    */ 
/* 171:    */ 
/* 172:    */ 
/* 173:    */ 
/* 174:    */ 
/* 175:    */ 
/* 176:295 */     byte[] pBytes = new byte[keyLen];
/* 177:296 */     int cp = 0;
/* 178:297 */     while (cp < keyLen - blocksize)
/* 179:    */     {
/* 180:298 */       System.arraycopy(tempResult, 0, pBytes, cp, blocksize);
/* 181:299 */       cp += blocksize;
/* 182:    */     }
/* 183:301 */     System.arraycopy(tempResult, 0, pBytes, cp, keyLen - cp);
/* 184:    */     
/* 185:    */ 
/* 186:    */ 
/* 187:    */ 
/* 188:    */ 
/* 189:307 */     altCtx = DigestUtils.getDigest(algorithm);
/* 190:316 */     for (int i = 1; i <= 16 + (altResult[0] & 0xFF); i++) {
/* 191:317 */       altCtx.update(saltBytes);
/* 192:    */     }
/* 193:324 */     tempResult = altCtx.digest();
/* 194:    */     
/* 195:    */ 
/* 196:    */ 
/* 197:    */ 
/* 198:    */ 
/* 199:    */ 
/* 200:    */ 
/* 201:    */ 
/* 202:    */ 
/* 203:    */ 
/* 204:    */ 
/* 205:    */ 
/* 206:337 */     byte[] sBytes = new byte[saltLen];
/* 207:338 */     cp = 0;
/* 208:339 */     while (cp < saltLen - blocksize)
/* 209:    */     {
/* 210:340 */       System.arraycopy(tempResult, 0, sBytes, cp, blocksize);
/* 211:341 */       cp += blocksize;
/* 212:    */     }
/* 213:343 */     System.arraycopy(tempResult, 0, sBytes, cp, saltLen - cp);
/* 214:356 */     for (int i = 0; i <= rounds - 1; i++)
/* 215:    */     {
/* 216:361 */       ctx = DigestUtils.getDigest(algorithm);
/* 217:368 */       if ((i & 0x1) != 0) {
/* 218:369 */         ctx.update(pBytes, 0, keyLen);
/* 219:    */       } else {
/* 220:371 */         ctx.update(altResult, 0, blocksize);
/* 221:    */       }
/* 222:378 */       if (i % 3 != 0) {
/* 223:379 */         ctx.update(sBytes, 0, saltLen);
/* 224:    */       }
/* 225:386 */       if (i % 7 != 0) {
/* 226:387 */         ctx.update(pBytes, 0, keyLen);
/* 227:    */       }
/* 228:395 */       if ((i & 0x1) != 0) {
/* 229:396 */         ctx.update(altResult, 0, blocksize);
/* 230:    */       } else {
/* 231:398 */         ctx.update(pBytes, 0, keyLen);
/* 232:    */       }
/* 233:405 */       altResult = ctx.digest();
/* 234:    */     }
/* 235:423 */     StringBuilder buffer = new StringBuilder(saltPrefix);
/* 236:424 */     if (roundsCustom)
/* 237:    */     {
/* 238:425 */       buffer.append("rounds=");
/* 239:426 */       buffer.append(rounds);
/* 240:427 */       buffer.append("$");
/* 241:    */     }
/* 242:429 */     buffer.append(saltString);
/* 243:430 */     buffer.append("$");
/* 244:456 */     if (blocksize == 32)
/* 245:    */     {
/* 246:457 */       B64.b64from24bit(altResult[0], altResult[10], altResult[20], 4, buffer);
/* 247:458 */       B64.b64from24bit(altResult[21], altResult[1], altResult[11], 4, buffer);
/* 248:459 */       B64.b64from24bit(altResult[12], altResult[22], altResult[2], 4, buffer);
/* 249:460 */       B64.b64from24bit(altResult[3], altResult[13], altResult[23], 4, buffer);
/* 250:461 */       B64.b64from24bit(altResult[24], altResult[4], altResult[14], 4, buffer);
/* 251:462 */       B64.b64from24bit(altResult[15], altResult[25], altResult[5], 4, buffer);
/* 252:463 */       B64.b64from24bit(altResult[6], altResult[16], altResult[26], 4, buffer);
/* 253:464 */       B64.b64from24bit(altResult[27], altResult[7], altResult[17], 4, buffer);
/* 254:465 */       B64.b64from24bit(altResult[18], altResult[28], altResult[8], 4, buffer);
/* 255:466 */       B64.b64from24bit(altResult[9], altResult[19], altResult[29], 4, buffer);
/* 256:467 */       B64.b64from24bit((byte)0, altResult[31], altResult[30], 3, buffer);
/* 257:    */     }
/* 258:    */     else
/* 259:    */     {
/* 260:469 */       B64.b64from24bit(altResult[0], altResult[21], altResult[42], 4, buffer);
/* 261:470 */       B64.b64from24bit(altResult[22], altResult[43], altResult[1], 4, buffer);
/* 262:471 */       B64.b64from24bit(altResult[44], altResult[2], altResult[23], 4, buffer);
/* 263:472 */       B64.b64from24bit(altResult[3], altResult[24], altResult[45], 4, buffer);
/* 264:473 */       B64.b64from24bit(altResult[25], altResult[46], altResult[4], 4, buffer);
/* 265:474 */       B64.b64from24bit(altResult[47], altResult[5], altResult[26], 4, buffer);
/* 266:475 */       B64.b64from24bit(altResult[6], altResult[27], altResult[48], 4, buffer);
/* 267:476 */       B64.b64from24bit(altResult[28], altResult[49], altResult[7], 4, buffer);
/* 268:477 */       B64.b64from24bit(altResult[50], altResult[8], altResult[29], 4, buffer);
/* 269:478 */       B64.b64from24bit(altResult[9], altResult[30], altResult[51], 4, buffer);
/* 270:479 */       B64.b64from24bit(altResult[31], altResult[52], altResult[10], 4, buffer);
/* 271:480 */       B64.b64from24bit(altResult[53], altResult[11], altResult[32], 4, buffer);
/* 272:481 */       B64.b64from24bit(altResult[12], altResult[33], altResult[54], 4, buffer);
/* 273:482 */       B64.b64from24bit(altResult[34], altResult[55], altResult[13], 4, buffer);
/* 274:483 */       B64.b64from24bit(altResult[56], altResult[14], altResult[35], 4, buffer);
/* 275:484 */       B64.b64from24bit(altResult[15], altResult[36], altResult[57], 4, buffer);
/* 276:485 */       B64.b64from24bit(altResult[37], altResult[58], altResult[16], 4, buffer);
/* 277:486 */       B64.b64from24bit(altResult[59], altResult[17], altResult[38], 4, buffer);
/* 278:487 */       B64.b64from24bit(altResult[18], altResult[39], altResult[60], 4, buffer);
/* 279:488 */       B64.b64from24bit(altResult[40], altResult[61], altResult[19], 4, buffer);
/* 280:489 */       B64.b64from24bit(altResult[62], altResult[20], altResult[41], 4, buffer);
/* 281:490 */       B64.b64from24bit((byte)0, (byte)0, altResult[63], 2, buffer);
/* 282:    */     }
/* 283:498 */     Arrays.fill(tempResult, (byte)0);
/* 284:499 */     Arrays.fill(pBytes, (byte)0);
/* 285:500 */     Arrays.fill(sBytes, (byte)0);
/* 286:501 */     ctx.reset();
/* 287:502 */     altCtx.reset();
/* 288:503 */     Arrays.fill(keyBytes, (byte)0);
/* 289:504 */     Arrays.fill(saltBytes, (byte)0);
/* 290:    */     
/* 291:506 */     return buffer.toString();
/* 292:    */   }
/* 293:    */   
/* 294:    */   public static String sha512Crypt(byte[] keyBytes)
/* 295:    */   {
/* 296:521 */     return sha512Crypt(keyBytes, null);
/* 297:    */   }
/* 298:    */   
/* 299:    */   public static String sha512Crypt(byte[] keyBytes, String salt)
/* 300:    */   {
/* 301:540 */     if (salt == null) {
/* 302:541 */       salt = "$6$" + B64.getRandomSalt(8);
/* 303:    */     }
/* 304:543 */     return sha2Crypt(keyBytes, salt, "$6$", 64, "SHA-512");
/* 305:    */   }
/* 306:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.digest.Sha2Crypt
 * JD-Core Version:    0.7.0.1
 */