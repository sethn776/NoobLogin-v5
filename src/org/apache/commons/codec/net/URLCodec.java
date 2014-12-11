/*   1:    */ package org.apache.commons.codec.net;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayOutputStream;
/*   4:    */ import java.io.UnsupportedEncodingException;
/*   5:    */ import java.util.BitSet;
/*   6:    */ import org.apache.commons.codec.BinaryDecoder;
/*   7:    */ import org.apache.commons.codec.BinaryEncoder;
/*   8:    */ import org.apache.commons.codec.DecoderException;
/*   9:    */ import org.apache.commons.codec.EncoderException;
/*  10:    */ import org.apache.commons.codec.StringDecoder;
/*  11:    */ import org.apache.commons.codec.StringEncoder;
/*  12:    */ import org.apache.commons.codec.binary.StringUtils;
/*  13:    */ 
/*  14:    */ public class URLCodec
/*  15:    */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*  16:    */ {
/*  17:    */   static final int RADIX = 16;
/*  18:    */   @Deprecated
/*  19:    */   protected String charset;
/*  20:    */   protected static final byte ESCAPE_CHAR = 37;
/*  21: 70 */   protected static final BitSet WWW_FORM_URL = new BitSet(256);
/*  22:    */   
/*  23:    */   static
/*  24:    */   {
/*  25: 75 */     for (int i = 97; i <= 122; i++) {
/*  26: 76 */       WWW_FORM_URL.set(i);
/*  27:    */     }
/*  28: 78 */     for (int i = 65; i <= 90; i++) {
/*  29: 79 */       WWW_FORM_URL.set(i);
/*  30:    */     }
/*  31: 82 */     for (int i = 48; i <= 57; i++) {
/*  32: 83 */       WWW_FORM_URL.set(i);
/*  33:    */     }
/*  34: 86 */     WWW_FORM_URL.set(45);
/*  35: 87 */     WWW_FORM_URL.set(95);
/*  36: 88 */     WWW_FORM_URL.set(46);
/*  37: 89 */     WWW_FORM_URL.set(42);
/*  38:    */     
/*  39: 91 */     WWW_FORM_URL.set(32);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public URLCodec()
/*  43:    */   {
/*  44: 99 */     this("UTF-8");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public URLCodec(String charset)
/*  48:    */   {
/*  49:109 */     this.charset = charset;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes)
/*  53:    */   {
/*  54:122 */     if (bytes == null) {
/*  55:123 */       return null;
/*  56:    */     }
/*  57:125 */     if (urlsafe == null) {
/*  58:126 */       urlsafe = WWW_FORM_URL;
/*  59:    */     }
/*  60:129 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  61:130 */     for (byte c : bytes)
/*  62:    */     {
/*  63:131 */       int b = c;
/*  64:132 */       if (b < 0) {
/*  65:133 */         b += 256;
/*  66:    */       }
/*  67:135 */       if (urlsafe.get(b))
/*  68:    */       {
/*  69:136 */         if (b == 32) {
/*  70:137 */           b = 43;
/*  71:    */         }
/*  72:139 */         buffer.write(b);
/*  73:    */       }
/*  74:    */       else
/*  75:    */       {
/*  76:141 */         buffer.write(37);
/*  77:142 */         char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
/*  78:143 */         char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
/*  79:144 */         buffer.write(hex1);
/*  80:145 */         buffer.write(hex2);
/*  81:    */       }
/*  82:    */     }
/*  83:148 */     return buffer.toByteArray();
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static final byte[] decodeUrl(byte[] bytes)
/*  87:    */     throws DecoderException
/*  88:    */   {
/*  89:162 */     if (bytes == null) {
/*  90:163 */       return null;
/*  91:    */     }
/*  92:165 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*  93:166 */     for (int i = 0; i < bytes.length; i++)
/*  94:    */     {
/*  95:167 */       int b = bytes[i];
/*  96:168 */       if (b == 43) {
/*  97:169 */         buffer.write(32);
/*  98:170 */       } else if (b == 37) {
/*  99:    */         try
/* 100:    */         {
/* 101:172 */           int u = Utils.digit16(bytes[(++i)]);
/* 102:173 */           int l = Utils.digit16(bytes[(++i)]);
/* 103:174 */           buffer.write((char)((u << 4) + l));
/* 104:    */         }
/* 105:    */         catch (ArrayIndexOutOfBoundsException e)
/* 106:    */         {
/* 107:176 */           throw new DecoderException("Invalid URL encoding: ", e);
/* 108:    */         }
/* 109:    */       } else {
/* 110:179 */         buffer.write(b);
/* 111:    */       }
/* 112:    */     }
/* 113:182 */     return buffer.toByteArray();
/* 114:    */   }
/* 115:    */   
/* 116:    */   public byte[] encode(byte[] bytes)
/* 117:    */   {
/* 118:194 */     return encodeUrl(WWW_FORM_URL, bytes);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public byte[] decode(byte[] bytes)
/* 122:    */     throws DecoderException
/* 123:    */   {
/* 124:210 */     return decodeUrl(bytes);
/* 125:    */   }
/* 126:    */   
/* 127:    */   public String encode(String str, String charset)
/* 128:    */     throws UnsupportedEncodingException
/* 129:    */   {
/* 130:225 */     if (str == null) {
/* 131:226 */       return null;
/* 132:    */     }
/* 133:228 */     return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String encode(String str)
/* 137:    */     throws EncoderException
/* 138:    */   {
/* 139:244 */     if (str == null) {
/* 140:245 */       return null;
/* 141:    */     }
/* 142:    */     try
/* 143:    */     {
/* 144:248 */       return encode(str, getDefaultCharset());
/* 145:    */     }
/* 146:    */     catch (UnsupportedEncodingException e)
/* 147:    */     {
/* 148:250 */       throw new EncoderException(e.getMessage(), e);
/* 149:    */     }
/* 150:    */   }
/* 151:    */   
/* 152:    */   public String decode(String str, String charset)
/* 153:    */     throws DecoderException, UnsupportedEncodingException
/* 154:    */   {
/* 155:270 */     if (str == null) {
/* 156:271 */       return null;
/* 157:    */     }
/* 158:273 */     return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
/* 159:    */   }
/* 160:    */   
/* 161:    */   public String decode(String str)
/* 162:    */     throws DecoderException
/* 163:    */   {
/* 164:289 */     if (str == null) {
/* 165:290 */       return null;
/* 166:    */     }
/* 167:    */     try
/* 168:    */     {
/* 169:293 */       return decode(str, getDefaultCharset());
/* 170:    */     }
/* 171:    */     catch (UnsupportedEncodingException e)
/* 172:    */     {
/* 173:295 */       throw new DecoderException(e.getMessage(), e);
/* 174:    */     }
/* 175:    */   }
/* 176:    */   
/* 177:    */   public Object encode(Object obj)
/* 178:    */     throws EncoderException
/* 179:    */   {
/* 180:310 */     if (obj == null) {
/* 181:311 */       return null;
/* 182:    */     }
/* 183:312 */     if ((obj instanceof byte[])) {
/* 184:313 */       return encode((byte[])obj);
/* 185:    */     }
/* 186:314 */     if ((obj instanceof String)) {
/* 187:315 */       return encode((String)obj);
/* 188:    */     }
/* 189:317 */     throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
/* 190:    */   }
/* 191:    */   
/* 192:    */   public Object decode(Object obj)
/* 193:    */     throws DecoderException
/* 194:    */   {
/* 195:335 */     if (obj == null) {
/* 196:336 */       return null;
/* 197:    */     }
/* 198:337 */     if ((obj instanceof byte[])) {
/* 199:338 */       return decode((byte[])obj);
/* 200:    */     }
/* 201:339 */     if ((obj instanceof String)) {
/* 202:340 */       return decode((String)obj);
/* 203:    */     }
/* 204:342 */     throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
/* 205:    */   }
/* 206:    */   
/* 207:    */   public String getDefaultCharset()
/* 208:    */   {
/* 209:353 */     return this.charset;
/* 210:    */   }
/* 211:    */   
/* 212:    */   @Deprecated
/* 213:    */   public String getEncoding()
/* 214:    */   {
/* 215:365 */     return this.charset;
/* 216:    */   }
/* 217:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.net.URLCodec
 * JD-Core Version:    0.7.0.1
 */