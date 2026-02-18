<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% String email=(String) session.getAttribute("user"); if (email==null || email.isEmpty()) {
        response.sendRedirect("index.jsp?error=session"); return; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Forum - Mental Wellness</title>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="sample.css">
        </head>

        <body>
            <div class="forum-page">
                <!-- Top Navigation Bar -->
                <div class="forum-navbar">
                    <div class="forum-navbar-left">
                        <i class="fa-solid fa-comments forum-navbar-icon"></i>
                        <span class="forum-navbar-title">Wellness Forum</span>
                    </div>
                    <div class="forum-navbar-right">
                        <span class="forum-navbar-user">
                            <i class="fa-solid fa-circle forum-online-dot"></i>
                            <%= email %>
                        </span>
                        <a href="welcome.jsp" class="forum-nav-btn"><i class="fa-solid fa-home"></i> Home</a>
                        <a href="logout.jsp" class="forum-nav-btn forum-nav-btn-danger"><i
                                class="fa-solid fa-right-from-bracket"></i> Logout</a>
                    </div>
                </div>

                <div class="forum-container">
                    <!-- Left Sidebar: User List -->
                    <div class="forum-sidebar">
                        <div class="forum-sidebar-header">
                            <h5><i class="fa-solid fa-users"></i> People</h5>
                        </div>
                        <div class="forum-user-list" id="userList">
                            <div class="forum-loading">
                                <i class="fa-solid fa-spinner fa-spin"></i> Loading users...
                            </div>
                        </div>
                    </div>

                    <!-- Right Panel: Chat Area -->
                    <div class="forum-chat-area" id="chatArea">
                        <!-- Empty state -->
                        <div class="forum-empty-state" id="emptyState">
                            <i class="fa-solid fa-comment-dots forum-empty-icon"></i>
                            <h4>Start a Conversation</h4>
                            <p>Select a person from the list to begin chatting privately.</p>
                        </div>

                        <!-- Chat header (hidden initially) -->
                        <div class="forum-chat-header" id="chatHeader" style="display:none;">
                            <div class="forum-chat-header-info">
                                <div class="forum-avatar-sm" id="chatAvatar"></div>
                                <span class="forum-chat-with" id="chatWith"></span>
                            </div>
                        </div>

                        <!-- Messages container (hidden initially) -->
                        <div class="forum-messages" id="messagesContainer" style="display:none;">
                        </div>

                        <!-- Input bar (hidden initially) -->
                        <div class="forum-input-bar" id="inputBar" style="display:none;">
                            <input type="text" id="messageInput" class="forum-message-input"
                                placeholder="Type a message..." autocomplete="off">
                            <button id="sendBtn" class="forum-send-btn">
                                <i class="fa-solid fa-paper-plane"></i>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                const currentUser = '<%= email %>';
                let activeChat = null;
                let pollInterval = null;

                // Load user list on page load
                document.addEventListener('DOMContentLoaded', function () {
                    loadUsers();
                });

                function loadUsers() {
                    fetch('forum-api?action=users')
                        .then(res => res.json())
                        .then(users => {
                            const list = document.getElementById('userList');
                            if (users.length === 0) {
                                list.innerHTML = '<div class="forum-loading">No other users found.<br>Run /demo/db-setup first.</div>';
                                return;
                            }
                            list.innerHTML = '';
                            users.forEach(u => {
                                const item = document.createElement('div');
                                item.className = 'forum-user-item';
                                item.setAttribute('data-email', u.email);
                                item.innerHTML =
                                    '<div class="forum-avatar">' + u.email.charAt(0).toUpperCase() + '</div>' +
                                    '<div class="forum-user-info">' +
                                    '<div class="forum-user-name">' + u.email.split('@')[0] + '</div>' +
                                    '<div class="forum-user-email">' + u.email + '</div>' +
                                    '</div>';
                                item.addEventListener('click', function () {
                                    openChat(u.email);
                                });
                                list.appendChild(item);
                            });
                        })
                        .catch(err => {
                            document.getElementById('userList').innerHTML =
                                '<div class="forum-loading" style="color:#ef4444;">Failed to load users. Make sure DB is set up.</div>';
                        });
                }

                function openChat(email) {
                    activeChat = email;

                    // Update sidebar active state
                    document.querySelectorAll('.forum-user-item').forEach(el => {
                        el.classList.toggle('active', el.getAttribute('data-email') === email);
                    });

                    // Show chat UI
                    document.getElementById('emptyState').style.display = 'none';
                    document.getElementById('chatHeader').style.display = 'flex';
                    document.getElementById('messagesContainer').style.display = 'flex';
                    document.getElementById('inputBar').style.display = 'flex';

                    // Set header info
                    document.getElementById('chatAvatar').textContent = email.charAt(0).toUpperCase();
                    document.getElementById('chatWith').textContent = email.split('@')[0];

                    // Load messages
                    loadMessages();

                    // Start polling
                    if (pollInterval) clearInterval(pollInterval);
                    pollInterval = setInterval(loadMessages, 3000);

                    // Focus input
                    document.getElementById('messageInput').focus();
                }

                function loadMessages() {
                    if (!activeChat) return;
                    fetch('forum-api?action=messages&with=' + encodeURIComponent(activeChat))
                        .then(res => res.json())
                        .then(messages => {
                            const container = document.getElementById('messagesContainer');
                            const wasAtBottom = container.scrollTop + container.clientHeight >= container.scrollHeight - 30;

                            container.innerHTML = '';

                            if (messages.length === 0) {
                                container.innerHTML = '<div class="forum-no-messages">No messages yet. Say hello! 👋</div>';
                                return;
                            }

                            messages.forEach(msg => {
                                const bubble = document.createElement('div');
                                const isSent = msg.sender === currentUser;
                                bubble.className = 'forum-bubble ' + (isSent ? 'forum-bubble-sent' : 'forum-bubble-received');

                                const time = new Date(msg.sentAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                                bubble.innerHTML =
                                    '<div class="forum-bubble-content">' + escapeHtml(msg.content) + '</div>' +
                                    '<div class="forum-bubble-time">' + time + '</div>';
                                container.appendChild(bubble);
                            });

                            // Auto-scroll if user was near bottom
                            if (wasAtBottom) {
                                container.scrollTop = container.scrollHeight;
                            }
                        })
                        .catch(err => console.error('Error loading messages:', err));
                }

                function sendMessage() {
                    const input = document.getElementById('messageInput');
                    const content = input.value.trim();
                    if (!content || !activeChat) return;

                    input.value = '';

                    const params = new URLSearchParams();
                    params.append('action', 'send');
                    params.append('receiver', activeChat);
                    params.append('content', content);

                    fetch('forum-api?action=send', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: params.toString()
                    })
                        .then(res => res.json())
                        .then(data => {
                            if (data.success) {
                                loadMessages();
                            }
                        })
                        .catch(err => console.error('Error sending message:', err));
                }

                // Send on button click
                document.getElementById('sendBtn').addEventListener('click', sendMessage);

                // Send on Enter key
                document.getElementById('messageInput').addEventListener('keydown', function (e) {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        sendMessage();
                    }
                });

                function escapeHtml(text) {
                    const div = document.createElement('div');
                    div.textContent = text;
                    return div.innerHTML;
                }
            </script>
        </body>

        </html>