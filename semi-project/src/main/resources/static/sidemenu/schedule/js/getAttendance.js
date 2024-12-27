document.addEventListener('DOMContentLoaded', function() {
    // 페이지 로드 시 알림창 띄우기 부분 삭제

    document.getElementById('request-date').addEventListener('change', function() {
        const attendanceTypeRadios = document.querySelectorAll('input[name="attendance-type"]');
        attendanceTypeRadios.forEach(radio => {
            radio.checked = false; // 날짜 변경 시 라디오 버튼 초기화
        });
    });

    const attendanceTypeRadios = document.querySelectorAll('input[name="division"]');
    attendanceTypeRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            const requestDate = document.getElementById('request-date').value;
            const typeValue = this.value;

            if (requestDate && typeValue) {
                fetch('/api/get-attendance', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        date: requestDate,
                        type: typeValue
                    })
                })
                    .then(response => {
                        if (!response.ok) {
                            // 상태 코드에 따라 알림창 띄우기
                            if (response.status === 404) {
                                alert('요청한 데이터가 없습니다. 다시 시도해 주세요.');
                            } else if (response.status === 403) {
                                alert('접근이 거부되었습니다. 권한을 확인해 주세요.');
                            } else {
                                alert('일치하는 정보가 없습니다. 다시 선택해주세요.');
                            }
                            throw new Error('네트워크 응답이 좋지 않습니다: ' + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('서버 응답:', data);
                        const attendanceTimeInput = document.getElementById('attendance-time');
                        if (data && data.attendanceTime !== undefined && data.attendanceTime !== null && data.attendanceTime !== '') {
                            attendanceTimeInput.value = data.attendanceTime; // data.attendanceTime이 '11:28' 형식이라고 가정
                        } else {
                            console.error('attendanceTime이 없습니다:', data);
                            alert('일치하는 정보가 없습니다. 다시 선택해주세요.'); // 알림창 추가
                            attendanceTimeInput.value = ''; // 값이 없을 경우 빈 문자열로 설정
                        }
                    })
                    .catch(error => {
                        console.error('오류 발생:', error);
                    });
            }
        });
    });

    // 폼 제출 시 유효성 검사
    document.querySelector('form').addEventListener('submit', function(event) {
        const requestDate = document.getElementById('request-date').value;
        const attendanceTime = document.getElementById('attendance-time').value;
        const modifyTime = document.querySelector('input[name="modify_time"]').value;
        const divisionChecked = Array.from(attendanceTypeRadios).some(radio => radio.checked);
        const adminCode = document.getElementById('selected-manager').value;
        const workModifyReason = document.querySelector('textarea[name="workModifyReason"]').value;

        if (!requestDate || !attendanceTime || !modifyTime || !divisionChecked || !adminCode || !workModifyReason) {
            alert('모든 필드를 올바르게 입력해 주세요.');
            event.preventDefault(); // 폼 제출 방지
        } else {
            alert('근태수정 요청이 등록되었습니다.'); // 모든 필드가 입력된 경우 알림창
        }
    });
});
