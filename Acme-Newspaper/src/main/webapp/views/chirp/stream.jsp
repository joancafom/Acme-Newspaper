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

<a href="chirp/user/create.do"><spring:message code="chirp.create"/></a>
<hr>
<jstl:forEach var="chirp" items="${chirps}">
	<p>[<acme:dateFormat code="date.format2" value="${chirp.moment}"/>] <a href="user/user/display.do?userId=${chirp.user.id}">${chirp.user.name} ${chirp.user.surnames}</a> <spring:message code="chirp.says"/>: ${chirp.title}</p><br>
	<p>${chirp.description}</p>
	<hr>
</jstl:forEach>
