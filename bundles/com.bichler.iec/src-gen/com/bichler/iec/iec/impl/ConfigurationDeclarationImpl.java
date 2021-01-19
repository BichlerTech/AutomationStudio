/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ConfigurationDeclaration;
import com.bichler.iec.iec.GlobalVarDeclarations;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ResourceDeclaration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ConfigurationDeclarationImpl#getGlobalVarDeclarations <em>Global Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ConfigurationDeclarationImpl#getResdecl <em>Resdecl</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConfigurationDeclarationImpl extends LibraryElementDeclarationImpl implements ConfigurationDeclaration
{
  /**
   * The cached value of the '{@link #getGlobalVarDeclarations() <em>Global Var Declarations</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalVarDeclarations()
   * @generated
   * @ordered
   */
  protected GlobalVarDeclarations globalVarDeclarations;

  /**
   * The cached value of the '{@link #getResdecl() <em>Resdecl</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResdecl()
   * @generated
   * @ordered
   */
  protected ResourceDeclaration resdecl;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConfigurationDeclarationImpl()
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
    return IecPackage.Literals.CONFIGURATION_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GlobalVarDeclarations getGlobalVarDeclarations()
  {
    return globalVarDeclarations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGlobalVarDeclarations(GlobalVarDeclarations newGlobalVarDeclarations, NotificationChain msgs)
  {
    GlobalVarDeclarations oldGlobalVarDeclarations = globalVarDeclarations;
    globalVarDeclarations = newGlobalVarDeclarations;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS, oldGlobalVarDeclarations, newGlobalVarDeclarations);
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
  public void setGlobalVarDeclarations(GlobalVarDeclarations newGlobalVarDeclarations)
  {
    if (newGlobalVarDeclarations != globalVarDeclarations)
    {
      NotificationChain msgs = null;
      if (globalVarDeclarations != null)
        msgs = ((InternalEObject)globalVarDeclarations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS, null, msgs);
      if (newGlobalVarDeclarations != null)
        msgs = ((InternalEObject)newGlobalVarDeclarations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS, null, msgs);
      msgs = basicSetGlobalVarDeclarations(newGlobalVarDeclarations, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS, newGlobalVarDeclarations, newGlobalVarDeclarations));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceDeclaration getResdecl()
  {
    return resdecl;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetResdecl(ResourceDeclaration newResdecl, NotificationChain msgs)
  {
    ResourceDeclaration oldResdecl = resdecl;
    resdecl = newResdecl;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.CONFIGURATION_DECLARATION__RESDECL, oldResdecl, newResdecl);
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
  public void setResdecl(ResourceDeclaration newResdecl)
  {
    if (newResdecl != resdecl)
    {
      NotificationChain msgs = null;
      if (resdecl != null)
        msgs = ((InternalEObject)resdecl).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONFIGURATION_DECLARATION__RESDECL, null, msgs);
      if (newResdecl != null)
        msgs = ((InternalEObject)newResdecl).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.CONFIGURATION_DECLARATION__RESDECL, null, msgs);
      msgs = basicSetResdecl(newResdecl, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CONFIGURATION_DECLARATION__RESDECL, newResdecl, newResdecl));
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
      case IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return basicSetGlobalVarDeclarations(null, msgs);
      case IecPackage.CONFIGURATION_DECLARATION__RESDECL:
        return basicSetResdecl(null, msgs);
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
      case IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return getGlobalVarDeclarations();
      case IecPackage.CONFIGURATION_DECLARATION__RESDECL:
        return getResdecl();
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
      case IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        setGlobalVarDeclarations((GlobalVarDeclarations)newValue);
        return;
      case IecPackage.CONFIGURATION_DECLARATION__RESDECL:
        setResdecl((ResourceDeclaration)newValue);
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
      case IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        setGlobalVarDeclarations((GlobalVarDeclarations)null);
        return;
      case IecPackage.CONFIGURATION_DECLARATION__RESDECL:
        setResdecl((ResourceDeclaration)null);
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
      case IecPackage.CONFIGURATION_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return globalVarDeclarations != null;
      case IecPackage.CONFIGURATION_DECLARATION__RESDECL:
        return resdecl != null;
    }
    return super.eIsSet(featureID);
  }

} //ConfigurationDeclarationImpl
