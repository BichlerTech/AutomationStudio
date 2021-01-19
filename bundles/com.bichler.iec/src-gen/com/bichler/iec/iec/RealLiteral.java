/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Real Literal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.RealLiteral#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getRealLiteral()
 * @model
 * @generated
 */
public interface RealLiteral extends NumericLiteral
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(RealType)
   * @see com.bichler.iec.iec.IecPackage#getRealLiteral_Type()
   * @model containment="true"
   * @generated
   */
  RealType getType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.RealLiteral#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(RealType value);

} // RealLiteral
