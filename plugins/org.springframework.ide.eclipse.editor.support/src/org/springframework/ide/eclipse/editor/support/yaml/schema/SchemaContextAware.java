/*******************************************************************************
 * Copyright (c) 2017 Spring IDE Developers
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Spring IDE Developers - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.editor.support.yaml.schema;

/**
 * Interface that can be implemented by something producing another
 * component (of some type `T`) where the returned component needs to
 * configured with a {@link DynamicSchemaContext}.
 *
 * @author Kris De Volder
 */
@FunctionalInterface
public interface SchemaContextAware<T> {
	T withContext(DynamicSchemaContext dc);

	/**
	 * Convert a plain value into a {@link SchemaContextAware} that ignores the context and simply returns the value.
	 */
	public static <T> SchemaContextAware<T> just(T it) {
		return (dc) -> it;
	}
}
