<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% String email=(String) session.getAttribute("user"); String loginTime=(String) session.getAttribute("loginTime");
        if (email==null || email.isEmpty()) { response.sendRedirect("index.jsp?error=session"); return; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>My Profile - Mental Wellness</title>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
                rel="stylesheet">
            <link rel="stylesheet" href="sample.css">
        </head>

        <body>

            <div class="prof-page">
                <!-- Navbar -->
                <nav class="dash-navbar">
                    <div class="dash-navbar-left">
                        <i class="fa-solid fa-spa dash-navbar-icon"></i>
                        <span class="dash-navbar-title">My Profile</span>
                    </div>
                    <div class="dash-navbar-right">
                        <a href="welcome.jsp" class="prof-nav-link"><i class="fa-solid fa-home"></i> Home</a>
                        <a href="dashboard.jsp" class="prof-nav-link"><i class="fa-solid fa-chart-line"></i>
                            Dashboard</a>
                        <a href="logout" class="prof-nav-link prof-nav-danger"><i
                                class="fa-solid fa-right-from-bracket"></i> Logout</a>
                    </div>
                </nav>

                <div class="prof-content">
                    <!-- Avatar Section -->
                    <div class="prof-card prof-avatar-section">
                        <div class="prof-avatar-display" id="currentAvatar">
                            <span class="prof-avatar-emoji" id="avatarEmoji">😊</span>
                        </div>
                        <h3 class="prof-user-name">
                            <%= email %>
                        </h3>
                        <p class="prof-subtitle">Choose your avatar</p>
                        <div class="prof-avatar-grid">
                            <button class="prof-avatar-option" data-avatar="😊" title="Smile">😊</button>
                            <button class="prof-avatar-option" data-avatar="🧘" title="Meditator">🧘</button>
                            <button class="prof-avatar-option" data-avatar="🌸" title="Blossom">🌸</button>
                            <button class="prof-avatar-option" data-avatar="🦋" title="Butterfly">🦋</button>
                            <button class="prof-avatar-option" data-avatar="🌿" title="Nature">🌿</button>
                            <button class="prof-avatar-option" data-avatar="🌙" title="Moon">🌙</button>
                            <button class="prof-avatar-option" data-avatar="✨" title="Sparkle">✨</button>
                        </div>
                    </div>

                    <!-- Session & Stats Row -->
                    <div class="prof-grid">
                        <!-- Session Info -->
                        <div class="prof-card">
                            <div class="dash-card-header">
                                <i class="fa-solid fa-id-badge"></i>
                                <h3>Session Info</h3>
                            </div>
                            <div class="prof-info-row">
                                <span class="prof-info-label"><i class="fa-solid fa-envelope"></i> Email</span>
                                <span class="prof-info-value">
                                    <%= email %>
                                </span>
                            </div>
                            <div class="prof-info-row">
                                <span class="prof-info-label"><i class="fa-solid fa-clock"></i> Session Started</span>
                                <span class="prof-info-value">
                                    <%= loginTime !=null ? loginTime : "N/A" %>
                                </span>
                            </div>
                            <div class="prof-info-row">
                                <span class="prof-info-label"><i class="fa-solid fa-fingerprint"></i> Session ID</span>
                                <span class="prof-info-value prof-session-id">
                                    <%= session.getId() %>
                                </span>
                            </div>
                        </div>

                        <!-- Wellness Stats -->
                        <div class="prof-card">
                            <div class="dash-card-header">
                                <i class="fa-solid fa-heart-pulse"></i>
                                <h3>Wellness Stats</h3>
                            </div>
                            <div class="prof-stats-grid">
                                <div class="prof-stat-box">
                                    <i class="fa-solid fa-brain" style="color: var(--primary-lavender);"></i>
                                    <span class="prof-stat-number" id="totalMedHours">--</span>
                                    <span class="prof-stat-label">Meditation Hours</span>
                                </div>
                                <div class="prof-stat-box">
                                    <i class="fa-solid fa-dumbbell" style="color: var(--primary-mint);"></i>
                                    <span class="prof-stat-number" id="totalExHours">--</span>
                                    <span class="prof-stat-label">Exercise Hours</span>
                                </div>
                                <div class="prof-stat-box">
                                    <i class="fa-solid fa-calendar-check" style="color: #f59e0b;"></i>
                                    <span class="prof-stat-number" id="totalMedDays">--</span>
                                    <span class="prof-stat-label">Meditation Days</span>
                                </div>
                                <div class="prof-stat-box">
                                    <i class="fa-solid fa-fire" style="color: #ef4444;"></i>
                                    <span class="prof-stat-number" id="totalExDays">--</span>
                                    <span class="prof-stat-label">Exercise Days</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- AI Wellness Score Table -->
                    <div class="prof-card">
                        <div class="dash-card-header">
                            <i class="fa-solid fa-robot"></i>
                            <h3>Wellness Score Index</h3>
                        </div>
                        <p class="prof-ai-disclaimer">
                            <i class="fa-solid fa-circle-info"></i>
                            This is an AI model generated report based on your wellness data. Scores are indicative
                            only.
                        </p>
                        <div id="wellnessTableContainer">
                            <table class="prof-wellness-table">
                                <thead>
                                    <tr>
                                        <th>Category</th>
                                        <th>Score</th>
                                        <th>Status</th>
                                        <th>Remark</th>
                                    </tr>
                                </thead>
                                <tbody id="wellnessTableBody">
                                    <tr>
                                        <td colspan="4" class="prof-table-loading">
                                            <i class="fa-solid fa-spinner fa-spin"></i> Loading wellness data...
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <button class="dash-log-btn" id="refreshWellnessBtn" style="margin-top: 16px;">
                            <i class="fa-solid fa-wand-magic-sparkles"></i> Generate AI Wellness Score
                        </button>
                    </div>
                </div>
            </div>

            <!-- AI Wellness Popup Modal -->
            <div class="wellness-popup-overlay" id="wellnessPopup" style="display: none;">
                <div class="wellness-popup-card">
                    <button class="wellness-popup-close" id="closeWellnessPopup">&times;</button>

                    <!-- Header -->
                    <div class="wellness-popup-header">
                        <i class="fa-solid fa-heart-pulse"></i>
                        <h3>Your Wellness Assessment</h3>
                        <p class="wellness-popup-sub">AI-powered wellness analysis</p>
                    </div>

                    <!-- Circular Progress -->
                    <div class="wellness-progress-section">
                        <div class="wellness-ring-container">
                            <svg class="wellness-ring-svg" viewBox="0 0 120 120">
                                <defs>
                                    <linearGradient id="wellnessGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                                        <stop offset="0%" stop-color="#9b87f5" />
                                        <stop offset="100%" stop-color="#7dd3c0" />
                                    </linearGradient>
                                </defs>
                                <circle class="wellness-ring-bg" cx="60" cy="60" r="52" />
                                <circle class="wellness-ring-progress" id="wellnessRingProgress" cx="60" cy="60"
                                    r="52" />
                            </svg>
                            <div class="wellness-ring-text">
                                <span class="wellness-ring-score" id="popupScore">0</span>
                                <span class="wellness-ring-label">/ 10</span>
                            </div>
                        </div>
                        <span class="wellness-status-label" id="popupStatus">Analyzing...</span>
                    </div>

                    <!-- Loading state -->
                    <div class="wellness-popup-loading" id="popupLoading">
                        <i class="fa-solid fa-spinner fa-spin"
                            style="font-size: 1.5rem; color: var(--primary-lavender);"></i>
                        <p>Our AI is analyzing your wellness data...</p>
                        <p style="font-size: 0.72rem;">This may take 15-30 seconds</p>
                    </div>

                    <!-- Suggestions (hidden until loaded) -->
                    <div class="wellness-popup-suggestions" id="popupSuggestions" style="display: none;">
                        <h4><i class="fa-solid fa-lightbulb"></i> Suggestions to Improve</h4>
                        <ul id="suggestionsList"></ul>
                    </div>

                    <!-- AI Quote (hidden until loaded) -->
                    <div class="wellness-popup-quote" id="popupQuote" style="display: none;">
                        <i class="fa-solid fa-quote-left"></i>
                        <p id="quoteText"></p>
                    </div>
                </div>
            </div>

            <script>
                // ========== AVATAR PICKER ==========
                const avatarBtns = document.querySelectorAll('.prof-avatar-option');
                const avatarDisplay = document.getElementById('avatarEmoji');
                const savedAvatar = localStorage.getItem('userAvatar') || '😊';
                avatarDisplay.textContent = savedAvatar;

                // Highlight saved avatar
                avatarBtns.forEach(btn => {
                    if (btn.dataset.avatar === savedAvatar) btn.classList.add('active');
                    btn.addEventListener('click', function () {
                        avatarBtns.forEach(b => b.classList.remove('active'));
                        this.classList.add('active');
                        const avatar = this.dataset.avatar;
                        avatarDisplay.textContent = avatar;
                        localStorage.setItem('userAvatar', avatar);
                    });
                });

                // ========== LOAD STATS ==========
                fetch('dashboard-api?action=profile-stats')
                    .then(r => r.json())
                    .then(data => {
                        if (!data.error) {
                            document.getElementById('totalMedHours').textContent =
                                (data.meditationMinutes / 60).toFixed(1);
                            document.getElementById('totalExHours').textContent =
                                (data.exerciseMinutes / 60).toFixed(1);
                            document.getElementById('totalMedDays').textContent = data.meditationDays;
                            document.getElementById('totalExDays').textContent = data.exerciseDays;

                            // Build default wellness table from stats (no AI yet)
                            buildDefaultWellnessTable(data);
                        }
                    });

                function buildDefaultWellnessTable(data) {
                    const tbody = document.getElementById('wellnessTableBody');
                    const medHrs = data.meditationMinutes / 60;
                    const exHrs = data.exerciseMinutes / 60;
                    const medDays = data.meditationDays;
                    const exDays = data.exerciseDays;

                    const rows = [
                        {
                            cat: '🧘 Meditation',
                            score: Math.min(10, Math.round(medHrs * 2)) || 0,
                            getStatus: s => s >= 7 ? 'Excellent' : s >= 4 ? 'Good' : s >= 1 ? 'Needs Work' : 'Not Started',
                            remark: medHrs > 0 ? medHrs.toFixed(1) + ' hrs logged over ' + medDays + ' days' : 'No sessions yet'
                        },
                        {
                            cat: '💪 Exercise',
                            score: Math.min(10, Math.round(exHrs * 1.5)) || 0,
                            getStatus: s => s >= 7 ? 'Excellent' : s >= 4 ? 'Good' : s >= 1 ? 'Needs Work' : 'Not Started',
                            remark: exHrs > 0 ? exHrs.toFixed(1) + ' hrs logged over ' + exDays + ' days' : 'No sessions yet'
                        },
                        {
                            cat: '📅 Consistency',
                            score: Math.min(10, Math.round((medDays + exDays) * 0.8)) || 0,
                            getStatus: s => s >= 7 ? 'Excellent' : s >= 4 ? 'Good' : s >= 1 ? 'Fair' : 'Not Started',
                            remark: (medDays + exDays) + ' total active days'
                        },
                        {
                            cat: '🌟 Overall Wellness',
                            score: 0, // computed below
                            getStatus: s => s >= 8 ? 'Thriving' : s >= 6 ? 'Healthy' : s >= 3 ? 'Developing' : 'Getting Started',
                            remark: 'Click Generate for AI insight'
                        }
                    ];

                    // Compute overall as average of first 3
                    rows[3].score = Math.round((rows[0].score + rows[1].score + rows[2].score) / 3);

                    tbody.innerHTML = rows.map(r => {
                        const status = r.getStatus(r.score);
                        const color = r.score >= 7 ? '#10b981' : r.score >= 4 ? '#f59e0b' : r.score >= 1 ? '#ef4444' : '#9ca3af';
                        return '<tr>' +
                            '<td><strong>' + r.cat + '</strong></td>' +
                            '<td><span class="prof-score-badge" style="background:' + color + '22; color:' + color + ';">' + r.score + '/10</span></td>' +
                            '<td><span style="color:' + color + '; font-weight:500;">' + status + '</span></td>' +
                            '<td style="font-size:0.82rem; color:#6b7280;">' + r.remark + '</td>' +
                            '</tr>';
                    }).join('');
                }

                // ========== AI WELLNESS POPUP ==========
                let currentWellnessScore = 0;

                document.getElementById('refreshWellnessBtn').addEventListener('click', function () {
                    const btn = this;
                    const popup = document.getElementById('wellnessPopup');
                    const loading = document.getElementById('popupLoading');
                    const suggestions = document.getElementById('popupSuggestions');
                    const quote = document.getElementById('popupQuote');

                    // Reset popup state
                    popup.style.display = 'flex';
                    loading.style.display = 'flex';
                    suggestions.style.display = 'none';
                    quote.style.display = 'none';
                    document.getElementById('popupScore').textContent = '0';
                    document.getElementById('popupStatus').textContent = 'Analyzing...';

                    // Animate ring to 0
                    const ring = document.getElementById('wellnessRingProgress');
                    const circumference = 2 * Math.PI * 52;
                    ring.style.strokeDasharray = circumference;
                    ring.style.strokeDashoffset = circumference;

                    // Compute score from loaded stats
                    fetch('dashboard-api?action=profile-stats')
                        .then(r => r.json())
                        .then(stats => {
                            const medScore = Math.min(10, Math.round((stats.meditationMinutes / 60) * 2)) || 0;
                            const exScore = Math.min(10, Math.round((stats.exerciseMinutes / 60) * 1.5)) || 0;
                            const conScore = Math.min(10, Math.round((stats.meditationDays + stats.exerciseDays) * 0.8)) || 0;
                            currentWellnessScore = Math.round((medScore + exScore + conScore) / 3);

                            // Animate score ring
                            const offset = circumference - (currentWellnessScore / 10) * circumference;
                            setTimeout(() => {
                                ring.style.strokeDashoffset = offset;
                                animateCounter('popupScore', 0, currentWellnessScore, 800);
                                const statusLabel = currentWellnessScore >= 8 ? 'Thriving 🌟' :
                                    currentWellnessScore >= 6 ? 'Healthy 💚' :
                                        currentWellnessScore >= 3 ? 'Developing 🌱' : 'Getting Started 🌸';
                                document.getElementById('popupStatus').textContent = statusLabel;
                                document.getElementById('popupStatus').style.color =
                                    currentWellnessScore >= 7 ? '#10b981' : currentWellnessScore >= 4 ? '#f59e0b' : '#ef4444';
                            }, 300);

                            // Also update the table's overall row
                            const overallRow = document.getElementById('wellnessTableBody').lastElementChild;
                            if (overallRow) {
                                const cells = overallRow.querySelectorAll('td');
                                if (cells.length >= 2) {
                                    const color = currentWellnessScore >= 7 ? '#10b981' : currentWellnessScore >= 4 ? '#f59e0b' : '#ef4444';
                                    cells[1].innerHTML = '<span class="prof-score-badge" style="background:' + color + '22; color:' + color + ';">' + currentWellnessScore + '/10</span>';
                                }
                            }
                        });

                    // Fetch AI report for suggestions and quote
                    fetch('report-api')
                        .then(r => r.json())
                        .then(data => {
                            loading.style.display = 'none';
                            if (data.report) {
                                // Parse suggestions from AI text
                                const lines = data.report.split('\n').filter(l => l.trim());
                                const suggestionItems = [];
                                let quoteCandidate = '';

                                lines.forEach(line => {
                                    const trimmed = line.trim();
                                    if (trimmed.startsWith('-') || trimmed.startsWith('•') || trimmed.match(/^\d+\./)) {
                                        suggestionItems.push(trimmed.replace(/^[-•\d.]+\s*/, ''));
                                    }
                                });

                                // If no bullet points found, extract sentences as suggestions
                                if (suggestionItems.length === 0) {
                                    const sentences = data.report.split('.').filter(s => s.trim().length > 15);
                                    for (let i = 0; i < Math.min(4, sentences.length); i++) {
                                        suggestionItems.push(sentences[i].trim() + '.');
                                    }
                                }

                                // Build suggestions list
                                const list = document.getElementById('suggestionsList');
                                list.innerHTML = suggestionItems.slice(0, 5).map(s =>
                                    '<li><i class="fa-solid fa-check-circle"></i> ' + s + '</li>'
                                ).join('');
                                suggestions.style.display = 'block';
                                suggestions.style.opacity = '0';
                                setTimeout(() => { suggestions.style.opacity = '1'; }, 100);

                                // Extract last meaningful line as quote, or generate one
                                const lastLines = lines.slice(-3);
                                for (let i = lastLines.length - 1; i >= 0; i--) {
                                    if (lastLines[i].length > 20 && !lastLines[i].startsWith('-')) {
                                        quoteCandidate = lastLines[i].trim();
                                        break;
                                    }
                                }
                                if (!quoteCandidate) {
                                    quoteCandidate = 'Every step you take toward wellness is a step toward a brighter tomorrow.';
                                }

                                document.getElementById('quoteText').textContent = quoteCandidate;
                                quote.style.display = 'block';
                                quote.style.opacity = '0';
                                setTimeout(() => { quote.style.opacity = '1'; }, 400);

                                // Update table overall remark
                                const overallRow = document.getElementById('wellnessTableBody').lastElementChild;
                                if (overallRow) {
                                    const cells = overallRow.querySelectorAll('td');
                                    if (cells.length >= 4) {
                                        const remark = data.report.split('.')[0] + '.';
                                        cells[3].textContent = remark.length > 100 ? remark.substring(0, 100) + '...' : remark;
                                        cells[3].style.fontStyle = 'italic';
                                    }
                                }
                            } else if (data.error) {
                                document.getElementById('suggestionsList').innerHTML =
                                    '<li style="color:#ef4444;"><i class="fa-solid fa-circle-exclamation"></i> ' + data.error + '</li>';
                                suggestions.style.display = 'block';
                            }
                        })
                        .catch(() => {
                            loading.style.display = 'none';
                            document.getElementById('suggestionsList').innerHTML =
                                '<li style="color:#ef4444;"><i class="fa-solid fa-circle-exclamation"></i> Could not connect to Ollama. Is it running?</li>';
                            suggestions.style.display = 'block';
                        });
                });

                // Counter animation
                function animateCounter(elementId, from, to, duration) {
                    const el = document.getElementById(elementId);
                    const start = performance.now();
                    function step(now) {
                        const progress = Math.min((now - start) / duration, 1);
                        const value = Math.round(from + (to - from) * progress);
                        el.textContent = value;
                        if (progress < 1) requestAnimationFrame(step);
                    }
                    requestAnimationFrame(step);
                }

                // Close popup
                document.getElementById('closeWellnessPopup').addEventListener('click', function () {
                    document.getElementById('wellnessPopup').style.display = 'none';
                });
                document.getElementById('wellnessPopup').addEventListener('click', function (e) {
                    if (e.target === this) this.style.display = 'none';
                });
            </script>
        </body>

        </html>