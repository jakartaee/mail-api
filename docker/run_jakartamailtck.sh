#!/bin/bash -xe
#
# Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

WGET_PROPS="-q --no-cache"
if [ -z "$JAF_BUNDLE_URL" ];then
  export JAF_BUNDLE_URL=https://repo1.maven.org/maven2/com/sun/activation/jakarta.activation/2.0.0-RC3/jakarta.activation-2.0.0-RC3.jar
fi
if [ -z "$MAIL_TCK_BUNDLE_URL" ];then
  export MAIL_TCK_BUNDLE_URL=https://ci.eclipse.org/mail/job/mail-tck/job/2.0.0/lastSuccessfulBuild/artifact/bundles/mail-tck-2.0.0-rc1.zip
fi
wget $WGET_PROPS $JAF_BUNDLE_URL -O jakarta.activation.jar
wget $WGET_PROPS $MAIL_TCK_BUNDLE_URL -O mailtck.zip
cp ${WORKSPACE}/mail/target/jakarta.mail.jar ${WORKSPACE}

unzip -q -o ${WORKSPACE}/mailtck.zip -d ${WORKSPACE}

export TS_HOME=${WORKSPACE}/mail-tck

sed -i "s#^TS_HOME=.*#TS_HOME=$TS_HOME#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVA_HOME=.*#JAVA_HOME=$JAVA_HOME#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JARPATH=.*#JARPATH=$WORKSPACE#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_SERVER=.*#JAVAMAIL_SERVER=localhost -pn 1143#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_PROTOCOL=.*#JAVAMAIL_PROTOCOL=imap#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_TRANSPORT_PROTOCOL=.*#JAVAMAIL_TRANSPORT_PROTOCOL=smtp#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_TRANSPORT_SERVER=.*#JAVAMAIL_TRANSPORT_SERVER=localhost -tpn 1025#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_USERNAME=.*#JAVAMAIL_USERNAME=$MAIL_USER#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^JAVAMAIL_PASSWORD=.*#JAVAMAIL_PASSWORD=1234#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^SMTP_DOMAIN=.*#SMTP_DOMAIN=james.local#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^SMTP_FROM=.*#SMTP_FROM=user01@james.local#g" "$TS_HOME/lib/ts.jte"
sed -i "s#^SMTP_TO=.*#SMTP_TO=user01@james.local#g" "$TS_HOME/lib/ts.jte"

mkdir -p ${HOME}/.m2

cd $TS_HOME/tests/mailboxes
export CLASSPATH=$TS_HOME/tests/mailboxes:$WORKSPACE/jakarta.mail.jar:$WORKSPACE/jakarta.activation.jar:$CLASSPATH
javac -cp $CLASSPATH fpopulate.java
java -cp $CLASSPATH fpopulate -s test1 -d imap://user01%40james.local:1234@localhost:1143

which ant
ant -version

cd $WORKSPACE/mail-tck/
ant -Dreport.dir=$WORKSPACE/JTreport/mail-tck -Dwork.dir=$WORKSPACE/JTwork/mail-tck run

HOST=`hostname -f`
echo "1 mail-tck $HOST" > $WORKSPACE/args.txt

mkdir -p $WORKSPACE/results/junitreports/

$JAVA_HOME/bin/java -Djunit.embed.sysout=true \
    -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar \
    $WORKSPACE/args.txt $WORKSPACE/JTreport $WORKSPACE/results/junitreports/ 

tar zcf ${WORKSPACE}/mail-tck-results.tar.gz \
    $WORKSPACE/JTreport/mail-tck \
    $WORKSPACE/JTwork/mail-tck \
    $WORKSPACE/results/junitreports/
