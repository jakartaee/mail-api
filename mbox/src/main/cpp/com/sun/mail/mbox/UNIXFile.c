/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

#include <jni.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>

extern int _fcntl();

#include "com_sun_mail_mbox_UNIXFile.h"

static	jfieldID IO_fd_fdID;
static	int fd_offset;

/*
 * Class:     com_sun_mail_mbox_UNIXFile
 * Method:    initIDs
 * Signature: (Ljava/lang/Class;Ljava/io/FileDescriptor;)V
 */
JNIEXPORT void JNICALL
Java_com_sun_mail_mbox_UNIXFile_initIDs(JNIEnv *env, jclass ufClass,
    jclass fdClass, jobject stdin_obj)
{
	IO_fd_fdID = (*env)->GetFieldID(env, fdClass, "fd", "I");
	/*
	 * Because pre-JDK 1.2 stored the "fd" as one more than
	 * its actual value, we remember the value it stored for
	 * stdin, which should be zero, and use it as the offset
	 * for other fd's we extract.
	 */
	fd_offset = (*env)->GetIntField(env, stdin_obj, IO_fd_fdID);
}

/*
 * Class:     com_sun_mail_mbox_UNIXFile
 * Method:    lock0
 * Signature: (Ljava/io/FileDescriptor;Ljava/lang/String;Z)Z
 */
JNIEXPORT jboolean JNICALL
Java_com_sun_mail_mbox_UNIXFile_lock0(JNIEnv *env, jclass clazz,
    jobject fdobj, jstring umode, jboolean block)
{
	int fd;
	const char *mode;
	static struct flock flock0;
	struct flock flock = flock0;

	fd = (*env)->GetIntField(env, fdobj, IO_fd_fdID);
	fd -= fd_offset;
	/* XXX - a lot of work to examine one character in a string */
	mode = (*env)->GetStringUTFChars(env, umode, 0);
	flock.l_type = mode[1] == 'w' ? F_WRLCK : F_RDLCK;
	(*env)->ReleaseStringUTFChars(env, umode, mode);
	flock.l_whence = SEEK_SET;
	flock.l_start = 0;
	flock.l_len = 0;
	return (_fcntl(fd, block ? F_SETLKW : F_SETLK, &flock) == 0 ?
		JNI_TRUE : JNI_FALSE);
}

/*
 * Class:     com_sun_mail_mbox_UNIXFile
 * Method:    lastAccessed0
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL
Java_com_sun_mail_mbox_UNIXFile_lastAccessed0(JNIEnv *env, jclass clazz,
	jstring uname)
{
	const char *name;
	jlong ret = -1;
	struct stat st;

	name = (*env)->GetStringUTFChars(env, uname, 0);
	if (stat(name, &st) == 0) {
		/*
		 * Should be...
		ret = (jlong)st.st_atim.tv_sec * 1000 +
			st.st_atim.tv_nsec / 1000000;
		 * but for compatibility with lastModified we use...
		 */
		ret = (jlong)st.st_atime * 1000;
	}
	(*env)->ReleaseStringUTFChars(env, uname, name);
	return ret;
}
