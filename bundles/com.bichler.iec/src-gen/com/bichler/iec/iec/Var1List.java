/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var1 List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Var1List#getVariables <em>Variables</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getVar1List()
 * @model
 * @generated
 */
public interface Var1List extends EObject
{
  /**
   * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.Variable}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variables</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getVar1List_Variables()
   * @model containment="true"
   * @generated
   */
  EList<Variable> getVariables();

} // Var1List
