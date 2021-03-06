/*******************************************************************************
 * Copyright (c) 2016 Pivotal Software, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal Software, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.boot.dash.views;

import org.springframework.ide.eclipse.boot.dash.cloudfoundry.CloudAppDashElement;
import org.springframework.ide.eclipse.boot.dash.model.BootDashElement;

/**
 * Action for {@link CloudAppDashElement} elements only
 *
 * @author Alex Boyko
 *
 */
public class AbstractCloudAppDashElementsAction extends AbstractBootDashElementsAction {

	public AbstractCloudAppDashElementsAction(Params params) {
		super(params);
	}

	@Override
	public void updateVisibility() {
		boolean visible = false;
		if (!getSelectedElements().isEmpty()) {
			visible = true;
			for (BootDashElement e : getSelectedElements()) {
				if (!(e instanceof CloudAppDashElement)) {
					visible = false;
					break;
				}
			}
		}
		setVisible(visible);
	}

}
