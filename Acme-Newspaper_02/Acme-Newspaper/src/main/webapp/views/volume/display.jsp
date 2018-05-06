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

<h1 style="text-align:center"><strong><jstl:out value="${volume.title}"/></strong></h1>
<h4 style="text-align:center"><strong><jstl:out value="${volume.year}"/></strong></h4>
<br>
<security:authorize access="hasRole('CUSTOMER')">
	<jstl:if test="${subscriber}">
		<h4 style="text-align:center;color:blue"><spring:message code="volume.subscribe.already"/>.</h4>
	</jstl:if>

	<jstl:if test="${!subscriber}">
		<h4  style="text-align:center"><a href="volumeSubscription/customer/create.do?volumeId=${volume.id}"><spring:message code="volume.subscribe.now"/></a></h4>
	</jstl:if>
</security:authorize>
<p style="text-align:center"><spring:message code="volume.by"/> <a href="user/${actorWS}display.do?userId=${publisher.id}">${publisher.name} ${publisher.surnames}</a></p><br/>
<jstl:if test="${mine}">
	<h3 style="color: green; text-align: center;"><spring:message code="yourVolume"/></h3>
</jstl:if>
<br>
<jstl:out value="${volume.description}"/>
<br>
<br>
<h3><strong><spring:message code="volume.newspapers"/></strong></h3>
<br>
<display:table name="newspapers" id="newspaper" requestURI="volume/${actorWS}list.do" pagesize="5" class="displaytag" style="width: 100%" partialList="true"  size="${resultSize}">
	<display:column titleKey="newspaper.title">
		<a href="newspaper/${actorWS}display.do?newspaperId=${newspaper.id}"><jstl:out value="${newspaper.title}"/></a>
	</display:column>
	<display:column titleKey="newspaper.description">
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
	<jstl:if test="${mine==true}">
		<display:column>
			<a href="volume/user/removeNewspaper.do?volumeId=${volume.id}&newspaperId=${newspaper.id}"><spring:message code="volume.removeNewspaper"/></a>
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${mine==true}">
	<a href="volume/user/addNewspaper.do?volumeId=${volume.id}"><spring:message code="volume.addNewspaper"/></a>
</jstl:if>