package kr.co.korbit.gia.config

import com.querydsl.jpa.impl.JPAQueryFactory
import com.zaxxer.hikari.HikariDataSource
import kr.co.korbit.gia.env.Env
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
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource
import kotlin.collections.HashMap

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "jpaTestEntityManagerFactory",
        transactionManagerRef = "jpaTestTransactionManager",
        basePackages = ["kr.co.korbit.gia.jpa.test.repository"])
class JpaTestConfig() {

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
        adapter.setDatabasePlatform(Env.dialect)
        adapter.setGenerateDdl(false)
        val properties = HashMap<String, String?>()
        properties["hibernate.ddl-auto"] = Env.ddlAuto
        properties["show_sql"] = Env.showSqlFlag
        properties["format_sql"] = Env.formatSqlFlag
        properties["hibernate.default_batch_fetch_size"] = Env.defaultBatchSize
        properties["use_sql_comments"] = Env.useSqlCommentFlag
        properties["hibernate.naming.implicit-strategy}"] = Env.implicitStrategy
        properties["hibernate.naming.physical-strategy}"] = Env.physicalStrategy
        properties["hibernate.use-new-id-generator-mappings"] = Env.useNewIdGeneratorMappingsFlag
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
                .persistenceUnit("test")
                .build()

        factory.setPackagesToScan("kr.co.korbit.gia.jpa.test.model")
        return factory
    }

    @Bean(name = ["jpaTestEntityManager"])
    fun jpaTestEntityManager(
        @Qualifier("jpaTestEntityManagerFactory") jpaTestEntityManagerFactory: EntityManagerFactory): EntityManager {
        return jpaTestEntityManagerFactory.createEntityManager()
    }


    @Bean(name = ["jpaTestTransactionManager"])
    fun jpaTestTransactionManager(
            @Qualifier("jpaTestEntityManagerFactory") jpaTestEntityManagerFactory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(jpaTestEntityManagerFactory!!)
    }

    @Bean(name = ["jpaTestQueryFactory"])
    fun jpaTestQueryFactory(@Qualifier("jpaTestEntityManagerFactory") jpaTestEntityManagerFactory: EntityManagerFactory): JPAQueryFactory {

        return JPAQueryFactory(jpaTestEntityManagerFactory.createEntityManager())
    }
}