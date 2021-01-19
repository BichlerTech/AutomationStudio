/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.GlobalVarDecl;
import com.bichler.iec.iec.GlobalVarDeclarations;
import com.bichler.iec.iec.IecPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Global Var Declarations</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.GlobalVarDeclarationsImpl#getGlobalVarDecl <em>Global Var Decl</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GlobalVarDeclarationsImpl extends MinimalEObjectImpl.Container implements GlobalVarDeclarations
{
  /**
   * The cached value of the '{@link #getGlobalVarDecl() <em>Global Var Decl</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalVarDecl()
   * @generated
   * @ordered
   */
  protected EList<GlobalVarDecl> globalVarDecl;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GlobalVarDeclarationsImpl()
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
    return IecPackage.Literals.GLOBAL_VAR_DECLARATIONS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<GlobalVarDecl> getGlobalVarDecl()
  {
    if (globalVarDecl == null)
    {
      globalVarDecl = new EObjectContainmentEList<GlobalVarDecl>(GlobalVarDecl.class, this, IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL);
    }
    return globalVarDecl;
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
      case IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL:
        return ((InternalEList<?>)getGlobalVarDecl()).basicRemove(otherEnd, msgs);
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
      case IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL:
        return getGlobalVarDecl();
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
      case IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL:
        getGlobalVarDecl().clear();
        getGlobalVarDecl().addAll((Collection<? extends GlobalVarDecl>)newValue);
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
      case IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL:
        getGlobalVarDecl().clear();
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
      case IecPackage.GLOBAL_VAR_DECLARATIONS__GLOBAL_VAR_DECL:
        return globalVarDecl != null && !globalVarDecl.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //GlobalVarDeclarationsImpl
