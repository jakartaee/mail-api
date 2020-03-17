<%--

    Copyright (c) 2001, 2020 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" import="jakarta.mail.*, demo.MailUserBean" %>
<%@ page errorPage="errorpage.jsp" %>
<jsp:useBean id="mailuser" scope="session" class="demo.MailUserBean" />
	
<html>
<head>
    <title>Jakarta Mail folders</title>
</head>
	
<body bgcolor="#ccccff">

<center>
<font face="Arial,Helvetica" font size=+3>
<b>Welcome to Jakarta Mail!</b> </font> </center>
<table width="50%" border=0 align=center>
<tr>
<td width="75%" bgcolor="#ffffcc">
<font face="Arial, Helvetica" font size=-1>
<b>FolderName</b></font></td><br>
<td width="25%" bgcolor="#ffffcc">
<font face="Arial, Helvetica" font size=-1>
<b>Messages</b></font></td><br>
</tr>
<tr>
<td width="75%" bgcolor="#ffffff">
<a href="messageheaders">Inbox</a></td><br>
<td width="25%" bgcolor="#ffffff">
<%= mailuser.getMessageCount() %>
</tr>
</table>
</body>
</html>

