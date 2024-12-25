package olesyaiv.recipebook.e2e.be.docker

import olesyaiv.recipebook.e2e.be.fixture.docker.AbstractDockerCompose

object SpringDockerCompose : AbstractDockerCompose(
    "app-spring_1", 8080, "docker-compose-spring-pg.yml"
)
