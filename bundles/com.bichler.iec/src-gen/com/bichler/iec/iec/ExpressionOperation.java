/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ExpressionOperation#getOperator <em>Operator</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ExpressionOperation#getOperand <em>Operand</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ExpressionOperation#getSimpleInstructionList <em>Simple Instruction List</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getExpressionOperation()
 * @model
 * @generated
 */
public interface ExpressionOperation extends Operation, SimpleInstruction
{
  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see #setOperator(String)
   * @see com.bichler.iec.iec.IecPackage#getExpressionOperation_Operator()
   * @model
   * @generated
   */
  String getOperator();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ExpressionOperation#getOperator <em>Operator</em>}' attribute.
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
   * @see com.bichler.iec.iec.IecPackage#getExpressionOperation_Operand()
   * @model containment="true"
   * @generated
   */
  Operand getOperand();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ExpressionOperation#getOperand <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operand</em>' containment reference.
   * @see #getOperand()
   * @generated
   */
  void setOperand(Operand value);

  /**
   * Returns the value of the '<em><b>Simple Instruction List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Simple Instruction List</em>' containment reference.
   * @see #setSimpleInstructionList(SimpleInstructionList)
   * @see com.bichler.iec.iec.IecPackage#getExpressionOperation_SimpleInstructionList()
   * @model containment="true"
   * @generated
   */
  SimpleInstructionList getSimpleInstructionList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ExpressionOperation#getSimpleInstructionList <em>Simple Instruction List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Simple Instruction List</em>' containment reference.
   * @see #getSimpleInstructionList()
   * @generated
   */
  void setSimpleInstructionList(SimpleInstructionList value);

} // ExpressionOperation
