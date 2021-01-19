/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statement List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StatementList#getStatements <em>Statements</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStatementList()
 * @model
 * @generated
 */
public interface StatementList extends FunctionBody, FunctionBlockBody
{
  /**
   * Returns the value of the '<em><b>Statements</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.Statement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Statements</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getStatementList_Statements()
   * @model containment="true"
   * @generated
   */
  EList<Statement> getStatements();

} // StatementList
