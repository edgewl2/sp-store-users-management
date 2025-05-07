package dev.edgeahz.ec.spstore.user_management.infrastructure.config;

import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class JooqConfig {

    @Bean
    public ConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public ExecuteListenerProvider executeListenerProvider() {
        return new DefaultExecuteListenerProvider(new JooqExceptionTranslator());
    }

    @Bean
    public DSLContext dsl(org.jooq.Configuration configuration) {
        return new DefaultDSLContext(configuration);
    }

    @Bean
    public org.jooq.Configuration configuration(ConnectionProvider connectionProvider, ExecuteListenerProvider executeListenerProvider) {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider);
        jooqConfiguration.set(SQLDialect.MYSQL);

        // Configurar listeners para logging y excepciones
        jooqConfiguration.set(executeListenerProvider);

        return jooqConfiguration;
    }
}
