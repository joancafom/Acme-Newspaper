<%--
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

<jstl:if test="${cookie.language.value eq null or cookie.language.value eq 'en'}">

<h3>1. Introduction</h3>
<p>The current general use conditions of the web page, regulate the terms and conditions of access and 
use of <strong>www.acme.com</strong>, property of <strong>Acme, Inc.</strong>, located at <strong>Av. 
de la Independencia 127, 28001, Madrid, Madrid, Spain</strong> and tax ID number <strong>B13538079
</strong>, from now on, "the Company", which the web portal user must read and accept in order to use 
all the services and information provided in the web portal. The mere access and/or utilization of the 
web portal, of all or part of its contents and/or services means the full acceptance of the current 
general use conditions.</p>

<h3>2. Use conditions</h3>
<p>The current general use conditions of the web portal regulate the access and the utilization of the 
web portal, including the contents and the services made available to the users in and/or through the 
web portal, either by the web portal, or by its users, or by third parties. However, the access and the 
utilization of some contents and/or services may be under some specific conditions.</p>

<h3>3. Modifications</h3>
<p>The company reserves the right to modify at any time the general use conditions of the web portal. 
In any case, it is recommended to consult regularly the current use terms of the web portal, since they 
can be modified.</p>

<h3>4. User obligations</h3>
<p>The user must respect at all times the terms and conditions established in the current general use 
conditions of the web portal. The user expressly declare that he will use the web portal diligently and 
assuming any responsibility that could result from the noncompliance with the rules.</p>
<p>Also, the user isn't allowed to use the web Portal to transmit, store, spread, promote or distribute 
data or contents which carry viruses or any other type of informatic code, archives or programs design 
to interrupt, destroy or damage the functioning of any program or computer equipment or 
telecommunication equipment.</p>

<h3>5. Responsibility of the web portal</h3>
<p>The user knows and accepts that the web portal doesn't grant any warranty of any type, either 
expressed or implicit, over the data, contents, information and services that are incorporated and 
offered from the web Portal.</p>
<p>Excluding the cases which the Law imposes expressly the opposite, and exclusively with the measure 
and extensions in which it is imposed, the web Portal neither ensures nor assumes any responsibility 
in respect to potential damages caused by the use and utilization of the information, data and services 
of the web Portal.</p>
<p>In any case, the web Portal excludes any responsibility for the damages that might be caused by the 
information and/or services provided or supplied by third parties separated to the Company. All 
resposibility will be of the third party, either supplier or colaborator.</p>

<h3>6. Intellectual and industrial property</h3>
<p>All the contents, brands, logos, drawings, documentation, computer programs or any other element 
subject to protection under the intellectual and industrial property legislation, which are available 
in the web Portal correspond exclusively to the Company or to those legally entitled to them and now 
remain expressly reserved all the rights over them. Now remains expressly prohibited the creation of 
hiperlinks to any element constituent of the web pages of the Portal without authorization of the 
Company, as long as they aren't to a webpage of the web Portal that doesn't require identification or 
authentication for its access, or it is restricted.</p>
<p>In any case, the web Portal reserves all the rights over the contents, information data and services 
that it holds over them. The web Portal doesn't grant any licence or use authorization to the user over 
its contents, data or services, different to the expressly detailed in the current general use 
conditions of the web portal.</p>

<h3>7. User data</h3>
<p>Following the spanish Organic Law 15/1999, when you register in this webpage, you accept our data 
practices, including the collection, the utilization, the processing and the shared use of your 
information, as well as the transfer and the processing of your information in Spain and other 
countries where we have or use facilities, service providers or partners, no matter where you use our 
services.</p>
<p>In any moment, you can send a message to <strong>rendezvous@acme.com</strong> requesting to modify 
or delete (as well as to see) any of the information we store about you in our servers. You will be 
sent in the next 24h further instructions on how to continue with this process.</p>

<h3>8. Applicable laws, competent jurisdiction and notifications</h3>
<p>The current conditions are subject and interpreted according to the spanish Laws. For any demand the 
competent authorities will be the courts and tribunals of <strong>Madrid</strong>. All the 
notifications, requirements, petitions and other communications that the User desires to make to the 
Company owner of the web Portal must be written and will be consider correctly made when they are 
received in the following address <strong>rendezvous@acme.com</strong>.</p>

</jstl:if>


<jstl:if test="${cookie.language.value eq 'es'}">

<h3>1. Introducción</h3>
<p>Las presentes condiciones generales de uso de la página web, regulan los términos y condiciones de 
acceso y uso de <strong>www.acme.com</strong>, propiedad de <strong>Acme, Inc.</strong>, con domicilio 
en <strong>Av. de la Independencia 127, 28001, Madrid, Madrid, España</strong> y con Código de 
Identificación Fiscal número <strong>B13538079</strong>, en adelante, "la Empresa", que el usuario del 
Portal deberá de leer y aceptar para usar todos los servicios e información que se facilitan desde el 
portal. El mero acceso y/o utilización del portal, de todos o parte de sus contenidos y/o servicios 
significa la plena aceptación de las presentes condiciones generales de uso.</p> 

