<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="subscription/customer/edit.do" modelAttribute="subscription">

	<form:hidden path="id"/>
	<form:hidden path="newspaper"/>
	
	<acme:textbox code="creditCard.holderName" path="creditCard.holderName"/>
	<acme:textbox code="creditCard.brandName" path="creditCard.brandName" />
	<acme:textbox code="creditCard.number" path="creditCard.number" />
	<acme:textbox code="creditCard.CVV" path="creditCard.CVV"/>
	<acme:textbox code="creditCard.month" path="creditCard.month"/>
	<acme:textbox code="creditCard.year" path="creditCard.year"/>
	
	<acme:cancel url="newspaper/customer/display.do?newspaperId=${subscription.newspaper.id}" code="action.cancel"/>
	<acme:submit name="save" code="action.save"/>

</form:form>
