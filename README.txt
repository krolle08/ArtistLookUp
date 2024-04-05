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

1. Open your Programming Software (VS-Code, IntelliJ, Visual Studio)
2. Go to file and Select "Open" or "Open as project"
3. Locate the project folder and open the build.gradle
3.1 When prompted for "Open as project" or "Open as file", choose "Open as project"
4. Depending on your programming software go to the plugin tab and make sure that the following plugins are installed:
    - Gradle
    - Gradle Extension
    - Maven
    - Maven Extension
5. If done correctly a gradle tab should appear in the UI of your programming software
5.1 In the gradle tab go to Tasks -> build -> clean and then build
5.2 If the tab is not present in your UI, try restart the program.
    If still not present try Ctrl + Shift + O or Cmd + Shift + O on macOS (Only IntelliJ IDEA)
    If still not present open the programs terminal and run ./gradlew clean build or ./gradle clean build
    (depending on your gradle setup)
6. Download sources for gradle with the "Download Sources" button in the gradletab.  Refresh gradle dependencies by
   tabbing the
   If not present run the following command in the terminal ./gradlew dependencies --refresh-dependencies --write-locks
   or ./gradle dependencies --refresh-dependencies --write-locks
7. If using IntelliJ IDEA, go to the gradle tab press the "Reload all Gradle Project"
6. Run the application from the run configuration (In IntelliJ IDEA it is located in the
    top right corner). If not present in the UI of the program locate the main method in the Application class and run
    it manually.

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
code implemented into the api carefully.
Furthermore, error due to bad user inputs, resulting in empty or bad responses, could be optimized by handling the
problem where they occur, as of now it restarts the whole process.
