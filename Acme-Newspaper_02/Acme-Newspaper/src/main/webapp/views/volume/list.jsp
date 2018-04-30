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

<jstl:set var="i" value="0" />
<display:table name="volumes" id="volume" requestURI="volume/${actorWS}list.do" pagesize="5" class="displaytag" style="width: 100%" partialList="true" size="${resultSize}">

	<display:column titleKey="volume.title">
		<a href="volume/${actorWS}display.do?volumeId=${volume.id}"><jstl:out value="${volume.title}"/></a>
	</display:column>
	<display:column property="description" titleKey="volume.description" />
	<display:column property="year" titleKey="volume.year" />
	<security:authorize access="hasRole('USER')">
	<display:column>
		<a href="user/${actorWS}display.do?userId=${creators[i].id}"><jstl:out value="${creators[i].name}" /></a>
		<jstl:set var="i" value="${i+1}" />
	</display:column>
	</security:authorize>
	
	
</display:table>

<security:authorize access="hasRole('USER')">
	<a href="volume/user/create.do"><spring:message code="volume.create"/></a>
</security:authorize>
