package np.sthaniya.dpis.common.util

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.File
import java.lang.reflect.Field
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

/**
 * Utility for automatically generating database schema documentation from entity classes.
 * This can be run as part of the build process using the `-Pgenerate-db-docs` property.
 */
@Configuration
@Profile("dev")
@ConditionalOnProperty(name = ["documentation.database.enabled"], havingValue = "true", matchIfMissing = false)
class DatabaseDocumentationGenerator {

    @Value("classpath:templates/db-doc-template.adoc")
    private lateinit var docTemplate: Resource

    @Value("\${documentation.database.output-dir:docs/database/generated}")
    private lateinit var outputDir: String

    @Bean
    fun databaseDocumentationRunner(): CommandLineRunner {
        return CommandLineRunner {
            println("Generating database documentation...")
            
            // Create output directory if it doesn't exist
            Files.createDirectories(Paths.get(outputDir))
            
            // Scan for entity classes
            val reflections = Reflections(
                ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage("np.sthaniya.dpis"))
                    .setScanners(Scanners.TypesAnnotated)
            )
            
            val entityClasses = reflections.getTypesAnnotatedWith(Entity::class.java)
            
            // Generate documentation for each entity
            entityClasses.forEach { entityClass ->
                generateEntityDocumentation(entityClass)
            }
            
            println("Database documentation generation complete.")
        }
    }
    
    private fun generateEntityDocumentation(entityClass: Class<*>) {
        val table = entityClass.getAnnotation(Table::class.java)
        val tableName = table?.name ?: entityClass.simpleName.lowercase()
        val fileName = "${tableName.replace("_", "-")}.adoc"
        
        val template = docTemplate.inputStream.reader().readText()
        
        val fields = entityClass.declaredFields
            .filter { !it.isSynthetic }
            .sortedBy { it.name }
        
        val fieldRows = fields.joinToString("\n") { field ->
            val comment = field.getAnnotation(Comment::class.java)?.value ?: ""
            "|`${field.name}` |`${getFieldType(field)}` |${isNullable(field)} |${comment}"
        }
        
        val documentation = template
            .replace("{entity-name}", entityClass.simpleName)
            .replace("{table-name}", tableName)
            .replace("{entity-description}", entityClass.getAnnotation(Comment::class.java)?.value ?: "")
            .replace("{field-rows}", fieldRows)
        
        val outputPath = Paths.get(outputDir, fileName)
        Files.write(
            outputPath,
            documentation.toByteArray(),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
        
        println("Generated documentation for $tableName -> $outputPath")
    }
    
    private fun getFieldType(field: Field): String {
        return field.type.simpleName
    }
    
    private fun isNullable(field: Field): String {
        // This is a simplified approach. In real-world, you might want to check
        // @Column(nullable=false) or other annotations
        return if (field.type.isPrimitive) "false" else "true"
    }
}
