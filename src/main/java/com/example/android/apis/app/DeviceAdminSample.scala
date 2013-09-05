/*
 * Copyright (C) 2010 The Android Open Source Project
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
package com.example.android.apis.app

import com.example.android.apis.R
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.EditTextPreference
import android.preference.ListPreference
import android.preference.Preference
import android.preference.Preference.OnPreferenceChangeListener
import android.preference.Preference.OnPreferenceClickListener
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

/**
 * This activity provides a comprehensive UI for exploring and operating the DevicePolicyManager
 * api.  It consists of two primary modules:
 *
 * 1:  A device policy controller, implemented here as a series of preference fragments.  Each
 * one contains code to monitor and control a particular subset of device policies.
 *
 * 2:  A DeviceAdminReceiver, to receive updates from the DevicePolicyManager when certain aspects
 * of the device security status have changed.
 */
object DeviceAdminSample {
  /**
   * Simple converter used for long expiration times reported in mSec.
   */
  private def timeToDaysMinutesSeconds(context: Context, time: Long) = {
    val days: java.lang.Long = time / MS_PER_DAY
    val hours: java.lang.Long = (time / MS_PER_HOUR) % 24
    val minutes: java.lang.Long = (time / MS_PER_MINUTE) % 60
    context.getString(R.string.status_days_hours_minutes, days, hours, minutes)
  }
  /**
   * If the "user" is a monkey, post an alert and notify the caller.  This prevents automated
   * test frameworks from stumbling into annoying or dangerous operations.
   */
  private def alertIfMonkey(context: Context, stringId: Int) = {
    if (ActivityManager.isUserAMonkey) {
      val builder = new AlertDialog.Builder(context)
      builder.setMessage(stringId)
      builder.setPositiveButton(R.string.monkey_ok, null)
      builder.show
      true
    }
    false
  }

  private val TAG = "DeviceAdminSample"
  private val REQUEST_CODE_ENABLE_ADMIN = 1
  private val REQUEST_CODE_START_ENCRYPTION = 2
  private val MS_PER_MINUTE = 60 * 1000
  private val MS_PER_HOUR = 60 * MS_PER_MINUTE
  private val MS_PER_DAY = 24 * MS_PER_HOUR
  private val KEY_ENABLE_ADMIN = "key_enable_admin"
  private val KEY_DISABLE_CAMERA = "key_disable_camera"
  private val KEY_CATEGORY_QUALITY = "key_category_quality"
  private val KEY_SET_PASSWORD = "key_set_password"
  private val KEY_RESET_PASSWORD = "key_reset_password"
  private val KEY_QUALITY = "key_quality"
  private val KEY_MIN_LENGTH = "key_minimum_length"
  private val KEY_MIN_LETTERS = "key_minimum_letters"
  private val KEY_MIN_NUMERIC = "key_minimum_numeric"
  private val KEY_MIN_LOWER_CASE = "key_minimum_lower_case"
  private val KEY_MIN_UPPER_CASE = "key_minimum_upper_case"
  private val KEY_MIN_SYMBOLS = "key_minimum_symbols"
  private val KEY_MIN_NON_LETTER = "key_minimum_non_letter"
  private val KEY_CATEGORY_EXPIRATION = "key_category_expiration"
  private val KEY_HISTORY = "key_history"
  private val KEY_EXPIRATION_TIMEOUT = "key_expiration_timeout"
  private val KEY_EXPIRATION_STATUS = "key_expiration_status"
  private val KEY_CATEGORY_LOCK_WIPE = "key_category_lock_wipe"
  private val KEY_MAX_TIME_SCREEN_LOCK = "key_max_time_screen_lock"
  private val KEY_MAX_FAILS_BEFORE_WIPE = "key_max_fails_before_wipe"
  private val KEY_LOCK_SCREEN = "key_lock_screen"
  private val KEY_WIPE_DATA = "key_wipe_data"
  private val KEY_WIP_DATA_ALL = "key_wipe_data_all"
  private val KEY_CATEGORY_ENCRYPTION = "key_category_encryption"
  private val KEY_REQUIRE_ENCRYPTION = "key_require_encryption"
  private val KEY_ACTIVATE_ENCRYPTION = "key_activate_encryption"
  /**
   * Common fragment code for DevicePolicyManager access.  Provides two shared elements:
   * 1.  Provides instance variables to access activity/context, DevicePolicyManager, etc.
   * 2.  Provides support for the "set password" button(s) shared by multiple fragments.
   */
  class AdminSampleFragment extends PreferenceFragment with OnPreferenceChangeListener with OnPreferenceClickListener {
    override def onActivityCreated(savedInstanceState: Bundle) {
      super.onActivityCreated(savedInstanceState)
      mActivity = getActivity.asInstanceOf[DeviceAdminSample]
      mDPM = mActivity.mDPM
      mDeviceAdminSample = mActivity.mDeviceAdminSample
      mAdminActive = mActivity.isActiveAdmin
      mResetPassword = findPreference(KEY_RESET_PASSWORD).asInstanceOf[EditTextPreference]
      mSetPassword = findPreference(KEY_SET_PASSWORD).asInstanceOf[PreferenceScreen]
      if (mResetPassword != null) {
        mResetPassword.setOnPreferenceChangeListener(this)
      }
      if (mSetPassword != null) {
        mSetPassword.setOnPreferenceClickListener(this)
      }
    }

