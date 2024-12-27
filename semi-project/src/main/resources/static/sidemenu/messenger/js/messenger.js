var stompClient = null;
var currentUser = null;
var selectedUser = null;
var employees = []; // 직원 목록을 저장할 배열

// 로그인한 사용자 정보 가져오기
function getCurrentUser(callback) {
    fetch('/user/info')  // 서버에서 로그인한 사용자 정보를 가져오는 API 호출
        .then(response => response.json())  // JSON 응답을 파싱
        .then(userInfo => {
            currentUser = userInfo.code;  // 'userInfo.code'를 사용하여 currentUser 설정
            console.log('Logged in user:', currentUser);  // currentUser 값을 확인
            callback();  // currentUser가 설정된 후 callback 호출
        })
        .catch(error => {
            console.error('Error fetching user info:', error);
        });
}

// 연결 설정 및 구독
function connect() {
    getCurrentUser(function() {
        if (currentUser !== null) {
            console.log('Connecting to WebSocket with currentUser:', currentUser);
            var socket = new SockJS('/chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/messages', function (message) {
                    var chat = JSON.parse(message.body);
                    if ((chat.senderCode === currentUser && chat.receiverCode === selectedUser) ||
                        (chat.senderCode === selectedUser && chat.receiverCode === currentUser)) {
                        displayMessage(chat);
                    }
                });
                loadUsers();  // 사용자 목록 로드
            });
        } else {
            console.error("currentUser is not set yet!");
        }
    });
}

// 페이지 로드 시 사용자 목록 로드
window.onload = function() {
    loadUsers(); // 페이지 로드 시 사용자 목록 로드
};

// 사용자 목록 로드
function loadUsers() {
    fetch('/employees') // 사용자 목록을 가져오는 API 호출
        .then(response => response.json())
        .then(users => {
            employees = users; // 직원 목록 저장
            displayUsers(users); // 사용자 목록 표시

            // 필터링 이벤트 리스너 추가
            document.getElementById('nameSearch').addEventListener('input', filterUsers);
            document.getElementById('departmentFilter').addEventListener('change', filterUsers);
            document.getElementById('positionFilter').addEventListener('change', filterUsers);
        })
        .catch(error => console.error('Error fetching users:', error));
}

// 사용자 목록 표시
function displayUsers(users) {
    var userList = document.getElementById('userList');
    userList.innerHTML = ''; // 이전 목록 초기화
    var today = new Date().toISOString().split('T')[0]; // 오늘 날짜 (YYYY-MM-DD 형식)

    users.forEach(function(user) {
        var li = document.createElement('li');
        li.className = 'user-item'; // 리스트 항목에 클래스 추가

        // 프로필 이미지 추가
        var img = document.createElement('img');
        img.src = user.profile_image && user.profile_image.startsWith('/img/profile/')
            ? user.profile_image
            : '/img/profile/' + (user.profile_image || 'default.png'); // 경로 붙이기, 기본 이미지 사용
        img.alt = user.empName + '의 프로필 이미지';
        img.className = 'profile-image'; // CSS 클래스 추가 (스타일링을 위해)

        // 사용자 정보 추가
        var userInfo = document.createElement('div');
        userInfo.className = 'user-info'; // CSS 클래스 추가 (스타일링을 위해)
        userInfo.innerHTML = `
            <div class="department">${user.deptDTO.deptName}</div>
            <div class="name-job">
                <strong>${user.empName}</strong> - ${user.jobDTO.jobName}
            </div>
        `;

        // 출근 및 퇴근 상태 표시
        var statusIndicator = document.createElement('div');
        statusIndicator.className = 'status-indicator'; // CSS 클래스 추가 (스타일링을 위해)

        // 출근 및 퇴근 시간 확인
        if (user.scheduleDTO.workEndTime) {
            // 퇴근 시간이 있는 경우
            statusIndicator.innerHTML = `<span class="red-circle"></span> 퇴근`; // 빨간색 동그라미
        } else if (user.scheduleDTO.workStartTime && user.scheduleDTO.workStartTime.startsWith(today)) {
            // 출근 시간이 오늘인 경우
            statusIndicator.innerHTML = `<span class="green-circle"></span> 업무중`;
        } else {
            // 출근 전 상태
            statusIndicator.innerHTML = `<span class="gray-circle"></span> 출근 전`; // 출근 전 상태
        }

        // 클릭 이벤트 추가
        li.onclick = function() {
            console.log('Selected user:', user.empCode); // 선택된 사원 코드 출력
            selectUser(user.empCode); // 사원 코드로 사용자 선택
        };

        // 리스트 항목에 이미지, 사용자 정보 및 상태 표시 추가
        li.appendChild(img);
        li.appendChild(userInfo);
        li.appendChild(statusIndicator);
        userList.appendChild(li);
    });
}

