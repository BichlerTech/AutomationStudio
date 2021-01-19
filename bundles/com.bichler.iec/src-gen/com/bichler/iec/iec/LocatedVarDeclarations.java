/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Located Var Declarations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.LocatedVarDeclarations#getLocatedVarDeclaration <em>Located Var Declaration</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclarations()
 * @model
 * @generated
 */
public interface LocatedVarDeclarations extends ProgramVarDeclarations
{
  /**
   * Returns the value of the '<em><b>Located Var Declaration</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.LocatedVarDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Located Var Declaration</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getLocatedVarDeclarations_LocatedVarDeclaration()
   * @model containment="true"
   * @generated
   */
  EList<LocatedVarDeclaration> getLocatedVarDeclaration();

} // LocatedVarDeclarations
