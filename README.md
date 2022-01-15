#### Create new
- Install sdkman (sdkman installs command line, https://sdkman.io/install)
    - `curl -s "https://get.sdkman.io" | bash`
    - `source "$HOME/.sdkman/bin/sdkman-init.sh"`
    - `sdk version` -> should print version
- Install spring: `sdk install springboot`
- Create new project: `spring init --dependencies=web,data-jpa first-mvn`

#### Install a JDBC (required to start server)
- To avoid database setup, add this to main: `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })`
- To connect to PostgrSQL: https://www.codejava.net/frameworks/spring-boot/connect-to-postgresql-database-examples

#### Starting application
- Start server dev mode: `mvn spring-boot:run`

#### Organization
- Controller (@RestController) -> Service (@Service) -> Dao (@Repository) -> Entity (@Component)
