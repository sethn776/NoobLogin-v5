/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import org.apache.commons.codec.BinaryDecoder;
/*   5:    */ import org.apache.commons.codec.BinaryEncoder;
/*   6:    */ import org.apache.commons.codec.DecoderException;
/*   7:    */ import org.apache.commons.codec.EncoderException;
/*   8:    */ 
/*   9:    */ public abstract class BaseNCodec
/*  10:    */   implements BinaryEncoder, BinaryDecoder
/*  11:    */ {
/*  12:    */   static final int EOF = -1;
/*  13:    */   public static final int MIME_CHUNK_SIZE = 76;
/*  14:    */   public static final int PEM_CHUNK_SIZE = 64;
/*  15:    */   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
/*  16:    */   private static final int DEFAULT_BUFFER_SIZE = 8192;
/*  17:    */   protected static final int MASK_8BITS = 255;
/*  18:    */   protected static final byte PAD_DEFAULT = 61;
/*  19:    */   
/*  20:    */   static class Context
/*  21:    */   {
/*  22:    */     int ibitWorkArea;
/*  23:    */     long lbitWorkArea;
/*  24:    */     byte[] buffer;
/*  25:    */     int pos;
/*  26:    */     int readPos;
/*  27:    */     boolean eof;
/*  28:    */     int currentLinePos;
/*  29:    */     int modulus;
/*  30:    */     
/*  31:    */     public String toString()
/*  32:    */     {
/*  33:103 */       return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", new Object[] {
/*  34:104 */         getClass().getSimpleName(), Arrays.toString(this.buffer), 
/*  35:105 */         Integer.valueOf(this.currentLinePos), Boolean.valueOf(this.eof), Integer.valueOf(this.ibitWorkArea), Long.valueOf(this.lbitWorkArea), Integer.valueOf(this.modulus), Integer.valueOf(this.pos), Integer.valueOf(this.readPos) });
/*  36:    */     }
/*  37:    */   }
/*  38:    */   
/*  39:156 */   protected final byte PAD = 61;
/*  40:    */   private final int unencodedBlockSize;
/*  41:    */   private final int encodedBlockSize;
/*  42:    */   protected final int lineLength;
/*  43:    */   private final int chunkSeparatorLength;
/*  44:    */   
/*  45:    */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength)
/*  46:    */   {
/*  47:186 */     this.unencodedBlockSize = unencodedBlockSize;
/*  48:187 */     this.encodedBlockSize = encodedBlockSize;
/*  49:188 */     boolean useChunking = (lineLength > 0) && (chunkSeparatorLength > 0);
/*  50:189 */     this.lineLength = (useChunking ? lineLength / encodedBlockSize * encodedBlockSize : 0);
/*  51:190 */     this.chunkSeparatorLength = chunkSeparatorLength;
/*  52:    */   }
/*  53:    */   
/*  54:    */   boolean hasData(Context context)
/*  55:    */   {
/*  56:200 */     return context.buffer != null;
/*  57:    */   }
/*  58:    */   
/*  59:    */   int available(Context context)
/*  60:    */   {
/*  61:210 */     return context.buffer != null ? context.pos - context.readPos : 0;
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected int getDefaultBufferSize()
/*  65:    */   {
/*  66:219 */     return 8192;
/*  67:    */   }
/*  68:    */   
/*  69:    */   private byte[] resizeBuffer(Context context)
/*  70:    */   {
/*  71:227 */     if (context.buffer == null)
/*  72:    */     {
/*  73:228 */       context.buffer = new byte[getDefaultBufferSize()];
/*  74:229 */       context.pos = 0;
/*  75:230 */       context.readPos = 0;
/*  76:    */     }
/*  77:    */     else
/*  78:    */     {
/*  79:232 */       byte[] b = new byte[context.buffer.length * 2];
/*  80:233 */       System.arraycopy(context.buffer, 0, b, 0, context.buffer.length);
/*  81:234 */       context.buffer = b;
/*  82:    */     }
/*  83:236 */     return context.buffer;
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected byte[] ensureBufferSize(int size, Context context)
/*  87:    */   {
/*  88:246 */     if ((context.buffer == null) || (context.buffer.length < context.pos + size)) {
/*  89:247 */       return resizeBuffer(context);
/*  90:    */     }
/*  91:249 */     return context.buffer;
/*  92:    */   }
/*  93:    */   
/*  94:    */   int readResults(byte[] b, int bPos, int bAvail, Context context)
/*  95:    */   {
/*  96:269 */     if (context.buffer != null)
/*  97:    */     {
/*  98:270 */       int len = Math.min(available(context), bAvail);
/*  99:271 */       System.arraycopy(context.buffer, context.readPos, b, bPos, len);
/* 100:272 */       context.readPos += len;
/* 101:273 */       if (context.readPos >= context.pos) {
/* 102:274 */         context.buffer = null;
/* 103:    */       }
/* 104:276 */       return len;
/* 105:    */     }
/* 106:278 */     return context.eof ? -1 : 0;
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected static boolean isWhiteSpace(byte byteToCheck)
/* 110:    */   {
/* 111:289 */     switch (byteToCheck)
/* 112:    */     {
/* 113:    */     case 9: 
/* 114:    */     case 10: 
/* 115:    */     case 13: 
/* 116:    */     case 32: 
/* 117:294 */       return true;
/* 118:    */     }
/* 119:296 */     return false;
/* 120:    */   }
/* 121:    */   
/* 122:    */   public Object encode(Object obj)
/* 123:    */     throws EncoderException
/* 124:    */   {
/* 125:312 */     if (!(obj instanceof byte[])) {
/* 126:313 */       throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
/* 127:    */     }
/* 128:315 */     return encode((byte[])obj);
/* 129:    */   }
/* 130:    */   
/* 131:    */   public String encodeToString(byte[] pArray)
/* 132:    */   {
/* 133:327 */     return StringUtils.newStringUtf8(encode(pArray));
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String encodeAsString(byte[] pArray)
/* 137:    */   {
/* 138:338 */     return StringUtils.newStringUtf8(encode(pArray));
/* 139:    */   }
/* 140:    */   
/* 141:    */   public Object decode(Object obj)
/* 142:    */     throws DecoderException
/* 143:    */   {
/* 144:354 */     if ((obj instanceof byte[])) {
/* 145:355 */       return decode((byte[])obj);
/* 146:    */     }
/* 147:356 */     if ((obj instanceof String)) {
/* 148:357 */       return decode((String)obj);
/* 149:    */     }
/* 150:359 */     throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
/* 151:    */   }
/* 152:    */   
/* 153:    */   public byte[] decode(String pArray)
/* 154:    */   {
/* 155:371 */     return decode(StringUtils.getBytesUtf8(pArray));
/* 156:    */   }
/* 157:    */   
/* 158:    */   public byte[] decode(byte[] pArray)
/* 159:    */   {
/* 160:383 */     if ((pArray == null) || (pArray.length == 0)) {
/* 161:384 */       return pArray;
/* 162:    */     }
/* 163:386 */     Context context = new Context();
/* 164:387 */     decode(pArray, 0, pArray.length, context);
/* 165:388 */     decode(pArray, 0, -1, context);
/* 166:389 */     byte[] result = new byte[context.pos];
/* 167:390 */     readResults(result, 0, result.length, context);
/* 168:391 */     return result;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public byte[] encode(byte[] pArray)
/* 172:    */   {
/* 173:403 */     if ((pArray == null) || (pArray.length == 0)) {
/* 174:404 */       return pArray;
/* 175:    */     }
/* 176:406 */     Context context = new Context();
/* 177:407 */     encode(pArray, 0, pArray.length, context);
/* 178:408 */     encode(pArray, 0, -1, context);
/* 179:409 */     byte[] buf = new byte[context.pos - context.readPos];
/* 180:410 */     readResults(buf, 0, buf.length, context);
/* 181:411 */     return buf;
/* 182:    */   }
/* 183:    */   
/* 184:    */   abstract void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
/* 185:    */   
/* 186:    */   abstract void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
/* 187:    */   
/* 188:    */   protected abstract boolean isInAlphabet(byte paramByte);
/* 189:    */   
/* 190:    */   public boolean isInAlphabet(byte[] arrayOctet, boolean allowWSPad)
/* 191:    */   {
/* 192:441 */     for (int i = 0; i < arrayOctet.length; i++) {
/* 193:442 */       if ((!isInAlphabet(arrayOctet[i])) && (
/* 194:443 */         (!allowWSPad) || ((arrayOctet[i] != 61) && (!isWhiteSpace(arrayOctet[i]))))) {
/* 195:444 */         return false;
/* 196:    */       }
/* 197:    */     }
/* 198:447 */     return true;
/* 199:    */   }
/* 200:    */   
/* 201:    */   public boolean isInAlphabet(String basen)
/* 202:    */   {
/* 203:460 */     return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected boolean containsAlphabetOrPad(byte[] arrayOctet)
/* 207:    */   {
/* 208:473 */     if (arrayOctet == null) {
/* 209:474 */       return false;
/* 210:    */     }
/* 211:476 */     for (byte element : arrayOctet) {
/* 212:477 */       if ((61 == element) || (isInAlphabet(element))) {
/* 213:478 */         return true;
/* 214:    */       }
/* 215:    */     }
/* 216:481 */     return false;
/* 217:    */   }
/* 218:    */   
/* 219:    */   public long getEncodedLength(byte[] pArray)
/* 220:    */   {
/* 221:495 */     long len = (pArray.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize * this.encodedBlockSize;
/* 222:496 */     if (this.lineLength > 0) {
/* 223:498 */       len += (len + this.lineLength - 1L) / this.lineLength * this.chunkSeparatorLength;
/* 224:    */     }
/* 225:500 */     return len;
/* 226:    */   }
/* 227:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.BaseNCodec
 * JD-Core Version:    0.7.0.1
 */