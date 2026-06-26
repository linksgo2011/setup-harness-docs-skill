package com.consultation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.List;

@Component
public class ResetDbService {

    private final JdbcTemplate jdbc;
    private final DataSource dataSource;

    @Value("classpath:data.sql")
    private Resource dataScript;

    public ResetDbService(JdbcTemplate jdbc, DataSource dataSource) {
        this.jdbc = jdbc;
        this.dataSource = dataSource;
    }

    public void resetAll() {
        List<String> tables = jdbc.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema=DATABASE()",
                String.class
        );
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 0");
        for (String table : tables) {
            jdbc.execute("TRUNCATE TABLE " + table);
        }
        jdbc.execute("SET FOREIGN_KEY_CHECKS = 1");
        reSeed();
    }

    private void reSeed() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataScript);
        populator.execute(dataSource);
    }
}
