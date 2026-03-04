FROM liquibase/liquibase:4.31.1

COPY liquibase.properties /liquibase/liquibase.properties
COPY changelog/ /liquibase/changelog/

CMD ["liquibase", "--changelog-file=changelog/changelog-v0.0.1.sql", "update"]