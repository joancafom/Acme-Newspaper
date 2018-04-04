<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<spring:message code="cookies" var="cookiesMessage"/>

<script>

window.onload = function(){
	
	// If the banner cookie isn't on
	if(cookieCurrentValue(window.cookieName) != window.cookieValue){
    	createDiv('${cookiesMessage}'); 
    	$("#readMore").hide();
    }
};

</script>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
    
      <!-- TODO: Place here the name and logo of the company -->
      <img src="images/logo.png" alt="Sample Co., Inc." style="max-height: 50px;"/>
      <a class="navbar-brand" href="#">Sample Co.</a>
    </div>
    
    <!-- TODO: Place here the role of the Actor -->
    <security:authorize access="hasRole('roleX')">
    	<ul class="nav navbar-nav">
    		
    		<!-- TODO: Place here the roles, actions and messageCodes -->
			<li ><a href="roleX/actionX.do"><spring:message code="master.page.sample"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
	</security:authorize>
	
    <security:authorize access="isAnonymous()">
    	<ul class="nav navbar-nav">
    	
			<!-- TODO: Place here the entities, actions and messageCodes -->
			<li ><a href="entityX/actionX.do"><spring:message code="master.page.sample"/></a></li>
			
		</ul>
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="security/login.do"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="master.page.login" /></a></li>
    	</ul>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
  </div>
</nav>