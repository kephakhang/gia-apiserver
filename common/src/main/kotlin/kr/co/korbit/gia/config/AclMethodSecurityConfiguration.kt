package kr.co.korbit.gia.config

import com.zaxxer.hikari.HikariDataSource
import org.aopalliance.intercept.MethodInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.cache.ehcache.EhCacheFactoryBean
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor
import org.springframework.security.access.intercept.aspectj.AspectJMethodSecurityInterceptor
import org.springframework.security.access.method.MethodSecurityMetadataSource
import org.springframework.security.acls.AclPermissionCacheOptimizer
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.acls.domain.*
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.jdbc.LookupStrategy
import org.springframework.security.acls.model.PermissionGrantingStrategy
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.sql.DataSource

/**
 * ACL Spring 보안 설정 클래스 (issue ref : https://github.com/spring-projects/spring-boot/issues/15882)
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class AclMethodSecurityConfiguration
    : GlobalMethodSecurityConfiguration() {

    @Autowired
    lateinit var jpaAdmin2DataSource: DataSource

    @Autowired
    lateinit var defaultMethodSecurityExpressionHandler: MethodSecurityExpressionHandler


    @Bean(name = ["jpaAdmin2DataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.admin2")
    fun jpaAdmin2DataSource(): DataSource {
        val dataSource = DataSourceBuilder.create().type(HikariDataSource::class.java).build()
        //dataSource.connectionInitSql = "SET NAMES utf8mb4; set @@session.time_zone = '+00:00'"
        return dataSource
    }

    @Bean
    fun aclCache(): EhCacheBasedAclCache {
        return EhCacheBasedAclCache(
            aclEhCacheFactoryBean().getObject(),
            permissionGrantingStrategy(),
            aclAuthorizationStrategy()
        )
    }

    @Bean
    fun aclEhCacheFactoryBean(): EhCacheFactoryBean {
        val ehCacheFactoryBean = EhCacheFactoryBean()
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject())
        ehCacheFactoryBean.setCacheName("aclCache")
        return ehCacheFactoryBean
    }

    @Bean
    fun aclCacheManager(): EhCacheManagerFactoryBean {
        return EhCacheManagerFactoryBean()
    }

    @Bean
    fun permissionGrantingStrategy(): PermissionGrantingStrategy {
        return DefaultPermissionGrantingStrategy(ConsoleAuditLogger())
    }

    @Bean
    fun aclAuthorizationStrategy(): AclAuthorizationStrategy {
        return AclAuthorizationStrategyImpl(SimpleGrantedAuthority("ROLE_ADMIN"))
    }

    @Bean
    fun lookupStrategy(): LookupStrategy {
        return BasicLookupStrategy(jpaAdmin2DataSource, aclCache(), aclAuthorizationStrategy(), ConsoleAuditLogger())
    }

    @Bean
    fun aclService(): JdbcMutableAclService {
        return JdbcMutableAclService(jpaAdmin2DataSource, lookupStrategy(), aclCache())
    }

    @Bean
    fun defaultMethodSecurityExpressionHandler(): MethodSecurityExpressionHandler {
        val expressionHandler = DefaultMethodSecurityExpressionHandler()
        val permissionEvaluator = AclPermissionEvaluator(aclService())
        expressionHandler.setPermissionEvaluator(permissionEvaluator)
        expressionHandler.setPermissionCacheOptimizer(AclPermissionCacheOptimizer(aclService()))
        return expressionHandler
    }

    override fun createExpressionHandler(): MethodSecurityExpressionHandler {
        return defaultMethodSecurityExpressionHandler
    }
}