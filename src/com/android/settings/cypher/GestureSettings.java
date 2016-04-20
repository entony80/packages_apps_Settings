/*
 * Copyright (C) 2016 Cypher OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cypher;

import android.os.Bundle;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.Utils;

import cyanogenmod.providers.CMSettings;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.Secure.CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED;

import com.android.internal.logging.MetricsLogger;

public class GestureSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "GestureSettings";
	
	private static final String KEY_CAMERA_DOUBLE_TAP_POWER_GESTURE
            = "camera_double_tap_power_gesture";
			
	private SwitchPreference mCameraDoubleTapPowerGesture;
	
	 @Override
    protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.gesture_settings);
		
	    // Double press power to launch camera.
        mCameraDoubleTapPowerGesture
                    = (SwitchPreference) findPreference(KEY_CAMERA_DOUBLE_TAP_POWER_GESTURE);
					
		if (mCameraDoubleTapPowerGesture != null &&
                isCameraDoubleTapPowerGestureAvailable(getResources())) {
            // Update double tap power to launch camera if available.
            mCameraDoubleTapPowerGesture.setOnPreferenceChangeListener(this);
            int cameraDoubleTapPowerDisabled = Settings.Secure.getInt(
                    getContentResolver(), CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, 0);
            mCameraDoubleTapPowerGesture.setChecked(cameraDoubleTapPowerDisabled == 0);
        } else {
            mCameraDoubleTapPowerGesture = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
       if (preference == mCameraDoubleTapPowerGesture) {
          int value = (Boolean)objValue ? 0 : 1;
          Settings.Secure.putInt(getContentResolver(), CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED, value);
          return true;
       }
       return false;
    }
	
	private static boolean isCameraDoubleTapPowerGestureAvailable(Resources res) {
        return res.getBoolean(
                com.android.internal.R.bool.config_cameraDoubleTapPowerGestureEnabled);
	}
}