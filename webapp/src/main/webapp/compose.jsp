<%--

    Copyright (c) 2001, 2019 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" %>
<%@ page errorPage="errorpage.jsp" %>

<html>
<head>
	<title>Jakarta Mail compose</title>
</head>

<body bgcolor="#ccccff">
<form ACTION="send" METHOD=POST>
<input type="hidden" name="send" value="send">
<p align="center">
<b><font size="4" face="Verdana, Arial, Helvetica">
Jakarta Mail Compose Message</font></b>
<p>
<table border="0" width="100%">
<tr>
<td width="16%" height="22">	
<p align="right">
<b><font face="Verdana, Arial, Helvetica">To:</font></b></td>
<td width="84%" height="22">
<% if (request.getParameter("to") != null) { %>
<input type="text" name="to" value="<%= request.getParameter("to") %>" size="30">
<% } else { %>
<input type="text" name="to" size="30"> 
<% } %>
<font size="1" face="Verdana, Arial, Helvetica">
 (separate addresses with commas)</font></td></tr>
<tr>
<td width="16%"><p align="right">
<b><font face="Verdana, Arial, Helvetica">From:</font></b></td>
<td width="84%">
<input type="text" name="from" size="30"> 
<font size="1" face="Verdana, Arial, Helvetica">
 (separate addresses with commas)</font></td></tr>
<tr>
<td width="16%"><p align="right">
<b><font face="Verdana, Arial, Helvetica">Subject:</font></b></td>
<td width="84%">
<input type="text" name="subject" size="55"></td></tr>
<tr>
<td width="16%">&nbsp;</td>
<td width="84%"><textarea name="text" rows="15" cols="53"></textarea></td></tr>
<tr>
<td width="16%" height="32">&nbsp;</td>
<td width="84%" height="32">
<input type="submit" name="Send" value="Send">
<input type="reset" name="Reset" value="Reset"></td></tr>
</table>
</form>
</body>
</html>

