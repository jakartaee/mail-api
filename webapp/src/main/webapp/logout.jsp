<%--

    Copyright (c) 2001, 2019 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Distribution License v. 1.0, which is available at
    http://www.eclipse.org/org/documents/edl-v10.php.

    SPDX-License-Identifier: BSD-3-Clause

--%>

<%@ page language="java" import="demo.MailUserBean" %>
<%@ page errorPage="errorpage.jsp" %>
<jsp:useBean id="mailuser" scope="session" class="demo.MailUserBean" />

<html>
<head>
	<title>Jakarta Mail logout</title>
</head>

<% mailuser.logout(); %>

<body>
<h2>Logged out OK</h2><a href=index.html>click here to login</a>
</body>
</html>

