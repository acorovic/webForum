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
	var sendMessageId = 'sendMessageForm';
	$('#' + sendMessageId).submit(function (e) {
		handleForm(e, sendMessageId);
	});
	
	var registerFormId = 'registerForm';
	$('#' + registerFormId).submit(function (e) {
		handleForm(e, registerFormId);
	});
	
	var changeUserRoleId = 'changeUserRoleForm';
	$('#' + changeUserRoleId).submit(function (e){
		e.preventDefault();
		changeUserRole();
	});
	
	var createSubforumId = 'createSubforumForm';
	$('#' + createSubforumId).submit(function (e) {
		handleForm(e, createSubforumId);
	});
	

	//buttons
	$('#logout').click(function () {
		logoutUser();
	});
	
	
	loadSubforums();
	
});


function changeUserRole() {
	var user = $('#usernameChangeRole').val();
	var selectRole = $('#selectUserRole').val();
	role = "";
	switch(selectRole) {
	case "0":
		role = "admin";
		break;
	case "1":
		role = "moderator";
		break;
	case "2":
		role = "user";
		break;
	default:
		role = "";
	}
	
	if(role != "") {
		$.ajax({
			url: baseUrl + "/users/update/" + role + "/" + user,
			method: 'PUT'
		}).then(function(message) {
			alert(message);
			refresh();
		});
	} else {
		alert("Error!");
	}
	
}

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
			$('#sendMessageButton').show();
			if(user.role != "ADMIN") {
				$('#changeUserRoleButton').hide();
				userRole = "ADMIN";
			} else {
				$('#changeUserRoleButton').show();
			}
			loadMessages();
		} else {
			$('#sendMessageButton').hide();
			$('#changeUserRoleButton').hide();
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
		var unseenMessages = 0;
		messages.forEach(function (message) {
			var messageRow ='<h4>Message from: ' + message.sender + '</h4>' + message.content;

			if(message.seen == false) {
				messageRow += '<br>' + '<br>' + '&nbsp; <button type="button" id="seen'+ message.messageId + '" class="btn btn-info btn-sm"> Read </button>'; 
				unseenMessages++;
				
			}

			$('#inboxMessages').append(messageRow + '<hr></p>');
			
			if(message.seen == false) {
				$('#seen' + message.messageId).click(function() {
					setMessageSeen(message.messageId);
				});
			}

		});

		$('#receivedMessages').text(unseenMessages);

	});
}

function loadSubforums() {
	$.ajax({
		url: baseUrl + "/subforums/getSubforums"
	}).then(function (subforums){
		if(subforums != undefined) {
			subforums.forEach(function (subforum) {
				var subforumRow = '<li> <a href="#" id="subforum' + subforum.subforumId + '">' + subforum.name + '</a></li>';
				
				$('#subforums').append(subforumRow);
				
				$('#subforum' + subforum.subforumId).click(function () {
					//TODO: add full view of subforum and all themes
					alert(subforum.name);
				});
				
				var subforumPreviewPanel = createSubforumPreviewPanel(subforum);
				
				$('#subforumsTopicsPreview').append(subforumPreviewPanel);
				
				addTopicClickHandlers(subforum.topics);
				
				$('#addTopic' + subforum.subforumId).click(function () {
					$('#createTopicModal').modal('show');
					$('#addTopicModalButton').click(function (e) {
						e.preventDefault();
						$.ajax({
							url: baseUrl + '/topics/' + subforum.subforumId,
							method: 'POST',
							data: $('#createTopicForm').serialize()
						}).then(function (message) {
							alert(message);
							refresh();
					});
					
			
				});
				
				$('#removeSubforum' + subforum.subforumId).click(function (){
					$.ajax({
						url: baseUrl + '/subforums/' + subforum.subforumId,
						method: 'DELETE'
					}).then(function (message) {
						alert(message);
						refresh();
					});
				});
				
				
				
			});
		});
		}
	});
}

function addTopicClickHandlers(topics) {
	topics.forEach(function(topic) {
		$('#topicClick' + topic.topicId).click(function (){
			$('#topicName').html("");
			$('#topicComments').html("");
			
			var row = '<h4 class="modal-title">' + topic.name + '</h4>';
			
			$('#topicName').append(row);
			if(topic.comments != undefined) {
				topic.comments.forEach(function (comment) {
					var commentRow = '<div id="commentId' + comment.commentId + '"> <section class="panel panel-info col-md-12"><section class="row panel-body">';
					commentRow += '<section class="col-md-3"> <h4>' + comment.author.username + '</h4> ' + comment.date + '</section>';
					commentRow += '<section class="col-md-9">' + comment.text + '</section>';
					commentRow += '<hr><section>' + '<button type="button" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-thumbs-up"> </i> Like</button>  ';
					commentRow += '<button type="button" class="btn btn-warning btn-sm"><i class="glyphicon glyphicon-exclamation-sign"> </i> Report</button>  ';
					commentRow += '<button type="button" class="btn btn-info btn-sm"><i class="glyphicon glyphicon-italic"> </i> Edit</button>  ';
					commentRow += '<button type="button" class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-trash"> </i> Delete</button>  '+ '</section>';
					
					commentRow += '</div>';
					$('#topicComments').append(commentRow);
		
				});
			}
			
			var addCommentFormId = 'commentForm';
			$('#' + addCommentFormId).submit(function (e){
				e.preventDefault();
				var commentContent = $('#commentContent').val();
				if(commentContent == "") {
					alert("Empty comment!");
				} else {
					$.ajax({
						method: 'POST',
						url: baseUrl + '/comments/' + topic.topicId,
						data: $('#' + addCommentFormId).serialize()
					}).then(function (message) {
						alert(message);
					});
				}
			});
			
			$('#topicModal').modal('show');
		});
	});
}

function createSubforumPreviewPanel(subforum) {
	var ret;
	var header = '<div class="row"> <section class="panel panel-info col-md-9"><section class="row panel-body"><section class="col-md-8"> <h4>'; 
	header += subforum.name + "</h4>  <h5>" + subforum.description + "</h5><hr>";
	ret = header;
	var row = '<section> <ul>';
	topics = subforum.topics;
	if(topics != undefined) {
		topics.forEach(function (topic) {
			row += '<li class="list-unstyled"><a href="#" id="topicClick' + topic.topicId + '"><i class="glyphicon glyphicon-comment"> </i>  ' + topic.name + '</a></li>';
		});
	}
	
	row += "</ul>";
	ret += row + '</section></section> <section class="col-md-2">';
	ret += '<button type="button" class="btn btn-primary btn-sm" id="addTopic' + subforum.subforumId + '"> <i class="glyphicon glyphicon-plus"> </i> Add topic</button>';
	ret += '<button type="button" class="btn btn-danger btn-sm" id="removeSubforum' + subforum.subforumId + '"> <i class="glyphicon glyphicon-trash"> </i> Delete</button></section></section></section>';
	
	return ret;
}
			


function setMessageSeen(messageId) {
	$.ajax({
		url: baseUrl + "/users/seen/" + messageId
	}).then(function(message) {
		alert(message);
		var cnt = $('#receivedMessages').text();
		$('#seen' + messageId).hide();
		$('#receivedMessages').text(--cnt);
		
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