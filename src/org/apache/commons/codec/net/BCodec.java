/*   1:    */ package org.apache.commons.codec.net;
/*   2:    */ 
/*   3:    */ import java.io.UnsupportedEncodingException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import org.apache.commons.codec.Charsets;
/*   6:    */ import org.apache.commons.codec.DecoderException;
/*   7:    */ import org.apache.commons.codec.EncoderException;
/*   8:    */ import org.apache.commons.codec.StringDecoder;
/*   9:    */ import org.apache.commons.codec.StringEncoder;
/*  10:    */ import org.apache.commons.codec.binary.Base64;
/*  11:    */ 
/*  12:    */ public class BCodec
/*  13:    */   extends RFC1522Codec
/*  14:    */   implements StringEncoder, StringDecoder
/*  15:    */ {
/*  16:    */   private final Charset charset;
/*  17:    */   
/*  18:    */   public BCodec()
/*  19:    */   {
/*  20: 56 */     this(Charsets.UTF_8);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public BCodec(Charset charset)
/*  24:    */   {
/*  25: 69 */     this.charset = charset;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public BCodec(String charsetName)
/*  29:    */   {
/*  30: 83 */     this(Charset.forName(charsetName));
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected String getEncoding()
/*  34:    */   {
/*  35: 88 */     return "B";
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected byte[] doEncoding(byte[] bytes)
/*  39:    */   {
/*  40: 93 */     if (bytes == null) {
/*  41: 94 */       return null;
/*  42:    */     }
/*  43: 96 */     return Base64.encodeBase64(bytes);
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected byte[] doDecoding(byte[] bytes)
/*  47:    */   {
/*  48:101 */     if (bytes == null) {
/*  49:102 */       return null;
/*  50:    */     }
/*  51:104 */     return Base64.decodeBase64(bytes);
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String encode(String value, Charset charset)
/*  55:    */     throws EncoderException
/*  56:    */   {
/*  57:120 */     if (value == null) {
/*  58:121 */       return null;
/*  59:    */     }
/*  60:123 */     return encodeText(value, charset);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String encode(String value, String charset)
/*  64:    */     throws EncoderException
/*  65:    */   {
/*  66:138 */     if (value == null) {
/*  67:139 */       return null;
/*  68:    */     }
/*  69:    */     try
/*  70:    */     {
/*  71:142 */       return encodeText(value, charset);
/*  72:    */     }
/*  73:    */     catch (UnsupportedEncodingException e)
/*  74:    */     {
/*  75:144 */       throw new EncoderException(e.getMessage(), e);
/*  76:    */     }
/*  77:    */   }
/*  78:    */   
/*  79:    */   public String encode(String value)
/*  80:    */     throws EncoderException
/*  81:    */   {
/*  82:159 */     if (value == null) {
/*  83:160 */       return null;
/*  84:    */     }
/*  85:162 */     return encode(value, getCharset());
/*  86:    */   }
/*  87:    */   
/*  88:    */   public String decode(String value)
/*  89:    */     throws DecoderException
/*  90:    */   {
/*  91:177 */     if (value == null) {
/*  92:178 */       return null;
/*  93:    */     }
/*  94:    */     try
/*  95:    */     {
/*  96:181 */       return decodeText(value);
/*  97:    */     }
/*  98:    */     catch (UnsupportedEncodingException e)
/*  99:    */     {
/* 100:183 */       throw new DecoderException(e.getMessage(), e);
/* 101:    */     }
/* 102:    */   }
/* 103:    */   
/* 104:    */   public Object encode(Object value)
/* 105:    */     throws EncoderException
/* 106:    */   {
/* 107:198 */     if (value == null) {
/* 108:199 */       return null;
/* 109:    */     }
/* 110:200 */     if ((value instanceof String)) {
/* 111:201 */       return encode((String)value);
/* 112:    */     }
/* 113:203 */     throw new EncoderException("Objects of type " + 
/* 114:204 */       value.getClass().getName() + 
/* 115:205 */       " cannot be encoded using BCodec");
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Object decode(Object value)
/* 119:    */     throws DecoderException
/* 120:    */   {
/* 121:222 */     if (value == null) {
/* 122:223 */       return null;
/* 123:    */     }
/* 124:224 */     if ((value instanceof String)) {
/* 125:225 */       return decode((String)value);
/* 126:    */     }
/* 127:227 */     throw new DecoderException("Objects of type " + 
/* 128:228 */       value.getClass().getName() + 
/* 129:229 */       " cannot be decoded using BCodec");
/* 130:    */   }
/* 131:    */   
/* 132:    */   public Charset getCharset()
/* 133:    */   {
/* 134:240 */     return this.charset;
/* 135:    */   }
/* 136:    */   
/* 137:    */   public String getDefaultCharset()
/* 138:    */   {
/* 139:249 */     return this.charset.name();
/* 140:    */   }
/* 141:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.net.BCodec
 * JD-Core Version:    0.7.0.1
 */