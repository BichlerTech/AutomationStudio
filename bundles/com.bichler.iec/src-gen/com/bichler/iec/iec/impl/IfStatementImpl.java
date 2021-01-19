/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ElseIf;
import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.IfStatement;
import com.bichler.iec.iec.StatementList;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.IfStatementImpl#getIfExpression <em>If Expression</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.IfStatementImpl#getThenStatementList <em>Then Statement List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.IfStatementImpl#getElseIfs <em>Else Ifs</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfStatementImpl extends SelectionStatementImpl implements IfStatement
{
  /**
   * The cached value of the '{@link #getIfExpression() <em>If Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIfExpression()
   * @generated
   * @ordered
   */
  protected Expression ifExpression;

  /**
   * The cached value of the '{@link #getThenStatementList() <em>Then Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThenStatementList()
   * @generated
   * @ordered
   */
  protected StatementList thenStatementList;

  /**
   * The cached value of the '{@link #getElseIfs() <em>Else Ifs</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElseIfs()
   * @generated
   * @ordered
   */
  protected EList<ElseIf> elseIfs;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IfStatementImpl()
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
    return IecPackage.Literals.IF_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getIfExpression()
  {
    return ifExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIfExpression(Expression newIfExpression, NotificationChain msgs)
  {
    Expression oldIfExpression = ifExpression;
    ifExpression = newIfExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.IF_STATEMENT__IF_EXPRESSION, oldIfExpression, newIfExpression);
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
  public void setIfExpression(Expression newIfExpression)
  {
    if (newIfExpression != ifExpression)
    {
      NotificationChain msgs = null;
      if (ifExpression != null)
        msgs = ((InternalEObject)ifExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.IF_STATEMENT__IF_EXPRESSION, null, msgs);
      if (newIfExpression != null)
        msgs = ((InternalEObject)newIfExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.IF_STATEMENT__IF_EXPRESSION, null, msgs);
      msgs = basicSetIfExpression(newIfExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.IF_STATEMENT__IF_EXPRESSION, newIfExpression, newIfExpression));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList getThenStatementList()
  {
    return thenStatementList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetThenStatementList(StatementList newThenStatementList, NotificationChain msgs)
  {
    StatementList oldThenStatementList = thenStatementList;
    thenStatementList = newThenStatementList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST, oldThenStatementList, newThenStatementList);
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
  public void setThenStatementList(StatementList newThenStatementList)
  {
    if (newThenStatementList != thenStatementList)
    {
      NotificationChain msgs = null;
      if (thenStatementList != null)
        msgs = ((InternalEObject)thenStatementList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST, null, msgs);
      if (newThenStatementList != null)
        msgs = ((InternalEObject)newThenStatementList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST, null, msgs);
      msgs = basicSetThenStatementList(newThenStatementList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST, newThenStatementList, newThenStatementList));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ElseIf> getElseIfs()
  {
    if (elseIfs == null)
    {
      elseIfs = new EObjectContainmentEList<ElseIf>(ElseIf.class, this, IecPackage.IF_STATEMENT__ELSE_IFS);
    }
    return elseIfs;
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
      case IecPackage.IF_STATEMENT__IF_EXPRESSION:
        return basicSetIfExpression(null, msgs);
      case IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST:
        return basicSetThenStatementList(null, msgs);
      case IecPackage.IF_STATEMENT__ELSE_IFS:
        return ((InternalEList<?>)getElseIfs()).basicRemove(otherEnd, msgs);
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
      case IecPackage.IF_STATEMENT__IF_EXPRESSION:
        return getIfExpression();
      case IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST:
        return getThenStatementList();
      case IecPackage.IF_STATEMENT__ELSE_IFS:
        return getElseIfs();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.IF_STATEMENT__IF_EXPRESSION:
        setIfExpression((Expression)newValue);
        return;
      case IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST:
        setThenStatementList((StatementList)newValue);
        return;
      case IecPackage.IF_STATEMENT__ELSE_IFS:
        getElseIfs().clear();
        getElseIfs().addAll((Collection<? extends ElseIf>)newValue);
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
      case IecPackage.IF_STATEMENT__IF_EXPRESSION:
        setIfExpression((Expression)null);
        return;
      case IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST:
        setThenStatementList((StatementList)null);
        return;
      case IecPackage.IF_STATEMENT__ELSE_IFS:
        getElseIfs().clear();
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
      case IecPackage.IF_STATEMENT__IF_EXPRESSION:
        return ifExpression != null;
      case IecPackage.IF_STATEMENT__THEN_STATEMENT_LIST:
        return thenStatementList != null;
      case IecPackage.IF_STATEMENT__ELSE_IFS:
        return elseIfs != null && !elseIfs.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //IfStatementImpl
