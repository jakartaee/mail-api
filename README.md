<br>

The Jakarta Mail API provides a platform-independent and
protocol-independent framework to build mail and messaging
applications.
The Jakarta Mail API is available as an optional package for use with the
[Java SE platform](http://www.oracle.com/technetwork/java/javase/index.html)
and is also included in the
[Java EE platform](http://www.oracle.com/technetwork/java/javaee/index.html).

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

# <a name="Latest_News"></a>Latest News

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

By contributing to this project, you agree to these additional terms of
use, described in [CONTRIBUTING](CONTRIBUTING.md).

# <a name="Download_Jakarta_Mail_Release"></a>Download Jakarta Mail Release

The latest release of Jakarta Mail is 1.6.3.

The following table provides easy access to the latest release. Most
people will only need the main Jakarta Mail implementation in the
jakarta.mail.jar file.

|Item|Description|
|:---|:----------|
|[jakarta.mail.jar](https://github.com/eclipse-ee4j/mail/releases/download/1.6.3/jakarta.mail.jar)|The Jakarta Mail implementation, including the SMTP, IMAP, and POP3 protocol providers|
|[README.txt](docs/README.txt)|Overview of the release|
|[NOTES.txt](docs/NOTES.txt)|Additional notes about using Jakarta Mail|
|[SSLNOTES.txt](docs/SSLNOTES.txt)|Notes on using SSL/TLS with Jakarta Mail|
|[NTLMNOTES.txt](docs/NTLMNOTES.txt)|Notes on using NTLM authentication with Jakarta Mail|
|[CHANGES.txt](docs/CHANGES.txt)|Changes since the previous release|
|[COMPAT.txt](docs/COMPAT.txt)|Important notes about compatibility|


In addition, the Jakarta Mail jar files are published to the Maven repository.
The main Jakarta Mail jar file, which is all most applications will need,
can be included using this Maven dependency:
```
        <dependencies>
            <dependency>
                <groupId>com.sun.mail</groupId>
                <artifactId>jakarta.mail</artifactId>
                <version>1.6.3</version>
            </dependency>
        </dependencies>
```
You can find all of the Jakarta Mail jar files in
[Maven Central](http://search.maven.org).


|jar file|groupId|artifactId|Description|
|:-------|:------|:---------|:----------|
|[jakarta.mail.jar](https://repo1.maven.org/maven2/com/sun/mail/jakarta.mail/1.6.3/jakarta.mail-1.6.3.jar)|com.sun.mail|jakarta.mail|The Jakarta Mail implementation jar file, including the SMTP, IMAP, and POP3 protocol providers|
|[jakarta.mail-api.jar](https://repo1.maven.org/maven2/jakarta/mail/jakarta.mail-api/1.6.3/jakarta.mail-api-1.6.3.jar)|jakarta.mail|jakarta.mail-api|The Jakarta Mail API definitions only, suitable for compiling against; use only with a Maven "provided" dependency scope|
|[mailapi.jar](https://repo1.maven.org/maven2/com/sun/mail/mailapi/1.6.3/mailapi-1.6.3.jar)|com.sun.mail|mailapi|The Jakarta Mail implementation with no protocol providers; use with one of the following providers|
|[smtp.jar](https://repo1.maven.org/maven2/com/sun/mail/smtp/1.6.3/smtp-1.6.3.jar)|com.sun.mail|smtp|The SMTP protocol provider|
|[imap.jar](https://repo1.maven.org/maven2/com/sun/mail/imap/1.6.3/imap-1.6.3.jar)|com.sun.mail|imap|The IMAP protocol provider|
|[pop3.jar](https://repo1.maven.org/maven2/com/sun/mail/pop3/1.6.3/pop3-1.6.3.jar)|com.sun.mail|pop3|The POP3 protocol provider|
|[gimap.jar](https://repo1.maven.org/maven2/com/sun/mail/gimap/1.6.3/gimap-1.6.3.jar)|com.sun.mail|gimap|An EXPERIMENTAL Gmail IMAP protocol provider that supports Gmail-specific features|
|[dsn.jar](https://repo1.maven.org/maven2/com/sun/mail/dsn/1.6.3/dsn-1.6.3.jar)|com.sun.mail|dsn|Support for parsing and creating messages containing Delivery Status Notifications|
|[logging-mailhandler.jar](https://repo1.maven.org/maven2/com/sun/mail/logging-mailhandler/1.6.3/logging-mailhandler-1.6.3.jar)|com.sun.mail|logging-mailhandler|A java.util.logging handler that uses Jakarta Mail, suitable for use in Google App Engine.|

# <a name="API_Documentation"></a>API Documentation

The JavaMail 1.6 and earlier API is defined through the Java Community Process as
[JSR 919](http://jcp.org/en/jsr/detail?id=919).

The Jakarta Mail API documentation is available
[here](https://eclipse-ee4j.github.io/mail/docs/api/)
and the Jakarta Mail specification is available
[here](https://javaee.github.io/mail/docs/mail-spec-1.6.pdf).

The following documents summarize the API changes in each release of
the JavaMail API specification:

-   [JavaMail 1.6](docs/JavaMail-1.6-changes.txt)
-   [JavaMail 1.5](docs/JavaMail-1.5-changes.txt)
-   [JavaMail 1.4](docs/JavaMail-1.4-changes.txt)
-   [JavaMail 1.3](docs/JavaMail-1.3-changes.txt)
-   [JavaMail 1.2](docs/JavaMail-1.2-changes.txt)
-   [JavaMail 1.1](docs/JavaMail-1.1-changes.txt)

# <a name="Samples"></a>Samples

Some sample programs showing how to use the Jakarta Mail APIs are available
[here](https://github.com/eclipse-ee4j/mail/releases/download/1.6.3/javamail-samples.zip).

# <a name="Help"></a>Help

Please read the
[Jakarta Mail FAQ](FAQ.html)!
Read it again. Tell everyone you know to read it. Thank you!

You can post questions to the
[mail-dev mailing list](https://accounts.eclipse.org/mailing-list/mail-dev).

Or, post a question on [Stack Overflow](http://stackoverflow.com/) using the
[javamail](http://stackoverflow.com/questions/tagged/javamail) tag.

Finally, you can send mail directly to the Jakarta Mail team at Oracle at
<javamail_ww@oracle.com>.

# <a name="Bugs"></a>Bugs

Jakarta Mail bugs are tracked in the
[GitHub Jakarta Mail project issue tracker](https://github.com/eclipse-ee4j/mail/issues).

# <a name="Development_Releases"></a>Development Releases

From time to time snapshot releases of the next version of Jakarta Mail
under development are published to the
[Sonatype OSSRH repository](http://oss.sonatype.org).
These snapshot releases have received only minimal testing, but may
provide previews of bug fixes or new features under development.

For example, you can download the jakarta.mail.jar file from the Jakarta Mail
1.6.4-SNAPSHOT release
[here](https://oss.sonatype.org/content/repositories/snapshots/com/sun/mail/jakarta.mail/1.6.4-SNAPSHOT/).
Be sure to scroll to the bottom and choose the jar file with the most
recent time stamp.

# <a name="Jakarta_Mail_for_Android"></a>Jakarta Mail for Android

The latest release includes support for Jakarta Mail on Android.
See the [Android](Android) page for details.

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
in the [Releases](https://github.com/eclipse-ee4j/mail/releases) area of
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
