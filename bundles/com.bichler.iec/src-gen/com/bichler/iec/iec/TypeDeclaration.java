/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.TypeDeclaration#getDerivedType <em>Derived Type</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getTypeDeclaration()
 * @model
 * @generated
 */
public interface TypeDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Derived Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Derived Type</em>' containment reference.
   * @see #setDerivedType(DerivedType)
   * @see com.bichler.iec.iec.IecPackage#getTypeDeclaration_DerivedType()
   * @model containment="true"
   * @generated
   */
  DerivedType getDerivedType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.TypeDeclaration#getDerivedType <em>Derived Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Derived Type</em>' containment reference.
   * @see #getDerivedType()
   * @generated
   */
  void setDerivedType(DerivedType value);

} // TypeDeclaration
