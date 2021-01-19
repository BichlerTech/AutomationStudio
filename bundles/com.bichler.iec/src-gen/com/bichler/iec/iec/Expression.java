/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.Expression#getVariable <em>Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Expression#getFbname <em>Fbname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Expression#getOpenbr <em>Openbr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Expression#getParamassignment <em>Paramassignment</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Expression#getClosebr <em>Closebr</em>}</li>
 *   <li>{@link com.bichler.iec.iec.Expression#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getExpression()
 * @model
 * @generated
 */
public interface Expression extends EObject
{
  /**
   * Returns the value of the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable</em>' containment reference.
   * @see #setVariable(VariableAccess)
   * @see com.bichler.iec.iec.IecPackage#getExpression_Variable()
   * @model containment="true"
   * @generated
   */
  VariableAccess getVariable();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Expression#getVariable <em>Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable</em>' containment reference.
   * @see #getVariable()
   * @generated
   */
  void setVariable(VariableAccess value);

  /**
   * Returns the value of the '<em><b>Fbname</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fbname</em>' reference.
   * @see #setFbname(FunctionDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getExpression_Fbname()
   * @model
   * @generated
   */
  FunctionDeclaration getFbname();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Expression#getFbname <em>Fbname</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fbname</em>' reference.
   * @see #getFbname()
   * @generated
   */
  void setFbname(FunctionDeclaration value);

  /**
   * Returns the value of the '<em><b>Openbr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Openbr</em>' attribute.
   * @see #setOpenbr(String)
   * @see com.bichler.iec.iec.IecPackage#getExpression_Openbr()
   * @model
   * @generated
   */
  String getOpenbr();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Expression#getOpenbr <em>Openbr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Openbr</em>' attribute.
   * @see #getOpenbr()
   * @generated
   */
  void setOpenbr(String value);

  /**
   * Returns the value of the '<em><b>Paramassignment</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.ParamAssignment}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Paramassignment</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getExpression_Paramassignment()
   * @model containment="true"
   * @generated
   */
  EList<ParamAssignment> getParamassignment();

  /**
   * Returns the value of the '<em><b>Closebr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Closebr</em>' attribute.
   * @see #setClosebr(String)
   * @see com.bichler.iec.iec.IecPackage#getExpression_Closebr()
   * @model
   * @generated
   */
  String getClosebr();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Expression#getClosebr <em>Closebr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Closebr</em>' attribute.
   * @see #getClosebr()
   * @generated
   */
  void setClosebr(String value);

  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(Expression)
   * @see com.bichler.iec.iec.IecPackage#getExpression_Expression()
   * @model containment="true"
   * @generated
   */
  Expression getExpression();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.Expression#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(Expression value);

} // Expression
