package com.personality.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.personality.R
import com.personality.core.bindFragment
import com.personality.di.PersonalityApp
import com.personality.presentation.fragments.PersonalityTestFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

internal class PersonalityActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        PersonalityApp.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personality)
        bindPersonalityTestFragment()
    }

    private fun bindPersonalityTestFragment() {
        bindFragment(R.id.containerLayout, PersonalityTestFragment.newInstance())
    }

    override fun androidInjector() = dispatchingAndroidInjector
}