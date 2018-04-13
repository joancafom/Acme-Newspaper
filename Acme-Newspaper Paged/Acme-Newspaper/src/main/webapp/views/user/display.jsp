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
<%@taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<br>

<h1><strong><jstl:out value="${user.name}"/></strong></h1>
<h2><jstl:out value="${user.surnames}"/></h2>

<br>

<div id="personalInfo">

	<jstl:choose>
		<jstl:when test="${user.postalAddress ne null}">
			<spring:message code="user.postalAddress" />: <jstl:out value="${user.postalAddress}"/>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="user.postalAddress" />: -
		</jstl:otherwise>
	</jstl:choose>
	
	<br>
	
	<jstl:choose>
		<jstl:when test="${user.phoneNumber ne null}">
			<spring:message code="user.phoneNumber" />: <jstl:out value="${user.phoneNumber}"/>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="user.phoneNumber" />: -
		</jstl:otherwise>
	</jstl:choose>
	
	<br>
	
	<spring:message code="user.email" />: <jstl:out value="${user.email}"/>
	
</div>


<security:authorize access="hasRole('USER')">
	<jstl:if test="${!mine}">
		<jstl:if test="${following}">
			<h2><a href="user/user/unfollow.do?userId=<jstl:out value="${user.id}" />"><spring:message code="user.unfollow" /></a></h2>
		</jstl:if>
	
		<jstl:if test="${!following}">
			<h2><a href="user/user/follow.do?userId=<jstl:out value="${user.id}" />"><spring:message code="user.follow" /></a></h2>
		</jstl:if>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('USER')">
<h3><spring:message code="user.publishedArticles"/></h3>

<display:table name="publishedArticles" id="article" requestURI="user/${actorWS}display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
	
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.newspaper" style="width:30%">
		<a href="newspaper/${actorWS}display.do?newspaperId=${article.newspaper.id}"><jstl:out value="${article.newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
</display:table>
</security:authorize>

<security:authorize access="hasRole('ADMINISTRATOR')">
<h3><spring:message code="user.publishedArticles"/></h3>

<display:table name="publishedArticles" id="article" requestURI="user/${actorWS}display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
	
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.newspaper" style="width:30%">
		<a href="newspaper/${actorWS}display.do?newspaperId=${article.newspaper.id}"><jstl:out value="${article.newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
</display:table>
</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">
<h3><spring:message code="user.publicPublishedArticles"/></h3>

<display:table name="publicArticles" id="article" requestURI="user/${actorWS}display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.newspaper" style="width:30%">
		<a href="newspaper/${actorWS}display.do?newspaperId=${article.newspaper.id}"><jstl:out value="${article.newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
</display:table>

<h3><spring:message code="user.suscribedArticles"/></h3>

<display:table name="privateArticles" id="article" requestURI="user/${actorWS}display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.newspaper" style="width:30%">
		<a href="newspaper/${actorWS}display.do?newspaperId=${article.newspaper.id}"><jstl:out value="${article.newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
</display:table>
</security:authorize>

<security:authorize access="isAnonymous()">
<h3><spring:message code="user.publicPublishedArticles"/></h3>

<display:table name="publicArticles" id="article" requestURI="user/${actorWS}display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
	<display:column titleKey="article.title" style="width:30%">
		<a href="article/${actorWS}display.do?articleId=${article.id}"><jstl:out value="${article.title}"/></a>
	</display:column>
	<display:column titleKey="article.newspaper" style="width:30%">
		<a href="newspaper/${actorWS}display.do?newspaperId=${article.newspaper.id}"><jstl:out value="${article.newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="article.summary" style="width:60%">
		<jstl:if test="${fn:length(article.summary)<=100}">
			<jstl:out value="${article.summary}"/>
		</jstl:if>
		<jstl:if test="${fn:length(article.summary)>100}">
			<jstl:out value="${fn:substring(article.summary, 0, 100)}"/>...
		</jstl:if>
	</display:column>
</display:table>
</security:authorize>

<jstl:if test="${mine}">
	<h3><spring:message code="user.chirps"/></h3>
	<display:table name="chirps" id="chirp" requestURI="user/user/display.do?userId=${user.id}" pagesize="5" class="displaytag" style="width:100%">
		
		<display:column titleKey="chirp.moment">
			<acme:dateFormat code="date.format2" value="${chirp.moment}"/>
		</display:column>
		<display:column titleKey="chirp.title" property="title" style="width:30%" />
		<display:column titleKey="chirp.moment" property="moment" style="width:30%" />
	</display:table>
</jstl:if>
