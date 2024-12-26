package olesyaiv.recipebook.e2e.be.fixture

import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCase
import olesyaiv.recipebook.e2e.be.fixture.docker.DockerCompose

abstract class BaseFunSpec(
    private val dockerCompose: DockerCompose,
    body: FunSpec.() -> Unit
) : FunSpec(body) {

    override suspend fun beforeSpec(spec: Spec) {
        dockerCompose.start()
    }

    override suspend fun afterSpec(spec: Spec) {
        dockerCompose.stop()
    }

    override suspend fun beforeEach(testCase: TestCase) {
        dockerCompose.clearDb()
    }
}
