package com.refrigerador67.smncli

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat


class SettingsActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val sharedPreferences by lazy {getDefaultSharedPreferences(context)}
            sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            when (sharedPreferences?.getString("provider", "openmeteo")) {
                "openmeteo" -> {findPreference<PreferenceCategory?>("smnheader")?.isEnabled = false}
                "smn" -> {findPreference<PreferenceCategory?>("smnheader")?.isEnabled = true}
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val appWidgetManager = AppWidgetManager.getInstance(this)

        val views = RemoteViews(this.packageName, R.layout.widgetlayout)

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)

        appWidgetManager.updateAppWidget(appWidgetId, views)
        finish()

        return super.onSupportNavigateUp()
    }
}

