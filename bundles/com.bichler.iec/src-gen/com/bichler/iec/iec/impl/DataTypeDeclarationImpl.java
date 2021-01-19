/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.DataTypeDeclaration;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.TypeDeclaration;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Type Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.DataTypeDeclarationImpl#getTypeDeclaration <em>Type Declaration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataTypeDeclarationImpl extends LibraryElementDeclarationImpl implements DataTypeDeclaration
{
  /**
   * The cached value of the '{@link #getTypeDeclaration() <em>Type Declaration</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeDeclaration()
   * @generated
   * @ordered
   */
  protected EList<TypeDeclaration> typeDeclaration;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DataTypeDeclarationImpl()
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
    return IecPackage.Literals.DATA_TYPE_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<TypeDeclaration> getTypeDeclaration()
  {
    if (typeDeclaration == null)
    {
      typeDeclaration = new EObjectContainmentEList<TypeDeclaration>(TypeDeclaration.class, this, IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION);
    }
    return typeDeclaration;
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
      case IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION:
        return ((InternalEList<?>)getTypeDeclaration()).basicRemove(otherEnd, msgs);
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
      case IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION:
        return getTypeDeclaration();
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
      case IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION:
        getTypeDeclaration().clear();
        getTypeDeclaration().addAll((Collection<? extends TypeDeclaration>)newValue);
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
      case IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION:
        getTypeDeclaration().clear();
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
      case IecPackage.DATA_TYPE_DECLARATION__TYPE_DECLARATION:
        return typeDeclaration != null && !typeDeclaration.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //DataTypeDeclarationImpl
