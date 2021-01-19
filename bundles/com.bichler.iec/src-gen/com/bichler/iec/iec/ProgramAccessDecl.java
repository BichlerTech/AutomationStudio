/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Program Access Decl</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ProgramAccessDecl#getAccessName <em>Access Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramAccessDecl#getSymbolicVariable <em>Symbolic Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramAccessDecl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ProgramAccessDecl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecl()
 * @model
 * @generated
 */
public interface ProgramAccessDecl extends EObject
{
  /**
   * Returns the value of the '<em><b>Access Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Access Name</em>' containment reference.
   * @see #setAccessName(Variable)
   * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecl_AccessName()
   * @model containment="true"
   * @generated
   */
  Variable getAccessName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramAccessDecl#getAccessName <em>Access Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Access Name</em>' containment reference.
   * @see #getAccessName()
   * @generated
   */
  void setAccessName(Variable value);

  /**
   * Returns the value of the '<em><b>Symbolic Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Symbolic Variable</em>' containment reference.
   * @see #setSymbolicVariable(NamedVariableAccess)
   * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecl_SymbolicVariable()
   * @model containment="true"
   * @generated
   */
  NamedVariableAccess getSymbolicVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramAccessDecl#getSymbolicVariable <em>Symbolic Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Symbolic Variable</em>' containment reference.
   * @see #getSymbolicVariable()
   * @generated
   */
  void setSymbolicVariable(NamedVariableAccess value);

  /**
   * Returns the value of the '<em><b>Type Name</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type Name</em>' containment reference.
   * @see #setTypeName(NonGenericType)
   * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecl_TypeName()
   * @model containment="true"
   * @generated
   */
  NonGenericType getTypeName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramAccessDecl#getTypeName <em>Type Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type Name</em>' containment reference.
   * @see #getTypeName()
   * @generated
   */
  void setTypeName(NonGenericType value);

  /**
   * Returns the value of the '<em><b>Direction</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Direction</em>' attribute.
   * @see #setDirection(String)
   * @see com.bichler.iec.iec.IecPackage#getProgramAccessDecl_Direction()
   * @model
   * @generated
   */
  String getDirection();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ProgramAccessDecl#getDirection <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Direction</em>' attribute.
   * @see #getDirection()
   * @generated
   */
  void setDirection(String value);

} // ProgramAccessDecl
