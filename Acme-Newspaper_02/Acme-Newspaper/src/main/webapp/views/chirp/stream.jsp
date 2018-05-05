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
<security:authorize access="hasRole('USER')">
	<a href="chirp/user/create.do"><spring:message code="chirp.create"/></a>
</security:authorize>
<security:authorize access="hasRole('ADMINISTRATOR')">
	<h3><spring:message code="chirp.taboo"/></h3>
</security:authorize>
<hr>

<display:table name="chirps" id="chirp" requestURI="chirp/${actorWS}${landing}.do" pagesize="5" class="displaytag" style="width:100%" partialList="true"  size="${resultSize}">
		<display:column titleKey="chirp.moment">
			<acme:dateFormat code="date.format2" value="${chirp.moment}"/>
		</display:column>
		<display:column titleKey="chirp.user" style="width:30%">
			 <a href="user/${actorWS}display.do?userId=${chirp.user.id}">${chirp.user.name} ${chirp.user.surnames}</a>
		</display:column>
		<display:column titleKey="chirp.title" property="title"/>
		<display:column titleKey="chirp.description" property="description"/>
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<display:column>
				 <a href="chirp/administrator/delete.do?chirpId=${chirp.id}"><spring:message code="chirp.delete"/></a>
			</display:column>
		</security:authorize>
</display:table>
