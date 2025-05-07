package dev.edgeahz.ec.spstore.user_management.infrastructure.config;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.jooq.SQLDialect;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import java.sql.SQLException;

public class JooqExceptionTranslator implements ExecuteListener {

    @Override
    public void exception(ExecuteContext context) {
        SQLException exception = context.sqlException();
        if (exception != null) {
            SQLDialect dialect = context.configuration().dialect();
            SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());

            DataAccessException translated = translator.translate("jOOQ", context.sql(), exception);

            if (translated != null) {
                context.exception(translated);
            } else if (exception.getMessage().contains("constraint")) {
                context.exception(new DataIntegrityViolationException(
                        "Violación de restricción de integridad", exception));
            } else {
                context.exception(new InvalidDataAccessApiUsageException(
                        "Error de acceso a datos", exception));
            }
        }
    }
}
