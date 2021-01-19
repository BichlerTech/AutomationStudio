/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Location#getDirectVariable <em>Direct Variable</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getLocation()
 * @model
 * @generated
 */
public interface Location extends EObject
{
  /**
   * Returns the value of the '<em><b>Direct Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Direct Variable</em>' containment reference.
   * @see #setDirectVariable(DirectVariable)
   * @see com.bichler.iec.iec.IecPackage#getLocation_DirectVariable()
   * @model containment="true"
   * @generated
   */
  DirectVariable getDirectVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Location#getDirectVariable <em>Direct Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Direct Variable</em>' containment reference.
   * @see #getDirectVariable()
   * @generated
   */
  void setDirectVariable(DirectVariable value);

} // Location
