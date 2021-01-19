/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Selection Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.SelectionStatement#getElseStatementList <em>Else Statement List</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getSelectionStatement()
 * @model
 * @generated
 */
public interface SelectionStatement extends Statement
{
  /**
   * Returns the value of the '<em><b>Else Statement List</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else Statement List</em>' containment reference.
   * @see #setElseStatementList(StatementList)
   * @see com.bichler.iec.iec.IecPackage#getSelectionStatement_ElseStatementList()
   * @model containment="true"
   * @generated
   */
  StatementList getElseStatementList();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.SelectionStatement#getElseStatementList <em>Else Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Else Statement List</em>' containment reference.
   * @see #getElseStatementList()
   * @generated
   */
  void setElseStatementList(StatementList value);

} // SelectionStatement
