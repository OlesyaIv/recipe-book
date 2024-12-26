package olesyaiv.recipebook.app.spring

import olesyaiv.recipebook.app.spring.config.RecipeBookConfigPostgres
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.*

@SpringBootTest
class ApplicationTests {

    @Autowired
    var pgConf: RecipeBookConfigPostgres = RecipeBookConfigPostgres()

    @Test
    fun contextLoads() {
        assertEquals(5432, pgConf.psql.port)
        assertEquals("education_recipes", pgConf.psql.database)
    }
}
