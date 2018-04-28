<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<h3><jstl:out value="${folder.name}"/></h3>

<a href="message/${actorWS}/create.do"><spring:message code="folder.message.send"/></a><br>

<security:authorize access="hasRole('ADMIN')">
	<a href="message/administrator/create.do?isNotification=true"><spring:message code="folder.notification.send"/></a><br>
</security:authorize>

<br>
<hr>
<display:table name="folders" id="folder" requestURI="folder/${actorWS}/list.do" class="displaytag" pagesize="5">
	<display:column titleKey="folder.name" sortable="true">
		<a href="folder/${actorWS}/list.do?folderId=${folder.id}"><jstl:out value="${folder.name}"/></a>
	</display:column>
	<display:column>
		<jstl:if test="${folder.isSystem == false}">
			<a href="folder/${actorWS}/edit.do?folderId=${folder.id}"><spring:message code="folder.edit"/></a>
		</jstl:if>
	</display:column>
</display:table>

<jstl:if test="${folder.isSystem==false || folderId==null}">
	<a href="folder/${actorWS}/create.do?folderId=${folderId}"><spring:message code="folder.create"/></a>
</jstl:if>

<jstl:if test="${folderId!=null}">
	<hr>
	<strong><spring:message code="folder.messages"/>:</strong>
	<display:table name="anMessages" id="anMessage" requestURI="${requestURI}" class="displaytag" pagesize="5">
	<display:column titleKey="folder.message.date">
		<jstl:out value="${anMessage.sentMoment}"/>
	</display:column>
	<display:column titleKey="folder.message.sender" sortable="true">
		<jstl:out value="${anMessage.sender.name}"/>
	</display:column>
	<display:column titleKey="folder.message.subject" sortable="true">
		<jstl:out value="${ anMessage.subject}"/>
	</display:column>
	<display:column titleKey="folder.message.priority" sortable="true">
		<jstl:out value="${anMessage.priority}"/>
	</display:column>
	<display:column >
		<a href="message/${actorWS}/display.do?messageId=${anMessage.id}"><spring:message code="folder.message.details"/></a>
		<a href="message/${actorWS}/edit.do?messageId=${anMessage.id}"><spring:message code="folder.message.edit"/></a>
	</display:column>
</display:table>
</jstl:if>