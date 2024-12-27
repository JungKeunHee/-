document.addEventListener('DOMContentLoaded', () => {
    fetchUserInfo();
});

function fetchUserInfo() {
    fetch('/user/info')
        .then(response => response.json())
        .then(data => {
            const profilePictureElement = document.getElementById('profile-picture');
            const basePath = '/img/profile/'; // 기본 경로 설정

            console.log(data.profilePictureUrl)

            // 프로필 사진 URL을 설정할 때 캐시 무효화를 위해 현재 시간을 쿼리 파라미터로 추가
            let profilePictureUrl = data.profilePictureUrl;
            if (!profilePictureUrl.startsWith(basePath)) {
                profilePictureUrl = basePath + profilePictureUrl;
            }

            // 캐시 무효화를 위해 현재 시간을 쿼리 파라미터로 추가
            profilePictureElement.src = `${profilePictureUrl}?t=${new Date().getTime()}`;

            document.getElementById('name').innerText = `😊${data.name}님 오늘 하루도 힘내세요!😊`; // 이름과 환영 메시지 설정

            // userName 변수를 data.name으로 설정
            const userName = data.name;
            document.getElementById('user-name').textContent = userName; // 두 번째 이름

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
            const departmentName = departmentMap[data.department] || '알 수 없는 부서'; // 기본값 설정
            document.getElementById('department').innerText = `${departmentName}`; // 부서명 설정

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
            const positionName = positionMap[data.position] || '알 수 없는 직급'; // 기본값 설정
            document.getElementById('position').innerText = positionName; // 직급 설정
        })
        .catch(error => console.error('Error fetching user info:', error));
}

function confirmLogout() {
    const confirmation = confirm("로그아웃 하시겠습니까?");
    if (confirmation) {
        document.getElementById("logout-form").submit(); // 확인 시 폼 제출
    }
    return false; // 기본 폼 제출 방지
}

let checkInTime = null;
let timerInterval = null;
let elapsedSeconds = 0;
let lastCheckTime = null; // 마지막 체크인 시간을 저장할 변수

// 페이지 로드 시 경과된 시간 복원
window.onload = function() {
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
        startTimer();
        document.getElementById('message').innerText = "퇴근 버튼을 눌러주세요👇";
        document.getElementById('message').classList.remove('checkin-message');
        document.getElementById('message').classList.add('checkout-message');
    }

    // 페이지가 로드될 때 경과 시간 계산
    calculateElapsedTime();
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
    const checkInStatus = localStorage.getItem('checkInStatus');

    // 출근 상태일 때만 경과 시간 계산
    if (storedLastCheckTime && checkInStatus === 'true') {
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


function checkIn() {
    // 이미 출근 처리가 되었는지 확인
    if (checkInTime) {
        alert("이미 출근 처리가 되었습니다."); // 알림창 표시
        return; // 함수 종료
    }

    const now = new Date();
    const date = formatDate(now); // YYYY-MM-DD 형식
    const time = formatTime(now); // HH:MM:SS 형식

    checkInTime = time;
    alert(`출근 처리되었습니다. \n현재 시간: ${time}`); // 알림창에 현재 시간 표시

    // 타이머 시작
    elapsedSeconds = 0; // 초기화
    localStorage.setItem('checkInStatus', 'true'); // 출근 상태 저장
    localStorage.setItem('checkInTime', checkInTime); // 출근 시간 저장
    startTimer();

    // UI 업데이트
    document.getElementById('message').innerText = "퇴근 버튼을 눌러주세요👇"; // 메시지 업데이트
    document.getElementById('message').classList.remove('checkin-message'); // 초록색 클래스 제거
    document.getElementById('message').classList.add('checkout-message'); // 빨간색 클래스 추가

    // 출근 버튼 비활성화
    document.getElementById('checkInButton').disabled = true; // 버튼 ID에 맞게 수정

    // 서버에 출근 시간 전송
    sendCheckInData(date, time);

}

// 날짜를 YYYY-MM-DD 형식으로 포맷하는 함수
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

// 출근 시간 데이터를 서버에 전송하는 함수
function sendCheckInData(date, time) {
    const checkInData = {
        date: date,
        startTime: time
    };

    fetch('/sidemenu/schedule/checkin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(checkInData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('출근 처리에 실패했습니다.');
            }
            return response.text();
        })
        .then(data => {
            console.log(data); // 서버로부터의 응답 처리
        })
        .catch(error => {
            console.error('Error:', error);
            alert('출근 처리 중 오류가 발생했습니다.');
        });
}

function checkOut() {
    if (checkInTime) {
        // 퇴근 처리 확인 메시지
        const confirmCheckOut = confirm("퇴근 처리 하시겠습니까?");

        if (confirmCheckOut) {
            const now = new Date();
            const checkOutTime = formatTime(now); // HH:MM:SS 형식

            // 총 근무 시간 계산
            const totalHours = Math.floor(elapsedSeconds / 3600);
            const totalMinutes = Math.floor((elapsedSeconds % 3600) / 60);
            const totalSeconds = elapsedSeconds % 60;

            // 알림창에 퇴근 시간과 총 근무 시간 표시
            alert(`퇴근 처리되었습니다.\n현재 시간: ${checkOutTime}\n총 근무 시간: ${totalHours}시간 ${totalMinutes}분 ${totalSeconds}초`);

            // 서버에 퇴근 시간 전송
            const date = formatDate(now); // YYYY-MM-DD 형식
            sendCheckOutData(date, checkOutTime, totalHours, totalMinutes, totalSeconds); // 퇴근 시간 전송 함수 호출

            // 타이머 정지
            clearInterval(timerInterval);
            timerInterval = null;

            // 경과된 시간 초기화
            localStorage.removeItem('elapsedSeconds');
            localStorage.removeItem('checkInStatus');
            localStorage.removeItem('checkInTime'); // 출근 시간 초기화
            checkInTime = null; // 출근 시간 초기화
        }
    } else {
        alert("먼저 출근 버튼을 눌러주세요.");
    }
}

// 퇴근 시간 데이터를 서버에 전송하는 함수
function sendCheckOutData(date, time, totalHours, totalMinutes, totalSeconds) {
    const checkOutData = {
        date: date,
        endTime: time,
        totalHours: totalHours, // 총 근무 시간 (시간 단위)
        totalMinutes: totalMinutes, // 총 근무 시간 (분 단위)
        totalSeconds: totalSeconds // 총 근무 시간 (초 단위)
    };

    fetch('/sidemenu/schedule/checkout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(checkOutData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('퇴근 처리에 실패했습니다.');
            }
            return response.text();
        })
        .then(data => {
            console.log(data); // 서버로부터의 응답 처리
        })
        .catch(error => {
            console.error('Error:', error);
            alert('퇴근 처리 중 오류가 발생했습니다.');
        });
}

function formatTime(date) {
    const hours = String(date.getHours()).padStart(2, '0'); // 24시간 형식으로 변환
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${hours}:${minutes}:${seconds}`; // 24시간 형식으로 반환
}