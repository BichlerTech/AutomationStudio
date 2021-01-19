/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structure Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructureDeclaration#getStructureElement <em>Structure Element</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructureDeclaration()
 * @model
 * @generated
 */
public interface StructureDeclaration extends EObject
{
  /**
   * Returns the value of the '<em><b>Structure Element</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.StructureElementDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Structure Element</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getStructureDeclaration_StructureElement()
   * @model containment="true"
   * @generated
   */
  EList<StructureElementDeclaration> getStructureElement();

} // StructureDeclaration
