async function fetchUserInfo() {
    try {
        // API 요청
        const response = await fetch('/user/info');

        // 응답이 성공적이지 않은 경우 에러 발생
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // JSON 형식으로 응답 데이터 파싱
        const userInfo = await response.json();

        console.log(userInfo);

        // 프로필 사진 URL 처리
        const basePath = '/img/profile/'; // 기본 경로 설정
        let profilePictureUrl = userInfo.profilePictureUrl;

        // 프로필 사진 URL이 기본 경로로 시작하지 않는 경우 기본 경로를 붙임
        if (!profilePictureUrl.startsWith(basePath)) {
            profilePictureUrl = basePath + profilePictureUrl;
        }

        // 프로필 이미지 설정
        document.getElementById('profile').src = profilePictureUrl;

        // 사용자 이름 설정
        document.getElementById('user-name').textContent = userInfo.name || '이름 없음';

        // 부서 코드와 부서명을 매핑하는 객체
        const departmentMap = {
            'B1': '경영부',
            'B2': '애견담당부',
            'B3': '마케팅부',
            'B4': '영업부',
            'B5': '물류부',
            'B6': '고객CS 업무부',
        };

        // 부서 코드에 따라 부서명을 설정
        const departmentName = departmentMap[userInfo.department] || '알 수 없는 부서'; // 기본값 설정
        document.getElementById('department').innerText = departmentName; // 부서명 설정

        // 직급 코드와 직급명을 매핑하는 객체
        const positionMap = {
            'J1': '대표',
            'J2': '부사장',
            'J3': '부장',
            'J4': '과장',
            'J5': '팀장',
            'J6': '대리',
            'J7': '주임',
            'J8': '사원'
            // 필요한 만큼 추가
        };

        // 직급 코드에 따라 직급명을 설정
        const positionName = positionMap[userInfo.position] || '알 수 없는 직급'; // 기본값 설정
        document.getElementById('position').innerText = positionName; // 직급 설정

    } catch (error) {
        console.error('Error fetching user info:', error);
    }
}


let checkInTime = null;
let timerInterval = null;
let elapsedSeconds = 0;
let lastCheckTime = null; // 마지막 체크인 시간을 저장할 변수

// 페이지가 로드될 때 사용자 정보 가져오기 및 경과 시간 복원
window.onload = async function() {
    // 사용자 정보 가져오기
    await fetchUserInfo();

    // 경과된 시간 복원
    const storedTime = localStorage.getItem('elapsedSeconds');
    if (storedTime) {
        elapsedSeconds = parseInt(storedTime, 10);
        updateTimeDisplay();
    }

    // 출근 시간 복원
    checkInTime = localStorage.getItem('checkInTime');

    // 타이머가 이미 실행 중인지 확인
    const checkInStatus = localStorage.getItem('checkInStatus');
    if (checkInStatus === 'true') {
        // 페이지가 로드될 때 경과 시간 계산
        calculateElapsedTime();
        startTimer();
        document.getElementById('message').innerText = "퇴근 버튼을 눌러주세요👇";
        document.getElementById('message').classList.remove('checkin-message');
        document.getElementById('message').classList.add('checkout-message');
    }
};

// 페이지를 떠날 때 현재 시간을 저장
window.onbeforeunload = function() {
    if (checkInTime) {
        localStorage.setItem('elapsedSeconds', elapsedSeconds); // 경과 시간 저장
        localStorage.setItem('checkInStatus', 'true'); // 출근 상태 저장
        localStorage.setItem('checkInTime', checkInTime); // 출근 시간 저장
        lastCheckTime = Date.now(); // 현재 시간을 저장
        localStorage.setItem('lastCheckTime', lastCheckTime); // 마지막 체크인 시간 저장
    }
};

// 페이지가 로드될 때 경과 시간 계산
function calculateElapsedTime() {
    const storedLastCheckTime = localStorage.getItem('lastCheckTime');
    if (storedLastCheckTime) {
        const currentTime = Date.now();
        const timeDifference = Math.floor((currentTime - parseInt(storedLastCheckTime, 10)) / 1000);
        elapsedSeconds += timeDifference; // 경과 시간에 시간 차이 추가
        localStorage.setItem('elapsedSeconds', elapsedSeconds); // 업데이트된 경과 시간 저장
    }
}

function startTimer() {
    if (timerInterval) {
        clearInterval(timerInterval); // 기존 타이머가 있으면 정지
    }
    timerInterval = setInterval(() => {
        elapsedSeconds++; // 초 증가
        updateTimeDisplay(); // 시간 표시 업데이트
    }, 1000); // 1초마다 호출
}

function updateTimeDisplay() {
    const hours = String(Math.floor(elapsedSeconds / 3600)).padStart(2, '0');
    const minutes = String(Math.floor((elapsedSeconds % 3600) / 60)).padStart(2, '0');
    const seconds = String(elapsedSeconds % 60).padStart(2, '0');
    document.getElementById('time-display').textContent = `${hours}:${minutes}:${seconds}`;
}




