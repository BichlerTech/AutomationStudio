/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.StructureDeclaration;
import com.bichler.iec.iec.StructureElementDeclaration;

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
 * An implementation of the model object '<em><b>Structure Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.StructureDeclarationImpl#getStructureElement <em>Structure Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StructureDeclarationImpl extends MinimalEObjectImpl.Container implements StructureDeclaration
{
  /**
   * The cached value of the '{@link #getStructureElement() <em>Structure Element</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStructureElement()
   * @generated
   * @ordered
   */
  protected EList<StructureElementDeclaration> structureElement;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected StructureDeclarationImpl()
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
    return IecPackage.Literals.STRUCTURE_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<StructureElementDeclaration> getStructureElement()
  {
    if (structureElement == null)
    {
      structureElement = new EObjectContainmentEList<StructureElementDeclaration>(StructureElementDeclaration.class, this, IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT);
    }
    return structureElement;
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
      case IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT:
        return ((InternalEList<?>)getStructureElement()).basicRemove(otherEnd, msgs);
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
      case IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT:
        return getStructureElement();
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
      case IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT:
        getStructureElement().clear();
        getStructureElement().addAll((Collection<? extends StructureElementDeclaration>)newValue);
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
      case IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT:
        getStructureElement().clear();
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
      case IecPackage.STRUCTURE_DECLARATION__STRUCTURE_ELEMENT:
        return structureElement != null && !structureElement.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //StructureDeclarationImpl
