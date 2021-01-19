/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Var List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.GlobalVarList#getVariables <em>Variables</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getGlobalVarList()
 * @model
 * @generated
 */
public interface GlobalVarList extends GlobalVarSpec
{
  /**
   * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.GlobalVar}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variables</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarList_Variables()
   * @model containment="true"
   * @generated
   */
  EList<GlobalVar> getVariables();

} // GlobalVarList
