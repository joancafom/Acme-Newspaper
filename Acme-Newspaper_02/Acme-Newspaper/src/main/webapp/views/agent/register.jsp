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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<spring:message code="agent.userAccount.passwordMatchMessage" var="passwordMatchError"></spring:message>

<script type="text/javascript">
	function checkData(){
		
		var pass1 = document.getElementById('pass1').value;
		var pass2 = document.getElementById('pass2').value;
		
		var messageDiv = document.getElementById('passwordMatchMessage');
		var saveButton = document.getElementById('boton');
		var checkboxTerms = document.getElementById('checkbox');
		
		if(!checkboxTerms.checked || pass1 != pass2 || pass1 == ''){
			
			if(pass1 != pass2){
				messageDiv.innerHTML="${passwordMatchError}";
			}
			else{
				messageDiv.innerHTML="";
			}
			
			saveButton.disabled=true;
		}
		else{
			
			messageDiv.innerHTML="";
			saveButton.disabled=false;
		}
	}
</script>

<form:form action="agent/register.do" modelAttribute="agentRegistrationForm">

	<!-- Hidden Inputs -->
	
	<!-- Inputs -->
	
	<h3 style="text-decoration:underline"><spring:message code="agent.userAccount"/></h3>
	
	<acme:textbox code="agent.userAccount.userName" path="username"/><br>
	
	<form:label path="password"><strong><spring:message code="agent.userAccount.password" />: </strong></form:label>
	<form:password id="pass1" path="password" onkeyup="checkData()"/>
	<form:errors cssClass="error" path="password"/>
	
	<br><br>
	<label for="pass2"><strong><spring:message code="agent.userAccount.repeatPassword" />:</strong></label>
	<form:password id="pass2" path="passwordConfirmation" onkeyup="checkData()" /><br><br>
	<div id="passwordMatchMessage" style="color:red; text-decoration:underline"></div>
	
	<h3 style="text-decoration:underline"><spring:message code="agent.personalData"/></h3>
	<acme:textbox code="agent.name" path="name"/><br>
	<acme:textbox code="agent.surnames" path="surnames"/><br>
	<acme:textbox code="agent.postalAddress" path="postalAddress"/><br>
	<acme:textbox code="agent.phoneNumber" path="phoneNumber" placeholder="+34555555555"/><br>
	<acme:textbox code="agent.email" path="email"/><br>
	
	<form:checkbox path="acceptedTerms" onchange="checkData()" id="checkbox" name="checkbox" /><spring:message code="agent.accept"/> <a href="misc/termsAndConditions.do" target="_blank"><spring:message code="agent.termsAndConditions"/></a>
	<br><br>
	
	<input type="submit" name="save" value="<spring:message code="agent.save"/>" id="boton" disabled="disabled"/>
	<acme:cancel url="welcome/index.do" code="agent.cancel"/>	
	
</form:form>