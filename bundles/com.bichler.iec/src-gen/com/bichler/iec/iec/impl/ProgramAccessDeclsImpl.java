/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgramAccessDecl;
import com.bichler.iec.iec.ProgramAccessDecls;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Program Access Decls</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramAccessDeclsImpl#getProgramAccessDecl <em>Program Access Decl</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgramAccessDeclsImpl extends ProgramVarDeclarationsImpl implements ProgramAccessDecls
{
  /**
   * The cached value of the '{@link #getProgramAccessDecl() <em>Program Access Decl</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgramAccessDecl()
   * @generated
   * @ordered
   */
  protected EList<ProgramAccessDecl> programAccessDecl;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgramAccessDeclsImpl()
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
    return IecPackage.Literals.PROGRAM_ACCESS_DECLS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ProgramAccessDecl> getProgramAccessDecl()
  {
    if (programAccessDecl == null)
    {
      programAccessDecl = new EObjectContainmentEList<ProgramAccessDecl>(ProgramAccessDecl.class, this, IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL);
    }
    return programAccessDecl;
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
      case IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL:
        return ((InternalEList<?>)getProgramAccessDecl()).basicRemove(otherEnd, msgs);
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
      case IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL:
        return getProgramAccessDecl();
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
      case IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL:
        getProgramAccessDecl().clear();
        getProgramAccessDecl().addAll((Collection<? extends ProgramAccessDecl>)newValue);
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
      case IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL:
        getProgramAccessDecl().clear();
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
      case IecPackage.PROGRAM_ACCESS_DECLS__PROGRAM_ACCESS_DECL:
        return programAccessDecl != null && !programAccessDecl.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ProgramAccessDeclsImpl
