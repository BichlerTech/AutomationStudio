/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.CaseElement;
import com.bichler.iec.iec.CaseList;
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
 * An implementation of the model object '<em><b>Case Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.CaseElementImpl#getCaseList <em>Case List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.CaseElementImpl#getStatementList <em>Statement List</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseElementImpl extends MinimalEObjectImpl.Container implements CaseElement
{
  /**
   * The cached value of the '{@link #getCaseList() <em>Case List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCaseList()
   * @generated
   * @ordered
   */
  protected CaseList caseList;

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
  protected CaseElementImpl()
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
    return IecPackage.Literals.CASE_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CaseList getCaseList()
  {
    return caseList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCaseList(CaseList newCaseList, NotificationChain msgs)
  {
    CaseList oldCaseList = caseList;
    caseList = newCaseList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CASE_ELEMENT__CASE_LIST, oldCaseList, newCaseList);
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
  public void setCaseList(CaseList newCaseList)
  {
    if (newCaseList != caseList)
    {
      NotificationChain msgs = null;
      if (caseList != null)
        msgs = ((InternalEObject)caseList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CASE_ELEMENT__CASE_LIST, null, msgs);
      if (newCaseList != null)
        msgs = ((InternalEObject)newCaseList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CASE_ELEMENT__CASE_LIST, null, msgs);
      msgs = basicSetCaseList(newCaseList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CASE_ELEMENT__CASE_LIST, newCaseList, newCaseList));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CASE_ELEMENT__STATEMENT_LIST, oldStatementList, newStatementList);
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
        msgs = ((InternalEObject)statementList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CASE_ELEMENT__STATEMENT_LIST, null, msgs);
      if (newStatementList != null)
        msgs = ((InternalEObject)newStatementList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CASE_ELEMENT__STATEMENT_LIST, null, msgs);
      msgs = basicSetStatementList(newStatementList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CASE_ELEMENT__STATEMENT_LIST, newStatementList, newStatementList));
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
      case IecPackage.CASE_ELEMENT__CASE_LIST:
        return basicSetCaseList(null, msgs);
      case IecPackage.CASE_ELEMENT__STATEMENT_LIST:
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
      case IecPackage.CASE_ELEMENT__CASE_LIST:
        return getCaseList();
      case IecPackage.CASE_ELEMENT__STATEMENT_LIST:
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
      case IecPackage.CASE_ELEMENT__CASE_LIST:
        setCaseList((CaseList)newValue);
        return;
      case IecPackage.CASE_ELEMENT__STATEMENT_LIST:
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
      case IecPackage.CASE_ELEMENT__CASE_LIST:
        setCaseList((CaseList)null);
        return;
      case IecPackage.CASE_ELEMENT__STATEMENT_LIST:
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
      case IecPackage.CASE_ELEMENT__CASE_LIST:
        return caseList != null;
      case IecPackage.CASE_ELEMENT__STATEMENT_LIST:
        return statementList != null;
    }
    return super.eIsSet(featureID);
  }

} //CaseElementImpl
