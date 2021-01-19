/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structure Element Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructureElementDeclaration#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StructureElementDeclaration#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructureElementDeclaration()
 * @model
 * @generated
 */
public interface StructureElementDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getStructureElementDeclaration_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureElementDeclaration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Spec Init</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec Init</em>' containment reference.
   * @see #setSpecInit(SpecInit)
   * @see com.bichler.iec.iec.IecPackage#getStructureElementDeclaration_SpecInit()
   * @model containment="true"
   * @generated
   */
  SpecInit getSpecInit();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureElementDeclaration#getSpecInit <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec Init</em>' containment reference.
   * @see #getSpecInit()
   * @generated
   */
  void setSpecInit(SpecInit value);

} // StructureElementDeclaration
