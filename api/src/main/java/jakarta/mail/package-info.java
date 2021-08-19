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
 * The Jakarta Mail API
 * provides classes that model a mail system.
 * The <code>jakarta.mail</code> package defines classes that are common to
 * all mail systems.
 * The <code>jakarta.mail.internet</code> package defines classes that are specific
 * to mail systems based on internet standards such as MIME, SMTP, POP3, and IMAP.
 * The Jakarta Mail API includes the <code>jakarta.mail</code> package and subpackages.
 *
 * <P>
 * For an overview of the Jakarta Mail API, read the
 * <A HREF="https://javaee.github.io/jakartamail/docs/JakartaMail-1.6.pdf" TARGET="_top">
 * Jakarta Mail specification</A>.
 * </P>
 * <P>
 * The code to send a plain text message can be as simple as the following:
 * </P>
 * <PRE>
 *     Properties props = new Properties();
 *     props.put("mail.smtp.host", "my-mail-server");
 *     Session session = Session.getInstance(props, null);
 *
 *     try {
 * 	MimeMessage msg = new MimeMessage(session);
 * 	msg.setFrom("me@example.com");
 * 	msg.setRecipients(Message.RecipientType.TO,
 * 			  "you@example.com");
 * 	msg.setSubject("Jakarta Mail hello world example");
 * 	msg.setSentDate(new Date());
 * 	msg.setText("Hello, world!\n");
 * 	Transport.send(msg, "me@example.com", "my-password");
 *     } catch (MessagingException mex) {
 * 	System.out.println("send failed, exception: " + mex);
 *     }
 * </PRE>
 * <P>
 * The Jakarta Mail download bundle contains many more complete examples
 * in the "demo" directory.
 * </P>
 * <P>
 * Don't forget to see the
 * <A HREF="https://javaee.github.io/jakartamail/FAQ.html" TARGET="_top">
 * Jakarta Mail API FAQ</A>
 * for answers to the most common questions.
 * The <A HREF="https://javaee.github.io/jakartamail/" TARGET="_top">
 * Jakarta Mail web site</A>
 * contains many additional resources.
 * </P>
 * <A ID="properties"><STRONG>Properties</STRONG></A>
 * <P>
 * The Jakarta Mail API supports the following standard properties,
 * which may be set in the <code>Session</code> object, or in the
 * <code>Properties</code> object used to create the <code>Session</code> object.
 * The properties are always set as strings; the Type column describes
 * how the string is interpreted.  For example, use
 * </P>
 * <PRE>
 * 	props.put("mail.debug", "true");
 * </PRE>
 * <P>
 * to set the <code>mail.debug</code> property, which is of type boolean.
 * </P>
 * <TABLE BORDER="1">
 * <CAPTION>Jakarta Mail properties</CAPTION>
 * <TR>
 * <TH>Name</TH>
 * <TH>Type</TH>
 * <TH>Description</TH>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.debug">mail.debug</A></TD>
 * <TD>boolean</TD>
 * <TD>
 * The initial debug mode.
 * Default is false.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.from">mail.from</A></TD>
 * <TD>String</TD>
 * <TD>
 * The return email address of the current user, used by the
 * <code>InternetAddress</code> method <code>getLocalAddress</code>.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.mime.address.strict">mail.mime.address.strict</A></TD>
 * <TD>boolean</TD>
 * <TD>
 * The MimeMessage class uses the <code>InternetAddress</code> method
 * <code>parseHeader</code> to parse headers in messages.  This property
 * controls the strict flag passed to the <code>parseHeader</code>
 * method.  The default is true.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.host">mail.host</A></TD>
 * <TD>String</TD>
 * <TD>
 * The default host name of the mail server for both Stores and Transports.
 * Used if the <code>mail.<i>protocol</i>.host</code> property isn't set.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.store.protocol">mail.store.protocol</A></TD>
 * <TD>String</TD>
 * <TD>
 * Specifies the default message access protocol.  The
 * <code>Session</code> method <code>getStore()</code> returns a Store
 * object that implements this protocol.  By default the first Store
 * provider in the configuration files is returned.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.transport.protocol">mail.transport.protocol</A></TD>
 * <TD>String</TD>
 * <TD>
 * Specifies the default message transport protocol.  The
 * <code>Session</code> method <code>getTransport()</code> returns a Transport
 * object that implements this protocol.  By default the first Transport
 * provider in the configuration files is returned.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.user">mail.user</A></TD>
 * <TD>String</TD>
 * <TD>
 * The default user name to use when connecting to the mail server.
 * Used if the <code>mail.<i>protocol</i>.user</code> property isn't set.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.protocol.class">mail.<i>protocol</i>.class</A></TD>
 * <TD>String</TD>
 * <TD>
 * Specifies the fully qualified class name of the provider for the
 * specified protocol.  Used in cases where more than one provider
 * for a given protocol exists; this property can be used to specify
 * which provider to use by default.  The provider must still be listed
 * in a configuration file.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.protocol.host">mail.<i>protocol</i>.host</A></TD>
 * <TD>String</TD>
 * <TD>
 * The host name of the mail server for the specified protocol.
 * Overrides the <code>mail.host</code> property.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.protocol.port">mail.<i>protocol</i>.port</A></TD>
 * <TD>int</TD>
 * <TD>
 * The port number of the mail server for the specified protocol.
 * If not specified the protocol's default port number is used.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.protocol.user">mail.<i>protocol</i>.user</A></TD>
 * <TD>String</TD>
 * <TD>
 * The user name to use when connecting to mail servers
 * using the specified protocol.
 * Overrides the <code>mail.user</code> property.
 * </TD>
 * </TR>
 *
 * </TABLE>
 *
 * <P>
 * The following properties are supported by the EE4J implementation of
 * Jakarta Mail, but are not currently a required part of the specification.
 * The names, types, defaults, and semantics of these properties may
 * change in future releases.
 * </P>
 * <TABLE BORDER="1">
 * <CAPTION>Jakarta Mail implementation properties</CAPTION>
 * <TR>
 * <TH>Name</TH>
 * <TH>Type</TH>
 * <TH>Description</TH>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.debug.auth">mail.debug.auth</A></TD>
 * <TD>boolean</TD>
 * <TD>
 * Include protocol authentication commands (including usernames and passwords)
 * in the debug output.
 * Default is false.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.debug.auth.username">mail.debug.auth.username</A></TD>
 * <TD>boolean</TD>
 * <TD>
 * Include the user name in non-protocol debug output.
 * Default is true.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.debug.auth.password">mail.debug.auth.password</A></TD>
 * <TD>boolean</TD>
 * <TD>
 * Include the password in non-protocol debug output.
 * Default is false.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.transport.protocol.address-type">mail.transport.protocol.<i>address-type</i></A></TD>
 * <TD>String</TD>
 * <TD>
 * Specifies the default message transport protocol for the specified address type.
 * The <code>Session</code> method <code>getTransport(Address)</code> returns a
 * Transport object that implements this protocol when the address is of the
 * specified type (e.g., "rfc822" for standard internet addresses).
 * By default the first Transport configured for that address type is used.
 * This property can be used to override the behavior of the
 * {@link jakarta.mail.Transport#send send} method of the
 * {@link jakarta.mail.Transport Transport} class so that (for example) the "smtps"
 * protocol is used instead of the "smtp" protocol by setting the property
 * <code>mail.transport.protocol.rfc822</code> to <code>"smtps"</code>.
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.event.scope">mail.event.scope</A></TD>
 * <TD>String</TD>
 * <TD>
 * Controls the scope of events.  (See the jakarta.mail.event package.)
 * By default, a separate event queue and thread is used for events for each
 * Store, Transport, or Folder.
 * If this property is set to "session", all such events are put in a single
 * event queue processed by a single thread for the current session.
 * If this property is set to "application", all such events are put in a single
 * event queue processed by a single thread for the current application.
 * (Applications are distinguished by their context class loader.)
 * </TD>
 * </TR>
 *
 * <TR>
 * <TD><A ID="mail.event.executor">mail.event.executor</A></TD>
 * <TD>java.util.concurrent.Executor</TD>
 * <TD>
 * By default, a new Thread is created for each event queue.
 * This thread is used to call the listeners for these events.
 * If this property is set to an instance of an Executor, the
 * Executor.execute method is used to run the event dispatcher
 * for an event queue.  The event dispatcher runs until the
 * event queue is no longer in use.
 * </TD>
 * </TR>
 *
 * </TABLE>
 *
 * <P>
 * The Jakarta Mail API also supports several System properties;
 * see the {@link jakarta.mail.internet} package documentation
 * for details.
 * </P>
 * <P>
 * The Jakarta Mail reference
 * implementation includes protocol providers in subpackages of
 * <code>com.sun.mail</code>.  Note that the APIs to these protocol
 * providers are not part of the standard Jakarta Mail API.  Portable
 * programs will not use these APIs.
 * </P>
 * <P>
 * Nonportable programs may use the APIs of the protocol providers
 * by (for example) casting a returned <code>Folder</code> object to a
 * <code>com.sun.mail.imap.IMAPFolder</code> object.  Similarly for
 * <code>Store</code> and <code>Message</code> objects returned from the
 * standard Jakarta Mail APIs.
 * </P>
 * <P>
 * The protocol providers also support properties that are specific to
 * those providers.  The package documentation for the
 * {@code com.sun.mail.imap IMAP}, {@code com.sun.mail.pop3 POP3},
 * and {@code com.sun.mail.smtp SMTP} packages provide details.
 * </P>
 * <P>
 * In addition to printing debugging output as controlled by the
 * {@link jakarta.mail.Session Session} configuration, the current
 * implementation of classes in this package log the same information using
 * {@link java.util.logging.Logger} as described in the following table:
 * </P>
 * <TABLE BORDER="1">
 * <CAPTION>Jakarta Mail Loggers</CAPTION>
 * <TR>
 * <TH>Logger Name</TH>
 * <TH>Logging Level</TH>
 * <TH>Purpose</TH>
 * </TR>
 *
 * <TR>
 * <TD>jakarta.mail</TD>
 * <TD>CONFIG</TD>
 * <TD>Configuration of the Session</TD>
 * </TR>
 *
 * <TR>
 * <TD>jakarta.mail</TD>
 * <TD>FINE</TD>
 * <TD>General debugging output</TD>
 * </TR>
 * </TABLE>
 */
package jakarta.mail;