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


<jstl:if test="${addNewspaper==true}">
	<form:form action="volume/user/addNewspaper.do" modelAttribute="manageVolumeForm">
		<!-- Hidden inputs -->
		<form:hidden path="volume"/>
		
		<!-- Inputs -->
		<form:label path="newspaperId"><spring:message code="volume.newspaper.select"/>:</form:label>
		<form:select path="newspaperId">
			<form:option value="0" label="----"/>
			<form:options items="${newspapers}" itemLabel="title" itemValue="id"/>
		</form:select>
		<form:errors cssClass="error" path="newspaperId"/><br><br>

		<br>
		<acme:submit name="add" code="volume.submit"/>
		<acme:cancel url="volume/user/list.do" code="volume.cancel"/>

	</form:form>
</jstl:if>
<jstl:if test="${removeNewspaper==true}">

</jstl:if>