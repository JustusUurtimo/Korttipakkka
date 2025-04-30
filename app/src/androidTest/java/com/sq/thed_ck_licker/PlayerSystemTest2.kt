package com.sq.thed_ck_licker

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sq.thed_ck_licker.ecs.systems.characterSystems.PlayerSystem
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PlayerSystemTest2 {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var playerSystem: PlayerSystem

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun playerSystemInjection_works() {
        assertNotNull(playerSystem)
    }
}