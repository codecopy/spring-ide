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
package org.springframework.ide.eclipse.boot.dash.cloudfoundry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.springframework.ide.eclipse.boot.dash.model.RunState;

/**
 * Caches {@link CloudApplication} by application name. Fetching an updated
 * cloud application can be a long process, therefore for responsiveness, a
 * cache is maintained.
 * <p/>
 * API is also available to update the cache for Cloud operations that fetch an
 * updated cloud application as part of their execution.
 */
public class CloudAppCache {

	private final Map<String, CacheItem> cacheItem = new HashMap<String, CacheItem>();

	public CloudAppCache(CloudFoundryBootDashModel model) {
	}

	public synchronized void updateAll(Collection<CloudApplication> apps) {

		cacheItem.clear();
		for (CloudApplication app : apps) {
			CacheItem item = new CacheItem();
			item.app = app;
			item.runState = getRunStateFromCloudApp(app);
			cacheItem.put(app.getName(), item);
		}
	}

	/**
	 * Update the cache
	 *
	 * @param appName
	 * @param runState
	 */
	public synchronized void updateCache(String appName, RunState runState) {
		CacheItem item = cacheItem.get(appName);
		if (item != null) {
			item.runState = runState;
		}
	}

	/**
	 * Update the cache
	 *
	 * @param app
	 * @param runState
	 *            optional. It overrides the run state in the Cloud app. For
	 *            example, it may be used for handling {@link RunState#STARTING}
	 */
	public synchronized void updateCache(CloudApplication app, RunState runState) {
		CacheItem item = new CacheItem();
		item.app = app;
		item.runState = runState != null ? runState : getRunStateFromCloudApp(app);

		cacheItem.put(app.getName(), item);
	}

	public synchronized void remove(String appName) {
		cacheItem.remove(appName);
	}

	public synchronized CloudApplication getApp(String appName) {
		CacheItem item = cacheItem.get(appName);
		if (item != null) {
			return item.app;
		}
		return null;
	}

	public synchronized RunState getRunState(String appName) {
		CacheItem item = cacheItem.get(appName);
		if (item != null) {
			return item.runState;
		}
		return RunState.INACTIVE;
	}

	public static RunState getRunStateFromCloudApp(CloudApplication app) {
		RunState appState = RunState.INACTIVE;

		if (app != null) {
			switch (app.getState()) {
			case STARTED:
				appState = RunState.RUNNING;
				break;
			case STOPPED:
				appState = RunState.INACTIVE;
				break;
			case UPDATING:
				appState = RunState.STARTING;
				break;
			}
		}

		return appState;
	}

	class CacheItem {

		protected CloudApplication app;
		protected RunState runState;

	}

}