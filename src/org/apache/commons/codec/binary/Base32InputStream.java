/*  1:   */ package org.apache.commons.codec.binary;
/*  2:   */ 
/*  3:   */ import java.io.InputStream;
/*  4:   */ 
/*  5:   */ public class Base32InputStream
/*  6:   */   extends BaseNCodecInputStream
/*  7:   */ {
/*  8:   */   public Base32InputStream(InputStream in)
/*  9:   */   {
/* 10:48 */     this(in, false);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public Base32InputStream(InputStream in, boolean doEncode)
/* 14:   */   {
/* 15:61 */     super(in, new Base32(false), doEncode);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Base32InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator)
/* 19:   */   {
/* 20:82 */     super(in, new Base32(lineLength, lineSeparator), doEncode);
/* 21:   */   }
/* 22:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.Base32InputStream
 * JD-Core Version:    0.7.0.1
 */