License

Copyright (c) [2024]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files, to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

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
2.      Open your preferred software e.g. IntelliJ and if you want to clone the project ensure that the git extension is
        installed.
2.1     If project is on pc: Go to file and Select "Open" or "Open as project", locate the project and open the build.gradle
2.2     If cloning: Open the git extension and import the project, when prompt for security concerns press "Yes, Trust the authors"
3.      Go to extension and install gradle and gradle extension. Depending on your settings, may the "Extension Pack for
        Java", "Maven" and "Maven extension" extensions be needed.
3.1     Install Java if not installed, https://www.oracle.com/java/technologies/downloads/.
3.2     Install gradle (recommended min. 7.6.4), https://gradle.org/install/
3.3     Add JAVA_HOME and GRADLE_HOME to the system variables, same goes for PATH
3.3     Install gradle extension
3.4     Update the project structure and settings to use gradle and java sdk.

3.4.1   Project Structure -> Language level 8 (Lampdas, type annotations etc.)
3.4.2   Project Structure -> Project Settings -> Project -> SDK: Set to the desired java sdk
3.4.3   Goto File | Settings | Build, Execution, Deployment | Build Tools | Gradle
3.4.4   Go to Grade Projects section and change make sure that 'Download external annotations and dependencies' is
        ticked on, 'Set Build and run using:' to Gradle (default) and 'Run Tests using:' to Gradle (default), Set
        'Distribution' to Wrapper and 'Gradle JVM' to 1.8
3.4.5   Go to File | Settings | Build, Execution, Deployment | Compiler | Java Compiler: Set 'Use compiler' to Javac and
        set 'Project bytecode version' to 1.8
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

Changes involving libraries, dependencies, build.gradle, imports, external features, methods, class and fields
requires a repeat of step 5.1 - 7.


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

ADDITIONALLY:
Existing service calls to MusicBrainz can be updated with inspiration from MusicBrainz own library
https://musicbrainz.org/doc/MusicBrainz_API#Libraries.
For example: A more detailed insight into Http responses and handling of them can be seen in
MyWebServiceImplementation.java and also the unittest required if implementing new features, can
be inspired from Unittests.java. But be aware that the library is deprecated, and it is therefore advised to test any
code implemented into the application carefully.

Furthermore, performance test has been added to evaluate the performance of the software under various
conditions, response times, throughput, and resource utilization, identify bottlenecks, but they are not completed yet.
Also, security test may be relevant depending on the future development of the application. Examples: Penetration
testing, vulnerability scanning, and injection attacks. Existing exception handling may be optimized to handle the
exception and correct the error when it occurs, as of now the whole process restarts.


