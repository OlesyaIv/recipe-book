package olesyaiv.recipebook.e2e.be.test

import io.kotest.core.annotation.Ignored
import olesyaiv.recipebook.e2e.be.docker.SpringDockerCompose
import olesyaiv.recipebook.e2e.be.docker.WiremockDockerCompose
import olesyaiv.recipebook.e2e.be.fixture.BaseFunSpec
import olesyaiv.recipebook.e2e.be.fixture.client.RestClient
import olesyaiv.recipebook.e2e.be.fixture.docker.DockerCompose
import olesyaiv.recipebook.e2e.be.test.action.v1.toV1

enum class TestDebug {
    STUB, PROD, TEST
}

@Ignored
open class AccRestTestBaseFull(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})

@Ignored
open class AccRestTestBaseShort(dockerCompose: DockerCompose, debug: TestDebug = TestDebug.STUB) : BaseFunSpec(dockerCompose, {
    val restClient = RestClient(dockerCompose)
    testApiV1(restClient, prefix = "rest ", debug = debug.toV1())
})

class AccRestWiremockTest : AccRestTestBaseFull(WiremockDockerCompose)

class AccRestSpringTest : AccRestTestBaseFull(SpringDockerCompose, debug = TestDebug.PROD)
