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
#include <maillock.h>
extern void touchlock();	/* XXX - should be in maillock.h */

#include "com_sun_mail_mbox_UNIXInbox.h"

/*
 * Class:     com_sun_mail_mbox_UNIXInbox
 * Method:    maillock
 * Signature: (Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL
Java_com_sun_mail_mbox_UNIXInbox_maillock(JNIEnv *env, jobject obj,
    jstring user, jint retry_count)
{
	jboolean ret;
	const char *name = (*env)->GetStringUTFChars(env, user, 0);
	ret = maillock((char *)name, retry_count) == L_SUCCESS ?
	    JNI_TRUE : JNI_FALSE;
	(*env)->ReleaseStringUTFChars(env, user, name);
	return (ret);
}

/*
 * Class:     com_sun_mail_mbox_UNIXInbox
 * Method:    mailunlock
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_sun_mail_mbox_UNIXInbox_mailunlock(JNIEnv *env, jobject obj)
{
	(void) mailunlock();
}

/*
 * Class:     com_sun_mail_mbox_UNIXInbox
 * Method:    touchlock0
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_sun_mail_mbox_UNIXInbox_touchlock0(JNIEnv *env, jobject obj)
{
	(void) touchlock();
}
