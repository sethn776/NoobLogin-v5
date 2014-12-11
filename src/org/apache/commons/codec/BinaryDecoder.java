package org.apache.commons.codec;

public abstract interface BinaryDecoder
  extends Decoder
{
  public abstract byte[] decode(byte[] paramArrayOfByte)
    throws DecoderException;
}


/* Location:           C:\Users\Ducagoose\AppData\Locals\NoobLogin.jar
 * Qualified Name:     org.apache.commons.codec.BinaryDecoder
 * JD-Core Version:    0.7.0.1
 */