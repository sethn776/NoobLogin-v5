/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.io.FilterOutputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.OutputStream;
/*   6:    */ 
/*   7:    */ public class BaseNCodecOutputStream
/*   8:    */   extends FilterOutputStream
/*   9:    */ {
/*  10:    */   private final boolean doEncode;
/*  11:    */   private final BaseNCodec baseNCodec;
/*  12: 40 */   private final byte[] singleByte = new byte[1];
/*  13: 42 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*  14:    */   
/*  15:    */   public BaseNCodecOutputStream(OutputStream out, BaseNCodec basedCodec, boolean doEncode)
/*  16:    */   {
/*  17: 46 */     super(out);
/*  18: 47 */     this.baseNCodec = basedCodec;
/*  19: 48 */     this.doEncode = doEncode;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void write(int i)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 61 */     this.singleByte[0] = ((byte)i);
/*  26: 62 */     write(this.singleByte, 0, 1);
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void write(byte[] b, int offset, int len)
/*  30:    */     throws IOException
/*  31:    */   {
/*  32: 85 */     if (b == null) {
/*  33: 86 */       throw new NullPointerException();
/*  34:    */     }
/*  35: 87 */     if ((offset < 0) || (len < 0)) {
/*  36: 88 */       throw new IndexOutOfBoundsException();
/*  37:    */     }
/*  38: 89 */     if ((offset > b.length) || (offset + len > b.length)) {
/*  39: 90 */       throw new IndexOutOfBoundsException();
/*  40:    */     }
/*  41: 91 */     if (len > 0)
/*  42:    */     {
/*  43: 92 */       if (this.doEncode) {
/*  44: 93 */         this.baseNCodec.encode(b, offset, len, this.context);
/*  45:    */       } else {
/*  46: 95 */         this.baseNCodec.decode(b, offset, len, this.context);
/*  47:    */       }
/*  48: 97 */       flush(false);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   private void flush(boolean propagate)
/*  53:    */     throws IOException
/*  54:    */   {
/*  55:111 */     int avail = this.baseNCodec.available(this.context);
/*  56:112 */     if (avail > 0)
/*  57:    */     {
/*  58:113 */       byte[] buf = new byte[avail];
/*  59:114 */       int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
/*  60:115 */       if (c > 0) {
/*  61:116 */         this.out.write(buf, 0, c);
/*  62:    */       }
/*  63:    */     }
/*  64:119 */     if (propagate) {
/*  65:120 */       this.out.flush();
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void flush()
/*  70:    */     throws IOException
/*  71:    */   {
/*  72:132 */     flush(true);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void close()
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:144 */     if (this.doEncode) {
/*  79:145 */       this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
/*  80:    */     } else {
/*  81:147 */       this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
/*  82:    */     }
/*  83:149 */     flush();
/*  84:150 */     this.out.close();
/*  85:    */   }
/*  86:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.BaseNCodecOutputStream
 * JD-Core Version:    0.7.0.1
 */