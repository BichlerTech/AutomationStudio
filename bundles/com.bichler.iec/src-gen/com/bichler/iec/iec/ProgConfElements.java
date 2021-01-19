/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Prog Conf Elements</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgConfElements#getProgconf <em>Progconf</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgConfElements()
 * @model
 * @generated
 */
public interface ProgConfElements extends EObject
{
  /**
   * Returns the value of the '<em><b>Progconf</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ProgConfElement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Progconf</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getProgConfElements_Progconf()
   * @model containment="true"
   * @generated
   */
  EList<ProgConfElement> getProgconf();

} // ProgConfElements
