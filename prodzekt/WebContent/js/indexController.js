var baseUrl = "http://localhost:8080/prodzekt/rest";

$(document).ready(function () {
	$('#navbarLoggedIn').hide();
	$('#adminActionsPanel').hide();
	$('#userPanel').hide();
	
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
		addSubforumForm(e, createSubforumId);
	});
	
	/*var reportId = 'reportForm';
	$('#' + reportId).submit(function (e) {
		handleForm(e, reportId);
	});*/

	//buttons
	$('#logout').click(function () {
		logoutUser();
	});
	
	$('#performSearchButton').click(function (e) {
		e.preventDefault();
		var keyWord = $('#keyWord').val();
		performSearch(keyWord);
	});
	
	loadSubforums();
	
});

function addSubforumForm(e, createSubforumId) {
	e.preventDefault();
	
	var file = $('#iconUpload')[0].files[0];

    $.ajax({
        url: baseUrl + "/subforums/icon",
        type: "POST",
        contentType: "multipart/form-data",
        dataType: "json",
        data: file,
        processData: false,
        async: false,
        complete: function (response) {

            //alert("File uploaded at: "+ response.responseText);

            var form = $('#' + createSubforumId);
            var input = $('#' + createSubforumId + ' :input');

            var data = {};
            for (var i = 0; i < input.length; i++) {
                if (input[i].name) {
                    if (input[i].type != "file") {
                        data[input[i].name] = input[i].value;
                    }
                }
            }

            data["icon"] = response.responseText;

            $.ajax({
                type: form.attr('method'),
                url: form.attr('action'),
                contentType: "application/json",
                data: JSON.stringify(data),
                complete: function (data) {
                		alert(data.responseText);
                        $('.modal').modal('hide');
                }
            });

        }
    });
}


function performSearch(keyWord) {	
	// search subforums
	if($('#subforumCriteriaName').is(':checked') || $('#subforumCriteriaDescription').is(':checked') 
			|| $('#subforumCriteriaModerator').is(':checked')) {
		$.ajax({
			url: baseUrl + "/subforums/search/" + keyWord,
			method: 'POST',
			data: $('#searchForm').serialize()
		}).then(function(subforums) {
			if(subforums != undefined) {
				subforums.forEach(function (subforum) {
					//add subforum to search result panel
					alert(subforum.name);
				});
			} else {
				// no result
				alert('No result');
			}		
		});
	}
	
	// search topics
	if($('#topicCriteriaName').is(':checked') || $('#topicCriteriaDescription').is(':checked') 
			|| $('#topicCriteriaAuthor').is(':checked') || $('#topicCriteriaSubforum').is(':checked')) {
		$.ajax({
			url: baseUrl + "/topics/search/" + keyWord,
			method: 'POST',
			data: $('#searchForm').serialize()
		}).then(function(topics) {
			if(topics != undefined) {
				topics.forEach(function (topic) {
					//add topic to search result panel
					alert(topic.name);
				});
			} else {
				// no result
				alert('No result');
			}		
		});
	}
	
	// search users
	if($('#userCriteriaUsername').is(':checked')) {
		$.ajax({
			url: baseUrl + "/users/search/" + keyWord,
			method: 'GET',
		}).then(function(users) {
			if(users != undefined) {
				users.forEach(function (user) {
					//add user to search result panel
					alert(user.username);
				});
			} else {
				// no result
				alert('No result');
			}		
		});
	}
}

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
			$('#userPanel').show();
			loadLikedTopics(user);
			loadDislikedTopics(user);
			loadLikedComments(user);
			loadDislikedComments(user);
			if(user.role != "USER"){
				$('#reportInbox').show();
			}else{
				$('#reportInbox').hide();
			}
			if(user.role != "ADMIN") {
				$('#changeUserRoleButton').hide();
				userRole = "ADMIN";
			} else {
				$('#changeUserRoleButton').show();
			}
			loadMessages();
			loadReports();
		} else {
			$('#sendMessageButton').hide();
			$('#changeUserRoleButton').hide();
		}
	});
}

