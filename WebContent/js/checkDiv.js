$(document).ready(function() {
   str = $('#msg-alert').text();
   if($.trim(str) === "") {
     $('#msg-alert').hide();
   }
});