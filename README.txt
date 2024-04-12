### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.3/gradle-plugin/reference/html/#build-image)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


To start the program follow this guide:

1.      Make sure Git is installed on your pc, if not download here: https://git-scm.com/
2.      Open your preferred IDE.
2.1     If repository is on pc: Go to file and Select "Open" or "Open as project", locate the project and open the build.gradle
2.2     If cloning: Open the git extension and import the project using the URL <https://github.com/krolle08/ArtistLookUp.git>
3.      Go to extension and install gradle and gradle extension. Depending on your settings, may the "Extension Pack for
        Java", "Maven" and "Maven extension" extensions be needed.
3.1     Install Java (17.0.10 recommended), if not installed go here: https://www.oracle.com/java/technologies/downloads/.
3.2     Install gradle (7.6.4 recommended), https://gradle.org/install/
3.3     Add JAVA_HOME and GRADLE_HOME to the system variables, also add them to PATH
3.3     Install gradle extension on your IDE.
3.4     Update the project structure and settings to use gradle and java
3.4.1   Project Structure -> Language min. level 11
3.4.2   Project Structure -> Project Settings -> Project -> SDK: decided on what java version you want
3.4.3   Goto File | Settings | Build, Execution, Deployment | Build Tools | Gradle
3.4.4   Go to Gradle Projects section and change make sure 'Download external annotations and dependencies' is
        ticked on, 'Set Build and run using:' to Gradle (default) and 'Run Tests using:' to Gradle (default), Set
        'Distribution' to Wrapper and 'Gradle JVM' to JAVA HOME or what you prefer.
3.4.4.1 If using gradle wrapper, content of gradle-wrapper.properties (version number may differ):

        distributionBase=GRADLE_USER_HOME
        distributionPath=wrapper/dists
        distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
        networkTimeout=10000
        validateDistributionUrl=true
        zipStoreBase=GRADLE_USER_HOME
        zipStorePath=wrapper/dists


3.4.5   Go to File | Settings | Build, Execution, Deployment | Compiler | Java Compiler: Set 'Use compiler' to Javac and
        set 'Project bytecode version' to 8
4.      Restart the program.
5.      A gradle tab should have appeared in your UI, if not check the "view" tab to see if it is disabled.
6.      If tab is available: In the gradle tab go to Tasks -> build -> clean and then build
        If tab not available: Open terminal in program and run ./gradlew clean build or ./gradle clean build
        (depending on your gradle setup)
6.1     If tab is available: Download sources for gradle by pressing the "Download Sources" button in the gradle tab.
        If tab not available: Run the following command in the terminal ./gradlew dependencies --refresh-dependencies
        --write-locks or ./gradle dependencies --refresh-dependencies --write-locks
6.2     If using IntelliJ IDEA, press the "Reload all Gradle Project" buttom.
7.      Project is now ready to run. To run go to the run configuration (Located in the
        top right corner in IntelliJ), and start the application, or else navigate to the main class Application and
        execute the main method.

Tip:

1. If having issues starting the application check your java and gradle settings, and change "INFO" to "DEBUG" for the
following two properties in the application.properties file:

logging.level.org.springframework=INFO
logging.level.org.springframework.boot=INFO

2. If you want to use spring boots own logging, you can delete the file or the content of logback-spring.xml

Changes involving libraries, dependencies, build.gradle, imports, external features, methods, class and fields
requires a repeat of step 6 - 7.


###Future Improvements
TYPE OF DATA:
Expanding the service to handle search request regarding other types of request allowed by MusicBrainz such as:
- Area:
- GENRE:
- INSTRUMENT:
- LABEL:
- PLACE:
- RECORDING:
- RELEASE_GROUP:
- URL:
- WORK:

Read more: MusicBrainz https://musicbrainz.org/doc

DATABASE:
Adding musicbrainz own database to improve system performance. It requires a batchjob to handle the dump
or update of the database. A complete data snapshot of the entire database is generated twice a week
https://musicbrainz.org/doc/MusicBrainz_Database
It is recommended to use PostgreSQL when querying the MusicBrainz database.
The server api can be found at: https://github.com/metabrainz/musicbrainz-server

MBID WEBSERVICE:
Further development on Artist information can be achieved by expanding the MusicBrain ID lookup
as they are compatible with other webservices such as
https://wiki.musicbrainz.org/User:Mineo/APIs_understanding_MBIDs

LANGUAGE:
Adding a feature allowing multiple language responses would be advisable, because tests has shown that some artist only
has wikipedia descriptions in specific languages. This service only looks for english descriptions.

TEST:
Expanding the different scenarios tested to see how internal errors and failed responses from external services are
handled, can be further developed.
Furthermore, performance test may be added to evaluate performance under various conditions, response times, throughput,
and resource utilization and to identify bottlenecks. Also, security test may be relevant depending on the future
development of the application. Examples: Penetration testing, vulnerability scanning, and injection attacks.

EXCEPTION:
Existing exception handling may be optimized to handle the various responses and type of request the application takes
care of. This helps improve errors with more precise description of the issue and where it appeared and occurred, which
helps to improve the stability of the application.

ADDITIONALLY:
Existing service calls to MusicBrainz can be updated with inspiration from MusicBrainz own library
https://musicbrainz.org/doc/MusicBrainz_API#Libraries.
For example: A more detailed insight into Http responses and handling of them can be seen in
MyWebServiceImplementation.java and also the unittest required if implementing new features, can
be inspired from Unittests.java. But be aware that the library is deprecated, and it is therefore advised to test any
code implemented into the application carefully.



