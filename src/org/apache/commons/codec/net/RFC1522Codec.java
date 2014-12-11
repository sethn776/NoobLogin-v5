/*   1:    */ package org.apache.commons.codec.net;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import org.apache.commons.codec.DecoderException;
/*   6:    */ import org.apache.commons.codec.EncoderException;
/*   7:    */ import org.apache.commons.codec.binary.StringUtils;
/*   8:    */ 
/*   9:    */ abstract class RFC1522Codec
/*  10:    */ {
/*  11:    */   protected static final char SEP = '?';
/*  12:    */   protected static final String POSTFIX = "?=";
/*  13:    */   protected static final String PREFIX = "=?";
/*  14:    */   
/*  15:    */   protected String encodeText(String text, Charset charset)
/*  16:    */     throws EncoderException
/*  17:    */   {
/*  18: 69 */     if (text == null) {
/*  19: 70 */       return null;
/*  20:    */     }
/*  21: 72 */     StringBuilder buffer = new StringBuilder();
/*  22: 73 */     buffer.append("=?");
/*  23: 74 */     buffer.append(charset);
/*  24: 75 */     buffer.append('?');
/*  25: 76 */     buffer.append(getEncoding());
/*  26: 77 */     buffer.append('?');
/*  27: 78 */     byte[] rawData = doEncoding(text.getBytes(charset));
/*  28: 79 */     buffer.append(StringUtils.newStringUsAscii(rawData));
/*  29: 80 */     buffer.append("?=");
/*  30: 81 */     return buffer.toString();
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected String encodeText(String text, String charsetName)
/*  34:    */     throws EncoderException, UnsupportedEncodingException
/*  35:    */   {
/*  36:104 */     if (text == null) {
/*  37:105 */       return null;
/*  38:    */     }
/*  39:107 */     return encodeText(text, Charset.forName(charsetName));
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected String decodeText(String text)
/*  43:    */     throws DecoderException, UnsupportedEncodingException
/*  44:    */   {
/*  45:126 */     if (text == null) {
/*  46:127 */       return null;
/*  47:    */     }
/*  48:129 */     if ((!text.startsWith("=?")) || (!text.endsWith("?="))) {
/*  49:130 */       throw new DecoderException("RFC 1522 violation: malformed encoded content");
/*  50:    */     }
/*  51:132 */     int terminator = text.length() - 2;
/*  52:133 */     int from = 2;
/*  53:134 */     int to = text.indexOf('?', from);
/*  54:135 */     if (to == terminator) {
/*  55:136 */       throw new DecoderException("RFC 1522 violation: charset token not found");
/*  56:    */     }
/*  57:138 */     String charset = text.substring(from, to);
/*  58:139 */     if (charset.equals("")) {
/*  59:140 */       throw new DecoderException("RFC 1522 violation: charset not specified");
/*  60:    */     }
/*  61:142 */     from = to + 1;
/*  62:143 */     to = text.indexOf('?', from);
/*  63:144 */     if (to == terminator) {
/*  64:145 */       throw new DecoderException("RFC 1522 violation: encoding token not found");
/*  65:    */     }
/*  66:147 */     String encoding = text.substring(from, to);
/*  67:148 */     if (!getEncoding().equalsIgnoreCase(encoding)) {
/*  68:149 */       throw new DecoderException("This codec cannot decode " + encoding + " encoded content");
/*  69:    */     }
/*  70:151 */     from = to + 1;
/*  71:152 */     to = text.indexOf('?', from);
/*  72:153 */     byte[] data = StringUtils.getBytesUsAscii(text.substring(from, to));
/*  73:154 */     data = doDecoding(data);
/*  74:155 */     return new String(data, charset);
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected abstract String getEncoding();
/*  78:    */   
/*  79:    */   protected abstract byte[] doEncoding(byte[] paramArrayOfByte)
/*  80:    */     throws EncoderException;
/*  81:    */   
/*  82:    */   protected abstract byte[] doDecoding(byte[] paramArrayOfByte)
/*  83:    */     throws DecoderException;
/*  84:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.net.RFC1522Codec
 * JD-Core Version:    0.7.0.1
 */