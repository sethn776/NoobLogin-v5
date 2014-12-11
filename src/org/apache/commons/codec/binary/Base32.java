/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ public class Base32
/*   4:    */   extends BaseNCodec
/*   5:    */ {
/*   6:    */   private static final int BITS_PER_ENCODED_BYTE = 5;
/*   7:    */   private static final int BYTES_PER_ENCODED_BLOCK = 8;
/*   8:    */   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
/*   9: 60 */   private static final byte[] CHUNK_SEPARATOR = { 13, 10 };
/*  10: 67 */   private static final byte[] DECODE_TABLE = {
/*  11:    */   
/*  12: 69 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  13: 70 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  14: 71 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  15: 72 */     -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  16: 73 */     -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
/*  17: 74 */     15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
/*  18: 81 */   private static final byte[] ENCODE_TABLE = {
/*  19: 82 */     65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 
/*  20: 83 */     78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 
/*  21: 84 */     50, 51, 52, 53, 54, 55 };
/*  22: 92 */   private static final byte[] HEX_DECODE_TABLE = {
/*  23:    */   
/*  24: 94 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  25: 95 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  26: 96 */     -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
/*  27: 97 */     0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, 
/*  28: 98 */     -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
/*  29: 99 */     25, 26, 27, 28, 29, 30, 31, 32 };
/*  30:106 */   private static final byte[] HEX_ENCODE_TABLE = {
/*  31:107 */     48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
/*  32:108 */     65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 
/*  33:109 */     78, 79, 80, 81, 82, 83, 84, 85, 86 };
/*  34:    */   private static final int MASK_5BITS = 31;
/*  35:    */   private final int decodeSize;
/*  36:    */   private final byte[] decodeTable;
/*  37:    */   private final int encodeSize;
/*  38:    */   private final byte[] encodeTable;
/*  39:    */   private final byte[] lineSeparator;
/*  40:    */   
/*  41:    */   public Base32()
/*  42:    */   {
/*  43:159 */     this(false);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public Base32(boolean useHex)
/*  47:    */   {
/*  48:170 */     this(0, null, useHex);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public Base32(int lineLength)
/*  52:    */   {
/*  53:185 */     this(lineLength, CHUNK_SEPARATOR);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Base32(int lineLength, byte[] lineSeparator)
/*  57:    */   {
/*  58:207 */     this(lineLength, lineSeparator, false);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Base32(int lineLength, byte[] lineSeparator, boolean useHex)
/*  62:    */   {
/*  63:234 */     super(5, 8, lineLength, lineSeparator == null ? 0 : lineSeparator.length);
/*  64:235 */     if (useHex)
/*  65:    */     {
/*  66:236 */       this.encodeTable = HEX_ENCODE_TABLE;
/*  67:237 */       this.decodeTable = HEX_DECODE_TABLE;
/*  68:    */     }
/*  69:    */     else
/*  70:    */     {
/*  71:239 */       this.encodeTable = ENCODE_TABLE;
/*  72:240 */       this.decodeTable = DECODE_TABLE;
/*  73:    */     }
/*  74:242 */     if (lineLength > 0)
/*  75:    */     {
/*  76:243 */       if (lineSeparator == null) {
/*  77:244 */         throw new IllegalArgumentException("lineLength " + lineLength + " > 0, but lineSeparator is null");
/*  78:    */       }
/*  79:247 */       if (containsAlphabetOrPad(lineSeparator))
/*  80:    */       {
/*  81:248 */         String sep = StringUtils.newStringUtf8(lineSeparator);
/*  82:249 */         throw new IllegalArgumentException("lineSeparator must not contain Base32 characters: [" + sep + "]");
/*  83:    */       }
/*  84:251 */       this.encodeSize = (8 + lineSeparator.length);
/*  85:252 */       this.lineSeparator = new byte[lineSeparator.length];
/*  86:253 */       System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
/*  87:    */     }
/*  88:    */     else
/*  89:    */     {
/*  90:255 */       this.encodeSize = 8;
/*  91:256 */       this.lineSeparator = null;
/*  92:    */     }
/*  93:258 */     this.decodeSize = (this.encodeSize - 1);
/*  94:    */   }
/*  95:    */   
/*  96:    */   void decode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
/*  97:    */   {
/*  98:287 */     if (context.eof) {
/*  99:288 */       return;
/* 100:    */     }
/* 101:290 */     if (inAvail < 0) {
/* 102:291 */       context.eof = true;
/* 103:    */     }
/* 104:293 */     for (int i = 0; i < inAvail; i++)
/* 105:    */     {
/* 106:294 */       byte b = in[(inPos++)];
/* 107:295 */       if (b == 61)
/* 108:    */       {
/* 109:297 */         context.eof = true;
/* 110:298 */         break;
/* 111:    */       }
/* 112:300 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 113:301 */       if ((b >= 0) && (b < this.decodeTable.length))
/* 114:    */       {
/* 115:302 */         int result = this.decodeTable[b];
/* 116:303 */         if (result >= 0)
/* 117:    */         {
/* 118:304 */           context.modulus = ((context.modulus + 1) % 8);
/* 119:    */           
/* 120:306 */           context.lbitWorkArea = ((context.lbitWorkArea << 5) + result);
/* 121:307 */           if (context.modulus == 0)
/* 122:    */           {
/* 123:308 */             buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 32 & 0xFF));
/* 124:309 */             buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 24 & 0xFF));
/* 125:310 */             buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 16 & 0xFF));
/* 126:311 */             buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 8 & 0xFF));
/* 127:312 */             buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea & 0xFF));
/* 128:    */           }
/* 129:    */         }
/* 130:    */       }
/* 131:    */     }
/* 132:322 */     if ((context.eof) && (context.modulus >= 2))
/* 133:    */     {
/* 134:323 */       byte[] buffer = ensureBufferSize(this.decodeSize, context);
/* 135:326 */       switch (context.modulus)
/* 136:    */       {
/* 137:    */       case 2: 
/* 138:328 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 2 & 0xFF));
/* 139:329 */         break;
/* 140:    */       case 3: 
/* 141:331 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 7 & 0xFF));
/* 142:332 */         break;
/* 143:    */       case 4: 
/* 144:334 */         context.lbitWorkArea >>= 4;
/* 145:335 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 8 & 0xFF));
/* 146:336 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea & 0xFF));
/* 147:337 */         break;
/* 148:    */       case 5: 
/* 149:339 */         context.lbitWorkArea >>= 1;
/* 150:340 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 16 & 0xFF));
/* 151:341 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 8 & 0xFF));
/* 152:342 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea & 0xFF));
/* 153:343 */         break;
/* 154:    */       case 6: 
/* 155:345 */         context.lbitWorkArea >>= 6;
/* 156:346 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 16 & 0xFF));
/* 157:347 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 8 & 0xFF));
/* 158:348 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea & 0xFF));
/* 159:349 */         break;
/* 160:    */       case 7: 
/* 161:351 */         context.lbitWorkArea >>= 3;
/* 162:352 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 24 & 0xFF));
/* 163:353 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 16 & 0xFF));
/* 164:354 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea >> 8 & 0xFF));
/* 165:355 */         buffer[(context.pos++)] = ((byte)(int)(context.lbitWorkArea & 0xFF));
/* 166:356 */         break;
/* 167:    */       default: 
/* 168:359 */         throw new IllegalStateException("Impossible modulus " + context.modulus);
/* 169:    */       }
/* 170:    */     }
/* 171:    */   }
/* 172:    */   
/* 173:    */   void encode(byte[] in, int inPos, int inAvail, BaseNCodec.Context context)
/* 174:    */   {
/* 175:383 */     if (context.eof) {
/* 176:384 */       return;
/* 177:    */     }
/* 178:388 */     if (inAvail < 0)
/* 179:    */     {
/* 180:389 */       context.eof = true;
/* 181:390 */       if ((context.modulus == 0) && (this.lineLength == 0)) {
/* 182:391 */         return;
/* 183:    */       }
/* 184:393 */       byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 185:394 */       int savedPos = context.pos;
/* 186:395 */       switch (context.modulus)
/* 187:    */       {
/* 188:    */       case 0: 
/* 189:    */         break;
/* 190:    */       case 1: 
/* 191:399 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 3) & 0x1F)];
/* 192:400 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea << 2) & 0x1F)];
/* 193:401 */         buffer[(context.pos++)] = 61;
/* 194:402 */         buffer[(context.pos++)] = 61;
/* 195:403 */         buffer[(context.pos++)] = 61;
/* 196:404 */         buffer[(context.pos++)] = 61;
/* 197:405 */         buffer[(context.pos++)] = 61;
/* 198:406 */         buffer[(context.pos++)] = 61;
/* 199:407 */         break;
/* 200:    */       case 2: 
/* 201:409 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 11) & 0x1F)];
/* 202:410 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 6) & 0x1F)];
/* 203:411 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 1) & 0x1F)];
/* 204:412 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea << 4) & 0x1F)];
/* 205:413 */         buffer[(context.pos++)] = 61;
/* 206:414 */         buffer[(context.pos++)] = 61;
/* 207:415 */         buffer[(context.pos++)] = 61;
/* 208:416 */         buffer[(context.pos++)] = 61;
/* 209:417 */         break;
/* 210:    */       case 3: 
/* 211:419 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 19) & 0x1F)];
/* 212:420 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 14) & 0x1F)];
/* 213:421 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 9) & 0x1F)];
/* 214:422 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 4) & 0x1F)];
/* 215:423 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea << 1) & 0x1F)];
/* 216:424 */         buffer[(context.pos++)] = 61;
/* 217:425 */         buffer[(context.pos++)] = 61;
/* 218:426 */         buffer[(context.pos++)] = 61;
/* 219:427 */         break;
/* 220:    */       case 4: 
/* 221:429 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 27) & 0x1F)];
/* 222:430 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 22) & 0x1F)];
/* 223:431 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 17) & 0x1F)];
/* 224:432 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 12) & 0x1F)];
/* 225:433 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 7) & 0x1F)];
/* 226:434 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 2) & 0x1F)];
/* 227:435 */         buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea << 3) & 0x1F)];
/* 228:436 */         buffer[(context.pos++)] = 61;
/* 229:437 */         break;
/* 230:    */       default: 
/* 231:439 */         throw new IllegalStateException("Impossible modulus " + context.modulus);
/* 232:    */       }
/* 233:441 */       context.currentLinePos += context.pos - savedPos;
/* 234:443 */       if ((this.lineLength > 0) && (context.currentLinePos > 0))
/* 235:    */       {
/* 236:444 */         System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 237:445 */         context.pos += this.lineSeparator.length;
/* 238:    */       }
/* 239:    */     }
/* 240:    */     else
/* 241:    */     {
/* 242:448 */       for (int i = 0; i < inAvail; i++)
/* 243:    */       {
/* 244:449 */         byte[] buffer = ensureBufferSize(this.encodeSize, context);
/* 245:450 */         context.modulus = ((context.modulus + 1) % 5);
/* 246:451 */         int b = in[(inPos++)];
/* 247:452 */         if (b < 0) {
/* 248:453 */           b += 256;
/* 249:    */         }
/* 250:455 */         context.lbitWorkArea = ((context.lbitWorkArea << 8) + b);
/* 251:456 */         if (context.modulus == 0)
/* 252:    */         {
/* 253:457 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 35) & 0x1F)];
/* 254:458 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 30) & 0x1F)];
/* 255:459 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 25) & 0x1F)];
/* 256:460 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 20) & 0x1F)];
/* 257:461 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 15) & 0x1F)];
/* 258:462 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 10) & 0x1F)];
/* 259:463 */           buffer[(context.pos++)] = this.encodeTable[((int)(context.lbitWorkArea >> 5) & 0x1F)];
/* 260:464 */           buffer[(context.pos++)] = this.encodeTable[((int)context.lbitWorkArea & 0x1F)];
/* 261:465 */           context.currentLinePos += 8;
/* 262:466 */           if ((this.lineLength > 0) && (this.lineLength <= context.currentLinePos))
/* 263:    */           {
/* 264:467 */             System.arraycopy(this.lineSeparator, 0, buffer, context.pos, this.lineSeparator.length);
/* 265:468 */             context.pos += this.lineSeparator.length;
/* 266:469 */             context.currentLinePos = 0;
/* 267:    */           }
/* 268:    */         }
/* 269:    */       }
/* 270:    */     }
/* 271:    */   }
/* 272:    */   
/* 273:    */   public boolean isInAlphabet(byte octet)
/* 274:    */   {
/* 275:485 */     return (octet >= 0) && (octet < this.decodeTable.length) && (this.decodeTable[octet] != -1);
/* 276:    */   }
/* 277:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.Base32
 * JD-Core Version:    0.7.0.1
 */