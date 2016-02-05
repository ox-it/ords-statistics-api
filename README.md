# ords-statistics-api

The statistics API for the Online Research Database Service (ORDS)

## REST API Documentation

The API consists only of GET /api/1.0/statistics.

## Configuration Properties

### Database (JDBC) configuration

    ords.database.rootdbuser=ords
    ords.database.rootdbpassword=ords
    ords.odbc.masterpassword=ords
    postgresConnectorFile=postgresql-8.4-702.jdbc4.jar

### Hibernate  and security configuration

    ords.hibernate.configuration=hibernate.cfg.xml

Optional; the location of the hibernate configuration file.

    ords.shiro.configuration=file:/etc/ords/shiro.ini

Optional; the location of the Shiro INI file

     ords.server.configuration=serverConfig.xml

Optional; the location of the Server configuration file

### Mail server configuration

The following properties are used for the email server connection

    mail.smtp.auth=true
    mail.smtp.starttls.enable=true
    mail.smtp.host=localhost
    mail.smtp.port=587
    mail.smtp.from=daemons@sysdev.oucs.ox.ac.uk
    mail.smtp.username=
    mail.smtp.password=
