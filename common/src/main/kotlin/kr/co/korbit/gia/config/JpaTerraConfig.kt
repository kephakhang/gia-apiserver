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
//@EntityScan(basePackages = ["kr.co.korbit.gia.jpa.terra.model"])
@EnableJpaRepositories(entityManagerFactoryRef = "jpaTerraEntityManagerFactory",
        transactionManagerRef = "jpaTerraTransactionManager",
        basePackages = ["kr.co.korbit.gia.jpa.terra.repository"])
class JpaTerraConfig {
    val dialect = "org.hibernate.dialect.MySQL57Dialect"
    var ddlAuto = "validate"
    var showSql = "true"
    var useNewIdGeneratorMappings = "false"
    var implicitStrategy = "org.hibernate.cfg.ImprovedNamingStrategy"
    var physicalStrategy = "org.hibernate.cfg.ImprovedNamingStrategy"

    @Autowired(required = false)
    private val persistenceUnitManager: PersistenceUnitManager? = null

    @Bean(name = ["jpaTerraDataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.terra")
    fun jpaTerraDataSource(): DataSource {
        val dataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()
        //dataSource.connectionInitSql = "SET NAMES utf8mb4; set @@session.time_zone = '+00:00'"
        return dataSource
    }

    @Bean(name = ["jpaTerraEntityManagerFactoryBuilder"])
    fun jpaTerraEntityManagerFactoryBuilder(): EntityManagerFactoryBuilder {
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

    @Bean(name = ["jpaTerraEntityManagerFactory"])
    fun jpaTerraEntityManagerFactory(
            @Qualifier("jpaTerraEntityManagerFactoryBuilder") builder: EntityManagerFactoryBuilder,
            @Qualifier("jpaTerraDataSource") jpaTerraDataSource: DataSource?): LocalContainerEntityManagerFactoryBean {
        val factory =  builder
                .dataSource(jpaTerraDataSource)
//                .packages("kr.co.korbit.gia.jpa.terra.model")
                .persistenceUnit("terra")
                .build()

        factory.setPackagesToScan("kr.co.korbit.gia.jpa.terra.model")
        return factory
    }

    @Bean(name = ["jpaTerraTransactionManager"])
    fun jpaTerraTransactionManager(
            @Qualifier("jpaTerraEntityManagerFactory") jpaTerraEntityManagerFactory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(jpaTerraEntityManagerFactory!!)
    }
}