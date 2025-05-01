package com.sq.thed_ck_licker.ecs.systems.characterSystems

import dagger.Component
import javax.inject.Singleton

@Component
@Singleton
interface TestComponent {
    fun inject(test: PlayerSystemTest)
}