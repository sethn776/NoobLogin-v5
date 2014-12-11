/*  1:   */ package org.apache.commons.codec.binary;
/*  2:   */ 
/*  3:   */ import java.io.OutputStream;
/*  4:   */ 
/*  5:   */ public class Base32OutputStream
/*  6:   */   extends BaseNCodecOutputStream
/*  7:   */ {
/*  8:   */   public Base32OutputStream(OutputStream out)
/*  9:   */   {
/* 10:48 */     this(out, true);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public Base32OutputStream(OutputStream out, boolean doEncode)
/* 14:   */   {
/* 15:61 */     super(out, new Base32(false), doEncode);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public Base32OutputStream(OutputStream out, boolean doEncode, int lineLength, byte[] lineSeparator)
/* 19:   */   {
/* 20:82 */     super(out, new Base32(lineLength, lineSeparator), doEncode);
/* 21:   */   }
/* 22:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.Base32OutputStream
 * JD-Core Version:    0.7.0.1
 */