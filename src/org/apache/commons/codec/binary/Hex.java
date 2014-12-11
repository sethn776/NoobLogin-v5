/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ import org.apache.commons.codec.BinaryDecoder;
/*   5:    */ import org.apache.commons.codec.BinaryEncoder;
/*   6:    */ import org.apache.commons.codec.Charsets;
/*   7:    */ import org.apache.commons.codec.DecoderException;
/*   8:    */ import org.apache.commons.codec.EncoderException;
/*   9:    */ 
/*  10:    */ public class Hex
/*  11:    */   implements BinaryEncoder, BinaryDecoder
/*  12:    */ {
/*  13: 45 */   public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
/*  14:    */   public static final String DEFAULT_CHARSET_NAME = "UTF-8";
/*  15: 58 */   private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*  16: 64 */   private static final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*  17:    */   private final Charset charset;
/*  18:    */   
/*  19:    */   public static byte[] decodeHex(char[] data)
/*  20:    */     throws DecoderException
/*  21:    */   {
/*  22: 79 */     int len = data.length;
/*  23: 81 */     if ((len & 0x1) != 0) {
/*  24: 82 */       throw new DecoderException("Odd number of characters.");
/*  25:    */     }
/*  26: 85 */     byte[] out = new byte[len >> 1];
/*  27:    */     
/*  28:    */ 
/*  29: 88 */     int i = 0;
/*  30: 88 */     for (int j = 0; j < len; i++)
/*  31:    */     {
/*  32: 89 */       int f = toDigit(data[j], j) << 4;
/*  33: 90 */       j++;
/*  34: 91 */       f |= toDigit(data[j], j);
/*  35: 92 */       j++;
/*  36: 93 */       out[i] = ((byte)(f & 0xFF));
/*  37:    */     }
/*  38: 96 */     return out;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public static char[] encodeHex(byte[] data)
/*  42:    */   {
/*  43:109 */     return encodeHex(data, true);
/*  44:    */   }
/*  45:    */   
/*  46:    */   public static char[] encodeHex(byte[] data, boolean toLowerCase)
/*  47:    */   {
/*  48:125 */     return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
/*  49:    */   }
/*  50:    */   
/*  51:    */   protected static char[] encodeHex(byte[] data, char[] toDigits)
/*  52:    */   {
/*  53:141 */     int l = data.length;
/*  54:142 */     char[] out = new char[l << 1];
/*  55:    */     
/*  56:144 */     int i = 0;
/*  57:144 */     for (int j = 0; i < l; i++)
/*  58:    */     {
/*  59:145 */       out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
/*  60:146 */       out[(j++)] = toDigits[(0xF & data[i])];
/*  61:    */     }
/*  62:148 */     return out;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public static String encodeHexString(byte[] data)
/*  66:    */   {
/*  67:161 */     return new String(encodeHex(data));
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected static int toDigit(char ch, int index)
/*  71:    */     throws DecoderException
/*  72:    */   {
/*  73:176 */     int digit = Character.digit(ch, 16);
/*  74:177 */     if (digit == -1) {
/*  75:178 */       throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
/*  76:    */     }
/*  77:180 */     return digit;
/*  78:    */   }
/*  79:    */   
/*  80:    */   public Hex()
/*  81:    */   {
/*  82:190 */     this.charset = DEFAULT_CHARSET;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Hex(Charset charset)
/*  86:    */   {
/*  87:201 */     this.charset = charset;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public Hex(String charsetName)
/*  91:    */   {
/*  92:215 */     this(Charset.forName(charsetName));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public byte[] decode(byte[] array)
/*  96:    */     throws DecoderException
/*  97:    */   {
/*  98:232 */     return decodeHex(new String(array, getCharset()).toCharArray());
/*  99:    */   }
/* 100:    */   
/* 101:    */   public Object decode(Object object)
/* 102:    */     throws DecoderException
/* 103:    */   {
/* 104:    */     try
/* 105:    */     {
/* 106:251 */       char[] charArray = (object instanceof String) ? ((String)object).toCharArray() : (char[])object;
/* 107:252 */       return decodeHex(charArray);
/* 108:    */     }
/* 109:    */     catch (ClassCastException e)
/* 110:    */     {
/* 111:254 */       throw new DecoderException(e.getMessage(), e);
/* 112:    */     }
/* 113:    */   }
/* 114:    */   
/* 115:    */   public byte[] encode(byte[] array)
/* 116:    */   {
/* 117:275 */     return encodeHexString(array).getBytes(getCharset());
/* 118:    */   }
/* 119:    */   
/* 120:    */   public Object encode(Object object)
/* 121:    */     throws EncoderException
/* 122:    */   {
/* 123:    */     try
/* 124:    */     {
/* 125:297 */       byte[] byteArray = (object instanceof String) ? 
/* 126:298 */         ((String)object).getBytes(getCharset()) : (byte[])object;
/* 127:299 */       return encodeHex(byteArray);
/* 128:    */     }
/* 129:    */     catch (ClassCastException e)
/* 130:    */     {
/* 131:301 */       throw new EncoderException(e.getMessage(), e);
/* 132:    */     }
/* 133:    */   }
/* 134:    */   
/* 135:    */   public Charset getCharset()
/* 136:    */   {
/* 137:312 */     return this.charset;
/* 138:    */   }
/* 139:    */   
/* 140:    */   public String getCharsetName()
/* 141:    */   {
/* 142:322 */     return this.charset.name();
/* 143:    */   }
/* 144:    */   
/* 145:    */   public String toString()
/* 146:    */   {
/* 147:332 */     return super.toString() + "[charsetName=" + this.charset + "]";
/* 148:    */   }
/* 149:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.Hex
 * JD-Core Version:    0.7.0.1
 */