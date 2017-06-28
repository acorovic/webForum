var baseUrl = "http://localhost:8080/prodzekt/rest";

$(document).ready(function () {
	$('#navbarLoggedIn').hide();
	$('#adminActionsPanel').hide();


	checkLoggedInStatus();

	
	
	//forms
	//login form
	var loginFormId = 'loginForm';
	$('#' + loginFormId).submit(function (e) {
		handleForm(e, loginFormId);
	});

	//send message form
	var sendMessageId = 'messageForm';
	$('#' + sendMessageId).submit(function (e) {
		handleForm(e, sendMessageId);
	});
	

	//buttons
	$('#logout').click(function () {
		logoutUser();
	});

	

});

function checkLoggedInStatus() {
	$.ajax({
		url: baseUrl + "/users/active"
	}).then(function (user) {
		if (user !== undefined) {
			$('#loggedInUserame').text(user.username);
			$('#navbarLoggedIn').show();
			$('#navbarLogin').hide();
			$('#navbarRegister').hide();
			$('#registerUser').hide();	
			loadMessages();
		}
	});
}

function logoutUser() {
	$.ajax({
		url: baseUrl + "/users/logout"
	}).then(function (message) {
		alert(message);
		refresh();
	});
} 

function loadMessages() {
	$.ajax({
		url: baseUrl + "/messages/getMessages"
	}).then(function (messages) {
		var i = 0;
		var unseenMessages = 0;
		
		messages.forEach(function (message) {
			var messageRow = '<p>' + message.sender + " " + message.content;

			if(message.seen == false) {
				messageRow += '&nbsp; <button type="button" id="seen'+ i + '" class="btn btn-info btn-sm"> Read </button>'; 
				unseenMessages++;
			}

			$('#inboxMessages').append(messageRow + '</p>');

			i++;
		});

		$('#receivedMessages').text(unseenMessages);

	});
}

function handleForm(e, formId) {
	var form = $('#' + formId);
	e.preventDefault();
	$.ajax({
		type: form.attr('method'),
		url: form.attr('action'),
		data: form.serialize(),
		success: function(data) {
			alert(data);
			refresh();
		},
		error: function(data) {
			alert('An error occured');
		},
	});
}

function refresh() {
	location.reload(true);
}