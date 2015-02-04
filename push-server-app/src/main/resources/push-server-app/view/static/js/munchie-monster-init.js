/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

/** @namespace window.location */
/** @namespace window.document */

// IE does not have a console when the debugger is closed.
// We create one here so that it can be freely referenced.
if (typeof window.console === "undefined") window.console = {};
if (typeof window.console.log === "undefined") console.log = function(msg){};
if (typeof window.console.warn === "undefined") console.warn = function(msg){};
if (typeof window.console.error === "undefined") console.error = function(msg){};
if (typeof window.console.debug === "undefined") console.debug = function(msg){};

// Replace the default onerror handler with a custom version
// that includes notification back to www.MunchieMonster.com
window.onerror = function(message, url, lineNumber) {

  var msg = "Message: ";

  if ("ReferenceError: Can't find variable: imenu_title" == message ||
      "ReferenceError: Can't find variable: imenu_list_id" == message ||
      "ReferenceError: Can't find variable: imenu_item_link" == message ||
      "ReferenceError: Can't find variable: imenu_item_subject" == message) {

    // Stupid iphone plugin bugs
    msg = "Suppressed " + msg + message;

  } else {
    alert(!message ? "Unspecified Error" : message);

    msg += (!message) ? "Unspecified" : message;

    msg += "\nSource: ";
    msg += (!url) ? "unspecified" : url;
    msg += " at line #";
    msg += lineNumber;

    msg += "\nPage: ";
    msg += window.location.href;
  }

  notifyException(msg);
};

// Update the prototypes to provide "missing" functions.
String.prototype.startsWith = function(text) {
  return this.indexOf(text) == 0;
};
String.prototype.equals = function(that) {
  return this == that;
};

$(document).ready(function(){
  var msg;
  var pleaseUpgrade = "\n\nWhile we at Munchie Monster have done everything we can to support every major web browser we can only take it so far... We must insist you upgrade your browser to the latest version in order to continue using our site.";
  var assistance = "\n\nIf you need additional assistance with this issue please feel free to contact us at support@munchiemonster.com.";

  try { (JSON === undefined); } catch (e) {
    msg = notifyException("Your web browser does not support the JSON factory.");
    alert(msg+pleaseUpgrade+assistance);
  }

  if (!$.support.ajax) {
    msg = notifyException("Your web browser does not support AJAX request.");
    alert(msg+pleaseUpgrade+assistance);
  }

  if (!$.support.boxModel) {
    msg = notifyException("Your web browser does not support the W3C Box Model.");
    alert(msg+"\n\nIf you are using Internet Explorer, you can fix this problem by switching your browser from Quirks Mode to Standards Mode."+assistance);
  }

});

function notifyException(msg) {
  try {
    makeAjaxCall("/api/system/client-exception", msg, function(){}, function(){});
  } catch (e) {
    console.log(e);
  }
  return msg;
}