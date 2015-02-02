/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */

$(document).ready(function() {
  updateFormFields();

  setTimeout(hideMessage, 5*1000);
});

function hideMessage() {
  $(".message").fadeOut(3*1000);
}

function updateFormFields() {

//  $("input[type='number']").each(function(index, element) {
//    $(element).blur(function() {
//      validateNumberField(element)
//    });
//  });

  $("select").each(function(index, element){
    var value = $(element).data("default");
    if (value != undefined) {
      $(element).val("");
      $(element).val(value+"");
    }
  });

//  $("input[type='submit']").each(confirmDelete);
//  $("button").each(confirmDelete);
}
