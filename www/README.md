<br>

The Jakarta Mail API provides a platform-independent and
protocol-independent framework to build mail and messaging
applications.
The Jakarta Mail API is available as an optional package for use with the
[Java SE platform](http://www.oracle.com/technetwork/java/javase/index.html)
and is also included in the
[Jakarta EE platform](http://jakarta.ee) and the
[Java EE platform](http://www.oracle.com/technetwork/java/javaee/index.html).

**NOTE:** The documentation for the implementation of this specification, Angus Mail, is available
[here](https://eclipse-ee4j.github.io/angus-mail/), the documentation for older versions,
Jakarta Mail/JavaMail, is available [here](README-JakartaMail)

<br/>

# Table of Contents
* [Latest News](#Latest_News)
* [API Documentation](#API_Documentation)
* [Help](#Help)
* [Bugs](#Bugs)
* [Development Releases](#Development_Releases)

# <a name="Latest_News"></a>Latest News

## May 5, 2023 - Jakarta Mail 2.1.2 Final Release ##

The 2.1.2 release is a bug fix release of the Jakarta Mail project
in the 2.1.x line, and includes several bug fixes and enhancements.
See [the changelog](docs/CHANGES.txt) for details.

## January 11, 2023 - Jakarta Mail 2.1.1 Final Release ##

The 2.1.1 release is a bug fix release of the Jakarta Mail project
in the 2.1.x line, and includes several bug fixes and enhancements.
See [the changelog](docs/CHANGES.txt) for details.

## November 20, 2021 - Jakarta Mail 2.1.0 Final Release ##

The 2.1.0 release breaks the tight integration between Jakarta Mail Specification
API and the implementation and provides standalone API jar file only.
The implementation itself, formerly JakartaMail, is now standalone project -
[Eclipse Angus](https://eclipse-ee4j.github.io/angus-mail/).
This version of the specification is included
in the [Jakarta EE 10 Platform](https://jakarta.ee/specifications/platform/10/).

## August 18, 2021 - Jakarta Mail implementation moves to Eclipse Angus ##

To break tight integration between Jakarta Mail Specification API and the implementation,
sources of the implementation were moved to the new
project - [Eclipse Angus](https://eclipse-ee4j.github.io/angus-mail) -
and further development continues there.
Eclipse Angus is the direct successor of JavaMail/JakartaMail.

## April 8, 2021 - Jakarta Mail 1.6.7 Final Release ##

The 1.6.7 release is a bug fix release of the Jakarta Mail project
in the 1.x line, and includes several bug fixes and enhancements.
The main jar file is located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.7/jakarta.mail-1.6.7.jar).

## April 6, 2021 - Jakarta Mail 2.0.1 Final Release ##

The 2.0.1 release is a bug fix release of the Jakarta Mail project
in the 2.x line, and includes several bug fixes and enhancements.
The main jar file is located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.1/jakarta.mail-2.0.1.jar).

## February 28, 2021 - Jakarta Mail 1.6.6 Final Release ##

The 1.6.6 release is a bug fix release of the Jakarta Mail project
in the 1.x line, and includes several bug fixes and enhancements.
The main jar file is located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.6/jakarta.mail-1.6.6.jar).


## October 23, 2020 - Jakarta Mail 2.0.0 Final Release ##

Jakarta Mail 2.0.0 release is the first release with package
namespace changed to `jakarta.mail`. The main jar file
is located at [com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.0/jakarta.mail-2.0.0.jar).

This release contains no other enhancements nor bug fixes,
except for the minimal required Java SE version which is now Java SE 8.
This is also the release included in Jakarta EE 9.
Applications are able to switch to this new version
by just changing all imports that use `javax.mail.*`
to instead use `jakarta.mail.*`.

## March 10, 2020 - Jakarta Mail 1.6.5 Final Release ##

The 1.6.5 release is (hopefully) the last release of the Jakarta Mail project
in the 1.x line, and includes several bug fixes and enhancements.
The main jar file is located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.5/jakarta.mail-1.6.5.jar).

Jakarta Mail, like other parts of [Jakarta EE](https://jakarta.ee),
is moving to the `jakarta.*` package namespace.
This is a major change, and so the next release will be Jakarta Mail 2.0.0,
which will be included in Jakarta EE 9.
Applications should be able to switch to this new version fairly easily
by just changing all imports that use `javax.mail.*` to instead use
`jakarta.mail.*`.

## August 28, 2019 - Jakarta Mail 1.6.4 Final Release ##

The 1.6.4 release is the first release of the Jakarta Mail project using the
[Jakarta EE Specification Process](https://jakarta.ee/about/jesp/)
and includes several bug fixes and enhancements.
The main jar file is located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.4/jakarta.mail-1.6.4.jar).

## July 3, 2019 - Jakarta Mail is the new name for JavaMail ##

The JavaMail technology contributed to the Eclipse Foundation has been renamed
to "Jakarta Mail" to reflect its role in the
[Jakarta EE platform](https://jakarta.ee/).

## November 26, 2018 - JavaMail 1.6.3 Final Release ##

The 1.6.3 release is the first release of the Eclipse project for JavaMail
and includes no bug fixes or enhancements. It does include changes
to the Maven coordinates. The main jar file is now located at
[com.sun.mail:jakarta.mail](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.3/jakarta.mail-1.6.3.jar).

## September 14, 2018 - JavaMail project moves to the Eclipse Foundation! ##

The JavaMail project is now hosted at the Eclipse Foundation as part of
the [EE4J project](https://projects.eclipse.org/projects/ee4j).

<br/>

# <a name="API_Documentation"></a>API Documentation

The Jakarta Mail API is defined through the
[Jakarta EE Specification Process](https://jakarta.ee/about/jesp/).

The Jakarta Mail specification and API documentation are available
[here](https://jakarta.ee/specifications/mail/).

Note that Jakarta Mail 1.6 is identical to JavaMail 1.6.

The JavaMail 1.6 and earlier API is defined through the Java Community Process as
[JSR 919](http://jcp.org/en/jsr/detail?id=919).

The following documents summarize the API changes in each release of
the JavaMail API specification:

-   [JavaMail 2.1](docs/JavaMail-2.1-changes.txt)
-   [JavaMail 2.0](docs/JavaMail-2.0-changes.txt)
-   [JavaMail 1.6](docs/JavaMail-1.6-changes.txt)
-   [JavaMail 1.5](docs/JavaMail-1.5-changes.txt)
-   [JavaMail 1.4](docs/JavaMail-1.4-changes.txt)
-   [JavaMail 1.3](docs/JavaMail-1.3-changes.txt)
-   [JavaMail 1.2](docs/JavaMail-1.2-changes.txt)
-   [JavaMail 1.1](docs/JavaMail-1.1-changes.txt)

<br/>

# <a name="Help"></a>Help

Please read the
[Jakarta Mail FAQ](FAQ.html)!
Read it again. Tell everyone you know to read it. Thank you!

You can post questions to the
[mail-dev mailing list](https://accounts.eclipse.org/mailing-list/mail-dev).

Or, post a question on [Stack Overflow](http://stackoverflow.com/) using the
[javamail](http://stackoverflow.com/questions/tagged/javamail) tag.

<br/>

# <a name="Bugs"></a>Bugs

Jakarta Mail API bugs are tracked in the
[GitHub Jakarta Mail project issue tracker](https://github.com/jakartaee/mail-api/issues).

<br/>

# <a name="Development_Releases"></a>Development Releases

From time to time snapshot releases of the next version of Jakarta Mail
Specification API under development are published to the
[Jakarta Sonatype OSS repository](https://jakarta.oss.sonatype.org).
These snapshot releases have received only minimal testing, but may
provide previews of bug fixes or new features under development.

For example, you can download the jakarta.mail-api.jar file from the Jakarta Mail
2.1.1-SNAPSHOT release
[here](https://jakarta.oss.sonatype.org/content/repositories/snapshots/jakarta/mail/jakarta.mail-api/2.1.1-SNAPSHOT/).
Be sure to scroll to the bottom and choose the jar file with the most
recent time stamp.

You'll need to add the following configuration to your Maven ~/.m2/settings.xml
to be able to use these with Maven:

```
    <profiles>
        <!-- to allow loading Jakarta snapshot artifacts -->
        <profile>
            <id>jakarta-snapshots</id>
            <pluginRepositories>
                <pluginRepository>
                    <id>jakarta-snapshots</id>
                    <name>Jakarta Snapshots</name>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>fail</checksumPolicy>
                    </snapshots>
                    <url>https://jakarta.oss.sonatype.org/content/repositories/snapshots/</url>
                    <layout>default</layout>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>
```

And then when you build use `mvn -Pjakarta-snapshots ...`.

If you want the plugin repository to be enabled all the time so you don't need the -P, add:

```
    <activeProfiles>
        <activeProfile>jakarta-snapshots</activeProfile>
    </activeProfiles>
```

<br/>

By contributing to this project, you agree to these additional terms of
use, described in [CONTRIBUTING](CONTRIBUTING).