function loadLikedComments(user) {
	if(user.likedComments != undefined) {
		$('#listOfLikedComments').html("");
		var likedComments = user.likedComments;
		
		for(key in likedComments) {
			$('#listOfLikedComments').append("<li>"  + likedComments[key] + "</li>");
		}
		
	}
}

function loadDislikedComments(user) {
	if(user.dislikedComments != undefined) {
		$('#listOfDislikedComments').html("");
		var dislikedComments = user.dislikedComments;
		
		for(key in dislikedComments) {
			$('#listOfDislikedComments').append("<li>"  + dislikedComments[key] + "</li>");
		}
		
	}
}

function loadLikedTopics(user) {
	if(user.likedTopics != undefined) {
		$('#listOfLikedTopics').html("");
		var likedTopics = user.likedTopics;
		
		for(key in likedTopics) {
			$('#listOfLikedTopics').append("<li>"  + likedTopics[key] + "</li>");
		}
		
	}
}

function loadDislikedTopics(user) {
	if(user.likedTopics != undefined) {
		$('#listOfDislikedTopics').html("");
		var dislikedTopics = user.dislikedTopics;
		
		for(key in dislikedTopics) {
			$('#listOfDislikedTopics').append("<li>"  + dislikedTopics[key] + "</li>");
		}
		
	}
}

