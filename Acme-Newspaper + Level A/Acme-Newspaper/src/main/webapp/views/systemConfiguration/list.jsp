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


<display:table name="tabooWords" id="tabooWord" requestURI="systemConfiguration/administrator/listTabooWords.do" pagesize="5" class="displaytag">
	<display:column titleKey="systemConfiguration.tabooWords.tabooWord">
		<jstl:out value="${tabooWord}"/>
	</display:column>
	<display:column>
		<a href="systemConfiguration/administrator/deleteTabooWord.do?tabooWord=${tabooWord}"><spring:message code="systemConfiguration.tabooWords.delete"/></a>
	</display:column>
</display:table>

<a href="systemConfiguration/administrator/addTabooWord.do"><spring:message code="systemConfiguration.tabooWords.create"/></a>