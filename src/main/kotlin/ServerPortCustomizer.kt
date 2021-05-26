
import org.springframework.boot.web.server.ConfigurableWebServerFactory

import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component


@Configuration
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Bean
    override fun customize(factory: ConfigurableWebServerFactory) {
        factory.setPort(8086)
    }
}