// 필터링 및 검색 기능
function filterUsers() {
    var department = document.getElementById('departmentFilter').value;
    var position = document.getElementById('positionFilter').value;
    var name = document.getElementById('nameSearch').value.toLowerCase();

    var filteredUsers = employees.filter(function(user) {
        var matchesDepartment = department ? user.deptDTO.deptName.toLowerCase() === department.toLowerCase() : true;
        var matchesPosition = position ? user.jobDTO.jobName.toLowerCase() === position.toLowerCase() : true;
        var matchesName = user.empName.toLowerCase().includes(name);
        return matchesDepartment && matchesPosition && matchesName;
    });

    displayUsers(filteredUsers);
}

// 실시간 필터링을 위한 이벤트 리스너 추가
document.getElementById('nameSearch').addEventListener('input', filterUsers);
document.getElementById('departmentFilter').addEventListener('change', filterUsers);
document.getElementById('positionFilter').addEventListener('change', filterUsers);

// 페이지 로드 시 사용자 목록 로드
window.onload = function() {
    loadUsers(); // 페이지 로드 시 사용자 목록 로드
};


// 사용자 선택 시 처리
function selectUser(empCode) {
    console.log('Before selecting user, currentUser:', currentUser); // currentUser 값 확인
    selectedUser = empCode; // 선택된 사용자의 사원 코드 저장
    document.getElementById('chatWith').innerText = '채팅 상대: ' + empCode; // UI 업데이트
    console.log('Selected user:', selectedUser); // selectedUser 값 확인
    console.log('After selecting user, currentUser:', currentUser); // currentUser가 변하지 않았는지 확인
    loadChatHistory(); // 채팅 기록 로드
}

function sendMessage() {
    var messageInput = document.getElementById('messageInput');
    var message = messageInput.value.trim();
    if (message && selectedUser) {
        var chatMessage = {
            senderCode: currentUser, // currentUser가 올바른 값인지 확인
            receiverCode: selectedUser,
            message: message
        };
        stompClient.send("/app/send", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    } else {
        console.error("Message or selected user is missing");
    }
}

// 채팅 히스토리 로드
function loadChatHistory() {
    var chatHistory = document.getElementById('chatHistory');
    chatHistory.innerHTML = '';
    if (selectedUser) {
        fetch('/chat/history/' + currentUser + '/' + selectedUser)
            .then(response => response.json())
            .then(data => {
                data.forEach(function(chat) {
                    displayMessage(chat);
                });
            });
    }
}

// 메시지 표시
function displayMessage(chat) {
    var chatHistory = document.getElementById('chatHistory');
    var messageDiv = document.createElement('div');
    messageDiv.className = 'chat-message';

    // 프로필 이미지와 메시지 정보를 포함한 HTML 구조
    const profileImagePath = chat.employeeJoinListDTO.profile_image
        ? (chat.employeeJoinListDTO.profile_image.startsWith('/img/profile/')
            ? chat.employeeJoinListDTO.profile_image
            : '/img/profile/' + chat.employeeJoinListDTO.profile_image)
        : '/img/profile/default-profile.png';

    messageDiv.innerHTML = `
        <div class="message-container">
            <div class="profile-image">
                <img src="${profileImagePath}" alt="${chat.employeeJoinListDTO.empName}" />
            </div>
            <div class="message-details">
                <div class="header">
                    <span class="department">${chat.employeeJoinListDTO.deptDTO.deptName}</span>
                </div>
                <div class="name-job">
                    <span class="name">${chat.employeeJoinListDTO.empName}</span>
                    <span class="job">${chat.employeeJoinListDTO.jobDTO.jobName}</span>
                </div>
                <div class="message-text">
                    <span class="sender">${chat.senderCode}:</span> ${chat.message}
                </div>
                <div class="timestamp">${new Date(chat.timestamp).toLocaleString()}</div>
            </div>
        </div>
    `;

    chatHistory.appendChild(messageDiv);
    chatHistory.scrollTop = chatHistory.scrollHeight;
}