<h3>2. Condiciones de uso</h3>
<p>Las presentes condiciones generales de uso del portal regulan el acceso y la utilización del portal, 
incluyendo los contenidos y los servicios puestos a disposición de los usuarios en y/o a través del 
portal, bien por el portal, bien por sus usuarios, bien por terceros. No obstante, el acceso y la 
utilización de ciertos contenidos y/o servicios puede encontrarse sometido a determinadas condiciones 
específicas.</p>

<h3>3. Modificaciones</h3>
<p>La empresa se reserva la facultad de modificar en cualquier momento las condiciones generales de 
uso del portal. En todo caso, se recomienda que consulte periódicamente los presentes términos de uso 
del portal, ya que pueden ser modificados.</p>

<h3>4. Obligaciones del Usuario</h3>
<p>El usuario deberá respetar en todo momento los términos y condiciones establecidos en las presentes 
condiciones generales de uso del portal. De forma expresa el usuario manifiesta que utilizará el portal 
de forma diligente y asumiendo cualquier responsabilidad que pudiera derivarse del incumplimiento de 
las normas.</p>
<p>Así mismo, el usuario no podrá utilizar el portal para transmitir, almacenar, divulgar promover o 
distribuir datos o contenidos que sean portadores de virus o cualquier otro código informático, archivos 
o programas diseñados para interrumpir, destruir o perjudicar el funcionamiento de cualquier programa o 
equipo informático o de telecomunicaciones.</p>

<h3>5. Responsabilidad del portal</h3>
<p>El usuario conoce y acepta que el portal no otorga ninguna garantía de cualquier naturaleza, ya sea 
expresa o implícita, sobre los datos, contenidos, información y servicios que se incorporan y ofrecen 
desde el Portal.</p>
<p>Exceptuando los casos que la Ley imponga expresamente lo contrario, y exclusivamente con la medida y 
extensión en que lo imponga, el Portal no garantiza ni asume responsabilidad alguna respecto a los 
posibles daños y perjuicios causados por el uso y utilización de la información, datos y servicios del 
Portal.</p>
<p>En todo caso, el Portal excluye cualquier responsabilidad por los daños y perjuicios que puedan 
deberse a la información y/o servicios prestados o suministrados por terceros diferentes de la Empresa. 
Toda responsabilidad será del tercero ya sea proveedor o colaborador.</p>

<h3>6. Propiedad intelectual e industrial</h3>
<p>Todos los contenidos, marcas, logos, dibujos, documentación, programas informáticos o cualquier otro 
elemento susceptible de protección por la legislación de propiedad intelectual o industrial, que sean 
accesibles en el portal corresponden exclusivamente a la empresa o a sus legítimos titulares y quedan 
expresamente reservados todos los derechos sobre los mismos. Queda expresamente prohibida la creación 
de enlaces de hipertexto (links) a cualquier elemento integrante de las páginas web del Portal sin la 
autorización de la empresa, siempre que no sean a una página web del Portal que no requiera 
identificación o autenticación para su acceso, o el mismo esté restringido.</p>
<p>En cualquier caso, el portal se reserva todos los derechos sobre los contenidos, información datos y 
servicios que ostente sobre los mismos. El portal no concede ninguna licencia o autorización de uso al 
usuario sobre sus contenidos, datos o servicios, distinta de la que expresamente se detalle en las 
presentes condiciones generales de uso del portal.</p>

<h3>7. Datos del Usuario</h3>
<p>De acuerdo con la Ley Orgánica 15/1999 de Protección de Datos, al registrarte en esta página web, 
aceptas nuestras prácticas de datos, que incluyen la recopilación, la utilización, el procesamiento y 
el uso compartido de tu información, así como la transferencia y el procesamiento de tu información en 
España y otros países donde tenemos o usamos instalaciones, proveedores de servicios o socios, sin 
importar dónde uses nuestros Servicios.</p>
<p>En cualquier momento, puedes enviar un mensaje a <strong>rendezvous@acme.com</strong> para solicitar 
la modificación o eliminación (además de ver) toda la información sobre ti que guardamos en nuestros 
servidores. Se te enviará en las siguientes 24h instrucciones más a fondo sobre cómo continuar con el 
proceso.</p>

<h3>8. Legislación aplicable, jurisdicción competente y notificaciones</h3>
<p>Las presentes condiciones se rigen y se interpretan de acuerdo con las Leyes de España. Para 
cualquier reclamación serán competentes los juzgados y tribunales de <strong>Madrid</strong>. Todas 
las notificaciones, requerimientos, peticiones y otras comunicaciones que el Usuario desee efectuar a 
la Empresa titular del Portal deberán realizarse por escrito y se entenderá que han sido correctamente 
realizadas cuando hayan sido recibidas en la siguiente dirección <strong>rendezvous@acme.com</strong>.
</p>

</jstl:if>