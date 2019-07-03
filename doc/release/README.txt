			README
			======

	    Jakarta Mail(TM) API ${mail.version} release
	    ----------------------------------

Welcome to the Jakarta Mail API ${mail.version} release!  This release includes
versions of the Jakarta Mail API implementation, IMAP, SMTP, and POP3
service providers, some examples, and documentation for the Jakarta Mail
API.

Please see the FAQ at https://eclipse-ee4j.github.io/mail/FAQ

JDK Version notes
-----------------

The Jakarta Mail API supports JDK 1.7 or higher.  Note that we have
currently tested this implementation with JDK 1.7 and 1.8.

While Jakarta Mail will work with JAF 1.0.2, we recommend the use of JAF 1.1
or newer.  JAF 1.2.1 is currently the newest version.  Note that JAF 1.1
is included in JDK 1.6 and JAF 1.1.1 is included in JDK 1.6.0_10 and
later.  JAF 1.2.0 is included but hidden in JDK 9 and has been removed
from JDK 11.  JAF 1.2.1 is available separately (see below).


Protocols supported
-------------------

This release supports the following Internet standard mail protocols:

    IMAP - a message Store protocol, for reading messages from a server
    POP3 - a message Store protocol, for reading messages from a server
    SMTP - a message Transport protocol, for sending messages to a server

The following table lists the names of the supported protocols (as used
in the Jakarta Mail API) and their capabilities:

	Protocol	Store or	Uses	Supports
	Name		Transport?	SSL?	STARTTLS?
	-------------------------------------------------
	imap		Store		No	Yes
	imaps		Store		Yes	N/A
	gimap		Store		Yes	N/A
	pop3		Store		No	Yes
	pop3s		Store		Yes	N/A
	smtp		Transport	No	Yes
	smtps		Transport	Yes	N/A

See our web page at https://eclipse-ee4j.github.io/mail/
for the latest information on third party protocol providers.


Download
--------

See the Jakarta Mail project page to download this release.

	https://eclipse-ee4j.github.io/mail/


Requirements
------------

Note that the Jakarta Mail API requires the JavaBeans(TM) Activation
Framework package to be installed as well if you're using or JDK 11 or later.

Download the latest version of the JavaBeans Activation Framework from

	https://github.com/eclipse-ee4j/jaf/releases

and install it in a suitable location.


Installation
------------

  UNIX/Linux
  ----------

  1. Download the jakarta.mail.jar file from the Jakarta Mail project website.
     https://github.com/eclipse-ee4j/mail/releases

  2. Set your CLASSPATH to include the "jakarta.mail.jar" file obtained from
     the download, as well as the current directory.

     Assuming you have downloaded jakarta.mail.jar to the /u/me/download/
     directory, the following would work:

      export CLASSPATH=$CLASSPATH:/u/me/download/jakarta.mail.jar:.

    (Don't forget the trailing "." for the current directory.)
    Also, if you're using JDK 1.5, include the "activation.jar" file that you
    obtained from downloading the JavaBeans Activation Framework.  For example:

      export CLASSPATH=$CLASSPATH:/u/me/download/activation/activation.jar

  3. Download the jakartamail-samples.zip file from the project website.
     https://github.com/eclipse-ee4j/mail/releases

  4. Compile any sample program using your Java compiler. For example:

      javac msgshow.java

  5. Run the sample program.  The '-' option lists the required and optional
     command-line options to successfully run any sample.  For example:

      java msgshow -

    lists the available options.  And

      java msgshow -T imap -H <mailserver> -U <username> -P <passwd> -f INBOX 5

    uses the IMAP protocol to display message number 5 from your INBOX.

  (Additional instructions on how to run the simple mail reader sample
  and servlet sample are provided in client/README.txt and servlet/README.txt,
  respectively.)


  Windows
  -------

  1. Download the jakarta.mail.jar file from the Jakarta Mail project website.
     https://github.com/eclipse-ee4j/mail/releases

  2. Set your CLASSPATH to include the "jakarta.mail.jar" file obtained from
     the download, as well as the current directory.

     Assuming you have downloaded jakarta.mail.jar to the /u/me/download/
     directory, the following would work:

      set CLASSPATH=%CLASSPATH%;c:\download\jakarta.mail.jar;.

    (Don't forget the trailing "." for the current directory.)
    Also, if you're using JDK 1.5, include the "activation.jar" file that you
    obtained from downloading the JavaBeans Activation Framework.  For example:

      set CLASSPATH=%CLASSPATH%;c:\download\activation\activation.jar

  3. Download the jakartamail-samples.zip file from the project website.
     https://github.com/eclipse-ee4j/mail/releases

  4. Compile any sample program using your Java compiler. For example:

      javac msgshow.java

  5. Run the sample program.  The '-' option lists the required and optional
     command-line options to successfully run any sample.  For example:

      java msgshow -

    lists the available options.  And

      java msgshow -T imap -H <mailserver> -U <username> -P <passwd> -f INBOX 5

    uses the IMAP protocol to display message number 5 from your INBOX.

  (Additional instructions on how to run the simple mail reader sample
  and servlet sample are provided in client/README.txt and servlet/README.txt,
  respectively.)


Problems?
---------

The Jakarta Mail FAQ at https://eclipse-ee4j.github.io/mail/FAQ
includes information on protocols supported, installation problems,
debugging tips, etc.

See the NOTES.txt file for information on how to report bugs.

Enjoy!

The Jakarta Mail API Team
