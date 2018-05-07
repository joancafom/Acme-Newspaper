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

<jstl:choose>
	<jstl:when test="${isFollowUp}">
		<h3><spring:message code="article.create.followUp"/></h3>
		<jstl:set var="entityId" value="${mainArticleId}"/>
	</jstl:when>
	<jstl:otherwise>
		<jstl:set var="entityId" value="${article.newspaper.id}"/>
	</jstl:otherwise>
</jstl:choose>

<form:form action="article/user/edit.do?entityId=${entityId}" modelAttribute="article">

	<!-- Writer is pruned -->
	<!-- Hidden Inputs -->
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="article.title" path="title"/><br>
	<acme:textarea code="article.summary" path="summary"/><br>
	<acme:textarea code="article.body" path="body"/><br>
	<acme:textarea code="article.pictures" path="pictures"/><br>
	
	<jstl:if test="${!isFollowUp}">
		<form:label path="isFinal"><spring:message code="article.isFinal"/>: </form:label>
		<form:radiobutton path="isFinal" value="false"/><spring:message code="article.draft"/>
		<form:radiobutton path="isFinal" value="true"/><spring:message code="article.final"/>
		<form:errors cssClass="error" path="isFinal"/>
		<br/>
	</jstl:if>
	
	<acme:submit name="save" code="article.save"/>
	<jstl:choose>
		<jstl:when test="${isFollowUp}">
			<acme:cancel url="article/user/display.do?articleId=${mainArticleId}" code="article.cancel"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:cancel url="newspaper/user/display.do?newspaperId=${article.newspaper.id}" code="article.cancel"/>
		</jstl:otherwise>
	</jstl:choose>

</form:form>