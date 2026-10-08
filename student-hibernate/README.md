# Student Hibernate Application (Maven + MySQL)

## Requirements
- JDK 11+
- Maven 3.6+
- Internet access (to download dependencies and reach the MySQL host)

## Configure
DB settings are in `src/main/resources/hibernate.cfg.xml`
(host `db01.dbhost.dev`, port `5051`, db `db_45537rwvw`).

## Run
```
mvn clean compile exec:java
```

## What it does
1. Creates the `students` table (id, name, email, course) via `hbm2ddl.auto=update`
2. Inserts a Student and prints the generated ID
3. Reads it back to verify the insert
4. Updates email and course, reads it back to verify the update
5. Lists all rows

You can also confirm in MySQL: `SELECT * FROM students;`
