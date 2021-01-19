/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Operand#getConstant <em>Constant</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Operand#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getOperand()
 * @model
 * @generated
 */
public interface Operand extends EObject
{
  /**
   * Returns the value of the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constant</em>' containment reference.
   * @see #setConstant(Constant)
   * @see com.bichler.iec.iec.IecPackage#getOperand_Constant()
   * @model containment="true"
   * @generated
   */
  Constant getConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Operand#getConstant <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' containment reference.
   * @see #getConstant()
   * @generated
   */
  void setConstant(Constant value);

  /**
   * Returns the value of the '<em><b>Reference</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reference</em>' containment reference.
   * @see #setReference(ReferencedOperand)
   * @see com.bichler.iec.iec.IecPackage#getOperand_Reference()
   * @model containment="true"
   * @generated
   */
  ReferencedOperand getReference();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Operand#getReference <em>Reference</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reference</em>' containment reference.
   * @see #getReference()
   * @generated
   */
  void setReference(ReferencedOperand value);

} // Operand
