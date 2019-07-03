#
# Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause
#

mkdir src/docroot/WEB-INF/classes
mkdir src/docroot/WEB-INF/lib
cd src/classes
echo "compiling classes directory"
javac -d ../docroot/WEB-INF/classes demo/*.java
cd ../taglib
echo "compiling lib directroy"
javac -classpath ../docroot/WEB-INF/classes:$CLASSPATH demo/*.java
echo "creating tag library archive"
jar cvf ../docroot/WEB-INF/lib/taglib.jar META-INF demo/*.class
rm demo/*.class
cd ../docroot
echo "creating web archive"
jar cvf ../../jakartamail.war index.html *.jsp WEB-INF
rm -r WEB-INF/classes
rm -r WEB-INF/lib
cd ../..
