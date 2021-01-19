/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.SimpleOperation#getOperator <em>Operator</em>}</li>
 *   <li>{@link com.bichler.iec.iec.SimpleOperation#getOperand <em>Operand</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getSimpleOperation()
 * @model
 * @generated
 */
public interface SimpleOperation extends Operation, SimpleInstruction
{
  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see #setOperator(String)
   * @see com.bichler.iec.iec.IecPackage#getSimpleOperation_Operator()
   * @model
   * @generated
   */
  String getOperator();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SimpleOperation#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see #getOperator()
   * @generated
   */
  void setOperator(String value);

  /**
   * Returns the value of the '<em><b>Operand</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operand</em>' containment reference.
   * @see #setOperand(Operand)
   * @see com.bichler.iec.iec.IecPackage#getSimpleOperation_Operand()
   * @model containment="true"
   * @generated
   */
  Operand getOperand();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SimpleOperation#getOperand <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operand</em>' containment reference.
   * @see #getOperand()
   * @generated
   */
  void setOperand(Operand value);

} // SimpleOperation
