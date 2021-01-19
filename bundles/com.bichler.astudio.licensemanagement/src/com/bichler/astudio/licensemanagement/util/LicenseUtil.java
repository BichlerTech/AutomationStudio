package com.bichler.astudio.licensemanagement.util;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.bichler.astudio.licensemanagement.dialog.AcademicLicenseDialog;
import com.bichler.astudio.licensemanagement.dialog.EvaluationLicenseDialog;
import com.bichler.astudio.licensemanagement.wizard.LicenseCategoryExceededWizard;
import com.bichler.astudio.licensemanagement.wizard.LicenseNoCmConnectionWizard;

public class LicenseUtil {

	static class Helper1 {
		Long long1 = 0l;
	}

	public static long openLicenseNoCmStickConnection(final long firmCode, final long productCode) {

		final Helper1 hcmse = new Helper1();

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				LicenseNoCmConnectionWizard wizard = new LicenseNoCmConnectionWizard(firmCode, productCode);
				WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard);

				if (WizardDialog.OK == dialog.open()) {

				}

				hcmse.long1 = wizard.getHCMSE();
			}
		});

		return hcmse.long1;
	}

	public static void openLicenseErrorStartup(final String title, final String description, final String text) {

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {

				LicenseCategoryExceededWizard wizard = new LicenseCategoryExceededWizard(title, description, text);
				WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard) {

					@Override
					public void updateButtons() {
						super.updateButtons();

						getButton(IDialogConstants.FINISH_ID).setVisible(false);
						getButton(IDialogConstants.CANCEL_ID).setText(IDialogConstants.FINISH_LABEL);
					}

				};

				if (WizardDialog.OK == dialog.open()) {

				}
			}
		});

	}

	public static void openLicenseValueExceeded(final String title, final String description, final String text) {

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {

				LicenseCategoryExceededWizard wizard = new LicenseCategoryExceededWizard(title, description, text);
				WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard) {

					@Override
					public void updateButtons() {
						super.updateButtons();

						getButton(IDialogConstants.FINISH_ID).setVisible(false);
						getButton(IDialogConstants.CANCEL_ID).setText(IDialogConstants.FINISH_LABEL);
					}

				};

				if (WizardDialog.OK == dialog.open()) {

				}
			}
		});

	}

	public static void openDialogStartupAcademicLicense() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				AcademicLicenseDialog dialog = new AcademicLicenseDialog(Display.getDefault().getActiveShell());
				dialog.open();
			}
		});
	}

	public static void openDialogStartupEvaluationLicense() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				EvaluationLicenseDialog dialog = new EvaluationLicenseDialog(Display.getDefault().getActiveShell());
				dialog.open();
			}
		});
	}
}
