package me.dio.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prd")
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public DataSource dataSource() throws Exception {
        String rawUrl = nullIfBlank(System.getenv("DATABASE_URL"));
        String pgHost = nullIfBlank(System.getenv("PGHOST"));
        String pgPort = nullIfBlank(System.getenv("PGPORT"));
        String pgDb = nullIfBlank(System.getenv("PGDATABASE"));
        String pgUser = nullIfBlank(System.getenv("PGUSER"));
        String pgPass = nullIfBlank(System.getenv("PGPASSWORD"));

        log.info("DB config — DATABASE_URL present: {}, PGHOST: {}, PGPORT: {}, PGDATABASE: {}",
                rawUrl != null, pgHost, pgPort, pgDb);

        String jdbcUrl;
        String username;
        String password;

        if (rawUrl != null) {
            URI uri = new URI(rawUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            String userInfo = uri.getUserInfo();

            jdbcUrl = "jdbc:postgresql://" + host + (port > 0 ? ":" + port : "") + path;

            if (userInfo != null && userInfo.contains(":")) {
                username = userInfo.split(":", 2)[0];
                password = userInfo.split(":", 2)[1];
            } else {
                username = pgUser;
                password = pgPass;
            }
        } else if (pgHost != null) {
            String port = pgPort != null ? pgPort : "5432";
            String db = pgDb != null ? pgDb : "railway";
            jdbcUrl = "jdbc:postgresql://" + pgHost + ":" + port + "/" + db;
            username = pgUser;
            password = pgPass;
        } else {
            throw new IllegalStateException(
                "No database configuration found. Set DATABASE_URL or PGHOST environment variable.");
        }

        log.info("Connecting to: {}", jdbcUrl.replaceAll(":[^@/]+@", ":***@"));

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    private static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
