package com.element.dpg.libs.chassis.test.containers.utils

import org.testcontainers.containers.Container
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget
import org.testcontainers.lifecycle.Startable

interface TestContainer<SELF : TestContainer<SELF>> : Container<SELF>, AutoCloseable, WaitStrategyTarget, Startable