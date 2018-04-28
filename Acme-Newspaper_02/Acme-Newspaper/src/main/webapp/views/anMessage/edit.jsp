<%--
 * anMessage/edit.jsp
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

<security:authorize access="hasRole('ADMINISTRATOR')">
	<jstl:if test="${isBroadcast}">
		<br />
		<strong><spring:message code="message.broadcast.info"/></strong> 
		<br />
		<br />
	</jstl:if>
</security:authorize>

<form:form action="anMessage/${actorWS}edit.do" modelAttribute="ANMessage">

	<jstl:if test="${ANMessage.id == 0}">

		<!-- Hidden Inputs -->
	
		<form:hidden path="id"/>
		<form:hidden path="version"/>
	
		<!-- Inputs -->
	
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<jstl:if test="${!isBroadcast}">
				<form:label path="recipient"><spring:message code="anMessage.recipient"/>: </form:label>
				<form:select path="recipient">
					<form:option value="0" label="----"/>
					<jstl:forEach items="${recipients}" var="r">
						<form:option value="${r.id}" label="${r.userAccount.username} - ${r.name} ${r.surnames}"/>
					</jstl:forEach>
				</form:select>
				<form:errors cssClass="error" path="recipient"/>
			</jstl:if>
		</security:authorize>

		<security:authorize access="!hasRole('ADMINISTRATOR')">
			<form:label path="recipient"><spring:message code="anMessage.recipient"/>: </form:label>
			<form:select path="recipient">
				<form:option value="0" label="----"/>
				<jstl:forEach items="${recipients}" var="r">
					<form:option value="${r.id}" label="${r.userAccount.username} - ${r.name} ${r.surnames}"/>
				</jstl:forEach>
			</form:select>
			<form:errors cssClass="error" path="recipient"/>
		</security:authorize>
	
		<acme:textbox code="anMessage.subject" path="subject"/><br>
		<acme:textarea code="anMessage.body" path="body"/><br>
	
		<form:label path="priority"><spring:message code="anMessage.priority"/>: </form:label>
		<form:radiobutton path="priority" value="NEUTRAL"/><spring:message code="anMessage.priority.neutral"/>
		<form:radiobutton path="priority" value="HIGH"/><spring:message code="anMessage.priority.high"/>
		<form:radiobutton path="priority" value="LOW"/><spring:message code="anMessage.priority.low"/>
		<form:errors cssClass="error" path="priority"/>
		<br/>
	
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<jstl:if test="${isBroadcast}">
				<acme:submit name="saveBroadcast" code="anMessage.send"/>
			</jstl:if>
			<jstl:if test="${!isBroadcast}">
				<acme:submit name="save" code="anMessage.send"/>
			</jstl:if>
		</security:authorize>

		<security:authorize access="!hasRole('ADMINISTRATOR')">
			<acme:submit name="save" code="anMessage.send"/>
		</security:authorize>
		<acme:cancel url="folder/${actorWS}list.do" code="anMessage.cancel"/>
		
	</jstl:if>
	
	<jstl:if test="${ANMessage.id != 0}">
	
		<!-- Hidden Inputs -->
		
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		<!-- Inputs -->
		
		<form:label path="folder"><spring:message code="anMessage.folder"/>: </form:label>
		<form:select path="folder">
			<jstl:forEach items="${folders}" var="f">
				<jstl:choose>
					<jstl:when test="${f.parentFolder == null}">
						<form:option value="${f.id}" label="${f.name}"/>
					</jstl:when>
					<jstl:otherwise>
						<form:option value="${f.id}" label="${f.parentFolder.name} -> ${f.name}"/>
					</jstl:otherwise>
				</jstl:choose>
			</jstl:forEach>
		</form:select>
		
		<acme:submit name="save" code="anMessage.save"/>
		<acme:cancel url="folder/${actorWS}list.do" code="anMessage.cancel"/>
	
	</jstl:if>

</form:form>