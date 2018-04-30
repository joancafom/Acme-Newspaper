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
<jstl:if test="${noAdvertsCreated==true}">
	<p style="color:red"><strong><spring:message code="advertisement.noAdvertsCreated"/>.</strong></p><br>
	
	<a href="advertisement/agent/create.do"><spring:message code="advertisement.create"/></a>
	<acme:cancel url="newspaper/agent/listNotAdvertised.do" code="advertisement.back"/>
</jstl:if>

<jstl:if test="${noMoreAdverts==true}">
	<p style="color:red"><strong><spring:message code="advertisement.noMoreAdverts"/>.</strong></p><br>
	
	<a href="advertisement/agent/create.do"><spring:message code="advertisement.create"/></a>
	<acme:cancel url="newspaper/agent/listNotAdvertised.do" code="advertisement.back"/>
</jstl:if>
<jstl:if test="${noMoreAdverts==false}">
	<form:form action="advertisement/agent/advertise.do" modelAttribute="advertiseForm">
	<!-- Hidden inputs -->
	<form:hidden path="newspaper"/>
	
	<!-- Inputs -->
	<form:label path="advertisement"><spring:message code="advertisement.select"/>:</form:label>
	<form:select path="advertisement">
		<form:option value="0" label="----"/>
		<form:options items="${advertisements}" itemLabel="title" itemValue="id"/>
	</form:select>
	<form:errors cssClass="error" path="advertisement"/><br><br>
	
	<a href="advertisement/agent/create.do"><spring:message code="advertisement.create"/></a>
	<br><br>
	<acme:submit name="advertise" code="advertisement.submit"/>
	<acme:cancel url="newspaper/agent/listNotAdvertised.do" code="advertisement.cancel"/>

</form:form>
</jstl:if>

