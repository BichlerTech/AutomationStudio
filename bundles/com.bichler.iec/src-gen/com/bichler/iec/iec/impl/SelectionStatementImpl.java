/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.SelectionStatement;
import com.bichler.iec.iec.StatementList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Selection Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.SelectionStatementImpl#getElseStatementList <em>Else Statement List</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SelectionStatementImpl extends StatementImpl implements SelectionStatement
{
  /**
   * The cached value of the '{@link #getElseStatementList() <em>Else Statement List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElseStatementList()
   * @generated
   * @ordered
   */
  protected StatementList elseStatementList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SelectionStatementImpl()
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
    return IecPackage.Literals.SELECTION_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList getElseStatementList()
  {
    return elseStatementList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetElseStatementList(StatementList newElseStatementList, NotificationChain msgs)
  {
    StatementList oldElseStatementList = elseStatementList;
    elseStatementList = newElseStatementList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST, oldElseStatementList, newElseStatementList);
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
  public void setElseStatementList(StatementList newElseStatementList)
  {
    if (newElseStatementList != elseStatementList)
    {
      NotificationChain msgs = null;
      if (elseStatementList != null)
        msgs = ((InternalEObject)elseStatementList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST, null, msgs);
      if (newElseStatementList != null)
        msgs = ((InternalEObject)newElseStatementList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST, null, msgs);
      msgs = basicSetElseStatementList(newElseStatementList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST, newElseStatementList, newElseStatementList));
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
      case IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST:
        return basicSetElseStatementList(null, msgs);
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
      case IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST:
        return getElseStatementList();
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
      case IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST:
        setElseStatementList((StatementList)newValue);
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
      case IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST:
        setElseStatementList((StatementList)null);
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
      case IecPackage.SELECTION_STATEMENT__ELSE_STATEMENT_LIST:
        return elseStatementList != null;
    }
    return super.eIsSet(featureID);
  }

} //SelectionStatementImpl
