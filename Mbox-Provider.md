Jakarta Mail Mbox Provider
==========================

The mbox provider is a Jakarta Mail local store provider that manages files
in [ Unix mbox format ](http://en.wikipedia.org/wiki/Mbox)
(in particular, the *mboxcl2* variant).

The mbox provider has only been tested on Solaris, OpenSolaris
and distributions based on Debian.
I tried it once on Windows, long ago, but there's no file
locking support for use on Windows. On Solaris and OpenSolaris it
depends on native file locking code.

To build the mbox provider for Solaris/OpenSolaris:

    % (cd mbox; mvn)
    % (cd mbox/native; mvn)

To build the mbox provider for distributions based on Debian:

    % (cd mbox; mvn)
    % (cd mbox/native; mvn -Plinux)

This depends on having the **c89** command in your PATH.

For based on Debian you need to have installed liblockfile-dev package.

To use the mbox provider you'll need to add mbox/target/mbox.jar to
your CLASSPATH and add mbox/native/target/libmbox.so to your
LD\_LIBRARY\_PATH.
