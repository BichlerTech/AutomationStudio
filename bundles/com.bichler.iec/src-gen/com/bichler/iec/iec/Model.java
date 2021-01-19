/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Model#getModelElement <em>Model Element</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Model Element</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ModelElement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Model Element</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getModel_ModelElement()
   * @model containment="true"
   * @generated
   */
  EList<ModelElement> getModelElement();

} // Model
