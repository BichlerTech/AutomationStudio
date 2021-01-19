/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enumeration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Enumeration#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getEnumeration()
 * @model
 * @generated
 */
public interface Enumeration extends EObject
{
  /**
   * Returns the value of the '<em><b>Values</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.EnumeratedValue}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Values</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getEnumeration_Values()
   * @model containment="true"
   * @generated
   */
  EList<EnumeratedValue> getValues();

} // Enumeration
