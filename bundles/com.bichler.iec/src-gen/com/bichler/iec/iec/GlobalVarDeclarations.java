/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Var Declarations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.GlobalVarDeclarations#getGlobalVarDecl <em>Global Var Decl</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getGlobalVarDeclarations()
 * @model
 * @generated
 */
public interface GlobalVarDeclarations extends EObject
{
  /**
   * Returns the value of the '<em><b>Global Var Decl</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.GlobalVarDecl}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Global Var Decl</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getGlobalVarDeclarations_GlobalVarDecl()
   * @model containment="true"
   * @generated
   */
  EList<GlobalVarDecl> getGlobalVarDecl();

} // GlobalVarDeclarations
