package com.bichler.astudio.editor.allenbradley.xml;

import org.opcfoundation.ua.builtintypes.StatusCode;

// import com.hbsoft.comdrv.utils.ComByteMessage;
public interface AllenBradleyTransformation
{
  /**
   * transforms a device byte array into internal opc ua value as object
   * 
   * @param value
   * @return
   */
  // public abstract Object transToIntern(ComByteMessage value);
  /**
   * transforms an internal opc ua data value into device byte array
   * 
   * @param value
   * @return
   */
  public abstract byte[] transToDevice(Object value);

  public abstract StatusCode getStatusCode();

  public abstract void setStatusCode(StatusCode stcode);

  public abstract boolean verifyValueRange(Object value);
}
