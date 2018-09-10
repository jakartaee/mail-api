<%--

    Copyright (c) 2001, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" import="demo.MessageInfo" %>
<%@ page errorPage="errorpage.jsp" %>
<%@ taglib uri="http://java.sun.com/products/javamail/demo/webapp" 
    prefix="javamail" %>

<html>
<head>
	<title>JavaMail messageheaders</title>
</head>

<body bgcolor="#ccccff"><hr>
    
<center><font face="Arial,Helvetica" font size="+3">
<b>Folder INBOX</b></font></center><p>
   
<font face="Arial,Helvetica" font size="+3">
<b><a href="logout">Logout</a>
<a href="compose" target="compose">Compose</a>
</b></font>
<hr>
    
<table cellpadding=1 cellspacing=1 width="100%" border=1>
<tr>
<td width="25%" bgcolor="ffffcc">
<font face="Arial,Helvetica" font size="+1">
<b>Sender</b></font></td>
<td width="15%" bgcolor="ffffcc">
<font face="Arial,Helvetica" font size="+1">
<b>Date</b></font></td>
<td bgcolor="ffffcc">
<font face="Arial,Helvetica" font size="+1">
<b>Subject</b></font></td>
</tr>
<javamail:listmessages
 id="msginfo"
 folder="folder">
<%-- from --%>
<tr valign=middle>
<td width="25%" bgcolor="ffffff">
<font face="Arial,Helvetica">
<% if (msginfo.hasFrom()) { %>
<%= msginfo.getFrom() %>
</font>
<% } else { %>
<font face="Arial,Helvetica,sans-serif">
Unknown
<% } %>
</font></td>
<%-- date --%>
<td nowrap width="15%" bgcolor="ffffff">
<font face="Arial,Helvetica">
<%= msginfo.getDate() %>
</font></td>
<%-- subject & link --%>
<td bgcolor="ffffff">
<font face="Arial,Helvetica">
<a href="messagecontent?message=<%= msginfo.getNum() %>">
<% if (msginfo.hasSubject()) { %>
<%=    msginfo.getSubject() %>
<% } else { %>
<i>No Subject</i>
<% } %>
</a>
</font></td>
</tr>
</javamail:listmessages>
</table>
</body>
</html>

