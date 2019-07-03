<%--

    Copyright (c) 2001, 2019 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" import="demo.MessageInfo, demo.AttachmentInfo" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ taglib uri="https://github.com/eclipse-ee4j/jakartamail/tree/master/webapp" 
    prefix="jakartamail" %>
 

<html>
<head>
    <title>Jakarta Mail messagecontent</title>
</head>

<jakartamail:message 
id="msginfo"
folder="folder"
num="<%= request.getParameter(\"message\") %>" 
/>

<body bgcolor="#ccccff">
<center><font face="Arial,Helvetica" font size="+3">
<b>Message<sp> 
<sp>in folder /INBOX</b></font></center><p>

<%-- first, display this message's headers --%>
<b>Date:</b>
<%= msginfo.getSentDate() %>
<br>
<% if (msginfo.hasFrom()) { %>
<b>From:</b>
<a href="compose?to=<%= msginfo.getReplyTo() %>"
    target="reply<%= msginfo.getNum() %>">
<%= msginfo.getFrom() %>
</a>
<br>
<% } %>

<% if (msginfo.hasTo()) { %>
<b>To:</b>
<%= msginfo.getTo() %>
<br>
<% } %>   

<% if (msginfo.hasCc()) { %>
<b>CC:</b>
<%= msginfo.getCc() %>
<br>
<% } %>

<b>Subject:</b>
<% if (msginfo.hasSubject()) { %>
<%= msginfo.getSubject() %>
<% } %>
<br>
<pre>
<%= msginfo.getBody() %>
</pre>
<% if (msginfo.hasAttachments()) { %>
<jakartamail:listattachments
 id="attachment"
 messageinfo="msginfo">
<p><hr>
<b>Attachment Type:</b>
<%= attachment.getAttachmentType() %>
<br>
<% if (attachment.hasMimeType("text/plain") && 
       attachment.isInline()){ %>
<pre>
<%= attachment.getContent() %>
</pre>
<% } else { %>
<b>Filename:</b>
<%= attachment.getFilename() %>
<br>
<b>Description:</b>
<%= attachment.getDescription() %>
<br>
<a href="attachment?message=
<%= msginfo.getNum() %>&part=<%= attachment.getNum() %>">
Display Attachment</a>
<% } %>
</jakartamail:listattachments>
<% } %>
</body>
</html>

