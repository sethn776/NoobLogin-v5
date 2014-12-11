/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import org.apache.commons.codec.Charsets;
/*   6:    */ 
/*   7:    */ public class StringUtils
/*   8:    */ {
/*   9:    */   private static byte[] getBytes(String string, Charset charset)
/*  10:    */   {
/*  11: 50 */     if (string == null) {
/*  12: 51 */       return null;
/*  13:    */     }
/*  14: 53 */     return string.getBytes(charset);
/*  15:    */   }
/*  16:    */   
/*  17:    */   public static byte[] getBytesIso8859_1(String string)
/*  18:    */   {
/*  19: 71 */     return getBytes(string, Charsets.ISO_8859_1);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static byte[] getBytesUnchecked(String string, String charsetName)
/*  23:    */   {
/*  24: 95 */     if (string == null) {
/*  25: 96 */       return null;
/*  26:    */     }
/*  27:    */     try
/*  28:    */     {
/*  29: 99 */       return string.getBytes(charsetName);
/*  30:    */     }
/*  31:    */     catch (UnsupportedEncodingException e)
/*  32:    */     {
/*  33:101 */       throw newIllegalStateException(charsetName, e);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public static byte[] getBytesUsAscii(String string)
/*  38:    */   {
/*  39:120 */     return getBytes(string, Charsets.US_ASCII);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static byte[] getBytesUtf16(String string)
/*  43:    */   {
/*  44:138 */     return getBytes(string, Charsets.UTF_16);
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static byte[] getBytesUtf16Be(String string)
/*  48:    */   {
/*  49:156 */     return getBytes(string, Charsets.UTF_16BE);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public static byte[] getBytesUtf16Le(String string)
/*  53:    */   {
/*  54:174 */     return getBytes(string, Charsets.UTF_16LE);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public static byte[] getBytesUtf8(String string)
/*  58:    */   {
/*  59:192 */     return getBytes(string, Charsets.UTF_8);
/*  60:    */   }
/*  61:    */   
/*  62:    */   private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e)
/*  63:    */   {
/*  64:197 */     return new IllegalStateException(charsetName + ": " + e);
/*  65:    */   }
/*  66:    */   
/*  67:    */   private static String newString(byte[] bytes, Charset charset)
/*  68:    */   {
/*  69:214 */     return bytes == null ? null : new String(bytes, charset);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static String newString(byte[] bytes, String charsetName)
/*  73:    */   {
/*  74:237 */     if (bytes == null) {
/*  75:238 */       return null;
/*  76:    */     }
/*  77:    */     try
/*  78:    */     {
/*  79:241 */       return new String(bytes, charsetName);
/*  80:    */     }
/*  81:    */     catch (UnsupportedEncodingException e)
/*  82:    */     {
/*  83:243 */       throw newIllegalStateException(charsetName, e);
/*  84:    */     }
/*  85:    */   }
/*  86:    */   
/*  87:    */   public static String newStringIso8859_1(byte[] bytes)
/*  88:    */   {
/*  89:260 */     return new String(bytes, Charsets.ISO_8859_1);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public static String newStringUsAscii(byte[] bytes)
/*  93:    */   {
/*  94:276 */     return new String(bytes, Charsets.US_ASCII);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static String newStringUtf16(byte[] bytes)
/*  98:    */   {
/*  99:292 */     return new String(bytes, Charsets.UTF_16);
/* 100:    */   }
/* 101:    */   
/* 102:    */   public static String newStringUtf16Be(byte[] bytes)
/* 103:    */   {
/* 104:308 */     return new String(bytes, Charsets.UTF_16BE);
/* 105:    */   }
/* 106:    */   
/* 107:    */   public static String newStringUtf16Le(byte[] bytes)
/* 108:    */   {
/* 109:324 */     return new String(bytes, Charsets.UTF_16LE);
/* 110:    */   }
/* 111:    */   
/* 112:    */   public static String newStringUtf8(byte[] bytes)
/* 113:    */   {
/* 114:340 */     return newString(bytes, Charsets.UTF_8);
/* 115:    */   }
/* 116:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.StringUtils
 * JD-Core Version:    0.7.0.1
 */