<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions'%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="number.format" var="numberFormat"/>

<!-- C-Level Requirements -->

<p><strong><spring:message code="avg.newspapersPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgNewspapersPerUser}" /></p>
<p><strong><spring:message code="std.newspapersPerUser"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdNewspapersPerUser}"/></p>
<p><strong><spring:message code="avg.articlesPerWriter"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgArticlesPerWriter}"/></p>
<p><strong><spring:message code="std.articlesPerWriter"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdArticlesPerWriter}"/></p>
<p><strong><spring:message code="avg.articlesPerNewspaper"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgArticlesPerNewspaper}"/></p>
<p><strong><spring:message code="std.articlesPerNewspaper"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdArticlesPerNewspaper}"/></p>

<h4><spring:message code="newspapers10MoreArticlesThanAverage"/>:</h4>
<display:table name="newspapers10MoreArticlesThanAverage" id="newspaper" requestURI="administrator/display-dashboard.do" class="displaytag" pagesize="5">
	<display:column titleKey="newspaper.title">
		<jstl:out value="${newspaper.title}"/>
	</display:column>
	<display:column titleKey="newspaper.description">
		<jstl:out value="${newspaper.description}"/>
	</display:column>
	<display:column titleKey="newspaper.publicationDate">
		<acme:dateFormat code="date.format" value="${newspaper.publicationDate}"/>
	</display:column>
	<display:column>
		<a href="newspaper/${actorWS}display.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.display"/></a>
	</display:column>
</display:table>

<h4><spring:message code="newspapers10FewerArticlesThanAverage"/>:</h4>
<display:table name="newspapers10FewerArticlesThanAverage" id="newspaper" requestURI="administrator/display-dashboard.do" class="displaytag" pagesize="5">
	<display:column titleKey="newspaper.title">
		<jstl:out value="${newspaper.title}"/>
	</display:column>
	<display:column titleKey="newspaper.description">
		<jstl:out value="${newspaper.description}"/>
	</display:column>
	<display:column titleKey="newspaper.publicationDate">
		<acme:dateFormat code="date.format" value="${newspaper.publicationDate}"/>
	</display:column>
	<display:column>
		<a href="newspaper/${actorWS}display.do?newspaperId=${newspaper.id}"><spring:message code="newspaper.display"/></a>
	</display:column>
</display:table>

<p><strong><spring:message code="ratioUsersHaveCreatedANewspaper"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioUsersHaveCreatedANewspaper}"/></p>
<p><strong><spring:message code="ratioUsersHaveWrittenAnArticle"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioUsersHaveWrittenAnArticle}"/></p>