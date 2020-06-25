package kr.co.korbit.gia.jpa.korbit.service

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kr.co.korbit.gia.config.*
import kr.co.korbit.gia.jpa.korbit.model.LneQuest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun Clock.fixWith(dateTime: LocalDateTime): Clock {

    val fixedClock = Clock.fixed(dateTime.atZone(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC)
    whenever(instant()).thenReturn(fixedClock.instant())
    whenever(zone).thenReturn(fixedClock.zone)
    return fixedClock
}

// create mocking Clock Object
fun mockClock(dateTime: LocalDateTime = LocalDateTime.now(Clock.systemDefaultZone())): Clock = mock<Clock>().apply { fixWith(dateTime) }

@SpringBootTest(classes=[JpaKorbitConfig::class, JpaKorbitApiConfig::class, JpaTerraConfig::class, SwaggerConfig::class, WebMvcConfig::class, SpringDataRestConfiguration::class])
@RunWith(value = SpringJUnit4ClassRunner::class)
class LneQuestServiceTest {

    @Autowired
    lateinit var lneQuestService: LneQuestService

    // create mocking Clock Object
    val clock = mockClock()

    @Test(expected = Exception::class)
    @Rollback(value = true)
    fun updateQuestTest() {
        val quest = lneQuestService.getLneQuest(1L)
        quest?.let {
            lneQuestService.updateLneQuest(quest)
            return
        }
        throw Exception("select row is null")
    }

    @Test
    fun selectQuestTest() {
        assertTrue(lneQuestService.getLneQuest(1L) is LneQuest)
    }

    @Test
    fun testClock() {
        val clock = clock.fixWith(LocalDateTime.now().withHour(10))

        assertEquals(LocalDateTime.ofInstant(clock.instant(), ZoneOffset.UTC).hour, 10)
    }
}
