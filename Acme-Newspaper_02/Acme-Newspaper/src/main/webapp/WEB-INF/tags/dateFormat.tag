<%--
 * dateFormat.tag
 *
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="code" required="true" %>
<%@ attribute name="value" required="true" type="java.util.Date"%>

<%@ attribute name="type" required="false" %>

<jstl:if test="${type == null}">
	<jstl:set var="type" value="date" />
</jstl:if>

<%-- Definition --%>

	<spring:message code="${code}" var="acmeDateFormat"></spring:message>
	<fmt:formatDate value="${value}" pattern="${acmeDateFormat}" type="${type}"/>

