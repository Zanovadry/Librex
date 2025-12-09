//package org.example.librex;
//
//import jakarta.annotation.PostConstruct;
//import org.example.librex.dictionaries.countries.CountryName;
//import org.example.librex.dictionaries.permissions.Role;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
////Temporary class for populating temporary H2 database for test purposes
////TODO: Delete after linking postgres
//
//@Component
//public class DataInitializer {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public DataInitializer(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @PostConstruct
//    public void init() {
//
//        System.out.println("Initializing permissions...");
//
//        jdbcTemplate.execute("DELETE FROM permission");
//        jdbcTemplate.execute("""
//                            INSERT INTO permission (ROLE) VALUES
//                            ('%s'), ('%s'), ('%s')
//                            """.formatted(
//                        Role.ADMIN,
//                        Role.CUSTOMER,
//                        Role.LIBRARIAN
//        ));
//
//        System.out.println("Initializing countries...");
//
//        jdbcTemplate.execute("DELETE FROM country");
//        jdbcTemplate.execute("""
//                            INSERT INTO country (COUNTRY_NAME) VALUES
//                            ('%s'), ('%s'), ('%s')
//                            """.formatted(
//                CountryName.POLAND,
//                CountryName.FINLAND,
//                CountryName.UNITED_STATES
//        ));
//    }
//
//
//}
//
