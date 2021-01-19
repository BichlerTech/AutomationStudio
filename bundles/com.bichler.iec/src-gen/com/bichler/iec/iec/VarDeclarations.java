/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Declarations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.VarDeclarations#isConstant <em>Constant</em>}</li>
 *   <li>{@link com.bichler.iec.iec.VarDeclarations#getInitDecls <em>Init Decls</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getVarDeclarations()
 * @model
 * @generated
 */
public interface VarDeclarations extends OtherVarDeclarations
{
  /**
   * Returns the value of the '<em><b>Constant</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Constant</em>' attribute.
   * @see #setConstant(boolean)
   * @see com.bichler.iec.iec.IecPackage#getVarDeclarations_Constant()
   * @model
   * @generated
   */
  boolean isConstant();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.VarDeclarations#isConstant <em>Constant</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Constant</em>' attribute.
   * @see #isConstant()
   * @generated
   */
  void setConstant(boolean value);

  /**
   * Returns the value of the '<em><b>Init Decls</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.VarInitDecl}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Init Decls</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getVarDeclarations_InitDecls()
   * @model containment="true"
   * @generated
   */
  EList<VarInitDecl> getInitDecls();

} // VarDeclarations
