<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <% String email=(String) session.getAttribute("user"); if (email==null || email.isEmpty()) {
        response.sendRedirect("index.jsp?error=session"); return; } %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard - Mental Wellness</title>
            <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
            <link rel="stylesheet" href="sample.css">
            <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
        </head>

        <body>
            <div class="dash-page">
                <!-- Navbar -->
                <div class="dash-navbar">
                    <div class="dash-navbar-left">
                        <i class="fa-solid fa-chart-line dash-navbar-icon"></i>
                        <span class="dash-navbar-title">Wellness Dashboard</span>
                    </div>
                    <div class="dash-navbar-right">
                        <span class="dash-navbar-user">
                            <%= email %>
                        </span>
                        <a href="welcome.jsp" class="forum-nav-btn"><i class="fa-solid fa-home"></i> Home</a>
                        <a href="forum.jsp" class="forum-nav-btn"><i class="fa-solid fa-comments"></i> Forum</a>
                        <a href="logout.jsp" class="forum-nav-btn forum-nav-btn-danger"><i
                                class="fa-solid fa-right-from-bracket"></i></a>
                    </div>
                </div>

                <div class="dash-content">
                    <!-- Row 1: Meditation Timer + Meditation Chart -->
                    <div class="dash-grid">

                        <!-- Meditation Timer Card -->
                        <div class="dash-card dash-timer-card">
                            <div class="dash-card-header">
                                <i class="fa-solid fa-brain"></i>
                                <h3>Meditation Timer</h3>
                            </div>

                            <!-- Duration Picker -->
                            <div class="dash-duration-picker" id="durationPicker">
                                <p class="dash-picker-label">Choose duration</p>
                                <div class="dash-duration-btns">
                                    <button class="dash-dur-btn" data-min="5">5 min</button>
                                    <button class="dash-dur-btn active" data-min="10">10 min</button>
                                    <button class="dash-dur-btn" data-min="15">15 min</button>
                                    <button class="dash-dur-btn" data-min="20">20 min</button>
                                    <button class="dash-dur-btn" data-min="30">30 min</button>
                                </div>
                            </div>

                            <!-- Timer Display -->
                            <div class="dash-timer-display">
                                <div class="dash-timer-circle">
                                    <svg class="dash-timer-svg" viewBox="0 0 200 200">
                                        <circle class="dash-timer-bg" cx="100" cy="100" r="90" />
                                        <circle class="dash-timer-progress" id="timerProgress" cx="100" cy="100"
                                            r="90" />
                                    </svg>
                                    <div class="dash-timer-text">
                                        <span class="dash-timer-time" id="timerTime">10:00</span>
                                        <span class="dash-timer-status" id="timerStatus">Ready</span>
                                    </div>
                                </div>
                            </div>

                            <!-- Affirmation Text -->
                            <div class="dash-affirmation" id="affirmation">
                                <i class="fa-solid fa-quote-left"></i>
                                <span id="affirmationText">Take a deep breath and begin.</span>
                            </div>

                            <!-- Controls -->
                            <div class="dash-timer-controls">
                                <button class="dash-ctrl-btn dash-ctrl-reset" id="btnReset" title="Reset">
                                    <i class="fa-solid fa-rotate-left"></i>
                                </button>
                                <button class="dash-ctrl-btn dash-ctrl-play" id="btnPlay" title="Play">
                                    <i class="fa-solid fa-play" id="playIcon"></i>
                                </button>
                                <button class="dash-ctrl-btn dash-ctrl-stop" id="btnStop" title="Stop & Log"
                                    style="display:none;">
                                    <i class="fa-solid fa-stop"></i>
                                </button>
                            </div>
                        </div>

                        <!-- Meditation Chart Card -->
                        <div class="dash-card">
                            <div class="dash-card-header">
                                <i class="fa-solid fa-spa"></i>
                                <h3>Meditation Log</h3>
                            </div>
                            <div class="dash-chart-container">
                                <canvas id="meditationChart"></canvas>
                            </div>
                            <div class="dash-log-form">
                                <h5><i class="fa-solid fa-plus-circle"></i> Manual Entry</h5>
                                <div class="dash-form-row">
                                    <input type="date" id="medDate" class="dash-input">
                                    <input type="number" id="medMinutes" class="dash-input" placeholder="Minutes"
                                        min="1">
                                    <button class="dash-log-btn" id="logMedBtn">
                                        <i class="fa-solid fa-check"></i> Log
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Row 2: Exercise Chart -->
                    <div class="dash-grid dash-grid-single">
                        <div class="dash-card">
                            <div class="dash-card-header">
                                <i class="fa-solid fa-dumbbell"></i>
                                <h3>Exercise Tracker</h3>
                            </div>
                            <div class="dash-chart-container">
                                <canvas id="exerciseChart"></canvas>
                            </div>
                            <div class="dash-log-form">
                                <h5><i class="fa-solid fa-plus-circle"></i> Log Exercise</h5>
                                <div class="dash-form-row">
                                    <select id="exType" class="dash-input">
                                        <option value="Running">Running</option>
                                        <option value="Yoga">Yoga</option>
                                        <option value="Walking">Walking</option>
                                        <option value="Gym">Gym</option>
                                        <option value="Swimming">Swimming</option>
                                        <option value="Cycling">Cycling</option>
                                        <option value="Other">Other</option>
                                    </select>
                                    <input type="date" id="exDate" class="dash-input">
                                    <input type="number" id="exMinutes" class="dash-input" placeholder="Minutes"
                                        min="1">
                                    <button class="dash-log-btn" id="logExBtn">
                                        <i class="fa-solid fa-check"></i> Log
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                // ========== MEDITATION TIMER ==========
                const affirmations = [
                    "You are at peace with yourself.",
                    "Breathe in calm, breathe out tension.",
                    "This moment is exactly where you need to be.",
                    "You are worthy of rest and stillness.",
                    "Let go of what you cannot control.",
                    "Your mind is clear and focused.",
                    "You deserve this time for yourself.",
                    "Every breath brings you deeper peace.",
                    "You are strong, capable, and resilient.",
                    "Embrace the silence within you.",
                    "Happiness begins with a peaceful mind.",
                    "You are enough, just as you are.",
                    "Feel gratitude for this present moment.",
                    "Your inner calm is your superpower.",
                    "With each breath, you grow more relaxed."
                ];

                let totalSeconds = 600; // default 10 min
                let remainingSeconds = 600;
                let timerInterval = null;
                let affirmationInterval = null;
                let isRunning = false;

                const circumference = 2 * Math.PI * 90;
                const progressCircle = document.getElementById('timerProgress');
                progressCircle.style.strokeDasharray = circumference;
                progressCircle.style.strokeDashoffset = 0;

                // Duration picker
                document.querySelectorAll('.dash-dur-btn').forEach(btn => {
                    btn.addEventListener('click', function () {
                        if (isRunning) return;
                        document.querySelectorAll('.dash-dur-btn').forEach(b => b.classList.remove('active'));
                        this.classList.add('active');
                        totalSeconds = parseInt(this.dataset.min) * 60;
                        remainingSeconds = totalSeconds;
                        updateTimerDisplay();
                        progressCircle.style.strokeDashoffset = 0;
                        document.getElementById('timerStatus').textContent = 'Ready';
                    });
                });

                function updateTimerDisplay() {
                    const min = Math.floor(remainingSeconds / 60);
                    const sec = remainingSeconds % 60;
                    document.getElementById('timerTime').textContent =
                        String(min).padStart(2, '0') + ':' + String(sec).padStart(2, '0');
                }

                function rotateAffirmation() {
                    const text = affirmations[Math.floor(Math.random() * affirmations.length)];
                    const el = document.getElementById('affirmationText');
                    el.style.opacity = 0;
                    setTimeout(() => {
                        el.textContent = text;
                        el.style.opacity = 1;
                    }, 300);
                }

                // Play / Pause
                document.getElementById('btnPlay').addEventListener('click', function () {
                    if (isRunning) {
                        // Pause
                        clearInterval(timerInterval);
                        clearInterval(affirmationInterval);
                        isRunning = false;
                        document.getElementById('playIcon').className = 'fa-solid fa-play';
                        document.getElementById('timerStatus').textContent = 'Paused';
                    } else {
                        // Play
                        if (remainingSeconds <= 0) {
                            remainingSeconds = totalSeconds;
                            progressCircle.style.strokeDashoffset = 0;
                        }
                        isRunning = true;
                        document.getElementById('playIcon').className = 'fa-solid fa-pause';
                        document.getElementById('timerStatus').textContent = 'Meditating...';
                        document.getElementById('btnStop').style.display = 'flex';
                        rotateAffirmation();

                        timerInterval = setInterval(() => {
                            remainingSeconds--;
                            updateTimerDisplay();

                            // Update circle progress
                            const progress = 1 - (remainingSeconds / totalSeconds);
                            progressCircle.style.strokeDashoffset = circumference * progress;

                            if (remainingSeconds <= 0) {
                                clearInterval(timerInterval);
                                clearInterval(affirmationInterval);
                                isRunning = false;
                                document.getElementById('playIcon').className = 'fa-solid fa-play';
                                document.getElementById('timerStatus').textContent = 'Complete!';
                                document.getElementById('affirmationText').textContent = 'Well done! Your session has been logged.';
                                document.getElementById('btnStop').style.display = 'none';
                                // Auto-log the meditation
                                logMeditationFromTimer(totalSeconds / 60);
                            }
                        }, 1000);

                        affirmationInterval = setInterval(rotateAffirmation, 8000);
                    }
                });

                // Reset
                document.getElementById('btnReset').addEventListener('click', function () {
                    clearInterval(timerInterval);
                    clearInterval(affirmationInterval);
                    isRunning = false;
                    remainingSeconds = totalSeconds;
                    updateTimerDisplay();
                    progressCircle.style.strokeDashoffset = 0;
                    document.getElementById('playIcon').className = 'fa-solid fa-play';
                    document.getElementById('timerStatus').textContent = 'Ready';
                    document.getElementById('affirmationText').textContent = 'Take a deep breath and begin.';
                    document.getElementById('btnStop').style.display = 'none';
                });

                // Stop & Log (logs elapsed time)
                document.getElementById('btnStop').addEventListener('click', function () {
                    clearInterval(timerInterval);
                    clearInterval(affirmationInterval);
                    const elapsedMinutes = Math.round((totalSeconds - remainingSeconds) / 60);
                    isRunning = false;
                    document.getElementById('playIcon').className = 'fa-solid fa-play';
                    document.getElementById('timerStatus').textContent = 'Stopped';
                    document.getElementById('btnStop').style.display = 'none';
                    if (elapsedMinutes > 0) {
                        logMeditationFromTimer(elapsedMinutes);
                        document.getElementById('affirmationText').textContent =
                            'Logged ' + elapsedMinutes + ' minute(s). Great effort!';
                    }
                });

                function logMeditationFromTimer(minutes) {
                    const today = new Date().toISOString().split('T')[0];
                    const params = new URLSearchParams();
                    params.append('action', 'log-meditation');
                    params.append('minutes', minutes);
                    params.append('date', today);
                    fetch('dashboard-api?action=log-meditation', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: params.toString()
                    }).then(() => loadMeditationChart());
                }

                // ========== CHARTS ==========
                // Set today's date as default for date inputs
                const today = new Date().toISOString().split('T')[0];
                document.getElementById('medDate').value = today;
                document.getElementById('exDate').value = today;

                function getLast7Days() {
                    const days = [];
                    for (let i = 6; i >= 0; i--) {
                        const d = new Date();
                        d.setDate(d.getDate() - i);
                        days.push(d.toISOString().split('T')[0]);
                    }
                    return days;
                }

                function formatDateLabel(dateStr) {
                    const d = new Date(dateStr + 'T00:00:00');
                    return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
                }

                // Meditation Chart
                const medCtx = document.getElementById('meditationChart').getContext('2d');
                const meditationChart = new Chart(medCtx, {
                    type: 'line',
                    data: {
                        labels: getLast7Days().map(formatDateLabel),
                        datasets: [{
                            label: 'Minutes Meditated',
                            data: [0, 0, 0, 0, 0, 0, 0],
                            borderColor: '#9b87f5',
                            backgroundColor: 'rgba(155, 135, 245, 0.15)',
                            fill: true,
                            tension: 0.4,
                            borderWidth: 3,
                            pointBackgroundColor: '#9b87f5',
                            pointRadius: 5,
                            pointHoverRadius: 7
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: { legend: { display: false } },
                        scales: {
                            y: {
                                beginAtZero: true, title: { display: true, text: 'Minutes' },
                                grid: { color: 'rgba(0,0,0,0.05)' }
                            },
                            x: { grid: { display: false } }
                        }
                    }
                });

                // Exercise Chart
                const exCtx = document.getElementById('exerciseChart').getContext('2d');
                const exerciseChart = new Chart(exCtx, {
                    type: 'bar',
                    data: {
                        labels: getLast7Days().map(formatDateLabel),
                        datasets: [{
                            label: 'Exercise (min)',
                            data: [0, 0, 0, 0, 0, 0, 0],
                            backgroundColor: 'rgba(125, 211, 192, 0.5)',
                            borderColor: '#7dd3c0',
                            borderWidth: 2,
                            borderRadius: 8,
                            borderSkipped: false
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: { legend: { display: false } },
                        scales: {
                            y: {
                                beginAtZero: true, title: { display: true, text: 'Minutes' },
                                grid: { color: 'rgba(0,0,0,0.05)' }
                            },
                            x: { grid: { display: false } }
                        }
                    }
                });

                function loadMeditationChart() {
                    fetch('dashboard-api?action=meditation-stats')
                        .then(r => r.json())
                        .then(data => {
                            const days = getLast7Days();
                            const values = days.map(d => {
                                const entry = data.find(e => e.date === d);
                                return entry ? entry.minutes : 0;
                            });
                            meditationChart.data.datasets[0].data = values;
                            meditationChart.update();
                        });
                }

                function loadExerciseChart() {
                    fetch('dashboard-api?action=exercise-stats')
                        .then(r => r.json())
                        .then(data => {
                            const days = getLast7Days();
                            const values = days.map(d => {
                                const entry = data.find(e => e.date === d);
                                return entry ? entry.minutes : 0;
                            });
                            exerciseChart.data.datasets[0].data = values;
                            exerciseChart.update();
                        });
                }

                // Load charts on page load
                loadMeditationChart();
                loadExerciseChart();

                // ========== MANUAL LOG FORMS ==========
                document.getElementById('logMedBtn').addEventListener('click', function () {
                    const date = document.getElementById('medDate').value;
                    const minutes = document.getElementById('medMinutes').value;
                    if (!date || !minutes || minutes <= 0) { alert('Please enter a valid date and minutes.'); return; }

                    const params = new URLSearchParams();
                    params.append('action', 'log-meditation');
                    params.append('minutes', minutes);
                    params.append('date', date);
                    fetch('dashboard-api?action=log-meditation', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: params.toString()
                    }).then(r => r.json()).then(d => {
                        if (d.success) {
                            document.getElementById('medMinutes').value = '';
                            loadMeditationChart();
                        }
                    });
                });

                document.getElementById('logExBtn').addEventListener('click', function () {
                    const type = document.getElementById('exType').value;
                    const date = document.getElementById('exDate').value;
                    const minutes = document.getElementById('exMinutes').value;
                    if (!date || !minutes || minutes <= 0) { alert('Please enter a valid date and minutes.'); return; }

                    const params = new URLSearchParams();
                    params.append('action', 'log-exercise');
                    params.append('type', type);
                    params.append('minutes', minutes);
                    params.append('date', date);
                    fetch('dashboard-api?action=log-exercise', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                        body: params.toString()
                    }).then(r => r.json()).then(d => {
                        if (d.success) {
                            document.getElementById('exMinutes').value = '';
                            loadExerciseChart();
                        }
                    });
                });
            </script>
        </body>

        </html>