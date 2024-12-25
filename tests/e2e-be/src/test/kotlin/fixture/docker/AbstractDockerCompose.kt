package olesyaiv.recipebook.e2e.be.fixture.docker

import co.touchlab.kermit.Logger
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import org.testcontainers.containers.DockerComposeContainer
import java.io.File

private val log = Logger

abstract class AbstractDockerCompose(
    private val apps: List<AppInfo>,
    private val dockerComposeNames: List<String>
) : DockerCompose {

    constructor(service: String, port: Int, vararg dockerComposeName: String)
        : this(listOf(AppInfo(service, port)), dockerComposeName.toList())
    private fun getComposeFiles(): List<File> = dockerComposeNames.map {
        val file = File("docker-compose/$it")
        if (!file.exists()) throw IllegalArgumentException("file $it not found!")
        file
    }

    private val compose =
        DockerComposeContainer(getComposeFiles()).apply {
//            withOptions("--compatibility")
            apps.forEach { (service, port) ->
                withExposedService(
                    service,
                    port,
                )
            }
        }

    override fun start() {
        kotlin.runCatching { compose.start() }.onFailure {
            log.e { "Failed to start $dockerComposeNames" }
            throw it
        }

        log.w("\n=========== $dockerComposeNames started =========== \n")
        apps.forEachIndexed { index, _ ->
            log.i { "Started docker-compose with App at: ${getUrl(index)}" }
        }
    }

    override fun stop() {
        compose.close()
        log.w("\n=========== $dockerComposeNames complete =========== \n")
    }

    override fun clearDb() {
        log.w("===== clearDb =====")
        // TODO сделать очистку БД, когда до этого дойдет
    }

    override val inputUrl: URLBuilder
        get() = getUrl(0)

    fun getUrl(no: Int) = URLBuilder(
        protocol = URLProtocol.HTTP,
        host = apps[no].let { compose.getServiceHost(it.service, it.port) },
        port = apps[no].let { compose.getServicePort(it.service, it.port) },
    )
    data class AppInfo(
        val service: String,
        val port: Int,
    )
}
