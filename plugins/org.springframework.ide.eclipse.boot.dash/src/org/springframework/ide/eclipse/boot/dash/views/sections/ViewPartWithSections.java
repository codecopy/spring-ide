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
package org.springframework.ide.eclipse.boot.dash.views.sections;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.ViewPart;
import org.springframework.ide.eclipse.boot.core.BootActivator;
import org.springframework.ide.eclipse.boot.dash.views.DefaultUserInteractions.UIContext;
import org.springsource.ide.eclipse.commons.frameworks.core.ExceptionUtil;
import org.springsource.ide.eclipse.commons.livexp.core.Validator;
import org.springsource.ide.eclipse.commons.livexp.ui.CommentSection;
import org.springsource.ide.eclipse.commons.livexp.ui.IPageSection;
import org.springsource.ide.eclipse.commons.livexp.ui.IPageWithSections;
import org.springsource.ide.eclipse.commons.livexp.ui.ValidatorSection;

public class ViewPartWithSections extends ViewPart implements UIContext, IPageWithSections {

	private ViewPageScroller scroller;
	private Composite page;

	@Override
	public void createPartControl(Composite parent) {
		scroller = new ViewPageScroller(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		page = scroller.getBody();
		page.setLayout(new GridLayout());

		for (IPageSection s : getSections()) {
			s.createContents(page);
		}
	}

	@Override
	public void setFocus() {
		page.setFocus();
	}

	////////////////////////////////////////////////////////////////////////

	@Override
	public Shell getShell() {
		return getSite().getShell();
	}

	@Override
	public IRunnableContext getRunnableContext() {
		return getSite().getWorkbenchWindow();
	}

	////////////////////////////////////////////////////////////////////////
	/// Sections... lazy creation etc.

	private List<IPageSection> sections;

	protected synchronized List<IPageSection> getSections() {
		if (sections==null) {
			sections = safeCreateSections();
		}
		return sections;
	}

	private List<IPageSection> safeCreateSections() {
		try {
			return createSections();
		} catch (CoreException e) {
			BootActivator.log(e);
			return Arrays.asList(new IPageSection[] {
					new CommentSection(this, "View contents couldn't be created because of an unexpected error:+\n"+ExceptionUtil.getMessage(e)+"\n\n"
							+ "Check the error log for details"),
					new ValidatorSection(Validator.alwaysError(ExceptionUtil.getMessage(e)), this)
			});
		}
	}

	/**
	 * This method should be implemented to generate the contents of the page.
	 */
	protected List<IPageSection> createSections() throws CoreException {
		//This default implementation is meant to be overridden
		return Arrays.asList(new IPageSection[]{
				new CommentSection(this, "Override ViewPartWithSections.createSections() to provide real content."),
				new ValidatorSection(Validator.alwaysError("Subclass must implement validation logic"), this)
		});
	}


}
