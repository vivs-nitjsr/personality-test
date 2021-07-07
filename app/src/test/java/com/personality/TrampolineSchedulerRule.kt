package com.personality

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class TrampolineSchedulerRule : TestRule {

    private val trampolineScheduler = Schedulers.trampoline()

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { trampolineScheduler }
                RxJavaPlugins.setComputationSchedulerHandler { trampolineScheduler }
                RxJavaPlugins.setNewThreadSchedulerHandler { trampolineScheduler }
                RxAndroidPlugins.setMainThreadSchedulerHandler { trampolineScheduler }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampolineScheduler }

                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                    RxAndroidPlugins.reset()
                }
            }
        }
    }
}
