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

<h1 style="text-align:center"><strong><jstl:out value="${article.title}"/></strong></h1>
<h3 style="text-align:center"><jstl:out value="${article.summary}"/></h3>
<p style="text-align:center"><spring:message code="article.by"/> <a href="user/${actorWS}display.do?userId=${article.writer.id}">${article.writer.name} ${article.writer.surnames}</a></p>
<br>
<jstl:if test="${article.pictures[0]!=null}">
	<img src="${article.pictures[0]}" style="display:block; margin-left: auto; margin-right:auto; width: 50%">
</jstl:if>
<div style="padding: 70px;">
	<jstl:if test="${article.mainArticle ne null}">
		<p><spring:message code="article.isFollowUp.message" /><a href="article/${actorWS}display.do?articleId=${article.mainArticle.id}"> <jstl:out value="${article.mainArticle.title}" /></a></p>
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

<jstl:if test="${owned and article.isFinal and article.newspaper.publicationDate ne null}">
	<h4><a href="article/user/create.do?entityId=${article.id}"><spring:message code="article.followUp.write"/></a></h4>
</jstl:if>