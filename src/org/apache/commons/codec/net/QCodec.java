/*   1:    */ package org.apache.commons.codec.net;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import java.util.BitSet;
/*   6:    */ import org.apache.commons.codec.Charsets;
/*   7:    */ import org.apache.commons.codec.DecoderException;
/*   8:    */ import org.apache.commons.codec.EncoderException;
/*   9:    */ import org.apache.commons.codec.StringDecoder;
/*  10:    */ import org.apache.commons.codec.StringEncoder;
/*  11:    */ 
/*  12:    */ public class QCodec
/*  13:    */   extends RFC1522Codec
/*  14:    */   implements StringEncoder, StringDecoder
/*  15:    */ {
/*  16:    */   private final Charset charset;
/*  17: 61 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*  18:    */   private static final byte BLANK = 32;
/*  19:    */   private static final byte UNDERSCORE = 95;
/*  20:    */   
/*  21:    */   static
/*  22:    */   {
/*  23: 65 */     PRINTABLE_CHARS.set(32);
/*  24: 66 */     PRINTABLE_CHARS.set(33);
/*  25: 67 */     PRINTABLE_CHARS.set(34);
/*  26: 68 */     PRINTABLE_CHARS.set(35);
/*  27: 69 */     PRINTABLE_CHARS.set(36);
/*  28: 70 */     PRINTABLE_CHARS.set(37);
/*  29: 71 */     PRINTABLE_CHARS.set(38);
/*  30: 72 */     PRINTABLE_CHARS.set(39);
/*  31: 73 */     PRINTABLE_CHARS.set(40);
/*  32: 74 */     PRINTABLE_CHARS.set(41);
/*  33: 75 */     PRINTABLE_CHARS.set(42);
/*  34: 76 */     PRINTABLE_CHARS.set(43);
/*  35: 77 */     PRINTABLE_CHARS.set(44);
/*  36: 78 */     PRINTABLE_CHARS.set(45);
/*  37: 79 */     PRINTABLE_CHARS.set(46);
/*  38: 80 */     PRINTABLE_CHARS.set(47);
/*  39: 81 */     for (int i = 48; i <= 57; i++) {
/*  40: 82 */       PRINTABLE_CHARS.set(i);
/*  41:    */     }
/*  42: 84 */     PRINTABLE_CHARS.set(58);
/*  43: 85 */     PRINTABLE_CHARS.set(59);
/*  44: 86 */     PRINTABLE_CHARS.set(60);
/*  45: 87 */     PRINTABLE_CHARS.set(62);
/*  46: 88 */     PRINTABLE_CHARS.set(64);
/*  47: 89 */     for (int i = 65; i <= 90; i++) {
/*  48: 90 */       PRINTABLE_CHARS.set(i);
/*  49:    */     }
/*  50: 92 */     PRINTABLE_CHARS.set(91);
/*  51: 93 */     PRINTABLE_CHARS.set(92);
/*  52: 94 */     PRINTABLE_CHARS.set(93);
/*  53: 95 */     PRINTABLE_CHARS.set(94);
/*  54: 96 */     PRINTABLE_CHARS.set(96);
/*  55: 97 */     for (int i = 97; i <= 122; i++) {
/*  56: 98 */       PRINTABLE_CHARS.set(i);
/*  57:    */     }
/*  58:100 */     PRINTABLE_CHARS.set(123);
/*  59:101 */     PRINTABLE_CHARS.set(124);
/*  60:102 */     PRINTABLE_CHARS.set(125);
/*  61:103 */     PRINTABLE_CHARS.set(126);
/*  62:    */   }
/*  63:    */   
/*  64:110 */   private boolean encodeBlanks = false;
/*  65:    */   
/*  66:    */   public QCodec()
/*  67:    */   {
/*  68:116 */     this(Charsets.UTF_8);
/*  69:    */   }
/*  70:    */   
/*  71:    */   public QCodec(Charset charset)
/*  72:    */   {
/*  73:130 */     this.charset = charset;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public QCodec(String charsetName)
/*  77:    */   {
/*  78:144 */     this(Charset.forName(charsetName));
/*  79:    */   }
/*  80:    */   
/*  81:    */   protected String getEncoding()
/*  82:    */   {
/*  83:149 */     return "Q";
/*  84:    */   }
/*  85:    */   
/*  86:    */   protected byte[] doEncoding(byte[] bytes)
/*  87:    */   {
/*  88:154 */     if (bytes == null) {
/*  89:155 */       return null;
/*  90:    */     }
/*  91:157 */     byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
/*  92:158 */     if (this.encodeBlanks) {
/*  93:159 */       for (int i = 0; i < data.length; i++) {
/*  94:160 */         if (data[i] == 32) {
/*  95:161 */           data[i] = 95;
/*  96:    */         }
/*  97:    */       }
/*  98:    */     }
/*  99:165 */     return data;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected byte[] doDecoding(byte[] bytes)
/* 103:    */     throws DecoderException
/* 104:    */   {
/* 105:170 */     if (bytes == null) {
/* 106:171 */       return null;
/* 107:    */     }
/* 108:173 */     boolean hasUnderscores = false;
/* 109:174 */     for (byte b : bytes) {
/* 110:175 */       if (b == 95)
/* 111:    */       {
/* 112:176 */         hasUnderscores = true;
/* 113:177 */         break;
/* 114:    */       }
/* 115:    */     }
/* 116:180 */     if (hasUnderscores)
/* 117:    */     {
/* 118:181 */       byte[] tmp = new byte[bytes.length];
/* 119:182 */       for (int i = 0; i < bytes.length; i++)
/* 120:    */       {
/* 121:183 */         byte b = bytes[i];
/* 122:184 */         if (b != 95) {
/* 123:185 */           tmp[i] = b;
/* 124:    */         } else {
/* 125:187 */           tmp[i] = 32;
/* 126:    */         }
/* 127:    */       }
/* 128:190 */       return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
/* 129:    */     }
/* 130:192 */     return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
/* 131:    */   }
/* 132:    */   
/* 133:    */   public String encode(String str, Charset charset)
/* 134:    */     throws EncoderException
/* 135:    */   {
/* 136:208 */     if (str == null) {
/* 137:209 */       return null;
/* 138:    */     }
/* 139:211 */     return encodeText(str, charset);
/* 140:    */   }
/* 141:    */   
/* 142:    */   public String encode(String str, String charset)
/* 143:    */     throws EncoderException
/* 144:    */   {
/* 145:226 */     if (str == null) {
/* 146:227 */       return null;
/* 147:    */     }
/* 148:    */     try
/* 149:    */     {
/* 150:230 */       return encodeText(str, charset);
/* 151:    */     }
/* 152:    */     catch (UnsupportedEncodingException e)
/* 153:    */     {
/* 154:232 */       throw new EncoderException(e.getMessage(), e);
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   public String encode(String str)
/* 159:    */     throws EncoderException
/* 160:    */   {
/* 161:247 */     if (str == null) {
/* 162:248 */       return null;
/* 163:    */     }
/* 164:250 */     return encode(str, getCharset());
/* 165:    */   }
/* 166:    */   
/* 167:    */   public String decode(String str)
/* 168:    */     throws DecoderException
/* 169:    */   {
/* 170:265 */     if (str == null) {
/* 171:266 */       return null;
/* 172:    */     }
/* 173:    */     try
/* 174:    */     {
/* 175:269 */       return decodeText(str);
/* 176:    */     }
/* 177:    */     catch (UnsupportedEncodingException e)
/* 178:    */     {
/* 179:271 */       throw new DecoderException(e.getMessage(), e);
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   public Object encode(Object obj)
/* 184:    */     throws EncoderException
/* 185:    */   {
/* 186:286 */     if (obj == null) {
/* 187:287 */       return null;
/* 188:    */     }
/* 189:288 */     if ((obj instanceof String)) {
/* 190:289 */       return encode((String)obj);
/* 191:    */     }
/* 192:291 */     throw new EncoderException("Objects of type " + 
/* 193:292 */       obj.getClass().getName() + 
/* 194:293 */       " cannot be encoded using Q codec");
/* 195:    */   }
/* 196:    */   
/* 197:    */   public Object decode(Object obj)
/* 198:    */     throws DecoderException
/* 199:    */   {
/* 200:310 */     if (obj == null) {
/* 201:311 */       return null;
/* 202:    */     }
/* 203:312 */     if ((obj instanceof String)) {
/* 204:313 */       return decode((String)obj);
/* 205:    */     }
/* 206:315 */     throw new DecoderException("Objects of type " + 
/* 207:316 */       obj.getClass().getName() + 
/* 208:317 */       " cannot be decoded using Q codec");
/* 209:    */   }
/* 210:    */   
/* 211:    */   public Charset getCharset()
/* 212:    */   {
/* 213:328 */     return this.charset;
/* 214:    */   }
/* 215:    */   
/* 216:    */   public String getDefaultCharset()
/* 217:    */   {
/* 218:337 */     return this.charset.name();
/* 219:    */   }
/* 220:    */   
/* 221:    */   public boolean isEncodeBlanks()
/* 222:    */   {
/* 223:346 */     return this.encodeBlanks;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public void setEncodeBlanks(boolean b)
/* 227:    */   {
/* 228:356 */     this.encodeBlanks = b;
/* 229:    */   }
/* 230:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.net.QCodec
 * JD-Core Version:    0.7.0.1
 */