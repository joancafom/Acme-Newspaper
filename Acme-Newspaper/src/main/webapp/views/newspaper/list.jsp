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

<display:table name="newspapers" id="newspaper" requestURI="newspaper/${actorWS}list.do" pagesize="5" class="displaytag" style="width: 100%">
	<display:column titleKey="newspaper.title">
		<jstl:out value="${newspaper.title}"/>
	</display:column>
	<display:column titleKey="newspaper.description">
		<jstl:out value="${newspaper.description}"/>
	</display:column>
	<jstl:if test="${!unpublished}">
		<display:column titleKey="newspaper.publicationDate">
			<acme:dateFormat code="date.format2" value="${newspaper.publicationDate}"/>
		</display:column>
	</jstl:if>
	<display:column>
		<a href="newspaper/${actorWS}display.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.display"/></a>
	</display:column>
</display:table>
