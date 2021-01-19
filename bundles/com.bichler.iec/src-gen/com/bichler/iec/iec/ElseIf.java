/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Else If</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ElseIf#getExpression <em>Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ElseIf#getStatementList <em>Statement List</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getElseIf()
 * @model
 * @generated
 */
public interface ElseIf extends EObject
{
  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(Expression)
   * @see com.bichler.iec.iec.IecPackage#getElseIf_Expression()
   * @model containment="true"
   * @generated
   */
  Expression getExpression();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ElseIf#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(Expression value);

  /**
   * Returns the value of the '<em><b>Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Statement List</em>' containment reference.
   * @see #setStatementList(StatementList)
   * @see com.bichler.iec.iec.IecPackage#getElseIf_StatementList()
   * @model containment="true"
   * @generated
   */
  StatementList getStatementList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ElseIf#getStatementList <em>Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Statement List</em>' containment reference.
   * @see #getStatementList()
   * @generated
   */
  void setStatementList(StatementList value);

} // ElseIf
