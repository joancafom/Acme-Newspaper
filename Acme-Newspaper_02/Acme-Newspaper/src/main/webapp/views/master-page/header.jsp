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
      <img src="images/logo.png" alt="Acme-News., Inc." style="max-height: 50px;"/>
      <a class="navbar-brand" href="#">Acme-News</a>
    </div>
    
    <security:authorize access="hasRole('ADMINISTRATOR')">
    	<ul class="nav navbar-nav">
			<li><a href="user/administrator/list.do"><spring:message code="master.page.user.list"/></a></li>
			<li><a href="newspaper/administrator/list.do"><spring:message code="master.page.newspapers"/></a></li>
			<li ><a href="article/administrator/search.do"><spring:message code="master.page.searchArticles"/></a></li>
			<li ><a href="article/administrator/list.do"><spring:message code="master.page.tabooedArticles"/></a></li>
			<li><a href="newspaper/administrator/search.do"><spring:message code="master.page.searchNewspapers"/></a></li>
			<li><a href="newspaper/administrator/listTabooed.do"><spring:message code="master.page.tabooedNewspapers"/></a></li>
			<li><a href="chirp/administrator/list.do"><spring:message code="master.page.tabooedChirps"/></a></li>
			<li><a href="administrator/display-dashboard.do"><spring:message code="master.page.administrator.dashboard"/></a></li>
			<li><a href="systemConfiguration/administrator/listTabooWords.do"><spring:message code="master.page.systemConfiguration"/></a></li>
			
			<li><a><security:authentication property="principal.username" /></a></li>
		</ul>
	</security:authorize>
	
	<security:authorize access="hasRole('AGENT')">
    	<ul class="nav navbar-nav">
    		<li><a href="newspaper/agent/listAdvertised.do"><spring:message code="master.page.advertisedNewspapers"/></a></li>

			<li><a><security:authentication property="principal.username" /></a></li>
		</ul>
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
    	<ul class="nav navbar-nav">
    		<li><a href="user/customer/list.do"><spring:message code="master.page.user.list"/></a></li>
			<li><a href="newspaper/customer/list.do"><spring:message code="master.page.newspapers"/></a></li>
			<li ><a href="article/customer/search.do"><spring:message code="master.page.searchArticles"/></a></li>
			<li><a href="newspaper/customer/search.do"><spring:message code="master.page.searchNewspapers"/></a></li>

			<li><a><security:authentication property="principal.username" /></a></li>
		</ul>
	</security:authorize>
	
	<security:authorize access="hasRole('USER')">
    	<ul class="nav navbar-nav">
    		
			<li><a href="user/user/list.do"><spring:message code="master.page.user.list"/></a></li>
			<li><a href="user/user/followers.do"><spring:message code="master.page.user.followers"/></a></li>
			<li><a href="user/user/following.do"><spring:message code="master.page.user.following"/></a></li>
			<li><a href="newspaper/user/listPublished.do"><spring:message code="master.page.publishedNewspapers"/></a></li>
			<li><a href="newspaper/user/listMine.do"><spring:message code="master.page.myNewspapers"/></a></li>
			<li><a href="newspaper/user/listUnpublished.do"><spring:message code="master.page.unpublishedNewspapers"/></a></li>
			<li ><a href="article/user/search.do"><spring:message code="master.page.searchArticles"/></a></li>
    		<li><a href="newspaper/user/search.do"><spring:message code="master.page.searchNewspapers"/></a></li>
    		<li><a href="chirp/user/stream.do"><spring:message code="master.page.chirps"/></a></li>


			<li><a><security:authentication property="principal.username" /></a></li>
		</ul>
	</security:authorize>
	
    <security:authorize access="isAnonymous()">
    	<ul class="nav navbar-nav">
    	
    		<li><a href="user/list.do"><spring:message code="master.page.user.list"/></a></li>
			<li><a href="newspaper/listPublished.do"><spring:message code="master.page.publishedNewspapers"/></a></li>
			<li ><a href="article/search.do"><spring:message code="master.page.searchArticles"/></a></li>
			<li><a href="newspaper/search.do"><spring:message code="master.page.searchNewspapers"/></a></li>
			
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="user/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.user.signup" /></a></li>
			<li><a href="customer/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.customer.signup" /></a></li>
      		<li ><a href="security/login.do"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="master.page.login" /></a></li>
    	</ul>
	</security:authorize>
	
	<security:authorize access="isAuthenticated()">
		<ul class="nav navbar-nav navbar-right">
      		<li><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
  </div>
</nav>