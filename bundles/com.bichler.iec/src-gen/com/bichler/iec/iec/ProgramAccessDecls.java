/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program Access Decls</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgramAccessDecls#getProgramAccessDecl <em>Program Access Decl</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecls()
 * @model
 * @generated
 */
public interface ProgramAccessDecls extends ProgramVarDeclarations
{
  /**
   * Returns the value of the '<em><b>Program Access Decl</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ProgramAccessDecl}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Program Access Decl</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecls_ProgramAccessDecl()
   * @model containment="true"
   * @generated
   */
  EList<ProgramAccessDecl> getProgramAccessDecl();

} // ProgramAccessDecls
