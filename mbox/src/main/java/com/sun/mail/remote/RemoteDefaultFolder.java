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

package com.sun.mail.remote;

import javax.mail.*;
import com.sun.mail.mbox.*;

/**
 * The default folder for the "remote" protocol.
 *
 * @author Bill Shannon
 */
public class RemoteDefaultFolder extends MboxFolder {

    protected RemoteDefaultFolder(RemoteStore store, String name) {
	super(store, name);
    }

    /**
     * Depending on the name of the requested folder, create an
     * appropriate <code>Folder</code> subclass.  If the name is
     * <code>null</code>, create a <code>RemoteDefaultFolder</code>.
     * If the name is "INBOX" (ignoring case), create a
     * <code>RemoteInbox</code>.  Otherwise, create an <code>MboxFolder</code>.
     *
     * @return	the new <code>Folder</code>
     */
    protected Folder createFolder(Store store, String name) {
	if (name == null)
	    return new RemoteDefaultFolder((RemoteStore)store, null);
	else if (name.equalsIgnoreCase("INBOX"))
	    return new RemoteInbox((RemoteStore)store, name);
	else
	    return new MboxFolder((MboxStore)store, name);
    }
}
