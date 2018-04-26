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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:choose>
	<jstl:when test="${keyword ne null}">
		<h3><spring:message code="article.resultsWith"/> <span style="text-decoration:underline;"><jstl:out value="${keyword}"/></span></h3><br>
	</jstl:when>
	<jstl:otherwise>
	
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<h3><spring:message code="article.tabooed"/></h3>
		</security:authorize>
	
	</jstl:otherwise>
</jstl:choose>


<display:table name="articles" id="article" requestURI="article/${actorWS}list.do" pagesize="5" class="displaytag" style="width:100%"  partialList="true"  size="${resultSize}">
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.writer" style="width:10%">
		<a href="user/${actorWS}display.do?userId=${article.writer.id}">${article.writer.name} ${article.writer.surnames}</a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
			<a href="article/administrator/delete.do?articleId=${article.id}"><spring:message code="article.delete"/></a>
		</display:column>
	</security:authorize>
</display:table>