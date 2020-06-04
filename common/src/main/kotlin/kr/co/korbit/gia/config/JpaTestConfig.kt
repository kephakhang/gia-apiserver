package kr.co.korbit.gia.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager
import org.springframework.orm.jpa.vendor.Database
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Calendar
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import kotlin.collections.HashMap

@Configuration
@EnableTransactionManagement
//@EntityScan(basePackages = ["kr.co.korbit.gia.jpa.test.model"])
@EnableJpaRepositories(entityManagerFactoryRef = "jpaTestEntityManagerFactory",
        transactionManagerRef = "jpaTestTransactionManager",
        basePackages = ["kr.co.korbit.gia.jpa.test.repository"])
class JpaTestConfig {
    val dialect = "org.hibernate.dialect.MySQL57Dialect"
    var ddlAuto = "validate"
    var showSql = "true"
    var useNewIdGeneratorMappings = "false"
    var implicitStrategy = "org.hibernate.cfg.ImprovedNamingStrategy"
    var physicalStrategy = "org.hibernate.cfg.ImprovedNamingStrategy"

    @Autowired(required = false)
    private val persistenceUnitManager: PersistenceUnitManager? = null


    @Bean(name = ["jpaTestDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.test")
    fun jpaTestDataSource(): DataSource {
        val dataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()
        //dataSource.connectionInitSql = "SET NAMES utf8mb4; set @@session.time_zone = '+00:00'"
        return dataSource
    }


    @Bean(name = ["jpaTestEntityManagerFactoryBuilder"])
    fun jpaTestEntityManagerFactoryBuilder(): EntityManagerFactoryBuilder {
        val adapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(true)
        adapter.setPrepareConnection(true)
        adapter.setDatabase(Database.MYSQL)
        adapter.setDatabasePlatform(dialect)
        adapter.setGenerateDdl(false)
        val properties = HashMap<String, String?>()
        properties["hibernate.ddl-auto"] = ddlAuto
        properties["show-sql"] = showSql
        properties["hibernate.naming.implicit-strategy}"] = implicitStrategy
        properties["hibernate.naming.physical-strategy}"] = physicalStrategy
        properties["hibernate.use-new-id-generator-mappings"] = useNewIdGeneratorMappings
        //builder.setCallback(getVendorCallback());
        return EntityManagerFactoryBuilder(
                adapter, properties, persistenceUnitManager)
    }


    @Bean(name = ["jpaTestEntityManagerFactory"])
    fun jpaTestEntityManagerFactory(
            @Qualifier("jpaTestEntityManagerFactoryBuilder") builder: EntityManagerFactoryBuilder,
            @Qualifier("jpaTestDataSource") jpaTestDataSource: DataSource?): LocalContainerEntityManagerFactoryBean {
        val factory =  builder
                .dataSource(jpaTestDataSource)
                .persistenceUnit("gia")
                .build()

        factory.setPackagesToScan("kr.co.korbit.gia.jpa.test.model")
        return factory
    }


    @Bean(name = ["jpaTestTransactionManager"])
    fun jpaTestTransactionManager(
            @Qualifier("jpaTestEntityManagerFactory") jpaTestEntityManagerFactory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(jpaTestEntityManagerFactory!!)
    }
}