/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spec Init</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.SpecInit#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.SpecInit#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getSpecInit()
 * @model
 * @generated
 */
public interface SpecInit extends EObject
{
  /**
   * Returns the value of the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Base Type</em>' containment reference.
   * @see #setBaseType(NonGenericType)
   * @see com.bichler.iec.iec.IecPackage#getSpecInit_BaseType()
   * @model containment="true"
   * @generated
   */
  NonGenericType getBaseType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SpecInit#getBaseType <em>Base Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Base Type</em>' containment reference.
   * @see #getBaseType()
   * @generated
   */
  void setBaseType(NonGenericType value);

  /**
   * Returns the value of the '<em><b>Constant</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constant</em>' containment reference.
   * @see #setConstant(InitialElement)
   * @see com.bichler.iec.iec.IecPackage#getSpecInit_Constant()
   * @model containment="true"
   * @generated
   */
  InitialElement getConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SpecInit#getConstant <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' containment reference.
   * @see #getConstant()
   * @generated
   */
  void setConstant(InitialElement value);

} // SpecInit
