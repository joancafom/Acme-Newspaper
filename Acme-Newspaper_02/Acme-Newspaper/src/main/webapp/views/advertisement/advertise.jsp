<%--
 * advertisement/edit.jsp
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
<br>
<form:form action="advertisement/agent/advertise.do" modelAttribute="advertiseForm">
	<!-- Hidden inputs -->
	<form:hidden path="newspaper"/>
	
	<!-- Inputs -->
	<form:label path="advertisementId"><spring:message code="advertisement.select"/>:</form:label>
	<form:select path="advertisementId">
		<form:option value="0" label="----"/>
		<form:options items="${advertisements}" itemLabel="title" itemValue="id"/>
	</form:select>
	<form:errors cssClass="error" path="advertisementId"/><br><br>
	
	<a href="advertisement/agent/create.do"><spring:message code="advertisement.create"/></a>
	<br><br>
	<acme:submit name="advertise" code="advertisement.submit"/>
	<acme:cancel url="/" code="advertisement.cancel"/>

</form:form>
