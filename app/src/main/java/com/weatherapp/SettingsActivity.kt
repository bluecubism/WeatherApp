package com.weatherapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {
    private lateinit var _btnSettings : ImageButton
    private lateinit var _fragment : SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // add fragment to the fragment container view in the XML
        if (savedInstanceState == null) {
            _fragment = SettingsFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, _fragment)
                .commit()
        }

        _btnSettings = findViewById(R.id.button_settings)
        _btnSettings.setOnClickListener {
            val intent = Intent()
            val prefsChanged = _fragment.getPrefsChanged()
            intent.putExtra("PrefsChanged", prefsChanged)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
        private var _prefsChanged = false

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
            // a preference was changed
            _prefsChanged = true
        }

        override fun onResume() {
            super.onResume()
            // register listener so any preference changes can be caught
            preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onDestroy() {
            super.onDestroy()
            preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        }

        fun getPrefsChanged() : Boolean {
            return _prefsChanged
        }
    }
}