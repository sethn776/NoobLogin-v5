/*  1:   */ package org.apache.commons.codec;
/*  2:   */ 
/*  3:   */ import java.util.Comparator;
/*  4:   */ 
/*  5:   */ public class StringEncoderComparator
/*  6:   */   implements Comparator
/*  7:   */ {
/*  8:   */   private final StringEncoder stringEncoder;
/*  9:   */   
/* 10:   */   @Deprecated
/* 11:   */   public StringEncoderComparator()
/* 12:   */   {
/* 13:48 */     this.stringEncoder = null;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public StringEncoderComparator(StringEncoder stringEncoder)
/* 17:   */   {
/* 18:58 */     this.stringEncoder = stringEncoder;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public int compare(Object o1, Object o2)
/* 22:   */   {
/* 23:77 */     int compareCode = 0;
/* 24:   */     try
/* 25:   */     {
/* 26:82 */       Comparable<Comparable<?>> s1 = (Comparable)this.stringEncoder.encode(o1);
/* 27:83 */       Comparable<?> s2 = (Comparable)this.stringEncoder.encode(o2);
/* 28:84 */       compareCode = s1.compareTo(s2);
/* 29:   */     }
/* 30:   */     catch (EncoderException ee)
/* 31:   */     {
/* 32:86 */       compareCode = 0;
/* 33:   */     }
/* 34:88 */     return compareCode;
/* 35:   */   }
/* 36:   */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.StringEncoderComparator
 * JD-Core Version:    0.7.0.1
 */