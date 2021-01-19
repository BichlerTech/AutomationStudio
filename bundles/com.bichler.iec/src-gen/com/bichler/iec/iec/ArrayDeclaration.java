/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Array Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ArrayDeclaration#getRanges <em>Ranges</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ArrayDeclaration#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ArrayDeclaration#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getArrayDeclaration()
 * @model
 * @generated
 */
public interface ArrayDeclaration extends TypeDeclaration
{
  /**
   * Returns the value of the '<em><b>Ranges</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ranges</em>' attribute list.
   * @see com.bichler.iec.iec.IecPackage#getArrayDeclaration_Ranges()
   * @model unique="false"
   * @generated
   */
  EList<String> getRanges();

  /**
   * Returns the value of the '<em><b>Base Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Base Type</em>' containment reference.
   * @see #setBaseType(NonGenericType)
   * @see com.bichler.iec.iec.IecPackage#getArrayDeclaration_BaseType()
   * @model containment="true"
   * @generated
   */
  NonGenericType getBaseType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ArrayDeclaration#getBaseType <em>Base Type</em>}' containment reference.
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
   * @see #setConstant(ArrayInitialization)
   * @see com.bichler.iec.iec.IecPackage#getArrayDeclaration_Constant()
   * @model containment="true"
   * @generated
   */
  ArrayInitialization getConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ArrayDeclaration#getConstant <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' containment reference.
   * @see #getConstant()
   * @generated
   */
  void setConstant(ArrayInitialization value);

} // ArrayDeclaration
