JavaMail Build Instructions
===========================

To download the most recent JavaMail source code you'll need
[git](https://git-scm.com/downloads).

Once you've installed git, the following command will check out a copy
of the source code:

    % git clone git@github.com:eclipse-ee4j/javamail.git

Or, to check out the version corresponding to a particular release, use
a tag. For example, to check out the 1.6.3 version:

    % git clone -b 1.6.3 git@github.com:eclipse-ee4j/javamail.git

To build JavaMail you'll need [Maven](http://maven.apache.org/).

To simply build everything, use:

    % cd javamail
    % mvn install

You'll find the jakarta.mail.jar file in mail/target/jakarta.mail.jar.

See [Workspace Structure](Workspace-Structure) for a description of the
workspace.
