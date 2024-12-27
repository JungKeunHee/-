document.addEventListener('DOMContentLoaded', function() {
    // 출근 및 퇴근 시간과 연차/휴가 데이터를 가져오는 함수
    function fetchScheduleAndLeaveData() {
        // 출근 시간을 가져오는 API 호출
        fetch('/sidemenu/schedule/checkin', {
            method: 'GET',
            credentials: 'include' // 인증된 사용자 정보를 포함하기 위해 필요할 수 있음
        })
            .then(response => {
                if (!response.ok) {
                    // HTTP 오류 처리
                    if (response.status === 401) {
                        throw new Error("인증되지 않은 사용자입니다.");
                    } else if (response.status === 404) {
                        throw new Error("출근 전");
                    } else {
                        throw new Error("출근 전");
                    }
                }
                return response.json(); // JSON 응답을 파싱
            })
            .then(data => {
                // 출근 시간을 span에 표시
                if (data && data.checkInTime) {
                    // 출근 시간을 Date 객체로 변환
                    const checkInTime = new Date(data.checkInTime);
                    // 시간 부분만 포맷팅
                    const formattedTime = checkInTime.toTimeString().split(' ')[0]; // "HH:MM:SS" 형식으로 변환
                    document.getElementById('check-in-time').textContent = formattedTime;
                } else {
                    document.getElementById('check-in-time').textContent = "출근 전";
                }
            })
            .catch(error => {
                // 오류 메시지 표시
                document.getElementById('check-in-time').textContent = error.message;
            });

        // 퇴근 시간을 가져오는 API 호출
        fetch('/sidemenu/schedule/checkout', {
            method: 'GET',
            credentials: 'include' // 인증된 사용자 정보를 포함하기 위해 필요할 수 있음
        })
            .then(response => {
                if (!response.ok) {
                    // HTTP 오류 처리
                    if (response.status === 401) {
                        throw new Error("인증되지 않은 사용자입니다.");
                    } else if (response.status === 404) {
                        throw new Error("퇴근 전");
                    } else {
                        throw new Error("퇴근 전");
                    }
                }
                return response.json(); // JSON 응답을 파싱
            })
            .then(data => {
                // 퇴근 시간을 span에 표시
                if (data && data.checkOutTime) {
                    // 퇴근 시간을 Date 객체로 변환
                    const checkOutTime = new Date(data.checkOutTime);
                    // 시간 부분만 포맷팅
                    const formattedTime = checkOutTime.toTimeString().split(' ')[0]; // "HH:MM:SS" 형식으로 변환
                    document.getElementById('check-out-time').textContent = formattedTime;
                } else {
                    document.getElementById('check-out-time').textContent = "퇴근 전";
                }
            })
            .catch(error => {
                // 오류 메시지 표시
                document.getElementById('check-out-time').textContent = error.message;
            });

        // 연차 및 휴가 데이터를 가져오는 API 호출
        fetch('/sidemenu/schedule/vacation') // API URL을 여기에 입력
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                document.getElementById('annualLeave').textContent = data.annualLeave; // 연차 데이터 표시
                document.getElementById('vacation').textContent = data.vacation; // 휴가 데이터 표시
            })
            .catch(error => {
                console.error('Fetch error:', error);
                document.getElementById('annualLeave').textContent = "연차 데이터를 가져오는 데 실패했습니다.";
                document.getElementById('vacation').textContent = "휴가 데이터를 가져오는 데 실패했습니다.";
            });
    }

    // 페이지 로드 시 데이터 가져오기
    fetchScheduleAndLeaveData();
});
