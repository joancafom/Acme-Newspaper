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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<display:table name="users" id="user" requestURI="user/${actorWS}list.do" pagesize="5" class="displaytag">

	<display:column titleKey="user.name">
		<a href="user/${actorWS}display.do?userId=${user.id}"><jstl:out value="${user.name}"/></a>
	</display:column>
	<display:column property="surnames" titleKey="user.surnames" />
	<display:column property="email" titleKey="user.email" />
	
</display:table>
