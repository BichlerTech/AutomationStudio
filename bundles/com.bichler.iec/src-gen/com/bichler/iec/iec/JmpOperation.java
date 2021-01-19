/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Jmp Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.JmpOperation#getOperator <em>Operator</em>}</li>
 *   <li>{@link com.bichler.iec.iec.JmpOperation#getLabel <em>Label</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getJmpOperation()
 * @model
 * @generated
 */
public interface JmpOperation extends Operation
{
  /**
   * Returns the value of the '<em><b>Operator</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Operator</em>' attribute.
   * @see #setOperator(String)
   * @see com.bichler.iec.iec.IecPackage#getJmpOperation_Operator()
   * @model
   * @generated
   */
  String getOperator();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.JmpOperation#getOperator <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Operator</em>' attribute.
   * @see #getOperator()
   * @generated
   */
  void setOperator(String value);

  /**
   * Returns the value of the '<em><b>Label</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' reference.
   * @see #setLabel(Label)
   * @see com.bichler.iec.iec.IecPackage#getJmpOperation_Label()
   * @model
   * @generated
   */
  Label getLabel();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.JmpOperation#getLabel <em>Label</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' reference.
   * @see #getLabel()
   * @generated
   */
  void setLabel(Label value);

} // JmpOperation
