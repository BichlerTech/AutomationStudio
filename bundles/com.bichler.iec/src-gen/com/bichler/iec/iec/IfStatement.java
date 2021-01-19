/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.IfStatement#getIfExpression <em>If Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.IfStatement#getThenStatementList <em>Then Statement List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.IfStatement#getElseIfs <em>Else Ifs</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getIfStatement()
 * @model
 * @generated
 */
public interface IfStatement extends SelectionStatement
{
  /**
   * Returns the value of the '<em><b>If Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>If Expression</em>' containment reference.
   * @see #setIfExpression(Expression)
   * @see com.bichler.iec.iec.IecPackage#getIfStatement_IfExpression()
   * @model containment="true"
   * @generated
   */
  Expression getIfExpression();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.IfStatement#getIfExpression <em>If Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>If Expression</em>' containment reference.
   * @see #getIfExpression()
   * @generated
   */
  void setIfExpression(Expression value);

  /**
   * Returns the value of the '<em><b>Then Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Then Statement List</em>' containment reference.
   * @see #setThenStatementList(StatementList)
   * @see com.bichler.iec.iec.IecPackage#getIfStatement_ThenStatementList()
   * @model containment="true"
   * @generated
   */
  StatementList getThenStatementList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.IfStatement#getThenStatementList <em>Then Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Then Statement List</em>' containment reference.
   * @see #getThenStatementList()
   * @generated
   */
  void setThenStatementList(StatementList value);

  /**
   * Returns the value of the '<em><b>Else Ifs</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ElseIf}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else Ifs</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getIfStatement_ElseIfs()
   * @model containment="true"
   * @generated
   */
  EList<ElseIf> getElseIfs();

} // IfStatement
