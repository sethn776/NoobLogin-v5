/*   1:    */ package org.apache.commons.codec.digest;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.security.MessageDigest;
/*   6:    */ import java.security.NoSuchAlgorithmException;
/*   7:    */ import org.apache.commons.codec.binary.Hex;
/*   8:    */ import org.apache.commons.codec.binary.StringUtils;
/*   9:    */ 
/*  10:    */ public class DigestUtils
/*  11:    */ {
/*  12:    */   private static final int STREAM_BUFFER_LENGTH = 1024;
/*  13:    */   
/*  14:    */   private static byte[] digest(MessageDigest digest, InputStream data)
/*  15:    */     throws IOException
/*  16:    */   {
/*  17: 50 */     return updateDigest(digest, data).digest();
/*  18:    */   }
/*  19:    */   
/*  20:    */   public static MessageDigest getDigest(String algorithm)
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 68 */       return MessageDigest.getInstance(algorithm);
/*  25:    */     }
/*  26:    */     catch (NoSuchAlgorithmException e)
/*  27:    */     {
/*  28: 70 */       throw new IllegalArgumentException(e);
/*  29:    */     }
/*  30:    */   }
/*  31:    */   
/*  32:    */   public static MessageDigest getMd2Digest()
/*  33:    */   {
/*  34: 85 */     return getDigest("MD2");
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static MessageDigest getMd5Digest()
/*  38:    */   {
/*  39: 98 */     return getDigest("MD5");
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static MessageDigest getSha1Digest()
/*  43:    */   {
/*  44:112 */     return getDigest("SHA-1");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static MessageDigest getSha256Digest()
/*  48:    */   {
/*  49:128 */     return getDigest("SHA-256");
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static MessageDigest getSha384Digest()
/*  53:    */   {
/*  54:144 */     return getDigest("SHA-384");
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static MessageDigest getSha512Digest()
/*  58:    */   {
/*  59:160 */     return getDigest("SHA-512");
/*  60:    */   }
/*  61:    */   
/*  62:    */   @Deprecated
/*  63:    */   public static MessageDigest getShaDigest()
/*  64:    */   {
/*  65:173 */     return getSha1Digest();
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static byte[] md2(byte[] data)
/*  69:    */   {
/*  70:185 */     return getMd2Digest().digest(data);
/*  71:    */   }
/*  72:    */   
/*  73:    */   public static byte[] md2(InputStream data)
/*  74:    */     throws IOException
/*  75:    */   {
/*  76:199 */     return digest(getMd2Digest(), data);
/*  77:    */   }
/*  78:    */   
/*  79:    */   public static byte[] md2(String data)
/*  80:    */   {
/*  81:211 */     return md2(StringUtils.getBytesUtf8(data));
/*  82:    */   }
/*  83:    */   
/*  84:    */   public static String md2Hex(byte[] data)
/*  85:    */   {
/*  86:223 */     return Hex.encodeHexString(md2(data));
/*  87:    */   }
/*  88:    */   
/*  89:    */   public static String md2Hex(InputStream data)
/*  90:    */     throws IOException
/*  91:    */   {
/*  92:237 */     return Hex.encodeHexString(md2(data));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public static String md2Hex(String data)
/*  96:    */   {
/*  97:249 */     return Hex.encodeHexString(md2(data));
/*  98:    */   }
/*  99:    */   
/* 100:    */   public static byte[] md5(byte[] data)
/* 101:    */   {
/* 102:260 */     return getMd5Digest().digest(data);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public static byte[] md5(InputStream data)
/* 106:    */     throws IOException
/* 107:    */   {
/* 108:274 */     return digest(getMd5Digest(), data);
/* 109:    */   }
/* 110:    */   
/* 111:    */   public static byte[] md5(String data)
/* 112:    */   {
/* 113:285 */     return md5(StringUtils.getBytesUtf8(data));
/* 114:    */   }
/* 115:    */   
/* 116:    */   public static String md5Hex(byte[] data)
/* 117:    */   {
/* 118:296 */     return Hex.encodeHexString(md5(data));
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static String md5Hex(InputStream data)
/* 122:    */     throws IOException
/* 123:    */   {
/* 124:310 */     return Hex.encodeHexString(md5(data));
/* 125:    */   }
/* 126:    */   
/* 127:    */   public static String md5Hex(String data)
/* 128:    */   {
/* 129:321 */     return Hex.encodeHexString(md5(data));
/* 130:    */   }
/* 131:    */   
/* 132:    */   @Deprecated
/* 133:    */   public static byte[] sha(byte[] data)
/* 134:    */   {
/* 135:334 */     return sha1(data);
/* 136:    */   }
/* 137:    */   
/* 138:    */   @Deprecated
/* 139:    */   public static byte[] sha(InputStream data)
/* 140:    */     throws IOException
/* 141:    */   {
/* 142:350 */     return sha1(data);
/* 143:    */   }
/* 144:    */   
/* 145:    */   @Deprecated
/* 146:    */   public static byte[] sha(String data)
/* 147:    */   {
/* 148:363 */     return sha1(data);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public static byte[] sha1(byte[] data)
/* 152:    */   {
/* 153:375 */     return getSha1Digest().digest(data);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static byte[] sha1(InputStream data)
/* 157:    */     throws IOException
/* 158:    */   {
/* 159:389 */     return digest(getSha1Digest(), data);
/* 160:    */   }
/* 161:    */   
/* 162:    */   public static byte[] sha1(String data)
/* 163:    */   {
/* 164:400 */     return sha1(StringUtils.getBytesUtf8(data));
/* 165:    */   }
/* 166:    */   
/* 167:    */   public static String sha1Hex(byte[] data)
/* 168:    */   {
/* 169:412 */     return Hex.encodeHexString(sha1(data));
/* 170:    */   }
/* 171:    */   
/* 172:    */   public static String sha1Hex(InputStream data)
/* 173:    */     throws IOException
/* 174:    */   {
/* 175:426 */     return Hex.encodeHexString(sha1(data));
/* 176:    */   }
/* 177:    */   
/* 178:    */   public static String sha1Hex(String data)
/* 179:    */   {
/* 180:438 */     return Hex.encodeHexString(sha1(data));
/* 181:    */   }
/* 182:    */   
/* 183:    */   public static byte[] sha256(byte[] data)
/* 184:    */   {
/* 185:453 */     return getSha256Digest().digest(data);
/* 186:    */   }
/* 187:    */   
/* 188:    */   public static byte[] sha256(InputStream data)
/* 189:    */     throws IOException
/* 190:    */   {
/* 191:470 */     return digest(getSha256Digest(), data);
/* 192:    */   }
/* 193:    */   
/* 194:    */   public static byte[] sha256(String data)
/* 195:    */   {
/* 196:485 */     return sha256(StringUtils.getBytesUtf8(data));
/* 197:    */   }
/* 198:    */   
/* 199:    */   public static String sha256Hex(byte[] data)
/* 200:    */   {
/* 201:500 */     return Hex.encodeHexString(sha256(data));
/* 202:    */   }
/* 203:    */   
/* 204:    */   public static String sha256Hex(InputStream data)
/* 205:    */     throws IOException
/* 206:    */   {
/* 207:517 */     return Hex.encodeHexString(sha256(data));
/* 208:    */   }
/* 209:    */   
/* 210:    */   public static String sha256Hex(String data)
/* 211:    */   {
/* 212:532 */     return Hex.encodeHexString(sha256(data));
/* 213:    */   }
/* 214:    */   
/* 215:    */   public static byte[] sha384(byte[] data)
/* 216:    */   {
/* 217:547 */     return getSha384Digest().digest(data);
/* 218:    */   }
/* 219:    */   
/* 220:    */   public static byte[] sha384(InputStream data)
/* 221:    */     throws IOException
/* 222:    */   {
/* 223:564 */     return digest(getSha384Digest(), data);
/* 224:    */   }
/* 225:    */   
/* 226:    */   public static byte[] sha384(String data)
/* 227:    */   {
/* 228:579 */     return sha384(StringUtils.getBytesUtf8(data));
/* 229:    */   }
/* 230:    */   
/* 231:    */   public static String sha384Hex(byte[] data)
/* 232:    */   {
/* 233:594 */     return Hex.encodeHexString(sha384(data));
/* 234:    */   }
/* 235:    */   
/* 236:    */   public static String sha384Hex(InputStream data)
/* 237:    */     throws IOException
/* 238:    */   {
/* 239:611 */     return Hex.encodeHexString(sha384(data));
/* 240:    */   }
/* 241:    */   
/* 242:    */   public static String sha384Hex(String data)
/* 243:    */   {
/* 244:626 */     return Hex.encodeHexString(sha384(data));
/* 245:    */   }
/* 246:    */   
/* 247:    */   public static byte[] sha512(byte[] data)
/* 248:    */   {
/* 249:641 */     return getSha512Digest().digest(data);
/* 250:    */   }
/* 251:    */   
/* 252:    */   public static byte[] sha512(InputStream data)
/* 253:    */     throws IOException
/* 254:    */   {
/* 255:658 */     return digest(getSha512Digest(), data);
/* 256:    */   }
/* 257:    */   
/* 258:    */   public static byte[] sha512(String data)
/* 259:    */   {
/* 260:673 */     return sha512(StringUtils.getBytesUtf8(data));
/* 261:    */   }
/* 262:    */   
/* 263:    */   public static String sha512Hex(byte[] data)
/* 264:    */   {
/* 265:688 */     return Hex.encodeHexString(sha512(data));
/* 266:    */   }
/* 267:    */   
/* 268:    */   public static String sha512Hex(InputStream data)
/* 269:    */     throws IOException
/* 270:    */   {
/* 271:705 */     return Hex.encodeHexString(sha512(data));
/* 272:    */   }
/* 273:    */   
/* 274:    */   public static String sha512Hex(String data)
/* 275:    */   {
/* 276:720 */     return Hex.encodeHexString(sha512(data));
/* 277:    */   }
/* 278:    */   
/* 279:    */   @Deprecated
/* 280:    */   public static String shaHex(byte[] data)
/* 281:    */   {
/* 282:733 */     return sha1Hex(data);
/* 283:    */   }
/* 284:    */   
/* 285:    */   @Deprecated
/* 286:    */   public static String shaHex(InputStream data)
/* 287:    */     throws IOException
/* 288:    */   {
/* 289:749 */     return sha1Hex(data);
/* 290:    */   }
/* 291:    */   
/* 292:    */   @Deprecated
/* 293:    */   public static String shaHex(String data)
/* 294:    */   {
/* 295:762 */     return sha1Hex(data);
/* 296:    */   }
/* 297:    */   
/* 298:    */   public static MessageDigest updateDigest(MessageDigest messageDigest, byte[] valueToDigest)
/* 299:    */   {
/* 300:776 */     messageDigest.update(valueToDigest);
/* 301:777 */     return messageDigest;
/* 302:    */   }
/* 303:    */   
/* 304:    */   public static MessageDigest updateDigest(MessageDigest digest, InputStream data)
/* 305:    */     throws IOException
/* 306:    */   {
/* 307:793 */     byte[] buffer = new byte[1024];
/* 308:794 */     int read = data.read(buffer, 0, 1024);
/* 309:796 */     while (read > -1)
/* 310:    */     {
/* 311:797 */       digest.update(buffer, 0, read);
/* 312:798 */       read = data.read(buffer, 0, 1024);
/* 313:    */     }
/* 314:801 */     return digest;
/* 315:    */   }
/* 316:    */   
/* 317:    */   public static MessageDigest updateDigest(MessageDigest messageDigest, String valueToDigest)
/* 318:    */   {
/* 319:816 */     messageDigest.update(StringUtils.getBytesUtf8(valueToDigest));
/* 320:817 */     return messageDigest;
/* 321:    */   }
/* 322:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.digest.DigestUtils
 * JD-Core Version:    0.7.0.1
 */