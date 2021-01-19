/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Param Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ParamAssignment#getVariablename <em>Variablename</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ParamAssignment#getExpression <em>Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ParamAssignment#isNot <em>Not</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ParamAssignment#getVariable1 <em>Variable1</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ParamAssignment#getVariable2 <em>Variable2</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getParamAssignment()
 * @model
 * @generated
 */
public interface ParamAssignment extends EObject
{
  /**
   * Returns the value of the '<em><b>Variablename</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variablename</em>' reference.
   * @see #setVariablename(NamedVariableAccess)
   * @see com.bichler.iec.iec.IecPackage#getParamAssignment_Variablename()
   * @model
   * @generated
   */
  NamedVariableAccess getVariablename();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ParamAssignment#getVariablename <em>Variablename</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variablename</em>' reference.
   * @see #getVariablename()
   * @generated
   */
  void setVariablename(NamedVariableAccess value);

  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(Expression)
   * @see com.bichler.iec.iec.IecPackage#getParamAssignment_Expression()
   * @model containment="true"
   * @generated
   */
  Expression getExpression();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ParamAssignment#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(Expression value);

  /**
   * Returns the value of the '<em><b>Not</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Not</em>' attribute.
   * @see #setNot(boolean)
   * @see com.bichler.iec.iec.IecPackage#getParamAssignment_Not()
   * @model
   * @generated
   */
  boolean isNot();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ParamAssignment#isNot <em>Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Not</em>' attribute.
   * @see #isNot()
   * @generated
   */
  void setNot(boolean value);

  /**
   * Returns the value of the '<em><b>Variable1</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable1</em>' reference.
   * @see #setVariable1(NamedVariableAccess)
   * @see com.bichler.iec.iec.IecPackage#getParamAssignment_Variable1()
   * @model
   * @generated
   */
  NamedVariableAccess getVariable1();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ParamAssignment#getVariable1 <em>Variable1</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable1</em>' reference.
   * @see #getVariable1()
   * @generated
   */
  void setVariable1(NamedVariableAccess value);

  /**
   * Returns the value of the '<em><b>Variable2</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable2</em>' reference.
   * @see #setVariable2(Variable)
   * @see com.bichler.iec.iec.IecPackage#getParamAssignment_Variable2()
   * @model
   * @generated
   */
  Variable getVariable2();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ParamAssignment#getVariable2 <em>Variable2</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable2</em>' reference.
   * @see #getVariable2()
   * @generated
   */
  void setVariable2(Variable value);

} // ParamAssignment
