<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="folder/user/edit.do" modelAttribute="folder">
	<!-- Hidden inputs -->
	<form:hidden path="version"/>
	<form:hidden path="id"/>

	<acme:textbox code="folder.name" path="name"/><br>
	
	<jstl:if test="${folder.id==0}">
		<form:hidden path="parentFolder"/>
	</jstl:if>
	<jstl:if test="${folder.id!=0}">
		<acme:select items="${folders}" itemLabel="name" code="folder.parentFolder" path="parentFolder"/>
	</jstl:if>
	
	<br><br>
	<input type="submit" name="save" value="<spring:message code="folder.save"/>"/>
	<jstl:if test="${folder.id!=0}">
		<input type="submit" name="delete" value="<spring:message code="folder.delete"/>"/>
	</jstl:if>
	<input type="button" name="cancel" value="<spring:message code="folder.cancel"/>" onclick="javascript: relativeRedir('folder/user/list.do');" />
	
</form:form>

