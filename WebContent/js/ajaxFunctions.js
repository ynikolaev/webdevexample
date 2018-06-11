function deleteFunction(id) {
	//alert(id);
	if (confirm('Are you sure you want to delete this record?')) {
		deleteMessageToDB(id);
		$("#"+id+"").fadeOut('slow', function() {$(this).remove();});
		setTimeout(updateTable, 1000);
	}
}

function changeFunction(id) {
	findMessageInDB(id);
}

function RefreshTable() {
    $( "#msgtable" ).load( "messages #msgtable" );
}

function updateMessageInDB (id) {
	var value = id;
	var msgStatus = $('#editStatus > option:selected').val().toString().toLowerCase();
	var msgMessage = $('#editMessage').val().toString();
	$.ajax({
		url : "updatemessage",
		data : {
			row : value,
			status : msgStatus,
			message : msgMessage
		},
		type : "POST",
		dataType : "json",
		success : function(data2) {
			$('#chgText').css("color", "green");
			$('#chgText').html('Message was edited!');
			setTimeout(updateTable, 500);
		},
		error : function(data) {
			$('#chgText').css("color", "red");
			$('#chgText').html('Message was not edited!');
			alert("Failed");
		}
	})
}

function findMessageInDB (id) {
	// alert("Clicked!");
	var value = id;
	var status = "";
	var message = "";
	$.ajax({
		url : "findmessage",
		data : {
			row : value
		},
		type : "POST",
		dataType : "json",
		success : function(data2) {
			status = data2.status[0].toString().toLowerCase();
			message = data2.content[0];
			id = data2.msgid[0];
			$('#editMessage').val(message);
			if(status == 'private'){
				$("#editStatus").val("Private").change();
			} else {
				$("#editStatus").val("Public").change();
			}
			document.getElementById('saveChg').setAttribute('onclick','updateMessageInDB('+id+')')
		},
		error : function(data) {
			alert("Failed");
		}
	})
}

function addFunction() {
	var tagResult = 0;
	var publicMsg = '';
	var message = $('#message').val();
	var username = $("#basic-addon1").text().substring(1);
	var e = document.getElementById("inputGroupSelect04");
	var status = e.options[e.selectedIndex].value;
	status = status.charAt(0).toUpperCase() + status.slice(1);
	var d = new Date();
	var strDate = $.datepicker.formatDate('d.mm.yy', new Date());
	var strTime = moment().format("HH:mm");
	if(status == 'Public'){
		publicMsg = 'dark';
	} else {
		publicMsg = 'info';
	}
	if (!message.trim()) {
	    alert("Enter your message!");
	} else {
		var lastId = getLastMsgId(function(err, result){
			if (!err){
				tagResult = parseInt(result, 10) + 1;
				var addRow = '<tr id="'+tagResult+'"><th style="width: 5%" scope="row">...</th>'+
				'<td>'+strDate+' | '+strTime+'</td><td  style="width: 15%">'+username+'</td><td text-justify style="width: 35%">'+message+'</td>'+
				'<td style="width: 10%"><h5 class="border border-'+publicMsg+' rounded text-'+publicMsg+' text-center">'+status+'</h5></td>'+
				'<td  style="width: 20%"><button type="button" class="btn btn-primary">Change</button>  <button type="button" class="btn btn-danger" onclick="deleteFunction()">Delete</button></td></tr>';
	      		$(''+addRow+'').prependTo("table > tbody").hide().fadeIn(1000);
	      		addMessageToDB();
	      		setTimeout(updateTable, 1000);
			}
		});
	}
}

function addMessageToDB () {
	// alert("Clicked!");
	var e = document.getElementById("inputGroupSelect04");
	var status = e.options[e.selectedIndex].value;
	var message = $('#message').val().trim();
	$('#message').val('');
	$('#spnCharLeft').text('(250)');
	var value = "list";
	$.ajax({
		url : "addmessage",
		data : {
			name : value,
			usrMessage : message,
			msgStatus : status
		},
		type : "POST",
		dataType : "json",
		success : function(data2) {
			if (data2 = 1){
				$('#chgText').css("color", "green");
				$('#chgText').html('Message was added!');
			} else {
				$('#chgText').css("color", "red");
				$('#chgText').html('Message was not added!');
			}
		},
		error : function(data) {
			alert("Failed");
		}
	})
}

function deleteMessageToDB (id) {
	// alert("Clicked!");
	var value = id;
	$.ajax({
		url : "deletemessage",
		data : {
			row : value
		},
		type : "POST",
		dataType : "json",
		success : function(data2) {
			if (data2 = 1){
				$('#chgText').css("color", "green");
				$('#chgText').html('Message was deleted!!');
			} else {
				$('#chgText').css("color", "red");
				$('#chgText').html('Message was not deleted!!');
			}
		},
		error : function(data) {
			alert("Failed");
		}
	})
}

function getLastMsgId (callback) {
	// alert("Clicked!");
	var value = "msgid";
	return $.ajax({

		// The URL for the request
		url : "messagelist",

		// The data to send (will be converted to a query
		// string)
		data : {
			name : value
		},

		// Whether this is a POST or GET request
		type : "GET",

		// The type of data we expect back
		dataType : "json",
		success : function(data2) {
			var keys = data2.msgid;
			var last = keys[0];
			//alert('Last: '+last)
			return callback(null, last);
		},
		error : function(data2) {
			alert("Failed");
		}
	})
}

function caution() {
	alert("You cannot edit messages of other users!")
}

