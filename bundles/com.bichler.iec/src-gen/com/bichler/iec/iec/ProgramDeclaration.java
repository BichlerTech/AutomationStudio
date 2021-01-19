/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgramDeclaration#getVarDeclarations <em>Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramDeclaration#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgramDeclaration()
 * @model
 * @generated
 */
public interface ProgramDeclaration extends LibraryElementDeclaration
{
  /**
   * Returns the value of the '<em><b>Var Declarations</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ProgramVarDeclarations}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var Declarations</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getProgramDeclaration_VarDeclarations()
   * @model containment="true"
   * @generated
   */
  EList<ProgramVarDeclarations> getVarDeclarations();

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(FunctionBlockBody)
   * @see com.bichler.iec.iec.IecPackage#getProgramDeclaration_Body()
   * @model containment="true"
   * @generated
   */
  FunctionBlockBody getBody();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramDeclaration#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(FunctionBlockBody value);

} // ProgramDeclaration
