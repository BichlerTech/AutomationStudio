/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.LocatedVarDeclaration;
import com.bichler.iec.iec.LocatedVarDeclarations;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Located Var Declarations</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.LocatedVarDeclarationsImpl#getLocatedVarDeclaration <em>Located Var Declaration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocatedVarDeclarationsImpl extends ProgramVarDeclarationsImpl implements LocatedVarDeclarations
{
  /**
   * The cached value of the '{@link #getLocatedVarDeclaration() <em>Located Var Declaration</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocatedVarDeclaration()
   * @generated
   * @ordered
   */
  protected EList<LocatedVarDeclaration> locatedVarDeclaration;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LocatedVarDeclarationsImpl()
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
    return IecPackage.Literals.LOCATED_VAR_DECLARATIONS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<LocatedVarDeclaration> getLocatedVarDeclaration()
  {
    if (locatedVarDeclaration == null)
    {
      locatedVarDeclaration = new EObjectContainmentEList<LocatedVarDeclaration>(LocatedVarDeclaration.class, this, IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION);
    }
    return locatedVarDeclaration;
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
      case IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION:
        return ((InternalEList<?>)getLocatedVarDeclaration()).basicRemove(otherEnd, msgs);
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
      case IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION:
        return getLocatedVarDeclaration();
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
      case IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION:
        getLocatedVarDeclaration().clear();
        getLocatedVarDeclaration().addAll((Collection<? extends LocatedVarDeclaration>)newValue);
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
      case IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION:
        getLocatedVarDeclaration().clear();
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
      case IecPackage.LOCATED_VAR_DECLARATIONS__LOCATED_VAR_DECLARATION:
        return locatedVarDeclaration != null && !locatedVarDeclaration.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //LocatedVarDeclarationsImpl
