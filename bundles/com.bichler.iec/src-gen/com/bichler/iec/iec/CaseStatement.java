/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Case Statement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.CaseStatement#getExpression <em>Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.CaseStatement#getCaseElements <em>Case Elements</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getCaseStatement()
 * @model
 * @generated
 */
public interface CaseStatement extends SelectionStatement
{
  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(Expression)
   * @see com.bichler.iec.iec.IecPackage#getCaseStatement_Expression()
   * @model containment="true"
   * @generated
   */
  Expression getExpression();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.CaseStatement#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(Expression value);

  /**
   * Returns the value of the '<em><b>Case Elements</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.CaseElement}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Case Elements</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getCaseStatement_CaseElements()
   * @model containment="true"
   * @generated
   */
  EList<CaseElement> getCaseElements();

} // CaseStatement
