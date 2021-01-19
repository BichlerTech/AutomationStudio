/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.FunctionDeclaration#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.FunctionDeclaration#getType <em>Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.FunctionDeclaration#getIoVarDeclarations <em>Io Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.FunctionDeclaration#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getFunctionDeclaration()
 * @model
 * @generated
 */
public interface FunctionDeclaration extends LibraryElement
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.bichler.iec.iec.IecPackage#getFunctionDeclaration_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.FunctionDeclaration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' containment reference.
   * @see #setType(NonGenericType)
   * @see com.bichler.iec.iec.IecPackage#getFunctionDeclaration_Type()
   * @model containment="true"
   * @generated
   */
  NonGenericType getType();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.FunctionDeclaration#getType <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' containment reference.
   * @see #getType()
   * @generated
   */
  void setType(NonGenericType value);

  /**
   * Returns the value of the '<em><b>Io Var Declarations</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.IoVarDeclarations}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Io Var Declarations</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getFunctionDeclaration_IoVarDeclarations()
   * @model containment="true"
   * @generated
   */
  EList<IoVarDeclarations> getIoVarDeclarations();

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(FunctionBody)
   * @see com.bichler.iec.iec.IecPackage#getFunctionDeclaration_Body()
   * @model containment="true"
   * @generated
   */
  FunctionBody getBody();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.FunctionDeclaration#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(FunctionBody value);

} // FunctionDeclaration
