/*   1:    */ package org.apache.commons.codec;
/*   2:    */ 
/*   3:    */ import java.nio.charset.Charset;
/*   4:    */ 
/*   5:    */ public class Charsets
/*   6:    */ {
/*   7:    */   public static Charset toCharset(Charset charset)
/*   8:    */   {
/*   9: 74 */     return charset == null ? Charset.defaultCharset() : charset;
/*  10:    */   }
/*  11:    */   
/*  12:    */   public static Charset toCharset(String charset)
/*  13:    */   {
/*  14: 87 */     return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
/*  15:    */   }
/*  16:    */   
/*  17: 97 */   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*  18:106 */   public static final Charset US_ASCII = Charset.forName("US-ASCII");
/*  19:116 */   public static final Charset UTF_16 = Charset.forName("UTF-16");
/*  20:125 */   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
/*  21:134 */   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
/*  22:143 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*  23:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.Charsets
 * JD-Core Version:    0.7.0.1
 */