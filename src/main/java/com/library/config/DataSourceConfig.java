package com.library.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(@Value("${DATABASE_URL:}") String databaseUrl) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");

        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException("DATABASE_URL environment variable is not set");
        }

        ds.setJdbcUrl(buildJdbcUrl(databaseUrl));
        return ds;
    }

    private String buildJdbcUrl(String url) {
        String raw = url;
        if (raw.startsWith("jdbc:")) {
            raw = raw.substring(5);
        }
        String prefix = "postgresql://";
        if (!raw.startsWith(prefix)) {
            return raw;
        }
        String rest = raw.substring(prefix.length());

        String credentials = null;
        int at = rest.lastIndexOf('@');
        if (at >= 0) {
            credentials = rest.substring(0, at);
            rest = rest.substring(at + 1);
        }

        String[] hostPortDb = rest.split("/", 2);
        String hostPort = hostPortDb[0];
        String db = hostPortDb.length > 1 ? hostPortDb[1] : "";

        String jdbcUrl = "jdbc:postgresql://" + hostPort;
        if (!db.isEmpty()) {
            jdbcUrl += "/" + db;
        }

        if (credentials != null) {
            String[] userPass = credentials.split(":", 2);
            if (userPass.length == 2) {
                jdbcUrl += "?user=" + userPass[0] + "&password=" + userPass[1];
            }
        }
        return jdbcUrl;
    }
}