function updateTable () {
	// alert("Clicked!");
	var value = "list";
	$.ajax({

		// The URL for the request
		url : "messagelist",

		// The data to send (will be converted to a query
		// string)
		data : {
			name : value
		},

		// Whether this is a POST or GET request
		type : "GET",

		// The type of data we expect back
		dataType : "json",
		success : function(data2) {
			$("#msgtable tr:gt(0)").remove();
			var content = '';
			$.each(data2.content,
					function(i, item) {
					var changeMsg = '';
					var publicMsg = '';
					var deleteMsg = '';
					if(data2.currUser[0]==data2.users[i]){
						changeMsg = '<button type="button" data-toggle="modal" data-target="#exampleModalCenter" class="btn btn-primary" id="changeBtn" onclick="changeFunction(' + data2.msgid[i] + ')">Change</button> ';
						deleteMsg = ' <button type="button" class="btn btn-danger" onclick="deleteFunction(' + data2.msgid[i] + ')">Delete</button>';
					} else {
						changeMsg = '<button type="button" class="btn btn-secondary disabled" onclick="caution()">Change</button> ';
						deleteMsg = ' <button type="button" class="btn btn-secondary disabled" onclick="caution()">Delete</button>';
					}
					if(data2.status[i] == 'Public'){
						publicMsg = 'dark';
					} else {
						publicMsg = 'info';
					}
					content += '<tr id="' + data2.msgid[i] + '"><th style="width: 5%" scope="row">' + [ i + 1 ]
								+ '</th><td>' + data2.date[i]
								+ '</td><td  style="width: 15%">' + data2.firstname[i]
								+ " " + data2.lastname[i] + '</td><td text-justify style="width: 35%">'
								+ data2.content[i] + '</td><td  style="width: 10%"><h5 class="border border-'+publicMsg+' rounded text-'+publicMsg+' text-center">'+data2.status[i]+'</h5></td><td  style="width: 20%">'+ changeMsg +' '+ deleteMsg +'</td></tr>';
					});

			$('#messages').append(content);
		},
		error : function(data) {
			alert("Failed");
		}
	})
}

function textCounter(field, cntfield, maxlimit) {        
    if (document.getElementById(""+field+"").value.length > maxlimit) {
        // if too long...trim it!
        document.getElementById(''+field+'').value = document.getElementById(''+field+'').value.substring(0, maxlimit);
    }
    // otherwise, update 'characters left' counter
    else {        
      document.getElementById(''+cntfield+'').value = maxlimit - document.getElementById(''+field+'').value.length;
    }
}


function findMember(t) {
    //alert(t);
	searchTable(t);
}

function searchTable (searchVal) {
	// alert("Clicked!");
	var value = searchVal;
	//alert(value);
	$.ajax({

		// The URL for the request
		url : "searchmessage",

		// The data to send (will be converted to a query
		// string)
		data : {
			search : value
		},

		// Whether this is a POST or GET request
		type : "GET",

		// The type of data we expect back
		dataType : "json",
		success : function(data2) {
			$("#msgtable tr:gt(0)").remove();
			var content = '';
			$.each(data2.content,
					function(i, item) {
					var changeMsg = '';
					var publicMsg = '';
					var deleteMsg = '';
					if(data2.currUser[0]==data2.users[i]){
						changeMsg = '<button type="button" data-toggle="modal" data-target="#exampleModalCenter" class="btn btn-primary" id="changeBtn" onclick="changeFunction(' + data2.msgid[i] + ')">Change</button> ';
						deleteMsg = ' <button type="button" class="btn btn-danger" onclick="deleteFunction(' + data2.msgid[i] + ')">Delete</button>';
					} else {
						changeMsg = '<button type="button" class="btn btn-secondary disabled" onclick="caution()">Change</button> ';
						deleteMsg = ' <button type="button" class="btn btn-secondary disabled" onclick="caution()">Delete</button>';
					}
					if(data2.status[i] == 'Public'){
						publicMsg = 'dark';
					} else {
						publicMsg = 'info';
					}
					content += '<tr id="' + data2.msgid[i] + '"><th style="width: 5%" scope="row">' + [ i + 1 ]
								+ '</th><td>' + data2.date[i]
								+ '</td><td  style="width: 15%">' + data2.firstname[i]
								+ " " + data2.lastname[i] + '</td><td text-justify style="width: 35%">'
								+ data2.content[i] + '</td><td  style="width: 10%"><h5 class="border border-'+publicMsg+' rounded text-'+publicMsg+' text-center">'+data2.status[i]+'</h5></td><td  style="width: 20%">'+ changeMsg +' '+ deleteMsg +'</td></tr>';
					});

			$('#messages').append(content);
		},
		error : function(data) {
			alert("Failed");
		}
	})
}

$(document).ready(
		function() {
			updateTable();
			//$('#spnCharLeft').css('display', 'none');
		    var maxLimit = 250;
			$('#message').keyup(function () {
	            var lengthCount = this.value.length;              
	            if (lengthCount > maxLimit) {
	                this.value = this.value.substring(0, maxLimit);
	                var charactersLeft = maxLimit - lengthCount + 1;                   
	            }
	            else {                   
	                var charactersLeft = maxLimit - lengthCount;                   
	            }
	            //$('#spnCharLeft').css('display', 'block');
	            $('#spnCharLeft').text('('+charactersLeft+')');
	        });
			var thread = null;

		    $('#searchVal').keyup(function() {
		      clearTimeout(thread);
		      var $this = $(this); thread = setTimeout(function(){findMember($this.val())}, 500);
		    });
		});