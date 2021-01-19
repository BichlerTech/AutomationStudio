/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.NamedVariableAccess;
import com.bichler.iec.iec.ParamAssignment;
import com.bichler.iec.iec.Variable;

import java.lang.Boolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Param Assignment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ParamAssignmentImpl#getVariablename <em>Variablename</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ParamAssignmentImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ParamAssignmentImpl#isNot <em>Not</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ParamAssignmentImpl#getVariable1 <em>Variable1</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ParamAssignmentImpl#getVariable2 <em>Variable2</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParamAssignmentImpl extends MinimalEObjectImpl.Container implements ParamAssignment
{
  /**
   * The cached value of the '{@link #getVariablename() <em>Variablename</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariablename()
   * @generated
   * @ordered
   */
  protected NamedVariableAccess variablename;

  /**
   * The cached value of the '{@link #getExpression() <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpression()
   * @generated
   * @ordered
   */
  protected Expression expression;

  /**
   * The default value of the '{@link #isNot() <em>Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNot()
   * @generated
   * @ordered
   */
  protected static final boolean NOT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isNot() <em>Not</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNot()
   * @generated
   * @ordered
   */
  protected boolean not = NOT_EDEFAULT;

  /**
   * The cached value of the '{@link #getVariable1() <em>Variable1</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariable1()
   * @generated
   * @ordered
   */
  protected NamedVariableAccess variable1;

  /**
   * The cached value of the '{@link #getVariable2() <em>Variable2</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVariable2()
   * @generated
   * @ordered
   */
  protected Variable variable2;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParamAssignmentImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return IecPackage.Literals.PARAM_ASSIGNMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NamedVariableAccess getVariablename()
  {
    if (variablename != null && variablename.eIsProxy())
    {
      InternalEObject oldVariablename = (InternalEObject)variablename;
      variablename = (NamedVariableAccess)eResolveProxy(oldVariablename);
      if (variablename != oldVariablename)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.PARAM_ASSIGNMENT__VARIABLENAME, oldVariablename, variablename));
      }
    }
    return variablename;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NamedVariableAccess basicGetVariablename()
  {
    return variablename;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVariablename(NamedVariableAccess newVariablename)
  {
    NamedVariableAccess oldVariablename = variablename;
    variablename = newVariablename;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__VARIABLENAME, oldVariablename, variablename));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getExpression()
  {
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpression(Expression newExpression, NotificationChain msgs)
  {
    Expression oldExpression = expression;
    expression = newExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__EXPRESSION, oldExpression, newExpression);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setExpression(Expression newExpression)
  {
    if (newExpression != expression)
    {
      NotificationChain msgs = null;
      if (expression != null)
        msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PARAM_ASSIGNMENT__EXPRESSION, null, msgs);
      if (newExpression != null)
        msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PARAM_ASSIGNMENT__EXPRESSION, null, msgs);
      msgs = basicSetExpression(newExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__EXPRESSION, newExpression, newExpression));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isNot()
  {
    return not;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setNot(boolean newNot)
  {
    boolean oldNot = not;
    not = newNot;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__NOT, oldNot, not));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NamedVariableAccess getVariable1()
  {
    if (variable1 != null && variable1.eIsProxy())
    {
      InternalEObject oldVariable1 = (InternalEObject)variable1;
      variable1 = (NamedVariableAccess)eResolveProxy(oldVariable1);
      if (variable1 != oldVariable1)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.PARAM_ASSIGNMENT__VARIABLE1, oldVariable1, variable1));
      }
    }
    return variable1;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NamedVariableAccess basicGetVariable1()
  {
    return variable1;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVariable1(NamedVariableAccess newVariable1)
  {
    NamedVariableAccess oldVariable1 = variable1;
    variable1 = newVariable1;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__VARIABLE1, oldVariable1, variable1));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable getVariable2()
  {
    if (variable2 != null && variable2.eIsProxy())
    {
      InternalEObject oldVariable2 = (InternalEObject)variable2;
      variable2 = (Variable)eResolveProxy(oldVariable2);
      if (variable2 != oldVariable2)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.PARAM_ASSIGNMENT__VARIABLE2, oldVariable2, variable2));
      }
    }
    return variable2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetVariable2()
  {
    return variable2;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVariable2(Variable newVariable2)
  {
    Variable oldVariable2 = variable2;
    variable2 = newVariable2;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PARAM_ASSIGNMENT__VARIABLE2, oldVariable2, variable2));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case IecPackage.PARAM_ASSIGNMENT__EXPRESSION:
        return basicSetExpression(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case IecPackage.PARAM_ASSIGNMENT__VARIABLENAME:
        if (resolve) return getVariablename();
        return basicGetVariablename();
      case IecPackage.PARAM_ASSIGNMENT__EXPRESSION:
        return getExpression();
      case IecPackage.PARAM_ASSIGNMENT__NOT:
        return isNot();
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE1:
        if (resolve) return getVariable1();
        return basicGetVariable1();
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE2:
        if (resolve) return getVariable2();
        return basicGetVariable2();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.PARAM_ASSIGNMENT__VARIABLENAME:
        setVariablename((NamedVariableAccess)newValue);
        return;
      case IecPackage.PARAM_ASSIGNMENT__EXPRESSION:
        setExpression((Expression)newValue);
        return;
      case IecPackage.PARAM_ASSIGNMENT__NOT:
        setNot((Boolean)newValue);
        return;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE1:
        setVariable1((NamedVariableAccess)newValue);
        return;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE2:
        setVariable2((Variable)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.PARAM_ASSIGNMENT__VARIABLENAME:
        setVariablename((NamedVariableAccess)null);
        return;
      case IecPackage.PARAM_ASSIGNMENT__EXPRESSION:
        setExpression((Expression)null);
        return;
      case IecPackage.PARAM_ASSIGNMENT__NOT:
        setNot(NOT_EDEFAULT);
        return;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE1:
        setVariable1((NamedVariableAccess)null);
        return;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE2:
        setVariable2((Variable)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.PARAM_ASSIGNMENT__VARIABLENAME:
        return variablename != null;
      case IecPackage.PARAM_ASSIGNMENT__EXPRESSION:
        return expression != null;
      case IecPackage.PARAM_ASSIGNMENT__NOT:
        return not != NOT_EDEFAULT;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE1:
        return variable1 != null;
      case IecPackage.PARAM_ASSIGNMENT__VARIABLE2:
        return variable2 != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (not: ");
    result.append(not);
    result.append(')');
    return result.toString();
  }

} //ParamAssignmentImpl
