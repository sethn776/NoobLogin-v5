/*   1:    */ package org.apache.commons.codec.binary;
/*   2:    */ 
/*   3:    */ import java.io.FilterInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ 
/*   7:    */ public class BaseNCodecInputStream
/*   8:    */   extends FilterInputStream
/*   9:    */ {
/*  10:    */   private final BaseNCodec baseNCodec;
/*  11:    */   private final boolean doEncode;
/*  12: 40 */   private final byte[] singleByte = new byte[1];
/*  13: 42 */   private final BaseNCodec.Context context = new BaseNCodec.Context();
/*  14:    */   
/*  15:    */   protected BaseNCodecInputStream(InputStream in, BaseNCodec baseNCodec, boolean doEncode)
/*  16:    */   {
/*  17: 45 */     super(in);
/*  18: 46 */     this.doEncode = doEncode;
/*  19: 47 */     this.baseNCodec = baseNCodec;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public int available()
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 64 */     return this.context.eof ? 0 : 1;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public synchronized void mark(int readLimit) {}
/*  29:    */   
/*  30:    */   public boolean markSupported()
/*  31:    */   {
/*  32: 85 */     return false;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public int read()
/*  36:    */     throws IOException
/*  37:    */   {
/*  38: 97 */     int r = read(this.singleByte, 0, 1);
/*  39: 98 */     while (r == 0) {
/*  40: 99 */       r = read(this.singleByte, 0, 1);
/*  41:    */     }
/*  42:101 */     if (r > 0)
/*  43:    */     {
/*  44:102 */       byte b = this.singleByte[0];
/*  45:103 */       return b < 0 ? 256 + b : b;
/*  46:    */     }
/*  47:105 */     return -1;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int read(byte[] b, int offset, int len)
/*  51:    */     throws IOException
/*  52:    */   {
/*  53:129 */     if (b == null) {
/*  54:130 */       throw new NullPointerException();
/*  55:    */     }
/*  56:131 */     if ((offset < 0) || (len < 0)) {
/*  57:132 */       throw new IndexOutOfBoundsException();
/*  58:    */     }
/*  59:133 */     if ((offset > b.length) || (offset + len > b.length)) {
/*  60:134 */       throw new IndexOutOfBoundsException();
/*  61:    */     }
/*  62:135 */     if (len == 0) {
/*  63:136 */       return 0;
/*  64:    */     }
/*  65:138 */     int readLen = 0;
/*  66:155 */     while (readLen == 0)
/*  67:    */     {
/*  68:156 */       if (!this.baseNCodec.hasData(this.context))
/*  69:    */       {
/*  70:157 */         byte[] buf = new byte[this.doEncode ? 4096 : 8192];
/*  71:158 */         int c = this.in.read(buf);
/*  72:159 */         if (this.doEncode) {
/*  73:160 */           this.baseNCodec.encode(buf, 0, c, this.context);
/*  74:    */         } else {
/*  75:162 */           this.baseNCodec.decode(buf, 0, c, this.context);
/*  76:    */         }
/*  77:    */       }
/*  78:165 */       readLen = this.baseNCodec.readResults(b, offset, len, this.context);
/*  79:    */     }
/*  80:167 */     return readLen;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public synchronized void reset()
/*  84:    */     throws IOException
/*  85:    */   {
/*  86:181 */     throw new IOException("mark/reset not supported");
/*  87:    */   }
/*  88:    */   
/*  89:    */   public long skip(long n)
/*  90:    */     throws IOException
/*  91:    */   {
/*  92:192 */     if (n < 0L) {
/*  93:193 */       throw new IllegalArgumentException("Negative skip length: " + n);
/*  94:    */     }
/*  95:197 */     byte[] b = new byte[512];
/*  96:198 */     long todo = n;
/*  97:200 */     while (todo > 0L)
/*  98:    */     {
/*  99:201 */       int len = (int)Math.min(b.length, todo);
/* 100:202 */       len = read(b, 0, len);
/* 101:203 */       if (len == -1) {
/* 102:    */         break;
/* 103:    */       }
/* 104:206 */       todo -= len;
/* 105:    */     }
/* 106:209 */     return n - todo;
/* 107:    */   }
/* 108:    */ }


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.binary.BaseNCodecInputStream
 * JD-Core Version:    0.7.0.1
 */