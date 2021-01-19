/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Output Declarations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.InputOutputDeclarations#getInitDecls <em>Init Decls</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getInputOutputDeclarations()
 * @model
 * @generated
 */
public interface InputOutputDeclarations extends IoVarDeclarations
{
  /**
   * Returns the value of the '<em><b>Init Decls</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.VarInitDecl}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Init Decls</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getInputOutputDeclarations_InitDecls()
   * @model containment="true"
   * @generated
   */
  EList<VarInitDecl> getInitDecls();

} // InputOutputDeclarations
