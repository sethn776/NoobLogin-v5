/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.math.BigInteger;
/*   4:    */ 
/*   5:    */ public class Base64
/*   6:    */   extends BaseNCodec
/*   7:    */ {
/*   8:    */   private static final int BITS_PER_ENCODED_BYTE = 6;
/*   9:    */   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
/*  10:    */   private static final int BYTES_PER_ENCODED_BLOCK = 4;
/*  11: 71 */   static final byte[] CHUNK_SEPARATOR = { 13, 10 };
/*  12: 80 */   private static final byte[] STANDARD_ENCODE_TABLE = {
/*  13: 81 */     65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 
/*  14: 82 */     78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 
/*  15: 83 */     97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 
/*  16: 84 */     110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 
/*  17: 85 */     48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*  18: 93 */   private static final byte[] URL_SAFE_ENCODE_TABLE = {
/*  19: 94 */     65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 
/*  20: 95 */     78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 
/*  21: 96 */     97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 
/*  22: 97 */     110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 
/*  23: 98 */     48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
/*  24:112 */   private static final byte[] DECODE_TABLE = {
/*  25:113 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  26:114 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  27:115 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 
/*  28:116 */     55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 
/*  29:117 */     5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 
/*  30:118 */     24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 
/*  31:119 */     35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*  32:    */   private static final int MASK_6BITS = 63;
/*  33:    */   private final byte[] encodeTable;
/*  34:140 */   private final byte[] decodeTable = DECODE_TABLE;
/*  35:    */   private final byte[] lineSeparator;
/*  36:    */   private final int decodeSize;
/*  37:    */   private final int encodeSize;
/*  38:    */   
/*  39:    */   public Base64()
/*  40:    */   {
/*  41:170 */     this(0);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Base64(boolean urlSafe)
/*  45:    */   {
/*  46:189 */     this(76, CHUNK_SEPARATOR, urlSafe);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Base64(int lineLength)
/*  50:    */   {
/*  51:212 */     this(lineLength, CHUNK_SEPARATOR);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Base64(int lineLength, byte[] lineSeparator)
/*  55:    */   {
/*  56:239 */     this(lineLength, lineSeparator, false);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe)
/*  60:    */   {
/*  61:272 */     super(3, 4, lineLength, lineSeparator == null ? 0 : lineSeparator.length);
/*  62:275 */     if (lineSeparator != null)
/*  63:    */     {
/*  64:276 */       if (containsAlphabetOrPad(lineSeparator))
/*  65:    */       {
/*  66:277 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/*  67:278 */         throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + sep + "]");
/*  68:    */       }
/*  69:280 */       if (lineLength > 0)
/*  70:    */       {
/*  71:281 */         this.encodeSize = (4 + lineSeparator.length);
/*  72:282 */         this.lineSeparator = new byte[lineSeparator.length];
/*  73:283 */         System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*  74:    */       }
/*  75:    */       else
/*  76:    */       {
/*  77:285 */         this.encodeSize = 4;
/*  78:286 */         this.lineSeparator = null;
/*  79:    */       }
/*  80:    */     }
/*  81:    */     else
/*  82:    */     {
/*  83:289 */       this.encodeSize = 4;
/*  84:290 */       this.lineSeparator = null;
/*  85:    */     }
/*  86:292 */     this.decodeSize = (this.encodeSize - 1);
/*  87:293 */     this.encodeTable = (urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE);
/*  88:    */   }
/*  89:    */   
/*  90:    */   public boolean isUrlSafe()
/*  91:    */   {
/*  92:303 */     return this.encodeTable == URL_SAFE_ENCODE_TABLE;
/*  93:    */   }
/*  94:    */   
/*  95:    */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
/*  96:    */   {
/*  97:329 */     if (context.eof) {
/*  98:330 */       return;
/*  99:    */     }
/* 100:334 */     if (inAvail < 0)
/* 101:    */     {
/* 102:335 */       context.eof = true;
/* 103:336 */       if ((context.modulus == 0) && (this.lineLength == 0)) {
/* 104:337 */         return;
/* 105:    */       }
/* 106:339 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 107:340 */       int savedPos = context.pos;
/* 108:341 */       switch (context.modulus)
/* 109:    */       {
/* 110:    */       case 0: 
/* 111:    */         break;
/* 112:    */       case 1: 
/* 113:346 */         buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 2 & 0x3F)];
/* 114:    */         
/* 115:348 */         buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea << 4 & 0x3F)];
/* 116:350 */         if (this.encodeTable == STANDARD_ENCODE_TABLE)
/* 117:    */         {
/* 118:351 */           buffer[(context.pos++)] = 61;
/* 119:352 */           buffer[(context.pos++)] = 61;
/* 120:    */         }
/* 121:354 */         break;
/* 122:    */       case 2: 
/* 123:357 */         buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 10 & 0x3F)];
/* 124:358 */         buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 4 & 0x3F)];
/* 125:359 */         buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea << 2 & 0x3F)];
/* 126:361 */         if (this.encodeTable == STANDARD_ENCODE_TABLE) {
/* 127:362 */           buffer[(context.pos++)] = 61;
/* 128:    */         }
/* 129:364 */         break;
/* 130:    */       default: 
/* 131:366 */         throw new IllegalStateException("Impossible modulus " + context.modulus);
/* 132:    */       }
/* 133:368 */       context.currentLinePos += context.pos - savedPos;
/* 134:370 */       if ((this.lineLength > 0) && (context.currentLinePos > 0))
/* 135:    */       {
/* 136:371 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 137:372 */         context.pos += this.lineSeparator.length;
/* 138:    */       }
/* 139:    */     }
/* 140:    */     else
/* 141:    */     {
/* 142:375 */       for (int i = 0; i < inAvail; i++)
/* 143:    */       {
/* 144:376 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 145:377 */         context.modulus = ((context.modulus + 1) % 3);
/* 146:378 */         int b = in[(inPos++)];
/* 147:379 */         if (b < 0) {
/* 148:380 */           b += 256;
/* 149:    */         }
/* 150:382 */         context.ibitWorkArea = ((context.ibitWorkArea << 8) + b);
/* 151:383 */         if (context.modulus == 0)
/* 152:    */         {
/* 153:384 */           buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 18 & 0x3F)];
/* 154:385 */           buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 12 & 0x3F)];
/* 155:386 */           buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea >> 6 & 0x3F)];
/* 156:387 */           buffer[(context.pos++)] = this.encodeTable[(context.ibitWorkArea & 0x3F)];
/* 157:388 */           context.currentLinePos += 4;
/* 158:389 */           if ((this.lineLength > 0) && (this.lineLength <= context.currentLinePos))
/* 159:    */           {
/* 160:390 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 161:391 */             context.pos += this.lineSeparator.length;
/* 162:392 */             context.currentLinePos = 0;
/* 163:    */           }
/* 164:    */         }
/* 165:    */       }
/* 166:    */     }
/* 167:    */   }
/* 168:    */   
/* 169:    */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
/* 170:    */   {
/* 171:426 */     if (context.eof) {
/* 172:427 */       return;
/* 173:    */     }
/* 174:429 */     if (inAvail < 0) {
/* 175:430 */       context.eof = true;
/* 176:    */     }
/* 177:432 */     for (int i = 0; i < inAvail; i++)
/* 178:    */     {
/* 179:433 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 180:434 */       byte b = in[(inPos++)];
/* 181:435 */       if (b == 61)
/* 182:    */       {
/* 183:437 */         context.eof = true;
/* 184:438 */         break;
/* 185:    */       }
/* 186:440 */       if ((b >= 0) && (b < DECODE_TABLE.length))
/* 187:    */       {
/* 188:441 */         int result = DECODE_TABLE[b];
/* 189:442 */         if (result >= 0)
/* 190:    */         {
/* 191:443 */           context.modulus = ((context.modulus + 1) % 4);
/* 192:444 */           context.ibitWorkArea = ((context.ibitWorkArea << 6) + result);
/* 193:445 */           if (context.modulus == 0)
/* 194:    */           {
/* 195:446 */             buffer[(context.pos++)] = ((byte)(context.ibitWorkArea >> 16 & 0xFF));
/* 196:447 */             buffer[(context.pos++)] = ((byte)(context.ibitWorkArea >> 8 & 0xFF));
/* 197:448 */             buffer[(context.pos++)] = ((byte)(context.ibitWorkArea & 0xFF));
/* 198:    */           }
/* 199:    */         }
/* 200:    */       }
/* 201:    */     }
/* 202:458 */     if ((context.eof) && (context.modulus != 0))
/* 203:    */     {
/* 204:459 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 205:463 */       switch (context.modulus)
/* 206:    */       {
/* 207:    */       case 1: 
/* 208:    */         break;
/* 209:    */       case 2: 
/* 210:469 */         context.ibitWorkArea >>= 4;
/* 211:470 */         buffer[(context.pos++)] = ((byte)(context.ibitWorkArea & 0xFF));
/* 212:471 */         break;
/* 213:    */       case 3: 
/* 214:473 */         context.ibitWorkArea >>= 2;
/* 215:474 */         buffer[(context.pos++)] = ((byte)(context.ibitWorkArea >> 8 & 0xFF));
/* 216:475 */         buffer[(context.pos++)] = ((byte)(context.ibitWorkArea & 0xFF));
/* 217:476 */         break;
/* 218:    */       default: 
/* 219:478 */         throw new IllegalStateException("Impossible modulus " + context.modulus);
/* 220:    */       }
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   @Deprecated
/* 225:    */   public static boolean isArrayByteBase64(byte[] arrayOctet)
/* 226:    */   {
/* 227:495 */     return isBase64(arrayOctet);
/* 228:    */   }
/* 229:    */   
/* 230:    */   public static boolean isBase64(byte octet)
/* 231:    */   {
/* 232:507 */     return (octet == 61) || ((octet >= 0) && (octet < DECODE_TABLE.length) && (DECODE_TABLE[octet] != -1));
/* 233:    */   }
/* 234:    */   
/* 235:    */   public static boolean isBase64(String base64)
/* 236:    */   {
/* 237:521 */     return isBase64(StringUtils.getBytesUtf8(base64));
/* 238:    */   }
/* 239:    */   
/* 240:    */   public static boolean isBase64(byte[] arrayOctet)
/* 241:    */   {
/* 242:535 */     for (int i = 0; i < arrayOctet.length; i++) {
/* 243:536 */       if ((!isBase64(arrayOctet[i])) && (!isWhiteSpace(arrayOctet[i]))) {
/* 244:537 */         return false;
/* 245:    */       }
/* 246:    */     }
/* 247:540 */     return true;
/* 248:    */   }
/* 249:    */   
/* 250:    */   public static byte[] encodeBase64(byte[] binaryData)
/* 251:    */   {
/* 252:551 */     return encodeBase64(binaryData, false);
/* 253:    */   }
/* 254:    */   
/* 255:    */   public static String encodeBase64String(byte[] binaryData)
/* 256:    */   {
/* 257:566 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false));
/* 258:    */   }
/* 259:    */   
/* 260:    */   public static byte[] encodeBase64URLSafe(byte[] binaryData)
/* 261:    */   {
/* 262:579 */     return encodeBase64(binaryData, false, true);
/* 263:    */   }
/* 264:    */   
/* 265:    */   public static String encodeBase64URLSafeString(byte[] binaryData)
/* 266:    */   {
/* 267:592 */     return StringUtils.newStringUtf8(encodeBase64(binaryData, false, true));
/* 268:    */   }
/* 269:    */   
/* 270:    */   public static byte[] encodeBase64Chunked(byte[] binaryData)
/* 271:    */   {
/* 272:603 */     return encodeBase64(binaryData, true);
/* 273:    */   }
/* 274:    */   
/* 275:    */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked)
/* 276:    */   {
/* 277:618 */     return encodeBase64(binaryData, isChunked, false);
/* 278:    */   }
/* 279:    */   
/* 280:    */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe)
/* 281:    */   {
/* 282:637 */     return encodeBase64(binaryData, isChunked, urlSafe, 2147483647);
/* 283:    */   }
/* 284:    */   
/* 285:    */   public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize)
/* 286:    */   {
/* 287:659 */     if ((binaryData == null) || (binaryData.length == 0)) {
/* 288:660 */       return binaryData;
/* 289:    */     }
/* 290:665 */     Base64 b64 = isChunked ? new Base64(urlSafe) : new Base64(0, CHUNK_SEPARATOR, urlSafe);
/* 291:666 */     long len = b64.getEncodedLength(binaryData);
/* 292:667 */     if (len > maxResultSize) {
/* 293:668 */       throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + 
/* 294:669 */         len + 
/* 295:670 */         ") than the specified maximum size of " + 
/* 296:671 */         maxResultSize);
/* 297:    */     }
/* 298:674 */     return b64.encode(binaryData);
/* 299:    */   }
/* 300:    */   
/* 301:    */   public static byte[] decodeBase64(String base64String)
/* 302:    */   {
/* 303:686 */     return new Base64().decode(base64String);
/* 304:    */   }
/* 305:    */   
/* 306:    */   public static byte[] decodeBase64(byte[] base64Data)
/* 307:    */   {
/* 308:697 */     return new Base64().decode(base64Data);
/* 309:    */   }
/* 310:    */   
/* 311:    */   public static BigInteger decodeInteger(byte[] pArray)
/* 312:    */   {
/* 313:712 */     return new BigInteger(1, decodeBase64(pArray));
/* 314:    */   }
/* 315:    */   
/* 316:    */   public static byte[] encodeInteger(BigInteger bigInt)
/* 317:    */   {
/* 318:726 */     if (bigInt == null) {
/* 319:727 */       throw new NullPointerException("encodeInteger called with null parameter");
/* 320:    */     }
/* 321:729 */     return encodeBase64(toIntegerBytes(bigInt), false);
/* 322:    */   }
/* 323:    */   
/* 324:    */   static byte[] toIntegerBytes(BigInteger bigInt)
/* 325:    */   {
/* 326:740 */     int bitlen = bigInt.bitLength();
/* 327:    */     
/* 328:742 */     bitlen = bitlen + 7 >> 3 << 3;
/* 329:743 */     byte[] bigBytes = bigInt.toByteArray();
/* 330:745 */     if ((bigInt.bitLength() % 8 != 0) && (bigInt.bitLength() / 8 + 1 == bitlen / 8)) {
/* 331:746 */       return bigBytes;
/* 332:    */     }
/* 333:749 */     int startSrc = 0;
/* 334:750 */     int len = bigBytes.length;
/* 335:753 */     if (bigInt.bitLength() % 8 == 0)
/* 336:    */     {
/* 337:754 */       startSrc = 1;
/* 338:755 */       len--;
/* 339:    */     }
/* 340:757 */     int startDst = bitlen / 8 - len;
/* 341:758 */     byte[] resizedBytes = new byte[bitlen / 8];
/* 342:759 */     System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
/* 343:760 */     return resizedBytes;
/* 344:    */   }
/* 345:    */   
/* 346:    */   protected boolean isInAlphabet(byte octet)
/* 347:    */   {
/* 348:772 */     return (octet >= 0) && (octet < this.decodeTable.length) && (this.decodeTable[octet] != -1);
/* 349:    */   }
/* 350:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.Base64
 * JD-Core Version:    0.7.0.1
 */