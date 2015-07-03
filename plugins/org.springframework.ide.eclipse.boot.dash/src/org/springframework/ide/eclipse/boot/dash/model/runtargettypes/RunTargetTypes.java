/*******************************************************************************
 * Copyright (c) 2015 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.dash.model.runtargettypes;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudFoundryUiUtil;
import org.springframework.ide.eclipse.boot.dash.model.RunTarget;
import org.springframework.ide.eclipse.boot.dash.views.RunTargetWizard;
import org.springsource.ide.eclipse.commons.livexp.core.LiveSet;

public class RunTargetTypes {

	public static final RunTargetType LOCAL = new AbstractRunTargetType("Local") {

		@Override
		public boolean canCreate() {
			return false;
		}

		public String toString() {
			return "RunTargetType(LOCAL)";
		}

		@Override
		public void openTargetCreationUi(LiveSet<RunTarget> targets) {
			throw new UnsupportedOperationException(
					this+" is a Singleton, it is not possible to create additional targets of this type.");
		};
	};

	public static final RunTargetType CLOUDFOUNDRY = new AbstractRunTargetType("Cloudfoundry") {

		@Override
		public void openTargetCreationUi(LiveSet<RunTarget> targets) {
			RunTargetWizard wizard = new RunTargetWizard(targets);
			Shell shell = CloudFoundryUiUtil.getShell();
			if (shell != null) {
				WizardDialog dialog = new WizardDialog(shell, wizard);
				if (dialog.open() == Dialog.OK) {
					RunTarget target = wizard.getRunTarget();
					if (target != null) {
						targets.add(target);
					}
				}
			}
		}

		@Override
		public boolean canCreate() {
			return true;
		}
	};

}