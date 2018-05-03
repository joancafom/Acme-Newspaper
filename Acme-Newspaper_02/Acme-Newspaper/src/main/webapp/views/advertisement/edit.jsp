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

<form:form action="advertisement/agent/edit.do" modelAttribute="advertisement">
	<!-- Hidden inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<acme:textbox code="advertisement.title" path="title"/>
	<acme:textbox code="advertisement.bannerURL" path="bannerURL"/>
	<acme:textbox code="advertisement.targetURL" path="targetURL"/>
	
	<hr>
	<h3><spring:message code="advertisement.creditCard"/></h3>
	<acme:textbox code="creditCard.holderName" path="creditCard.holderName"/>
	<acme:textbox code="creditCard.brandName" path="creditCard.brandName" />
	<acme:textbox code="creditCard.number" path="creditCard.number" />
	<acme:textbox code="creditCard.CVV" path="creditCard.CVV"/>
	<acme:textbox code="creditCard.month" path="creditCard.month"/>
	<acme:textbox code="creditCard.year" path="creditCard.year"/>
	<br>
	<acme:submit name="save" code="advertisement.submit"/>
	<acme:cancel url="advertisement/agent/list.do" code="advertisement.cancel"/>

</form:form>
