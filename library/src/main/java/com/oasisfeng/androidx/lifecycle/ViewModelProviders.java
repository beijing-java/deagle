/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oasisfeng.androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.oasisfeng.android.app.LifecycleActivity;
import com.oasisfeng.android.app.LifecycleFragment;

/**
 * Utilities methods for {@link ViewModelStore} class.
 */
public class ViewModelProviders {

	private static Application checkApplication(Activity activity) {
		Application application = activity.getApplication();
		if (application == null) {
			throw new IllegalStateException("Your activity/fragment is not yet attached to "
					+ "Application. You can't request ViewModel before onCreate call.");
		}
		return application;
	}

	private static Activity checkActivity(Fragment fragment) {
		Activity activity = fragment.getActivity();
		if (activity == null) {
			throw new IllegalStateException("Can't create ViewModelProvider for detached fragment");
		}
		return activity;
	}

	/**
	 * Creates a {@link ViewModelProvider}, which retains ViewModels while a scope of given
	 * {@code fragment} is alive. More detailed explanation is in {@link ViewModel}.
	 * <p>
	 * It uses {@link ViewModelProvider.AndroidViewModelFactory} to instantiate new ViewModels.
	 *
	 * @param fragment a fragment, in whose scope ViewModels should be retained
	 * @return a ViewModelProvider instance
	 */
	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull LifecycleFragment fragment) {
		return of(fragment, null);
	}

	/**
	 * Creates a {@link ViewModelProvider}, which retains ViewModels while a scope of given Activity
	 * is alive. More detailed explanation is in {@link ViewModel}.
	 * <p>
	 * It uses {@link ViewModelProvider.AndroidViewModelFactory} to instantiate new ViewModels.
	 *
	 * @param activity an activity, in whose scope ViewModels should be retained
	 * @return a ViewModelProvider instance
	 */
	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull LifecycleActivity activity) {
		return of(activity, null);
	}

	/**
	 * Creates a {@link ViewModelProvider}, which retains ViewModels while a scope of given
	 * {@code fragment} is alive. More detailed explanation is in {@link ViewModel}.
	 * <p>
	 * It uses the given {@link ViewModelProvider.Factory} to instantiate new ViewModels.
	 *
	 * @param fragment a fragment, in whose scope ViewModels should be retained
	 * @param factory  a {@code Factory} to instantiate new ViewModels
	 * @return a ViewModelProvider instance
	 */
	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull LifecycleFragment fragment, @Nullable ViewModelProvider.Factory factory) {
		Application application = checkApplication(checkActivity(fragment));
		if (factory == null) {
			factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new ViewModelProvider(fragment.getViewModelStore(), factory);
	}

	/**
	 * Creates a {@link ViewModelProvider}, which retains ViewModels while a scope of given Activity
	 * is alive. More detailed explanation is in {@link ViewModel}.
	 * <p>
	 * It uses the given {@link ViewModelProvider.Factory} to instantiate new ViewModels.
	 *
	 * @param activity an activity, in whose scope ViewModels should be retained
	 * @param factory  a {@code Factory} to instantiate new ViewModels
	 * @return a ViewModelProvider instance
	 */
	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull LifecycleActivity activity,
									   @Nullable ViewModelProvider.Factory factory) {
		Application application = checkApplication(activity);
		if (factory == null) {
			factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
		}
		return new ViewModelProvider(activity.getViewModelStore(), factory);
	}

	/**
	 * {@link ViewModelProvider.Factory} which may create {@link AndroidViewModel} and
	 * {@link ViewModel}, which have an empty constructor.
	 *
	 * @deprecated Use {@link ViewModelProvider.AndroidViewModelFactory}
	 */
	@SuppressWarnings("WeakerAccess")
	@Deprecated
	public static class DefaultFactory extends ViewModelProvider.AndroidViewModelFactory {
		/**
		 * Creates a {@code AndroidViewModelFactory}
		 *
		 * @param application an application to pass in {@link AndroidViewModel}
		 * @deprecated Use {@link ViewModelProvider.AndroidViewModelFactory} or
		 * {@link ViewModelProvider.AndroidViewModelFactory#getInstance(Application)}.
		 */
		@Deprecated
		public DefaultFactory(@NonNull Application application) {
			super(application);
		}
	}
}
