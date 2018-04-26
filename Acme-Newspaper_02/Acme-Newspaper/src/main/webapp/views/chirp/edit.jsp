<%--
 * article/edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="chirp/user/edit.do" modelAttribute="chirp">

	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="user"/>
	<form:hidden path="moment"/>
	<form:hidden path="containsTaboo"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="chirp.title" path="title"/><br>
	<acme:textarea code="chirp.description" path="description"/><br>
	
	
	<acme:submit name="save" code="chirp.save"/>
	<acme:cancel url="chirp/user/stream.do" code="chirp.cancel"/>

</form:form>