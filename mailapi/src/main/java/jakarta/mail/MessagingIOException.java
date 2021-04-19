/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.mail;

import java.io.IOException;

/**
 * The base class for all exceptions thrown by the Messaging IO classes
 */
public class MessagingIOException extends IOException {

	private static final long serialVersionUID = -4070923996670992522L;
	transient private Folder folder;

    /**
     * Constructs a MessagingIOException with no detail message.
     */
    public MessagingIOException() {
    	super();
    }

    /**
     * Constructs a MessagingIOException with the specified detail message.
     * @param s		the detail message
     */
    public MessagingIOException(String s) {
    	super(s);
    }

	/**
	 * Constructor
	 * 
	 * @param folder the Folder
	 */
	public MessagingIOException(Folder folder) {
		this(folder, null);
	}

	/**
	 * Constructor
	 * 
	 * @param folder  the Folder
	 * @param message the detailed error message
	 */
	public MessagingIOException(Folder folder, String message) {
		super(message);
		this.folder = folder;
	}

	/**
	 * Returns the dead Folder object
	 *
	 * @return the dead Folder
	 */
	public Folder getFolder() {
		return folder;
	}
}
