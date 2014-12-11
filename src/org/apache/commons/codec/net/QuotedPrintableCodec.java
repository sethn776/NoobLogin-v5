/*   1:    */ package org.apache.commons.codec.net;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.nio.charset.Charset;
/*   6:    */ import java.nio.charset.IllegalCharsetNameException;
/*   7:    */ import java.nio.charset.UnsupportedCharsetException;
/*   8:    */ import java.util.BitSet;
/*   9:    */ import org.apache.commons.codec.BinaryDecoder;
/*  10:    */ import org.apache.commons.codec.BinaryEncoder;
/*  11:    */ import org.apache.commons.codec.Charsets;
/*  12:    */ import org.apache.commons.codec.DecoderException;
/*  13:    */ import org.apache.commons.codec.EncoderException;
/*  14:    */ import org.apache.commons.codec.StringDecoder;
/*  15:    */ import org.apache.commons.codec.StringEncoder;
/*  16:    */ import org.apache.commons.codec.binary.StringUtils;
/*  17:    */ 
/*  18:    */ public class QuotedPrintableCodec
/*  19:    */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*  20:    */ {
/*  21:    */   private final Charset charset;
/*  22: 70 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*  23:    */   private static final byte ESCAPE_CHAR = 61;
/*  24:    */   private static final byte TAB = 9;
/*  25:    */   private static final byte SPACE = 32;
/*  26:    */   
/*  27:    */   static
/*  28:    */   {
/*  29: 80 */     for (int i = 33; i <= 60; i++) {
/*  30: 81 */       PRINTABLE_CHARS.set(i);
/*  31:    */     }
/*  32: 83 */     for (int i = 62; i <= 126; i++) {
/*  33: 84 */       PRINTABLE_CHARS.set(i);
/*  34:    */     }
/*  35: 86 */     PRINTABLE_CHARS.set(9);
/*  36: 87 */     PRINTABLE_CHARS.set(32);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public QuotedPrintableCodec()
/*  40:    */   {
/*  41: 94 */     this(Charsets.UTF_8);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public QuotedPrintableCodec(Charset charset)
/*  45:    */   {
/*  46:105 */     this.charset = charset;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public QuotedPrintableCodec(String charsetName)
/*  50:    */     throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException
/*  51:    */   {
/*  52:125 */     this(Charset.forName(charsetName));
/*  53:    */   }
/*  54:    */   
/*  55:    */   private static final void encodeQuotedPrintable(int b, ByteArrayOutputStream buffer)
/*  56:    */   {
/*  57:137 */     buffer.write(61);
/*  58:138 */     char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/*  59:139 */     char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/*  60:140 */     buffer.write(hex1);
/*  61:141 */     buffer.write(hex2);
/*  62:    */   }
/*  63:    */   
/*  64:    */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes)
/*  65:    */   {
/*  66:157 */     if (bytes == null) {
/*  67:158 */       return null;
/*  68:    */     }
/*  69:160 */     if (printable == null) {
/*  70:161 */       printable = PRINTABLE_CHARS;
/*  71:    */     }
/*  72:163 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  73:164 */     for (byte c : bytes)
/*  74:    */     {
/*  75:165 */       int b = c;
/*  76:166 */       if (b < 0) {
/*  77:167 */         b += 256;
/*  78:    */       }
/*  79:169 */       if (printable.get(b)) {
/*  80:170 */         buffer.write(b);
/*  81:    */       } else {
/*  82:172 */         encodeQuotedPrintable(b, buffer);
/*  83:    */       }
/*  84:    */     }
/*  85:175 */     return buffer.toByteArray();
/*  86:    */   }
/*  87:    */   
/*  88:    */   public static final byte[] decodeQuotedPrintable(byte[] bytes)
/*  89:    */     throws DecoderException
/*  90:    */   {
/*  91:192 */     if (bytes == null) {
/*  92:193 */       return null;
/*  93:    */     }
/*  94:195 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  95:196 */     for (int i = 0; i < bytes.length; i++)
/*  96:    */     {
/*  97:197 */       int b = bytes[i];
/*  98:198 */       if (b == 61) {
/*  99:    */         try
/* 100:    */         {
/* 101:200 */           int u = Utils.digit16(bytes[(++i)]);
/* 102:201 */           int l = Utils.digit16(bytes[(++i)]);
/* 103:202 */           buffer.write((char)((u << 4) + l));
/* 104:    */         }
/* 105:    */         catch (ArrayIndexOutOfBoundsException e)
/* 106:    */         {
/* 107:204 */           throw new DecoderException("Invalid quoted-printable encoding", e);
/* 108:    */         }
/* 109:    */       } else {
/* 110:207 */         buffer.write(b);
/* 111:    */       }
/* 112:    */     }
/* 113:210 */     return buffer.toByteArray();
/* 114:    */   }
/* 115:    */   
/* 116:    */   public byte[] encode(byte[] bytes)
/* 117:    */   {
/* 118:225 */     return encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public byte[] decode(byte[] bytes)
/* 122:    */     throws DecoderException
/* 123:    */   {
/* 124:243 */     return decodeQuotedPrintable(bytes);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String encode(String str)
/* 128:    */     throws EncoderException
/* 129:    */   {
/* 130:262 */     return encode(str, getCharset());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String decode(String str, Charset charset)
/* 134:    */     throws DecoderException
/* 135:    */   {
/* 136:279 */     if (str == null) {
/* 137:280 */       return null;
/* 138:    */     }
/* 139:282 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String decode(String str, String charset)
/* 143:    */     throws DecoderException, UnsupportedEncodingException
/* 144:    */   {
/* 145:300 */     if (str == null) {
/* 146:301 */       return null;
/* 147:    */     }
/* 148:303 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String decode(String str)
/* 152:    */     throws DecoderException
/* 153:    */   {
/* 154:319 */     return decode(str, getCharset());
/* 155:    */   }
/* 156:    */   
/* 157:    */   public Object encode(Object obj)
/* 158:    */     throws EncoderException
/* 159:    */   {
/* 160:334 */     if (obj == null) {
/* 161:335 */       return null;
/* 162:    */     }
/* 163:336 */     if ((obj instanceof byte[])) {
/* 164:337 */       return encode((byte[])obj);
/* 165:    */     }
/* 166:338 */     if ((obj instanceof String)) {
/* 167:339 */       return encode((String)obj);
/* 168:    */     }
/* 169:341 */     throw new EncoderException("Objects of type " + 
/* 170:342 */       obj.getClass().getName() + 
/* 171:343 */       " cannot be quoted-printable encoded");
/* 172:    */   }
/* 173:    */   
/* 174:    */   public Object decode(Object obj)
/* 175:    */     throws DecoderException
/* 176:    */   {
/* 177:360 */     if (obj == null) {
/* 178:361 */       return null;
/* 179:    */     }
/* 180:362 */     if ((obj instanceof byte[])) {
/* 181:363 */       return decode((byte[])obj);
/* 182:    */     }
/* 183:364 */     if ((obj instanceof String)) {
/* 184:365 */       return decode((String)obj);
/* 185:    */     }
/* 186:367 */     throw new DecoderException("Objects of type " + 
/* 187:368 */       obj.getClass().getName() + 
/* 188:369 */       " cannot be quoted-printable decoded");
/* 189:    */   }
/* 190:    */   
/* 191:    */   public Charset getCharset()
/* 192:    */   {
/* 193:380 */     return this.charset;
/* 194:    */   }
/* 195:    */   
/* 196:    */   public String getDefaultCharset()
/* 197:    */   {
/* 198:389 */     return this.charset.name();
/* 199:    */   }
/* 200:    */   
/* 201:    */   public String encode(String str, Charset charset)
/* 202:    */   {
/* 203:406 */     if (str == null) {
/* 204:407 */       return null;
/* 205:    */     }
/* 206:409 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
/* 207:    */   }
/* 208:    */   
/* 209:    */   public String encode(String str, String charset)
/* 210:    */     throws UnsupportedEncodingException
/* 211:    */   {
/* 212:427 */     if (str == null) {
/* 213:428 */       return null;
/* 214:    */     }
/* 215:430 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
/* 216:    */   }
/* 217:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.net.QuotedPrintableCodec
 * JD-Core Version:    0.7.0.1
 */