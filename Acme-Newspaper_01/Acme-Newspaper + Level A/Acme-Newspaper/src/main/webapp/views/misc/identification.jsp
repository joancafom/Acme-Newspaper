<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<jstl:if test="${cookie.language.value eq null or cookie.language.value eq 'en'}">
<div id="info_en">
	<p>
	Acme Rendezvous, Inc. provides a fast and convenient way to arrange meetings with other people, from employees to friends!
	</p>
	
	<p>
	Acme Rendevous Inc, is a part of Acme Inc., a worldwide holding that encompasses many other companies.
	</p>
	
	<p>
	We're here to help! Some ways to contact us:
	</p>
	<ul>
		<li>Hotline 24/7 on +34 600000001</li>
		<li>rendezvous@acme.com</li>
	</ul>
	
	<p>
	Acme, Inc. <strong>B13538079</strong>
	</p>
	<p>
	Avd de la Independencia 127,
	</p>
	<p>
	28001 Madrid (MADRID)
	</p>
	<p>
	VAT Number Identifier: <strong>ESA0011012B</strong>
	</p>
</div>
</jstl:if>
<jstl:if test="${cookie.language.value eq 'es'}">
<div id="info_es">
	<p>
	Acme Rendezvous, Inc. le proporciona una manera fácil y rápida de organizar reuniones con otras personas, ¡desde empleados hasta amigos!
	</p>
	
	<p>
	Acme Rendevous Inc, forma parte de Acme Inc., un holding mundial que cuenta con muchas otras compañías.
	</p>
	
	<p>
	¡Estamos aquí para ayudarle! Algunas formas de contactarnos:
	</p>
	<ul>
		<li>Línea de Atención al cliente 24h - 600000001</li>
		<li>rendezvous@acme.com</li>
	</ul>
	
	<p>
	Acme, Inc. <strong>B13538079</strong>
	</p>
	<p>
	Avd de la Independencia 127,
	</p>
	<p>
	28001 Madrid (MADRID)
	</p>
	<p>
	Identificador fiscal: <strong>ESA0011012B</strong>
	</p>
</div>
</jstl:if>