function loadReports(){
	$.ajax({
		url: baseUrl + "/reports",
		method: 'GET'
	}).then(function(reports){
		var unprocessedReports = 0;
		reports.forEach(function (report) {
			var reportRow = '<h4>Report from: ' + report.offended + '</h4>' + report.reportText;
			if(report.processed == false) {
				reportRow += '<br><br>&nbsp';
				reportRow += '<button type="button" id="deleteEntity' + report.reportId + '" class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-trash"> </i> Delete </button>';
				reportRow += '<button type="button" id="warnAuthor' + report.reportId + '" class="btn btn-warning btn-sm"><i class="glyphicon glyphicon-exclamation-sign"> </i> Warn author </button>'; 
				reportRow += '<button type="button" id="informAuthor' + report.reportId + '" class="btn btn-success btn-sm"><i class="glyphicon glyphicon-share-alt"> </i> Inform report author </button>'; 
				unprocessedReports++;		
			}
			$('#inboxReports').append(reportRow + '<hr>');
			
			$('#deleteEntity' + report.reportId).click(function (){
		  		$.ajax({
					method: 'DELETE',
					url: baseUrl+'/reports/'+report.reportId
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
			
			$('#warnAuthor' + report.reportId).click(function (){
		  		$.ajax({
					method: 'POST',
					url: baseUrl+'/reports/'+report.reportId+'/warn'
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
			
			$('#informAuthor' + report.reportId).click(function (){
		  		$.ajax({
					method: 'POST',
					url: baseUrl+'/reports/'+report.reportId+'/inform'
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
		});
		$('#receivedReports').text(unprocessedReports);
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
		url: baseUrl + "/messages",
		method: 'GET'
	}).then(function (messages) {
		var unseenMessages = 0;
		messages.forEach(function (message) {
			var messageRow ='<h4>Message from: ' + message.sender + '</h4>' + message.content;

			if(message.seen == false) {
				messageRow += '<br>' + '<br>' + '&nbsp; <button type="button" id="seen'+ message.messageId + '" class="btn btn-info btn-sm"> Read </button>'; 
				unseenMessages++;
				
			}

			$('#inboxMessages').append(messageRow + '<hr>');
			
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
		url: baseUrl + "/subforums",
		method: 'GET'
	}).then(function (subforums){
		if(subforums != undefined) {
			subforums.forEach(function (subforum) {
				var subforumRow = '<li>';
			
				subforumRow += '<a href="#" id="subforum' + subforum.subforumId + '">' + subforum.name + '</a></li>';
				
				$('#subforums').append(subforumRow);
				
				$('#subforum' + subforum.subforumId).click(function () {
					//TODO: add full view of subforum and all themes
					alert(subforum.name);
				});
				
				var subforumPreviewPanel = createSubforumPreviewPanel(subforum);
				
				$('#subforumsTopicsPreview').append(subforumPreviewPanel);
				
				addTopicClickHandlers(subforum.topics, subforum.subforumId);
				
				$('#reportSubforum' + subforum.subforumId).click(function (){
				  	$('#reportModal').modal('show');
				  	$('#addReportModalButton').click(function (e) {
				  		e.preventDefault();
				  		$.ajax({
							method: 'POST',
							url: baseUrl+'/reports/'+subforum.subforumId,
							data: $('#reportForm').serialize()
						}).then(function (message) {
							alert(message);
							refresh();
						});
				  	});				
				});
				
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
		}
	});
}

function addTopicClickHandlers(topics, subforumId) {
	topics.forEach(function(topic) {
		$('#topicClick' + topic.topicId).click(function (){
			$('#topicName').html("");
			$('#topicComments').html("");
			
			var row = '<h4 class="modal-title" style="display:inline-block;">' + topic.name + '</h4>';
			row += '<button type="button" id="deleteTopic' + topic.topicId + '" class="btn btn-danger" style="float:right"><i class="glyphicon glyphicon-trash"> </i> Delete </button>  ';
			row += '<button type="button" id="reportTopic' + topic.topicId + '" class="btn btn-warning" style="float:right"><i class="glyphicon glyphicon-flag"> </i> Report </button>  ';
			row += '<button type="button" id="dislikeTopic' + topic.topicId + '" class="btn btn-danger" style="float:right"><i class="glyphicon glyphicon-thumbs-down"> </i> Dislike</button>  ';
			row += '<button type="button" id="likeTopic' + topic.topicId + '" class="btn btn-primary" style="float:right"><i class="glyphicon glyphicon-thumbs-up"> </i> Like</button>  ';
			$('#topicName').append(row);
			
			var counters = "<p>Topic likes: " + topic.likes + "</p>";
			counters += "<p> Topic dislikes: " + topic.dislikes + "</p>";
			$('#topicName').append(counters);
			
			// Topic button actions setup
			
			$('#reportTopic' + topic.topicId).click(function (){
			  	$('#reportModal').modal('show');
			  	$('#addReportModalButton').click(function (e) {
			  		e.preventDefault();
			  		$.ajax({
						method: 'POST',
						url: baseUrl+'/reports/'+topic.topicId,
						data: $('#reportForm').serialize()
					}).then(function (message) {
						alert(message);
						refresh();
					});
			  	});				
			});
			
			$('#deleteTopic' + topic.topicId).click(function (){
				$.ajax({
					method: 'DELETE',
					url: baseUrl + '/topics/' + subforumId + '/' +  topic.topicId,
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
			
			$('#likeTopic' + topic.topicId).click(function (){
				$.ajax({
					method: 'PUT',
					url: baseUrl + '/topics/like/' + subforumId + '/' +  topic.topicId,
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
			
			$('#dislikeTopic' + topic.topicId).click(function (){
				$.ajax({
					method: 'PUT',
					url: baseUrl + '/topics/dislike/' + subforumId + '/' +  topic.topicId,
				}).then(function (message) {
					alert(message);
					refresh();
				});
			});
			
			
			if(topic.comments != undefined) {
				topic.comments.forEach(function (comment) {
					var commentRow = '<div id="commentId' + comment.commentId + '"> <section class="panel panel-info col-md-12"><section class="row panel-body">';
					commentRow += '<section class="col-md-3"> <h4>' + comment.author.username + '</h4><br> Created on: <br> ' + comment.date + '<br>';
					if(comment.modified == true) {
						commentRow += " Edited on: " + comment.modifiedData;
					}
					commentRow += '</section>';
					commentRow += '<section class="col-md-9">' + comment.text + '</section>';
					commentRow += '<hr><section>' + '<button type="button" id="commentLike' + comment.commentId +'" class="btn btn-primary btn-sm"><i class="glyphicon glyphicon-thumbs-up"> </i> Like</button>  ';
					commentRow += '<button type="button" id="commentDislike' + comment.commentId + '" class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-thumbs-down"> </i> Dislike</button>  ';
					commentRow += '<button type="button" id="commentReport' + comment.commentId + '" class="btn btn-warning btn-sm"><i class="glyphicon glyphicon-flag" > </i> Report</button>  ';
					commentRow += '<button type="button" id="commentEdit' + comment.commentId + '" class="btn btn-info btn-sm"><i class="glyphicon glyphicon-italic"> </i> Edit</button>  ';
					commentRow += '<button type="button" id="commentDelete' + comment.commentId + '" class="btn btn-danger btn-sm"><i class="glyphicon glyphicon-trash"> </i> Delete</button>  '+ '</section>';
					
					commentRow += '</div>';
					$('#topicComments').append(commentRow);
					
					//Setup comment buttons
					$('#commentLike' + comment.commentId).click(function (){
						$.ajax({
							method: 'POST',
							url: baseUrl + '/comments/like/' + subforumId + '/' + topic.topicId /*+ '/' + comment.commentId*/,
							dataType: 'json',
							data: JSON.stringify(comment),
							contentType: 'application/json',
							complete: function(message) {
								alert(message.responseText);
							}
						});
					});
					
					$('#commentDislike' + comment.commentId).click(function (){
						$.ajax({
							method: 'POST',
							url: baseUrl + '/comments/dislike/' + subforumId + '/' + topic.topicId /*+ '/' + comment.commentId*/,
							dataType: 'json',
							data: JSON.stringify(comment),
							contentType: 'application/json',
							complete: function(message) {
								alert(message.responseText);
							}
						});
					});
					
					$('#commentEdit' + comment.commentId).click(function (){
						$('#editCommentContent').val(comment.text);
						$('#editCommentModal').modal('show');

						$('#editCommentForm').submit(function(e){
							e.preventDefault();
							$.ajax({
								method: 'PUT',
								url: baseUrl+'/comments/'+ subforumId + '/' + topic.topicId + '/' + comment.commentId,
								data: $('#editCommentForm').serialize()
							}).then(function (message) {
								alert(message);
								refresh();
							});
						});
						
					});
					
					$('#commentReport' + comment.commentId).click(function (){
					  	$('#reportModal').modal('show');
					  	$('#addReportModalButton').click(function (e) {
					  		e.preventDefault();
					  		$.ajax({
								method: 'POST',
								url: baseUrl+'/reports/'+comment.commentId,
								data: $('#reportForm').serialize()
							}).then(function (message) {
								alert(message);
								refresh();
							});
					  	});				
					});
					
					$('#commentDelete' + comment.commentId).click(function () {
						$.ajax({
							method: 'DELETE',
							url: baseUrl + '/comments/' +  topic.topicId + '/' + comment.commentId,
						}).then(function (message) {
							alert(message);
							refresh();
						});
					});
		
				});
			}
			
			
			// Comment form setup
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
						refresh();
					});
				}
			});
			
			$('#topicModal').modal('show');
		});
	});
}

function createSubforumPreviewPanel(subforum) {
	if(subforum.icon == "") {
         subforum.icon = "test.png";
    }
	
	var ret;
	var header = '<div class="row"> <section class="panel panel-info col-md-9"><section class="row panel-body"><section class="col-md-8"><h4><img src="' + subforum.icon + '" alt="" />'; 
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
	ret += '<button type="button" class="btn btn-danger btn-sm" id="removeSubforum' + subforum.subforumId + '"> <i class="glyphicon glyphicon-trash"> </i> Delete</button>';
	ret += '<button type="button" id="reportSubforum' + subforum.subforumId + '" class="btn btn-warning btn-sm"> <i class="glyphicon glyphicon-flag"> </i> Report</button></section></section></section>';
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