    override def onResume() {
      super.onResume()
      mAdminActive = mActivity.isActiveAdmin
      reloadSummaries()
      if (mResetPassword != null) {
        mResetPassword.setEnabled(mAdminActive)
      }
    }

    /**
     * Called automatically at every onResume.  Should also call explicitly any time a
     * policy changes that may affect other policy values.
     */
    protected def reloadSummaries() {
      if (mSetPassword != null) {
        if (mAdminActive) {
          val sufficient = mDPM.isActivePasswordSufficient
          mSetPassword.setSummary(if (sufficient) R.string.password_sufficient else R.string.password_insufficient)
        }
        else {
          mSetPassword.setSummary(null)
        }
      }
    }

    def onPreferenceClick(preference: Preference) = {
      if (mSetPassword != null && preference == mSetPassword) {
        startActivity(new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD))
        true
      }
      false
    }

    def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (mResetPassword != null && preference == mResetPassword) {
        doResetPassword(newValue.asInstanceOf[String])
        true
      }
      false
    }

    /**
     * This is dangerous, so we prevent automated tests from doing it, and we
     * remind the user after we do it.
     */
    private def doResetPassword(newPassword: String) {
      if (alertIfMonkey(mActivity, R.string.monkey_reset_password)) {
        return
      }
      mDPM.resetPassword(newPassword, DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY)
      val builder = new AlertDialog.Builder(mActivity)
      val message = mActivity.getString(R.string.reset_password_warning, newPassword)
      builder.setMessage(message)
      builder.setPositiveButton(R.string.reset_password_ok, null)
      builder.show
    }

    /**
     * Simple helper for summaries showing local & global (aggregate) policy settings
     */
    protected def localGlobalSummary(local: AnyRef, global: AnyRef) = {
      getString(R.string.status_local_global, local, global)
    }

    protected var mActivity: DeviceAdminSample = null
    protected var mDPM: DevicePolicyManager = null
    protected var mDeviceAdminSample: ComponentName = null
    protected var mAdminActive: Boolean = false
    private var mSetPassword: PreferenceScreen = null
    private var mResetPassword: EditTextPreference = null
  }

  /**
   * PreferenceFragment for "general" preferences.
   */
  class GeneralFragment extends AdminSampleFragment with OnPreferenceChangeListener {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.device_admin_general)
      mEnableCheckbox = findPreference(KEY_ENABLE_ADMIN).asInstanceOf[CheckBoxPreference]
      mEnableCheckbox.setOnPreferenceChangeListener(this)
      mDisableCameraCheckbox = findPreference(KEY_DISABLE_CAMERA).asInstanceOf[CheckBoxPreference]
      mDisableCameraCheckbox.setOnPreferenceChangeListener(this)
    }

    override def onResume() {
      super.onResume()
      mEnableCheckbox.setChecked(mAdminActive)
      enableDeviceCapabilitiesArea(mAdminActive)
      if (mAdminActive) {
        mDPM.setCameraDisabled(mDeviceAdminSample, mDisableCameraCheckbox.isChecked)
        reloadSummaries()
      }
    }

    override def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (super.onPreferenceChange(preference, newValue)) {
        true
      }
      val value = newValue.asInstanceOf[Boolean]
      if (preference eq mEnableCheckbox) {
        if (value != mAdminActive) {
          if (value) {
            val intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, mActivity.getString(R.string.add_admin_extra_app_text))
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
            false
          }
          else {
            mDPM.removeActiveAdmin(mDeviceAdminSample)
            enableDeviceCapabilitiesArea(false)
            mAdminActive = false
          }
        }
      }
      else if (preference eq mDisableCameraCheckbox) {
        mDPM.setCameraDisabled(mDeviceAdminSample, value)
        reloadSummaries()
      }
      true
    }

    protected override def reloadSummaries() {
      super.reloadSummaries()
      val cameraSummary = getString(if (mDPM.getCameraDisabled(mDeviceAdminSample)) R.string.camera_disabled else R.string.camera_enabled)
      mDisableCameraCheckbox.setSummary(cameraSummary)
    }

    /** Updates the device capabilities area (dis/enabling) as the admin is (de)activated */
    private def enableDeviceCapabilitiesArea(enabled: Boolean) {
      mDisableCameraCheckbox.setEnabled(enabled)
    }

    private var mEnableCheckbox: CheckBoxPreference = null
    private var mDisableCameraCheckbox: CheckBoxPreference = null
  }

  /**
   * PreferenceFragment for "password quality" preferences.
   */
  class QualityFragment extends AdminSampleFragment with OnPreferenceChangeListener {
    private[app] final val mPasswordQualityValues = Array[Int](DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED, DevicePolicyManager.PASSWORD_QUALITY_SOMETHING, DevicePolicyManager.PASSWORD_QUALITY_NUMERIC, DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC, DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC, DevicePolicyManager.PASSWORD_QUALITY_COMPLEX)
    private[app] final val mPasswordQualityValueStrings = Array[CharSequence](String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED), String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_SOMETHING), String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_NUMERIC), String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC), String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC), String.valueOf(DevicePolicyManager.PASSWORD_QUALITY_COMPLEX))

    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.device_admin_quality)
      mQualityCategory = findPreference(KEY_CATEGORY_QUALITY).asInstanceOf[PreferenceCategory]
      mPasswordQuality = findPreference(KEY_QUALITY).asInstanceOf[ListPreference]
      mMinLength = findPreference(KEY_MIN_LENGTH).asInstanceOf[EditTextPreference]
      mMinLetters = findPreference(KEY_MIN_LETTERS).asInstanceOf[EditTextPreference]
      mMinNumeric = findPreference(KEY_MIN_NUMERIC).asInstanceOf[EditTextPreference]
      mMinLowerCase = findPreference(KEY_MIN_LOWER_CASE).asInstanceOf[EditTextPreference]
      mMinUpperCase = findPreference(KEY_MIN_UPPER_CASE).asInstanceOf[EditTextPreference]
      mMinSymbols = findPreference(KEY_MIN_SYMBOLS).asInstanceOf[EditTextPreference]
      mMinNonLetter = findPreference(KEY_MIN_NON_LETTER).asInstanceOf[EditTextPreference]
      mPasswordQuality.setOnPreferenceChangeListener(this)
      mMinLength.setOnPreferenceChangeListener(this)
      mMinLetters.setOnPreferenceChangeListener(this)
      mMinNumeric.setOnPreferenceChangeListener(this)
      mMinLowerCase.setOnPreferenceChangeListener(this)
      mMinUpperCase.setOnPreferenceChangeListener(this)
      mMinSymbols.setOnPreferenceChangeListener(this)
      mMinNonLetter.setOnPreferenceChangeListener(this)
      mPasswordQuality.setEntryValues(mPasswordQualityValueStrings)
    }

    override def onResume() {
      super.onResume()
      mQualityCategory.setEnabled(mAdminActive)
    }

    /**
     * Update the summaries of each item to show the local setting and the global setting.
     */
    protected override def reloadSummaries() {
      super.reloadSummaries()
      var local: java.lang.Integer = 0
      var global: java.lang.Integer = 0
      local = mDPM.getPasswordQuality(mDeviceAdminSample)
      global = mDPM.getPasswordQuality(null)
      mPasswordQuality.setSummary(localGlobalSummary(qualityValueToString(local), qualityValueToString(global)))
      local = mDPM.getPasswordMinimumLength(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumLength(null)
      mMinLength.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumLetters(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumLetters(null)
      mMinLetters.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumNumeric(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumNumeric(null)
      mMinNumeric.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumLowerCase(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumLowerCase(null)
      mMinLowerCase.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumUpperCase(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumUpperCase(null)
      mMinUpperCase.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumSymbols(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumSymbols(null)
      mMinSymbols.setSummary(localGlobalSummary(local, global))
      local = mDPM.getPasswordMinimumNonLetter(mDeviceAdminSample)
      global = mDPM.getPasswordMinimumNonLetter(null)
      mMinNonLetter.setSummary(localGlobalSummary(local, global))
    }

    override def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (super.onPreferenceChange(preference, newValue)) {
        true
      }
      val valueString = newValue.asInstanceOf[String]
      if (TextUtils.isEmpty(valueString)) {
        false
      }
      var value: Int = 0
      try {
        value = Integer.parseInt(valueString)
      }
      catch {
        case nfe: NumberFormatException => {
          val warning = mActivity.getString(R.string.number_format_warning, valueString)
          Toast.makeText(mActivity, warning, Toast.LENGTH_SHORT).show()
        }
      }

      preference match {
        case p if (p == mPasswordQuality) => mDPM.setPasswordQuality(mDeviceAdminSample, value)
        case p if (p == mMinLength) => mDPM.setPasswordMinimumLength(mDeviceAdminSample, value)
        case p if (p == mMinLetters) => mDPM.setPasswordMinimumLetters(mDeviceAdminSample, value)
        case p if (p == mMinNumeric) => mDPM.setPasswordMinimumNumeric(mDeviceAdminSample, value)
        case p if (p == mMinLowerCase) => mDPM.setPasswordMinimumLowerCase(mDeviceAdminSample, value)
        case p if (p == mMinUpperCase) => mDPM.setPasswordMinimumUpperCase(mDeviceAdminSample, value)
        case p if (p == mMinSymbols) => mDPM.setPasswordMinimumSymbols(mDeviceAdminSample, value)
        case p if (p == mMinNonLetter) => mDPM.setPasswordMinimumNonLetter(mDeviceAdminSample, value)
      }

      reloadSummaries()
      true
    }

    private def qualityValueToString(quality: Int) = {
      {
        var i: Int = 0
        while (i < mPasswordQualityValues.length) {
          if (mPasswordQualityValues(i) == quality) {
            val qualities = mActivity.getResources.getStringArray(R.array.password_qualities)
            qualities(i)
          }
          i += 1
        }
      }
      "(0x" + Integer.toString(quality, 16) + ")"
    }

    private var mQualityCategory: PreferenceCategory = null
    private var mPasswordQuality: ListPreference = null
    private var mMinLength: EditTextPreference = null
    private var mMinLetters: EditTextPreference = null
    private var mMinNumeric: EditTextPreference = null
    private var mMinLowerCase: EditTextPreference = null
    private var mMinUpperCase: EditTextPreference = null
    private var mMinSymbols: EditTextPreference = null
    private var mMinNonLetter: EditTextPreference = null
  }

  /**
   * PreferenceFragment for "password expiration" preferences.
   */
  class ExpirationFragment extends AdminSampleFragment with OnPreferenceChangeListener with OnPreferenceClickListener {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.device_admin_expiration)
      mExpirationCategory = findPreference(KEY_CATEGORY_EXPIRATION).asInstanceOf[PreferenceCategory]
      mHistory = findPreference(KEY_HISTORY).asInstanceOf[EditTextPreference]
      mExpirationTimeout = findPreference(KEY_EXPIRATION_TIMEOUT).asInstanceOf[EditTextPreference]
      mExpirationStatus = findPreference(KEY_EXPIRATION_STATUS).asInstanceOf[PreferenceScreen]
      mHistory.setOnPreferenceChangeListener(this)
      mExpirationTimeout.setOnPreferenceChangeListener(this)
      mExpirationStatus.setOnPreferenceClickListener(this)
    }

    override def onResume() {
      super.onResume()
      mExpirationCategory.setEnabled(mAdminActive)
    }

    /**
     * Update the summaries of each item to show the local setting and the global setting.
     */
    protected override def reloadSummaries() {
      super.reloadSummaries()
      val local: java.lang.Integer = mDPM.getPasswordHistoryLength(mDeviceAdminSample)
      val global: java.lang.Integer = mDPM.getPasswordHistoryLength(null)
      mHistory.setSummary(localGlobalSummary(local, global))
      val localLong = mDPM.getPasswordExpirationTimeout(mDeviceAdminSample)
      val globalLong = mDPM.getPasswordExpirationTimeout(null)
      mExpirationTimeout.setSummary(localGlobalSummary((localLong / MS_PER_MINUTE): java.lang.Long, (globalLong / MS_PER_MINUTE): java.lang.Long))
      val expirationStatus = getExpirationStatus
      mExpirationStatus.setSummary(expirationStatus)
    }

    override def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (super.onPreferenceChange(preference, newValue)) {
        true
      }
      val valueString = newValue.asInstanceOf[String]
      if (TextUtils.isEmpty(valueString)) {
        false
      }
      var value: Int = 0
      try {
        value = Integer.parseInt(valueString)
      }
      catch {
        case nfe: NumberFormatException => {
          val warning = mActivity.getString(R.string.number_format_warning, valueString)
          Toast.makeText(mActivity, warning, Toast.LENGTH_SHORT).show()
        }
      }
      if (preference == mHistory) {
        mDPM.setPasswordHistoryLength(mDeviceAdminSample, value)
      }
      else if (preference == mExpirationTimeout) {
        mDPM.setPasswordExpirationTimeout(mDeviceAdminSample, value * MS_PER_MINUTE)
      }
      reloadSummaries()
      true
    }

    override def onPreferenceClick(preference: Preference) = {
      if (super.onPreferenceClick(preference)) {
        true
      }
      if (preference == mExpirationStatus) {
        mExpirationStatus.setSummary(getExpirationStatus)
        true
      }
      false
    }

    /**
     * Create a summary string describing the expiration status for the sample app,
     * as well as the global (aggregate) status.
     */
    private def getExpirationStatus = {
      var localExpiration = mDPM.getPasswordExpiration(mDeviceAdminSample)
      var globalExpiration = mDPM.getPasswordExpiration(null)
      val now = System.currentTimeMillis
      val local = if (localExpiration == 0) {
        mActivity.getString(R.string.expiration_status_none)
      } else {
        localExpiration -= now
        val dms = timeToDaysMinutesSeconds(mActivity, math.abs(localExpiration))
        val msg = if (localExpiration >= 0) R.string.expiration_status_future else R.string.expiration_status_past
        mActivity.getString(msg, dms)
      }
      val global = if (globalExpiration == 0) {
        mActivity.getString(R.string.expiration_status_none)
      } else {
        globalExpiration -= now
        val dms = timeToDaysMinutesSeconds(mActivity, math.abs(globalExpiration))
        val msg = if (globalExpiration >= 0) R.string.expiration_status_future else R.string.expiration_status_past
        mActivity.getString(msg, dms)
      }
      mActivity.getString(R.string.status_local_global, local, global)
    }

    private var mExpirationCategory: PreferenceCategory = null
    private var mHistory: EditTextPreference = null
    private var mExpirationTimeout: EditTextPreference = null
    private var mExpirationStatus: PreferenceScreen = null
  }

  /**
   * PreferenceFragment for "lock screen & wipe" preferences.
   */
  class LockWipeFragment extends AdminSampleFragment with OnPreferenceChangeListener with OnPreferenceClickListener {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.device_admin_lock_wipe)
      mLockWipeCategory = findPreference(KEY_CATEGORY_LOCK_WIPE).asInstanceOf[PreferenceCategory]
      mMaxTimeScreenLock = findPreference(KEY_MAX_TIME_SCREEN_LOCK).asInstanceOf[EditTextPreference]
      mMaxFailures = findPreference(KEY_MAX_FAILS_BEFORE_WIPE).asInstanceOf[EditTextPreference]
      mLockScreen = findPreference(KEY_LOCK_SCREEN).asInstanceOf[PreferenceScreen]
      mWipeData = findPreference(KEY_WIPE_DATA).asInstanceOf[PreferenceScreen]
      mWipeAppData = findPreference(KEY_WIP_DATA_ALL).asInstanceOf[PreferenceScreen]
      mMaxTimeScreenLock.setOnPreferenceChangeListener(this)
      mMaxFailures.setOnPreferenceChangeListener(this)
      mLockScreen.setOnPreferenceClickListener(this)
      mWipeData.setOnPreferenceClickListener(this)
      mWipeAppData.setOnPreferenceClickListener(this)
    }

    override def onResume() {
      super.onResume()
      mLockWipeCategory.setEnabled(mAdminActive)
    }

    /**
     * Update the summaries of each item to show the local setting and the global setting.
     */
    protected override def reloadSummaries() {
      super.reloadSummaries()
      val localLong = mDPM.getMaximumTimeToLock(mDeviceAdminSample)
      val globalLong = mDPM.getMaximumTimeToLock(null)
      mMaxTimeScreenLock.setSummary(localGlobalSummary((localLong / MS_PER_MINUTE): java.lang.Long, (globalLong / MS_PER_MINUTE): java.lang.Long))
      val local: java.lang.Integer = mDPM.getMaximumFailedPasswordsForWipe(mDeviceAdminSample)
      val global: java.lang.Integer = mDPM.getMaximumFailedPasswordsForWipe(null)
      mMaxFailures.setSummary(localGlobalSummary(local, global))
    }

    override def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (super.onPreferenceChange(preference, newValue)) {
        true
      }
      val valueString = newValue.asInstanceOf[String]
      if (TextUtils.isEmpty(valueString)) {
        false
      }
      var value: Int = 0
      try {
        value = Integer.parseInt(valueString)
      }
      catch {
        case nfe: NumberFormatException => {
          val warning = mActivity.getString(R.string.number_format_warning, valueString)
          Toast.makeText(mActivity, warning, Toast.LENGTH_SHORT).show()
        }
      }
      if (preference eq mMaxTimeScreenLock) {
        mDPM.setMaximumTimeToLock(mDeviceAdminSample, value * MS_PER_MINUTE)
      }
      else if (preference eq mMaxFailures) {
        if (alertIfMonkey(mActivity, R.string.monkey_wipe_data)) {
          true
        }
        mDPM.setMaximumFailedPasswordsForWipe(mDeviceAdminSample, value)
      }
      reloadSummaries()
      true
    }

    override def onPreferenceClick(preference: Preference) = {
      if (super.onPreferenceClick(preference)) {
        true
      }
      if (preference == mLockScreen) {
        if (alertIfMonkey(mActivity, R.string.monkey_lock_screen)) {
          true
        }
        mDPM.lockNow()
        true
      }
      else if (preference == mWipeData || preference == mWipeAppData) {
        if (alertIfMonkey(mActivity, R.string.monkey_wipe_data)) {
          true
        }
        promptForRealDeviceWipe(preference eq mWipeAppData)
        true
      }
      false
    }

    /**
     * Wiping data is real, so we don't want it to be easy.  Show two alerts before wiping.
     */
    private def promptForRealDeviceWipe(wipeAllData: Boolean) {
      val activity = mActivity
      val builder = new AlertDialog.Builder(activity)
      builder.setMessage(R.string.wipe_warning_first)
      builder.setPositiveButton(R.string.wipe_warning_first_ok, new DialogInterface.OnClickListener {
        def onClick(dialog: DialogInterface, which: Int) {
          val builder = new AlertDialog.Builder(activity)
          builder.setMessage(if (wipeAllData) R.string.wipe_warning_second_full else R.string.wipe_warning_second)
          builder.setPositiveButton(R.string.wipe_warning_second_ok, new DialogInterface.OnClickListener {
            def onClick(dialog: DialogInterface, which: Int) {
              if (mActivity.isActiveAdmin) {
                mDPM.wipeData(if (wipeAllData) DevicePolicyManager.WIPE_EXTERNAL_STORAGE else 0)
              }
            }
          })
          builder.setNegativeButton(R.string.wipe_warning_second_no, null)
          builder.show
        }
      })
      builder.setNegativeButton(R.string.wipe_warning_first_no, null)
      builder.show
    }

    private var mLockWipeCategory: PreferenceCategory = null
    private var mMaxTimeScreenLock: EditTextPreference = null
    private var mMaxFailures: EditTextPreference = null
    private var mLockScreen: PreferenceScreen = null
    private var mWipeData: PreferenceScreen = null
    private var mWipeAppData: PreferenceScreen = null
  }

  /**
   * PreferenceFragment for "encryption" preferences.
   */
  class EncryptionFragment extends AdminSampleFragment with OnPreferenceChangeListener with OnPreferenceClickListener {
    override def onCreate(savedInstanceState: Bundle) {
      super.onCreate(savedInstanceState)
      addPreferencesFromResource(R.xml.device_admin_encryption)
      mEncryptionCategory = findPreference(KEY_CATEGORY_ENCRYPTION).asInstanceOf[PreferenceCategory]
      mRequireEncryption = findPreference(KEY_REQUIRE_ENCRYPTION).asInstanceOf[CheckBoxPreference]
      mActivateEncryption = findPreference(KEY_ACTIVATE_ENCRYPTION).asInstanceOf[PreferenceScreen]
      mRequireEncryption.setOnPreferenceChangeListener(this)
      mActivateEncryption.setOnPreferenceClickListener(this)
    }

    override def onResume() {
      super.onResume()
      mEncryptionCategory.setEnabled(mAdminActive)
      mRequireEncryption.setChecked(mDPM.getStorageEncryption(mDeviceAdminSample))
    }

    /**
     * Update the summaries of each item to show the local setting and the global setting.
     */
    protected override def reloadSummaries() {
      super.reloadSummaries()
      val local: java.lang.Boolean = mDPM.getStorageEncryption(mDeviceAdminSample)
      val global: java.lang.Boolean = mDPM.getStorageEncryption(null)
      mRequireEncryption.setSummary(localGlobalSummary(local, global))
      val deviceStatusCode = mDPM.getStorageEncryptionStatus
      val deviceStatus = statusCodeToString(deviceStatusCode)
      val status = mActivity.getString(R.string.status_device_encryption, deviceStatus)
      mActivateEncryption.setSummary(status)
    }

    override def onPreferenceChange(preference: Preference, newValue: AnyRef) = {
      if (super.onPreferenceChange(preference, newValue)) {
        true
      }
      if (preference eq mRequireEncryption) {
        val newActive = newValue.asInstanceOf[Boolean]
        mDPM.setStorageEncryption(mDeviceAdminSample, newActive)
        reloadSummaries()
        true
      }
      true
    }

    override def onPreferenceClick(preference: Preference) = {
      if (super.onPreferenceClick(preference)) {
        true
      }
      if (preference eq mActivateEncryption) {
        if (alertIfMonkey(mActivity, R.string.monkey_encryption)) {
          true
        }
        if (mDPM.getStorageEncryptionStatus == DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED) {
          val builder = new AlertDialog.Builder(mActivity)
          builder.setMessage(R.string.encryption_not_supported)
          builder.setPositiveButton(R.string.encryption_not_supported_ok, null)
          builder.show
          true
        }
        val intent = new Intent(DevicePolicyManager.ACTION_START_ENCRYPTION)
        startActivityForResult(intent, REQUEST_CODE_START_ENCRYPTION)
        true
      }
      false
    }

    private def statusCodeToString(newStatusCode: Int) = {
      var newStatus: Int = R.string.encryption_status_unknown
      newStatus = newStatusCode match {
        case DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED => R.string.encryption_status_unsupported
        case DevicePolicyManager.ENCRYPTION_STATUS_INACTIVE => R.string.encryption_status_inactive
        case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVATING => R.string.encryption_status_activating
        case DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE => R.string.encryption_status_active
      }
      mActivity.getString(newStatus)
    }
    private var mEncryptionCategory: PreferenceCategory = null
    private var mRequireEncryption: CheckBoxPreference = null
    private var mActivateEncryption: PreferenceScreen = null
  }

  /**
   * Sample implementation of a DeviceAdminReceiver.  Your controller must provide one,
   * although you may or may not implement all of the methods shown here.
   *
   * All callbacks are on the UI thread and your implementations should not engage in any
   * blocking operations, including disk I/O.
   */
  class DeviceAdminSampleReceiver extends DeviceAdminReceiver {
    private[app] def showToast(context: Context, msg: String) {
      val status = context.getString(R.string.admin_receiver_status, msg)
      Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
    }

    override def onEnabled(context: Context, intent: Intent) {
      showToast(context, context.getString(R.string.admin_receiver_status_enabled))
    }

    override def onDisableRequested(context: Context, intent: Intent) = {
      context.getString(R.string.admin_receiver_status_disable_warning)
    }

    override def onDisabled(context: Context, intent: Intent) {
      showToast(context, context.getString(R.string.admin_receiver_status_disabled))
    }

    override def onPasswordChanged(context: Context, intent: Intent) {
      showToast(context, context.getString(R.string.admin_receiver_status_pw_changed))
    }

    override def onPasswordFailed(context: Context, intent: Intent) {
      showToast(context, context.getString(R.string.admin_receiver_status_pw_failed))
    }

    override def onPasswordSucceeded(context: Context, intent: Intent) {
      showToast(context, context.getString(R.string.admin_receiver_status_pw_succeeded))
    }

    override def onPasswordExpiring(context: Context, intent: Intent) {
      val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE).asInstanceOf[DevicePolicyManager]
      val expr = dpm.getPasswordExpiration(new ComponentName(context, classOf[DeviceAdminSample.DeviceAdminSampleReceiver]))
      val delta = expr - System.currentTimeMillis
      val expired = delta < 0L
      val message = context.getString(if (expired) R.string.expiration_status_past else R.string.expiration_status_future)
      showToast(context, message)
      Log.v(TAG, message)
    }
  }

}

class DeviceAdminSample extends PreferenceActivity {
  protected override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    mDPM = getSystemService(Context.DEVICE_POLICY_SERVICE).asInstanceOf[DevicePolicyManager]
    mDeviceAdminSample = new ComponentName(this, classOf[DeviceAdminSample.DeviceAdminSampleReceiver])
  }

  /**
   * We override this method to provide PreferenceActivity with the top-level preference headers.
   */
  override def onBuildHeaders(target: java.util.List[PreferenceActivity.Header]) {
    loadHeadersFromResource(R.xml.device_admin_headers, target)
  }

  /**
   * Helper to determine if we are an active admin
   */
  private def isActiveAdmin = {
    mDPM.isAdminActive(mDeviceAdminSample)
  }

  private[app] var mDPM: DevicePolicyManager = null
  private[app] var mDeviceAdminSample: ComponentName = null
}