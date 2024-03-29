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

/**
 * Message search terms for the Jakarta Mail API.
 * This package defines classes that can be used to construct a search
 * expression to search a folder for messages matching the expression;
 * see the {@link jakarta.mail.Folder#search search} method on
 * {@link jakarta.mail.Folder jakarta.mail.Folder}.
 * See {@link jakarta.mail.search.SearchTerm SearchTerm}.
 *
 * <P>
 * Note that the exact search capabilities depend on the protocol,
 * provider, and server in use.  For the POP3 protocol, all searching is
 * done on the client side using the Jakarta Mail classes.  For IMAP, all
 * searching is done on the server side and is limited by the search
 * capabilities of the IMAP protocol and the IMAP server being used.
 * For example, IMAP date based searches have only day granularity.
 * </P>
 * <P>
 * In general, all of the string patterns supported by search terms are
 * just simple strings; no regular expressions are supported.
 */
package jakarta.mail.search;