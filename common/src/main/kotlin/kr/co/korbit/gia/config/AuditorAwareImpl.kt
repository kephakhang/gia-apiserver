package kr.co.korbit.gia.config

import kr.co.korbit.gia.env.Env
import org.springframework.data.domain.AuditorAware
import java.util.*

class AuditorAwareImpl: AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        TODO("Not yet implemented")
    }
}