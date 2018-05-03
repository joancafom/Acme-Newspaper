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

<jstl:if test="${landing == 'listTabooed'}">
	<h3><spring:message code="newspaper.tabooed"/></h3>
</jstl:if>

<display:table name="newspapers" id="newspaper" requestURI="newspaper/${actorWS}${landing}.do" pagesize="5" class="displaytag" style="width: 100%" partialList="true"  size="${resultSize}">
	<display:column titleKey="newspaper.title" sortable="true">
		<a href="newspaper/${actorWS}display.do?newspaperId=${newspaper.id}"><jstl:out value="${newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="newspaper.description" sortable="true">
		<jstl:out value="${newspaper.description}"/>
	</display:column>
	<jstl:if test="${!unpublished}">
		<display:column titleKey="newspaper.publicationDate">
			<acme:dateFormat code="date.format" value="${newspaper.publicationDate}"/>
		</display:column>
	</jstl:if>
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
		<a href="newspaper/administrator/delete.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.delete"/></a>
	</display:column>
	</security:authorize>
	<security:authorize access="hasRole('USER')">
		<jstl:if test="${mine==true}">
			<display:column>
				<jstl:if test="${newspaper.isPublic}">
					<a href="newspaper/user/privatize.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.privatize"/></a>
				</jstl:if>	
				<jstl:if test="${!newspaper.isPublic and (newspaper.publicationDate eq null)}">
					<a href="newspaper/user/unprivatize.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.unprivatize"/></a>
				</jstl:if>	
			</display:column>
		</jstl:if>
	</security:authorize>
	<security:authorize access="hasRole('AGENT')">
		<display:column>
			<a href="advertisement/agent/advertise.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.advertise"/></a>
		</display:column>
	</security:authorize>
</display:table>

<jstl:if test="${mine==true}">
	<a href="newspaper/user/create.do"><spring:message code="newspaper.create"/></a>
</jstl:if>

<security:authorize access="hasRole('AGENT')">
	<br>
	<br>
	<a href="advertisement/agent/create.do"><spring:message code="newspaper.advertisement.create"/></a>
</security:authorize>
