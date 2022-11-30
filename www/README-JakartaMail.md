# Table of Contents
* [Latest News](#Latest_News)
* [Download Jakarta Mail Release](#Download_Jakarta_Mail_Release)
* [API Documentation](#API_Documentation)
* [Samples](#Samples)
* [Help](#Help)
* [Bugs](#Bugs)
* [Development Releases](#Development_Releases)
* [Jakarta Mail for Android](#Jakarta_Mail_for_Android)
* [Project Documentation](#Project_Documentation)

<br/>

# <a name="Latest_News"></a>Latest News

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

# <a name="Download_Jakarta_Mail_Release"></a>Download Jakarta Mail Release

The latest release of Jakarta Mail is 2.0.1.

The following table provides easy access to the latest release. Most
people will only need the main Jakarta Mail implementation in the
jakarta.mail.jar file.

| Item                                                                                                                            |Description|
|:--------------------------------------------------------------------------------------------------------------------------------|:----------|
| [jakarta.mail.jar](https://github.com/jakartaee/mail-api/releases/download/2.0.1/jakarta.mail-2.0.1.jar/jakarta.mail-2.0.1.jar) |The Jakarta Mail implementation, including the SMTP, IMAP, and POP3 protocol providers|
| [README.txt](docs/README.txt)                                                                                                   |Overview of the release|
| [NOTES.txt](docs/NOTES.txt)                                                                                                     |Additional notes about using Jakarta Mail|
| [SSLNOTES.txt](docs/SSLNOTES.txt)                                                                                               |Notes on using SSL/TLS with Jakarta Mail|
| [NTLMNOTES.txt](docs/NTLMNOTES.txt)                                                                                             |Notes on using NTLM authentication with Jakarta Mail|
| [CHANGES.txt](docs/CHANGES.txt)                                                                                                 |Changes since the previous release|
| [COMPAT.txt](docs/COMPAT.txt)                                                                                                   |Important notes about compatibility|


In addition, the Jakarta Mail jar files are published to the Maven repository.
The main Jakarta Mail jar file, which is all most applications will need,
can be included using this Maven dependency:
```
        <dependencies>
            <dependency>
                <groupId>com.sun.mail</groupId>
                <artifactId>jakarta.mail</artifactId>
                <version>2.0.1</version>
            </dependency>
        </dependencies>
```
You can find all of the Jakarta Mail jar files in
[Maven Central](http://search.maven.org).


|jar file|groupId|artifactId|Description|
|:-------|:------|:---------|:----------|
|[jakarta.mail.jar](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/2.0.1/jakarta.mail-2.0.1.jar)|com.sun.mail|jakarta.mail|The Jakarta Mail implementation jar file, including the SMTP, IMAP, and POP3 protocol providers|
|[jakarta.mail-api.jar](https://repo1.maven.org/maven2/jakarta/mail/jakarta.mail-api/2.0.1/jakarta.mail-api-2.0.1.jar)|jakarta.mail|jakarta.mail-api|The Jakarta Mail API definitions only, suitable for compiling against; use only with a Maven "provided" dependency scope|
|[mailapi.jar](https://repo1.maven.org/maven2/com/sun/mail/mailapi/2.0.1/mailapi-2.0.1.jar)|com.sun.mail|mailapi|The Jakarta Mail implementation with no protocol providers; use with one of the following providers|
|[smtp.jar](https://repo1.maven.org/maven2/com/sun/mail/smtp/2.0.1/smtp-2.0.1.jar)|com.sun.mail|smtp|The SMTP protocol provider|
|[imap.jar](https://repo1.maven.org/maven2/com/sun/mail/imap/2.0.1/imap-2.0.1.jar)|com.sun.mail|imap|The IMAP protocol provider|
|[pop3.jar](https://repo1.maven.org/maven2/com/sun/mail/pop3/2.0.1/pop3-2.0.1.jar)|com.sun.mail|pop3|The POP3 protocol provider|
|[gimap.jar](https://repo1.maven.org/maven2/com/sun/mail/gimap/2.0.1/gimap-2.0.1.jar)|com.sun.mail|gimap|An EXPERIMENTAL Gmail IMAP protocol provider that supports Gmail-specific features|
|[dsn.jar](https://repo1.maven.org/maven2/com/sun/mail/dsn/2.0.1/dsn-2.0.1.jar)|com.sun.mail|dsn|Support for parsing and creating messages containing Delivery Status Notifications|
|[logging-mailhandler.jar](https://repo1.maven.org/maven2/com/sun/mail/logging-mailhandler/2.0.1/logging-mailhandler-2.0.1.jar)|com.sun.mail|logging-mailhandler|A java.util.logging handler that uses Jakarta Mail, suitable for use in Google App Engine.|

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

-   [JavaMail 2.0](docs/JavaMail-2.0-changes.txt)
-   [JavaMail 1.6](docs/JavaMail-1.6-changes.txt)
-   [JavaMail 1.5](docs/JavaMail-1.5-changes.txt)
-   [JavaMail 1.4](docs/JavaMail-1.4-changes.txt)
-   [JavaMail 1.3](docs/JavaMail-1.3-changes.txt)
-   [JavaMail 1.2](docs/JavaMail-1.2-changes.txt)
-   [JavaMail 1.1](docs/JavaMail-1.1-changes.txt)

<br/>

# <a name="Samples"></a>Samples

Some sample programs showing how to use the Jakarta Mail APIs are available
[here](https://github.com/jakartaee/mail-api/releases/download/2.0.1/jakartamail-samples.zip).

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

Jakarta Mail bugs are tracked in the
[GitHub Jakarta Mail project issue tracker](https://github.com/jakartaee/mail-api/issues).

<br/>

# <a name="Development_Releases"></a>Development Releases

From time to time snapshot releases of the next version of Jakarta Mail
under development are published to the
[Jakarta Sonatype OSS repository](https://jakarta.oss.sonatype.org).
These snapshot releases have received only minimal testing, but may
provide previews of bug fixes or new features under development.

For example, you can download the jakarta.mail.jar file from the Jakarta Mail
2.0.2-SNAPSHOT release
[here](https://jakarta.oss.sonatype.org/content/repositories/snapshots/com/sun/mail/jakarta.mail/2.0.2-SNAPSHOT/).
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

# <a name="Jakarta_Mail_for_Android"></a>Jakarta Mail for Android

The latest release includes support for Jakarta Mail on Android.
See the [Android](Android) page for details.

<br/>

# <a name="Project_Documentation"></a>Project Documentation

You'll find more information about the protocol providers supported by
Jakarta Mail on the following pages:

-   [ smtp ](SMTP-Transport)
-   [ imap ](IMAP-Store)
-   [ pop3 ](POP3-Store)
-   [ mbox ](Mbox-Store)
-   [ pop3remote ](POP3-Remote-Store)

If you're interested in writing your own protocol provider (most people
won't need to), you can find more documentation on protocol providers
[here](https://javaee.github.io/javamail/docs/Providers.pdf).

The use of
[OAuth2 authentication](https://developers.google.com/gmail/xoauth2_protocol)
with Jakarta Mail is described [here](OAuth2).

The following pages provide hints and tips for using particular mail servers:

-   [Gmail](Gmail)
-   [ Yahoo! Mail ](Yahoo)
-   [ Exchange and Office 365 ](Exchange)
-   [ Outlook.com ](Outlook)

The following pages provide hints and tips for using Jakarta Mail on
particular operating systems or environments:

-   [Windows](Windows)
-   [Google App Engine](Google-App-Engine)

See [Build Instructions](Build-Instructions) for instructions on how to
download and build the most recent Jakarta Mail source code. You can also
find a bundle of the source code for the most recent Jakarta Mail release
in the [Releases](https://github.com/jakartaee/mail-api/releases) area of
this project.

If you're interested in contributing to Jakarta Mail, see the
[Contributions](Contributions) page.

You can find a list of products related to Jakarta Mail on the
[Third Party Products](ThirdPartyProducts) page.

Please see our page of
[links to additional information about Jakarta Mail and Internet email](Links)
and our list of
[books about Jakarta Mail and Internet email](Books).

To understand the Jakarta Mail license, see the [License](JakartaMail-License) page.

<br/>

By contributing to this project, you agree to these additional terms of
use, described in [CONTRIBUTING](CONTRIBUTING.md).
