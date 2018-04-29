<%--
 * anMessage/display.jsp
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

<strong><spring:message code="anMessage.sender"/>:</strong> <jstl:out value="${ANMessage.sender.userAccount.username} - ${ANMessage.sender.name} ${ANMessage.sender.surnames}"/><br/>
<strong><spring:message code="anMessage.recipient"/>:</strong><br/>
<jstl:forEach items="${ANMessage.recipients}" var="r">
	<jstl:out value="· ${r.userAccount.username} - ${r.name} ${r.surnames}"/><br/>	
</jstl:forEach> 

<strong><spring:message code="anMessage.sentMoment"/>:</strong> <acme:dateFormat code="date.format2" value="${ANMessage.sentMoment}"/><br/> 

<strong><spring:message code="anMessage.subject"/>:</strong> <jstl:out value="${ANMessage.subject}"/><br/><br/>
<strong><spring:message code="anMessage.body"/>:</strong>
<hr>
<jstl:out value="${ANMessage.body}"/>
<hr>
