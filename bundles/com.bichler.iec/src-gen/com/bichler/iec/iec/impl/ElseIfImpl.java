/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ElseIf;
import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.StatementList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Else If</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ElseIfImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ElseIfImpl#getStatementList <em>Statement List</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ElseIfImpl extends MinimalEObjectImpl.Container implements ElseIf
{
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
   * The cached value of the '{@link #getStatementList() <em>Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatementList()
   * @generated
   * @ordered
   */
  protected StatementList statementList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ElseIfImpl()
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
    return IecPackage.Literals.ELSE_IF;
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ELSE_IF__EXPRESSION, oldExpression, newExpression);
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
        msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ELSE_IF__EXPRESSION, null, msgs);
      if (newExpression != null)
        msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ELSE_IF__EXPRESSION, null, msgs);
      msgs = basicSetExpression(newExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ELSE_IF__EXPRESSION, newExpression, newExpression));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList getStatementList()
  {
    return statementList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetStatementList(StatementList newStatementList, NotificationChain msgs)
  {
    StatementList oldStatementList = statementList;
    statementList = newStatementList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ELSE_IF__STATEMENT_LIST, oldStatementList, newStatementList);
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
  public void setStatementList(StatementList newStatementList)
  {
    if (newStatementList != statementList)
    {
      NotificationChain msgs = null;
      if (statementList != null)
        msgs = ((InternalEObject)statementList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ELSE_IF__STATEMENT_LIST, null, msgs);
      if (newStatementList != null)
        msgs = ((InternalEObject)newStatementList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ELSE_IF__STATEMENT_LIST, null, msgs);
      msgs = basicSetStatementList(newStatementList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ELSE_IF__STATEMENT_LIST, newStatementList, newStatementList));
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
      case IecPackage.ELSE_IF__EXPRESSION:
        return basicSetExpression(null, msgs);
      case IecPackage.ELSE_IF__STATEMENT_LIST:
        return basicSetStatementList(null, msgs);
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
      case IecPackage.ELSE_IF__EXPRESSION:
        return getExpression();
      case IecPackage.ELSE_IF__STATEMENT_LIST:
        return getStatementList();
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
      case IecPackage.ELSE_IF__EXPRESSION:
        setExpression((Expression)newValue);
        return;
      case IecPackage.ELSE_IF__STATEMENT_LIST:
        setStatementList((StatementList)newValue);
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
      case IecPackage.ELSE_IF__EXPRESSION:
        setExpression((Expression)null);
        return;
      case IecPackage.ELSE_IF__STATEMENT_LIST:
        setStatementList((StatementList)null);
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
      case IecPackage.ELSE_IF__EXPRESSION:
        return expression != null;
      case IecPackage.ELSE_IF__STATEMENT_LIST:
        return statementList != null;
    }
    return super.eIsSet(featureID);
  }

} //ElseIfImpl
