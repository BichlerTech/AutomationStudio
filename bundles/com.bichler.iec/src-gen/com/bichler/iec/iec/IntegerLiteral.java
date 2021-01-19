/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Integer Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.IntegerLiteral#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getIntegerLiteral()
 * @model
 * @generated
 */
public interface IntegerLiteral extends NumericLiteral
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(IntegerType)
   * @see com.bichler.iec.iec.IecPackage#getIntegerLiteral_Type()
   * @model containment="true"
   * @generated
   */
  IntegerType getType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.IntegerLiteral#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(IntegerType value);

} // IntegerLiteral
