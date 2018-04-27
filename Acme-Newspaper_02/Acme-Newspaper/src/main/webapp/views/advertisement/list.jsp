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

<display:table name="advertisements" id="advertisement" requestURI="advertisement/administrator/list.do" pagesize="5" class="displaytag" style="width: 100%">
	<display:column titleKey="advertisement.title">
		<jstl:out value="${advertisement.title}"/>
	</display:column>
	<display:column titleKey="advertisement.bannerURL">
		<a href="${advertisement.bannerURL}"><jstl:out value="${advertisement.bannerURL}"/></a>
	</display:column>
	<display:column titleKey="advertisement.targetURL">
		<a href="${advertisement.targetURL}"><jstl:out value="${advertisement.targetURL}"/></a>
	</display:column>
	<display:column titleKey="advertisement.agent">
		<jstl:out value="${advertisement.agent.name} ${advertisement.agent.surnames}"/>
	</display:column>
</display:table>
