package me.dio.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
@Profile("prd")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        String rawUrl = System.getenv("DATABASE_URL");
        String pgHost = System.getenv("PGHOST");
        String pgPort = System.getenv("PGPORT");
        String pgDb = System.getenv("PGDATABASE");
        String pgUser = System.getenv("PGUSER");
        String pgPass = System.getenv("PGPASSWORD");

        String jdbcUrl;
        String username;
        String password;

        if (rawUrl != null && !rawUrl.isBlank()) {
            URI uri = new URI(rawUrl);
            String userInfo = uri.getUserInfo();
            if (userInfo != null && userInfo.contains(":")) {
                username = userInfo.split(":", 2)[0];
                password = userInfo.split(":", 2)[1];
            } else {
                username = pgUser;
                password = pgPass;
            }
            jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
        } else {
            jdbcUrl = "jdbc:postgresql://" + pgHost + ":" + pgPort + "/" + pgDb;
            username = pgUser;
            password = pgPass;
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
}
