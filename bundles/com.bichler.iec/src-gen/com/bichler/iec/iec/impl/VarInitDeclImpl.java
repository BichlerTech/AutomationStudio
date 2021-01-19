/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.SpecInit;
import com.bichler.iec.iec.Var1List;
import com.bichler.iec.iec.VarInitDecl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Var Init Decl</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.VarInitDeclImpl#getVar1List <em>Var1 List</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.VarInitDeclImpl#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VarInitDeclImpl extends MinimalEObjectImpl.Container implements VarInitDecl
{
  /**
   * The cached value of the '{@link #getVar1List() <em>Var1 List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVar1List()
   * @generated
   * @ordered
   */
  protected Var1List var1List;

  /**
   * The cached value of the '{@link #getSpecInit() <em>Spec Init</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSpecInit()
   * @generated
   * @ordered
   */
  protected SpecInit specInit;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected VarInitDeclImpl()
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
    return IecPackage.Literals.VAR_INIT_DECL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Var1List getVar1List()
  {
    return var1List;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetVar1List(Var1List newVar1List, NotificationChain msgs)
  {
    Var1List oldVar1List = var1List;
    var1List = newVar1List;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.VAR_INIT_DECL__VAR1_LIST, oldVar1List, newVar1List);
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
  public void setVar1List(Var1List newVar1List)
  {
    if (newVar1List != var1List)
    {
      NotificationChain msgs = null;
      if (var1List != null)
        msgs = ((InternalEObject)var1List).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.VAR_INIT_DECL__VAR1_LIST, null, msgs);
      if (newVar1List != null)
        msgs = ((InternalEObject)newVar1List).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.VAR_INIT_DECL__VAR1_LIST, null, msgs);
      msgs = basicSetVar1List(newVar1List, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.VAR_INIT_DECL__VAR1_LIST, newVar1List, newVar1List));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SpecInit getSpecInit()
  {
    return specInit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSpecInit(SpecInit newSpecInit, NotificationChain msgs)
  {
    SpecInit oldSpecInit = specInit;
    specInit = newSpecInit;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.VAR_INIT_DECL__SPEC_INIT, oldSpecInit, newSpecInit);
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
  public void setSpecInit(SpecInit newSpecInit)
  {
    if (newSpecInit != specInit)
    {
      NotificationChain msgs = null;
      if (specInit != null)
        msgs = ((InternalEObject)specInit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.VAR_INIT_DECL__SPEC_INIT, null, msgs);
      if (newSpecInit != null)
        msgs = ((InternalEObject)newSpecInit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.VAR_INIT_DECL__SPEC_INIT, null, msgs);
      msgs = basicSetSpecInit(newSpecInit, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.VAR_INIT_DECL__SPEC_INIT, newSpecInit, newSpecInit));
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
      case IecPackage.VAR_INIT_DECL__VAR1_LIST:
        return basicSetVar1List(null, msgs);
      case IecPackage.VAR_INIT_DECL__SPEC_INIT:
        return basicSetSpecInit(null, msgs);
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
      case IecPackage.VAR_INIT_DECL__VAR1_LIST:
        return getVar1List();
      case IecPackage.VAR_INIT_DECL__SPEC_INIT:
        return getSpecInit();
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
      case IecPackage.VAR_INIT_DECL__VAR1_LIST:
        setVar1List((Var1List)newValue);
        return;
      case IecPackage.VAR_INIT_DECL__SPEC_INIT:
        setSpecInit((SpecInit)newValue);
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
      case IecPackage.VAR_INIT_DECL__VAR1_LIST:
        setVar1List((Var1List)null);
        return;
      case IecPackage.VAR_INIT_DECL__SPEC_INIT:
        setSpecInit((SpecInit)null);
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
      case IecPackage.VAR_INIT_DECL__VAR1_LIST:
        return var1List != null;
      case IecPackage.VAR_INIT_DECL__SPEC_INIT:
        return specInit != null;
    }
    return super.eIsSet(featureID);
  }

} //VarInitDeclImpl
