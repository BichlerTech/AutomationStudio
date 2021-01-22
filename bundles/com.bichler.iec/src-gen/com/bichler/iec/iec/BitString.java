/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bit String</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.BitString#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getBitString()
 * @model
 * @generated
 */
public interface BitString extends Constant
{
  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see #setValue(String)
   * @see com.bichler.iec.iec.IecPackage#getBitString_Value()
   * @model
   * @generated
   */
  String getValue();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.BitString#getValue <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Value</em>' attribute.
   * @see #getValue()
   * @generated
   */
  void setValue(String value);

} // BitString