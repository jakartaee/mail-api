# Jakarta Mail

[![Build Status](https://github.com/eclipse-ee4j/mail/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/eclipse-ee4j/mail/actions/workflows/maven.yml?branch=master)
[![Jakarta Staging (Snapshots)](https://img.shields.io/nexus/s/https/jakarta.oss.sonatype.org/jakarta.mail/jakarta.mail-api.svg)](https://jakarta.oss.sonatype.org/content/repositories/staging/jakarta/mail/jakarta.mail-api/)

Jakarta Mail defines a platform-independent and protocol-independent
framework to build mail and messaging applications.

**IMPORTANT:** Implementation of the Jakarta Mail API, aka JakartaMail (formerly JavaMail),
is no longer part of this repository.
As part of breaking tight integration between Jakarta Mail API and the implementation,
its sources were moved to the new project - [Eclipse Angus](https://github.com/eclipse-ee4j/angus-mail) -
and further development continues there. [Eclipse Angus](https://github.com/eclipse-ee4j/angus-mail) 
is direct accessor of JavaMail/JakartaMail.

See the [Jakarta Mail web site](https://eclipse-ee4j.github.io/mail).

## License

* The Jakarta Mail API project source code is licensed
  under the [Eclipse Public License (EPL) v2.0](https://www.eclipse.org/legal/epl-2.0/)
  and [GNU General Public License (GPL) v2 with Classpath Exception](https://www.gnu.org/software/classpath/license.html);
  see the license information at the top of each source file.
* The binary jar files published to the Maven repository are licensed
  under the same licenses as the corresponding source code;
  see the file `META-INF/LICENSE.*` in each jar file.

You'll find the text of the licenses in the workspace in various `LICENSE.txt` or `LICENSE.md` files.
Don't let the presence of these license files in the workspace confuse you into thinking
that they apply to all files in the workspace.

You should always read the license file included with every download, and read
the license text included in every source file.

## Contributing

We use [contribution policy](CONTRIBUTING.md), which means we can only accept contributions under
the terms of [Eclipse Contributor Agreement](http://www.eclipse.org/legal/ECA.php).

## Links
* [Jakarta Mail web site](https://eclipse-ee4j.github.io/mail)
* [Jakarta Mail Specification project](https://github.com/eclipse-ee4j/mail-spec)
* [Jakarta Mail TCK project](https://github.com/eclipse-ee4j/mail-tck)
* [Jakarta Mail API nightly build job](https://ci.eclipse.org/mail/job/mail-api-build/)
* [Eclipse Angus project](https://github.com/eclipse-ee4j/angus-mail)
