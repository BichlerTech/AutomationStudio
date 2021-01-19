/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.CaseList#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getCaseList()
 * @model
 * @generated
 */
public interface CaseList extends EObject
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.CaseListElement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getCaseList_Elements()
   * @model containment="true"
   * @generated
   */
  EList<CaseListElement> getElements();

} // CaseList
