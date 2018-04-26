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

<style>

.div-ok{
	background-color:#cbffc9;
	padding:10px;
	color:green;
}

.div-not{
	background-color:#feffc9;
	padding:10px;
	color:#c1be00;
}
</style>

<h1 style="text-align:center"><strong><jstl:out value="${newspaper.title}"/></strong></h1>
<jstl:if test="${!newspaper.isPublic}">
	<h4 style="text-align:center;color:blue">
		<strong>
			<spring:message code="newspaper.privateMessage"/>.
			<security:authorize access="hasRole('CUSTOMER')">
				<jstl:if test="${subscriber}">
					<spring:message code="newspaper.subscribe.already"/>.
				</jstl:if>
				<jstl:if test="${!subscriber}">
					<a href="subscription/customer/create.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.subscribe.now"/></a>
				</jstl:if>
			</security:authorize>
		</strong>
	</h4>
</jstl:if>
<h3 style="text-align:center"><jstl:out value="${newspaper.description}"/></h3><br>
<img src="${newspaper.picture}" style="display:block; margin-left: auto; margin-right:auto; width: 20%">

<jstl:if test="${own and (newspaper.publicationDate eq null)}">

	<br>
		<jstl:choose>
			<jstl:when test="${canBePublished}">
				<div class="div-ok" id="publisherActions">
					<span><spring:message code="newspaper.publish.infoOk" /></span> <a href="newspaper/user/publish.do?newspaperId=<jstl:out value="${newspaper.id}" />" ><spring:message code="newspaper.publish" /></a>
				</div>
			</jstl:when>
			<jstl:otherwise>
				<div class="div-not" id="publisherActions">
					<span><spring:message code="newspaper.publish.infoNot" /></span>
				</div>
			</jstl:otherwise>
		</jstl:choose>
	
	<br> 
</jstl:if>

<display:table name="articles" id="article" requestURI="newspaper/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true"  size="${resultSize}">
	<display:column titleKey="newspaper.article.title" style="width:30%">
		<security:authorize access="hasRole('CUSTOMER')">
		<jstl:if test="${newspaper.isPublic or subscriber}">
			<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
		</jstl:if>
		<jstl:if test="${!newspaper.isPublic and !subscriber}">
			<jstl:out value="${article.title}"/>
		</jstl:if>
		</security:authorize>
		<security:authorize access="isAnonymous()">
		<jstl:if test="${newspaper.isPublic or subscriber}">
			<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
		</jstl:if>
		<jstl:if test="${!newspaper.isPublic and !subscriber}">
			<jstl:out value="${article.title}"/>
		</jstl:if>
		</security:authorize>
		
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
		</security:authorize>
		<security:authorize access="hasRole('USER')">
			<jstl:if test="${newspaper.isPublic or userId eq article.writer.id or own}">
				<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
			</jstl:if>
			<jstl:if test="${!newspaper.isPublic and userId ne article.writer.id and !own}">
				<jstl:out value="${article.title}"/>
			</jstl:if>
		</security:authorize>
	</display:column>
	<display:column titleKey="newspaper.article.writer" style="width:10%">
		<a href="user/${actorWS}display.do?userId=${article.writer.id}">${article.writer.name} ${article.writer.surnames}</a>
	</display:column>
	<display:column titleKey="newspaper.article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
	<security:authorize access="isAuthenticated()">
		<display:column titleKey="newspaper.status">
		<jstl:choose>
			<jstl:when test="${article.isFinal}">
				<spring:message code="newspaper.status.final" />
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="newspaper.status.draft" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	</security:authorize>
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column>
			<a href="article/administrator/delete.do?articleId=${article.id}"><spring:message code="article.delete"/></a>
		</display:column>
	</security:authorize>
</display:table>

<security:authorize access="hasRole('USER')">
	<jstl:if test="${newspaper.publicationDate eq null}">
		<h4><a href="article/user/create.do?entityId=${newspaper.id}"><spring:message code="article.create"/></a></h4>
	</jstl:if>
</security:authorize>

