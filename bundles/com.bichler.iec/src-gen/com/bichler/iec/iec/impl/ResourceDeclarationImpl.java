/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.GlobalVarDeclarations;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ResourceDeclaration;
import com.bichler.iec.iec.SingleResourceDeclaration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ResourceDeclarationImpl#getResname <em>Resname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ResourceDeclarationImpl#getGlobalVarDeclarations <em>Global Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ResourceDeclarationImpl#getSingleresource <em>Singleresource</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResourceDeclarationImpl extends LibraryElementDeclarationImpl implements ResourceDeclaration
{
  /**
   * The default value of the '{@link #getResname() <em>Resname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResname()
   * @generated
   * @ordered
   */
  protected static final String RESNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getResname() <em>Resname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResname()
   * @generated
   * @ordered
   */
  protected String resname = RESNAME_EDEFAULT;

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
   * The cached value of the '{@link #getSingleresource() <em>Singleresource</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSingleresource()
   * @generated
   * @ordered
   */
  protected SingleResourceDeclaration singleresource;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ResourceDeclarationImpl()
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
    return IecPackage.Literals.RESOURCE_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getResname()
  {
    return resname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setResname(String newResname)
  {
    String oldResname = resname;
    resname = newResname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.RESOURCE_DECLARATION__RESNAME, oldResname, resname));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS, oldGlobalVarDeclarations, newGlobalVarDeclarations);
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
        msgs = ((InternalEObject)globalVarDeclarations).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS, null, msgs);
      if (newGlobalVarDeclarations != null)
        msgs = ((InternalEObject)newGlobalVarDeclarations).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS, null, msgs);
      msgs = basicSetGlobalVarDeclarations(newGlobalVarDeclarations, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS, newGlobalVarDeclarations, newGlobalVarDeclarations));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SingleResourceDeclaration getSingleresource()
  {
    return singleresource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSingleresource(SingleResourceDeclaration newSingleresource, NotificationChain msgs)
  {
    SingleResourceDeclaration oldSingleresource = singleresource;
    singleresource = newSingleresource;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE, oldSingleresource, newSingleresource);
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
  public void setSingleresource(SingleResourceDeclaration newSingleresource)
  {
    if (newSingleresource != singleresource)
    {
      NotificationChain msgs = null;
      if (singleresource != null)
        msgs = ((InternalEObject)singleresource).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE, null, msgs);
      if (newSingleresource != null)
        msgs = ((InternalEObject)newSingleresource).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE, null, msgs);
      msgs = basicSetSingleresource(newSingleresource, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE, newSingleresource, newSingleresource));
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
      case IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return basicSetGlobalVarDeclarations(null, msgs);
      case IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE:
        return basicSetSingleresource(null, msgs);
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
      case IecPackage.RESOURCE_DECLARATION__RESNAME:
        return getResname();
      case IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return getGlobalVarDeclarations();
      case IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE:
        return getSingleresource();
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
      case IecPackage.RESOURCE_DECLARATION__RESNAME:
        setResname((String)newValue);
        return;
      case IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        setGlobalVarDeclarations((GlobalVarDeclarations)newValue);
        return;
      case IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE:
        setSingleresource((SingleResourceDeclaration)newValue);
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
      case IecPackage.RESOURCE_DECLARATION__RESNAME:
        setResname(RESNAME_EDEFAULT);
        return;
      case IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        setGlobalVarDeclarations((GlobalVarDeclarations)null);
        return;
      case IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE:
        setSingleresource((SingleResourceDeclaration)null);
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
      case IecPackage.RESOURCE_DECLARATION__RESNAME:
        return RESNAME_EDEFAULT == null ? resname != null : !RESNAME_EDEFAULT.equals(resname);
      case IecPackage.RESOURCE_DECLARATION__GLOBAL_VAR_DECLARATIONS:
        return globalVarDeclarations != null;
      case IecPackage.RESOURCE_DECLARATION__SINGLERESOURCE:
        return singleresource != null;
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
    result.append(" (resname: ");
    result.append(resname);
    result.append(')');
    return result.toString();
  }

} //ResourceDeclarationImpl
