/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.GlobalVarDecl;
import com.bichler.iec.iec.GlobalVarSpec;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.SpecInit;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Global Var Decl</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.GlobalVarDeclImpl#getSpec <em>Spec</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.GlobalVarDeclImpl#getSpecInit <em>Spec Init</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GlobalVarDeclImpl extends MinimalEObjectImpl.Container implements GlobalVarDecl
{
  /**
   * The cached value of the '{@link #getSpec() <em>Spec</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSpec()
   * @generated
   * @ordered
   */
  protected GlobalVarSpec spec;

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
  protected GlobalVarDeclImpl()
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
    return IecPackage.Literals.GLOBAL_VAR_DECL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarSpec getSpec()
  {
    return spec;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSpec(GlobalVarSpec newSpec, NotificationChain msgs)
  {
    GlobalVarSpec oldSpec = spec;
    spec = newSpec;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.GLOBAL_VAR_DECL__SPEC, oldSpec, newSpec);
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
  public void setSpec(GlobalVarSpec newSpec)
  {
    if (newSpec != spec)
    {
      NotificationChain msgs = null;
      if (spec != null)
        msgs = ((InternalEObject)spec).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.GLOBAL_VAR_DECL__SPEC, null, msgs);
      if (newSpec != null)
        msgs = ((InternalEObject)newSpec).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.GLOBAL_VAR_DECL__SPEC, null, msgs);
      msgs = basicSetSpec(newSpec, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.GLOBAL_VAR_DECL__SPEC, newSpec, newSpec));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.GLOBAL_VAR_DECL__SPEC_INIT, oldSpecInit, newSpecInit);
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
        msgs = ((InternalEObject)specInit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.GLOBAL_VAR_DECL__SPEC_INIT, null, msgs);
      if (newSpecInit != null)
        msgs = ((InternalEObject)newSpecInit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.GLOBAL_VAR_DECL__SPEC_INIT, null, msgs);
      msgs = basicSetSpecInit(newSpecInit, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.GLOBAL_VAR_DECL__SPEC_INIT, newSpecInit, newSpecInit));
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
      case IecPackage.GLOBAL_VAR_DECL__SPEC:
        return basicSetSpec(null, msgs);
      case IecPackage.GLOBAL_VAR_DECL__SPEC_INIT:
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
      case IecPackage.GLOBAL_VAR_DECL__SPEC:
        return getSpec();
      case IecPackage.GLOBAL_VAR_DECL__SPEC_INIT:
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
      case IecPackage.GLOBAL_VAR_DECL__SPEC:
        setSpec((GlobalVarSpec)newValue);
        return;
      case IecPackage.GLOBAL_VAR_DECL__SPEC_INIT:
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
      case IecPackage.GLOBAL_VAR_DECL__SPEC:
        setSpec((GlobalVarSpec)null);
        return;
      case IecPackage.GLOBAL_VAR_DECL__SPEC_INIT:
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
      case IecPackage.GLOBAL_VAR_DECL__SPEC:
        return spec != null;
      case IecPackage.GLOBAL_VAR_DECL__SPEC_INIT:
        return specInit != null;
    }
    return super.eIsSet(featureID);
  }

} //GlobalVarDeclImpl
