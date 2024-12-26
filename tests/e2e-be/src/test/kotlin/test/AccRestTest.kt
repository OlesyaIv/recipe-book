package olesyaiv.recipebook.e2e.be.test

import io.kotest.core.annotation.Ignored
import olesyaiv.recipebook.e2e.be.docker.WiremockDockerCompose
import olesyaiv.recipebook.e2e.be.fixture.BaseFunSpec
import olesyaiv.recipebook.e2e.be.fixture.client.RestClient
import olesyaiv.recipebook.e2e.be.fixture.docker.DockerCompose

@Ignored
open class AccRestTestBase(dockerCompose: DockerCompose) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, "rest ")
})

class AccRestWiremockTest : AccRestTestBase(WiremockDockerCompose)
