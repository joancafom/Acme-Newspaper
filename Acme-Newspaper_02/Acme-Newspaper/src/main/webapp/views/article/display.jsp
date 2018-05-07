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


<jstl:if test="${ad ne null}">
<div style="text-align:center;">
	<a href="<jstl:out value="${ad.targetURL}" />" target="_blank"><img src="<jstl:out value="${ad.bannerURL}" />" title="${ad.title}" alt="<jstl:out value="${ad.title}" />" style="max-height:150px;"/></a>
</div>
</jstl:if>
<br>

<h1 style="text-align:center"><strong><jstl:out value="${article.title}"/></strong></h1>
<h3 style="text-align:center"><jstl:out value="${article.summary}"/></h3>
<p style="text-align:center"><spring:message code="article.by"/> <a href="user/${actorWS}display.do?userId=${article.writer.id}">${article.writer.name} ${article.writer.surnames}</a><jstl:if test="${article.publicationDate ne null}"> - <acme:dateFormat code="date.format" value="${article.publicationDate}"/></jstl:if></p>
<br>
<jstl:if test="${article.pictures[0]!=null}">
	<img src="${article.pictures[0]}" style="display:block; margin-left: auto; margin-right:auto; width: 50%">
</jstl:if>
<div style="padding: 70px;">
	<jstl:if test="${article.mainArticle ne null}">
		<strong><spring:message code="article.isFollowUp.message" /> <em style="text-decoration:underline;"><jstl:out value="${article.mainArticle.title}" /></em> <spring:message code="article.followUp.newspaper.message" /> <a href="newspaper/${actorWS}display.do?newspaperId=${article.mainArticle.newspaper.id}"> <jstl:out value="${article.mainArticle.newspaper.title}"></jstl:out> </a></strong>
	</jstl:if>
	<p><jstl:out value="${article.body}"/></p>

</div>
<br><br>
<jstl:forEach begin="1" end="${fn:length(article.pictures)}" items="${article.pictures}" var="picture">
	<img src="${picture}" style="display:block; margin-left: auto; margin-right:auto; width: 50%"><br>
</jstl:forEach>

<jstl:if test="${owned and !article.isFinal}">
	<h4><a href="article/user/edit.do?articleId=${article.id}"><spring:message code="article.edit"/></a></h4>
</jstl:if>

<jstl:if test="${owned and article.isFinal and article.newspaper.publicationDate ne null and article.mainArticle eq null}">
	<h4 style="text-align: center"><a href="article/user/create.do?entityId=${article.id}"><spring:message code="article.followUp.write"/></a></h4>
</jstl:if>

<jstl:if test="${article.mainArticle eq null and article.isFinal}">
	<h3><spring:message code="article.followUps"/></h3>

	<display:table name="followUps" id="followUp" requestURI="article/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%">
		<display:column titleKey="article.title" style="width:20%">
			<a href="article/${actorWS}display.do?articleId=${followUp.id}"><jstl:out value="${followUp.title}"/></a>
		</display:column>
	
		<display:column titleKey="article.summary" style="width:60%">
			<jstl:if test="${fn:length(followUp.summary)<=100}">
				<jstl:out value="${followUp.summary}"/>
			</jstl:if>
			<jstl:if test="${fn:length(followUp.summary)>100}">
				<jstl:out value="${fn:substring(followUp.summary, 0, 100)}"/>...
			</jstl:if>
		</display:column>
		
		<display:column titleKey="article.publicationDate" style="width:20%">
			<acme:dateFormat code="date.format" value="${followUp.publicationDate}"/>
		</display:column>
	
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<display:column>
				<a href="article/administrator/delete.do?articleId=${followUp.id}"><spring:message code="article.delete"/></a>
			</display:column>
		</security:authorize>
	</display:table>
</jstl:if>