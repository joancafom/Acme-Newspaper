<%--
 * index.jsp
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

<form:form action="newspaper/user/edit.do" modelAttribute="newspaper">
	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br>
	<acme:textbox code="newspaper.title" path="title"/><br>
	<acme:textarea code="newspaper.description" path="description"/><br>
	<acme:textbox code="newspaper.picture" path="picture"/>
	<br>
	
	<form:label path="isPublic"><spring:message code="newspaper.isPublic"/></form:label>
	<form:radiobutton path="isPublic" value="false"/><spring:message code="newspaper.private"/>
	<form:radiobutton path="isPublic" value="true"/><spring:message code="newspaper.public"/>
	<form:errors cssClass="error" path="isPublic"/>
	<br/>
	
	<acme:submit name="save" code="newspaper.save"/>
	<acme:cancel url="newspaper/user/listMine.do" code="newspaper.cancel"/>
</form:form>
