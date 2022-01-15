#### Create new
- Install sdkman (sdkman installs command line, https://sdkman.io/install)
    - `curl -s "https://get.sdkman.io" | bash`
    - `source "$HOME/.sdkman/bin/sdkman-init.sh"`
    - `sdk version` -> should print version
- Install spring: `sdk install springboot`
- Create new project: `spring init --dependencies=web,data-jpa first-mvn`
- 

#### Starting application
- Start server dev mode: `mvn spring-boot:run`

#### Organization
- Controller (@RestController) -> Service (@Service) -> Dao (@Repository) -> Entity (@Component)
