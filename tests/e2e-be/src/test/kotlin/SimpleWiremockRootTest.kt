package olesyaiv.recipebook.e2e.test

import co.touchlab.kermit.Logger
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

private val log = Logger

@OptIn(ExperimentalKotest::class)
class SimpleWiremockRootTest : StringSpec({
    this.blockingTest = true
    isolationMode = IsolationMode.SingleInstance
    beforeSpec { start() }

    "Root GET method is working" {
        val client = HttpClient(OkHttp)
        val response = client.get(getUrl().buildString())
        val bodyString = response.call.response.bodyAsText()
        bodyString shouldBe "Hello, world!"
    }

    afterSpec { stop() }
}) {
    companion object {
        private val service = "app-wiremock_1"
        private val port = 8080

        private val compose by lazy {
            DockerComposeContainer(File("docker-compose/docker-compose-wiremock.yml")).apply {
                withOptions("--compatibility")
                withExposedService(service, port)
//                withLocalCompose(true)
            }
        }

        fun start() {
            runCatching { compose.start() }.onFailure {
                log.e { "Failed to start wiremock" }
                throw it
            }
            log.w("\n=========== wiremock started =========== \n")
        }

        fun stop() {
            compose.close()
            log.w("\n=========== wiremock complete =========== \n")
        }

        private fun getUrl() = URLBuilder(
            protocol = URLProtocol.Companion.HTTP,
            host = compose.getServiceHost(service, port),
            port = compose.getServicePort(service, port),
        )
    }
}
