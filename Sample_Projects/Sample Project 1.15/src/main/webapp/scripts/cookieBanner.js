var cookieDuration = 14;
var cookieName = 'bannerCookie';
var cookieValue = 'on';

function createDiv(inContent) {
	var contentWrapper = document.getElementById("content-wrapper");
	var div = document.createElement('div');

	div.setAttribute('id', 'cookieBanner');
	div.innerHTML = inContent;

	// Adds the Cookie Banner just after the content-wrapper
	contentWrapper.insertBefore(div, contentWrapper.firstChild);

	// Create the cookie
	createCookie(window.cookieName, window.cookieValue, window.cookieDuration);
}

function createCookie(name, value, days) {
	var expires = "";

	// Set the expiring date
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));

		expires = "expires=" + date.toGMTString() + ";";
	}

	// Create the cookie with its name, value and expiring date
	document.cookie = name + "=" + value + "; " + expires + " path=/";
}

function cookieCurrentValue(name) {
	var nameEquals = name + "=";
	var languageEquals = window.languageName + "=";

	var allCookies = document.cookie.split(';');

	// Iterate over all the cookies that are in the page
	for ( var i = 0; i < allCookies.length; i++) {
		var currentCookie = allCookies[i];

		// Eliminate spaces at the begining
		while (currentCookie.charAt(0) == ' ')
			currentCookie = currentCookie.substring(1, currentCookie.length);

		// Set language cookie
		if (currentCookie.indexOf(languageEquals) == 0)
			window.languageValue = currentCookie.substring(languageEquals.length, currentCookie.length);

		// When we are in the banner cookie, return a substring with its value
		if (currentCookie.indexOf(nameEquals) == 0)
			return currentCookie.substring(nameEquals.length, currentCookie.length);

	}
	return null;
}

function removeMe() {
	var element = document.getElementById('cookieBanner');
	element.parentNode.removeChild(element);
}

function toggleMoreInfo() {
	$("#readMore").toggle(100);
}
