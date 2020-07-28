package io.jrb.labs.songs.config

import io.jrb.common.rest.GlobalErrorHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestJavaConfig {

    @Bean
    fun globalErrorHandler() = GlobalErrorHandler()

}
