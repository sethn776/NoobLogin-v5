/*  1:   */ package org.apache.commons.codec.digest;
/*  2:   */ 
/*  3:   */ import java.util.Random;
/*  4:   */ 
/*  5:   */ class B64
/*  6:   */ {
/*  7:   */   static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
/*  8:   */   
/*  9:   */   static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer)
/* 10:   */   {
/* 11:57 */     int w = b2 << 16 & 0xFFFFFF | b1 << 8 & 0xFFFF | b0 & 0xFF;
/* 12:   */     
/* 13:59 */     int n = outLen;
/* 14:60 */     while (n-- > 0)
/* 15:   */     {
/* 16:61 */       buffer.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(w & 0x3F));
/* 17:62 */       w >>= 6;
/* 18:   */     }
/* 19:   */   }
/* 20:   */   
/* 21:   */   static String getRandomSalt(int num)
/* 22:   */   {
/* 23:73 */     StringBuilder saltString = new StringBuilder();
/* 24:74 */     for (int i = 1; i <= num; i++) {
/* 25:75 */       saltString.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(new Random().nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
/* 26:   */     }
/* 27:77 */     return saltString.toString();
/* 28:   */   }
/* 29:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.digest.B64
 * JD-Core Version:    0.7.0.1
 */