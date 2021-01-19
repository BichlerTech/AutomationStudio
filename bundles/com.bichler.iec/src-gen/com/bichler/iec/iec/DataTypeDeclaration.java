/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.DataTypeDeclaration#getTypeDeclaration <em>Type Declaration</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getDataTypeDeclaration()
 * @model
 * @generated
 */
public interface DataTypeDeclaration extends LibraryElementDeclaration
{
  /**
   * Returns the value of the '<em><b>Type Declaration</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.TypeDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type Declaration</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getDataTypeDeclaration_TypeDeclaration()
   * @model containment="true"
   * @generated
   */
  EList<TypeDeclaration> getTypeDeclaration();

} // DataTypeDeclaration
