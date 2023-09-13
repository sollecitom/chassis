package org.sollecitom.chassis.test.utils.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.withContext
import kotlin.time.Duration

context(TestScope)
suspend fun pauseFor(duration: Duration) = withContext(Dispatchers.Default) { delay(duration) }