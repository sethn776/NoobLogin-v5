/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import org.apache.commons.codec.BinaryDecoder;
/*   4:    */ import org.apache.commons.codec.BinaryEncoder;
/*   5:    */ import org.apache.commons.codec.DecoderException;
/*   6:    */ import org.apache.commons.codec.EncoderException;
/*   7:    */ 
/*   8:    */ public class BinaryCodec
/*   9:    */   implements BinaryDecoder, BinaryEncoder
/*  10:    */ {
/*  11: 42 */   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*  12: 45 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*  13:    */   private static final int BIT_0 = 1;
/*  14:    */   private static final int BIT_1 = 2;
/*  15:    */   private static final int BIT_2 = 4;
/*  16:    */   private static final int BIT_3 = 8;
/*  17:    */   private static final int BIT_4 = 16;
/*  18:    */   private static final int BIT_5 = 32;
/*  19:    */   private static final int BIT_6 = 64;
/*  20:    */   private static final int BIT_7 = 128;
/*  21: 71 */   private static final int[] BITS = { 1, 2, 4, 8, 16, 32, 64, 128 };
/*  22:    */   
/*  23:    */   public byte[] encode(byte[] raw)
/*  24:    */   {
/*  25: 83 */     return toAsciiBytes(raw);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Object encode(Object raw)
/*  29:    */     throws EncoderException
/*  30:    */   {
/*  31: 98 */     if (!(raw instanceof byte[])) {
/*  32: 99 */       throw new EncoderException("argument not a byte array");
/*  33:    */     }
/*  34:101 */     return toAsciiChars((byte[])raw);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object decode(Object ascii)
/*  38:    */     throws DecoderException
/*  39:    */   {
/*  40:116 */     if (ascii == null) {
/*  41:117 */       return EMPTY_BYTE_ARRAY;
/*  42:    */     }
/*  43:119 */     if ((ascii instanceof byte[])) {
/*  44:120 */       return fromAscii((byte[])ascii);
/*  45:    */     }
/*  46:122 */     if ((ascii instanceof char[])) {
/*  47:123 */       return fromAscii((char[])ascii);
/*  48:    */     }
/*  49:125 */     if ((ascii instanceof String)) {
/*  50:126 */       return fromAscii(((String)ascii).toCharArray());
/*  51:    */     }
/*  52:128 */     throw new DecoderException("argument not a byte array");
/*  53:    */   }
/*  54:    */   
/*  55:    */   public byte[] decode(byte[] ascii)
/*  56:    */   {
/*  57:141 */     return fromAscii(ascii);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public byte[] toByteArray(String ascii)
/*  61:    */   {
/*  62:153 */     if (ascii == null) {
/*  63:154 */       return EMPTY_BYTE_ARRAY;
/*  64:    */     }
/*  65:156 */     return fromAscii(ascii.toCharArray());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static byte[] fromAscii(char[] ascii)
/*  69:    */   {
/*  70:172 */     if ((ascii == null) || (ascii.length == 0)) {
/*  71:173 */       return EMPTY_BYTE_ARRAY;
/*  72:    */     }
/*  73:176 */     byte[] l_raw = new byte[ascii.length >> 3];
/*  74:    */     
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:181 */     int ii = 0;
/*  79:181 */     for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8)
/*  80:    */     {
/*  81:182 */       for (int bits = 0; bits < BITS.length; bits++) {
/*  82:183 */         if (ascii[(jj - bits)] == '1')
/*  83:    */         {
/*  84:184 */           int tmp49_48 = ii; byte[] tmp49_47 = l_raw;tmp49_47[tmp49_48] = ((byte)(tmp49_47[tmp49_48] | BITS[bits]));
/*  85:    */         }
/*  86:    */       }
/*  87:181 */       ii++;
/*  88:    */     }
/*  89:188 */     return l_raw;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static byte[] fromAscii(byte[] ascii)
/*  93:    */   {
/*  94:199 */     if (isEmpty(ascii)) {
/*  95:200 */       return EMPTY_BYTE_ARRAY;
/*  96:    */     }
/*  97:203 */     byte[] l_raw = new byte[ascii.length >> 3];
/*  98:    */     
/*  99:    */ 
/* 100:    */ 
/* 101:    */ 
/* 102:208 */     int ii = 0;
/* 103:208 */     for (int jj = ascii.length - 1; ii < l_raw.length; jj -= 8)
/* 104:    */     {
/* 105:209 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 106:210 */         if (ascii[(jj - bits)] == 49)
/* 107:    */         {
/* 108:211 */           int tmp47_46 = ii; byte[] tmp47_45 = l_raw;tmp47_45[tmp47_46] = ((byte)(tmp47_45[tmp47_46] | BITS[bits]));
/* 109:    */         }
/* 110:    */       }
/* 111:208 */       ii++;
/* 112:    */     }
/* 113:215 */     return l_raw;
/* 114:    */   }
/* 115:    */   
/* 116:    */   private static boolean isEmpty(byte[] array)
/* 117:    */   {
/* 118:226 */     return (array == null) || (array.length == 0);
/* 119:    */   }
/* 120:    */   
/* 121:    */   public static byte[] toAsciiBytes(byte[] raw)
/* 122:    */   {
/* 123:239 */     if (isEmpty(raw)) {
/* 124:240 */       return EMPTY_BYTE_ARRAY;
/* 125:    */     }
/* 126:243 */     byte[] l_ascii = new byte[raw.length << 3];
/* 127:    */     
/* 128:    */ 
/* 129:    */ 
/* 130:    */ 
/* 131:248 */     int ii = 0;
/* 132:248 */     for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8)
/* 133:    */     {
/* 134:249 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 135:250 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 136:251 */           l_ascii[(jj - bits)] = 48;
/* 137:    */         } else {
/* 138:253 */           l_ascii[(jj - bits)] = 49;
/* 139:    */         }
/* 140:    */       }
/* 141:248 */       ii++;
/* 142:    */     }
/* 143:257 */     return l_ascii;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public static char[] toAsciiChars(byte[] raw)
/* 147:    */   {
/* 148:269 */     if (isEmpty(raw)) {
/* 149:270 */       return EMPTY_CHAR_ARRAY;
/* 150:    */     }
/* 151:273 */     char[] l_ascii = new char[raw.length << 3];
/* 152:    */     
/* 153:    */ 
/* 154:    */ 
/* 155:    */ 
/* 156:278 */     int ii = 0;
/* 157:278 */     for (int jj = l_ascii.length - 1; ii < raw.length; jj -= 8)
/* 158:    */     {
/* 159:279 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 160:280 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 161:281 */           l_ascii[(jj - bits)] = '0';
/* 162:    */         } else {
/* 163:283 */           l_ascii[(jj - bits)] = '1';
/* 164:    */         }
/* 165:    */       }
/* 166:278 */       ii++;
/* 167:    */     }
/* 168:287 */     return l_ascii;
/* 169:    */   }
/* 170:    */   
/* 171:    */   public static String toAsciiString(byte[] raw)
/* 172:    */   {
/* 173:299 */     return new String(toAsciiChars(raw));
/* 174:    */   }
/* 175:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.BinaryCodec
 * JD-Core Version:    0.7.0.1
 */