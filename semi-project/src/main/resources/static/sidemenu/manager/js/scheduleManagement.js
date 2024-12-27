document.addEventListener("DOMContentLoaded", function() {
    const attendanceList = document.getElementById('attendance-list');
    const attendanceDetails = document.getElementById('attendance-details');
    const userReasonTextarea = document.getElementById('user-reason');
    const requestTimeSpan = document.getElementById('request-time');
    const requestTitle = document.getElementById('request-title');
    const statusValueInput = document.getElementById('statusValue');
    const workCodeInput = document.getElementById('workCode');
    const modifyTimeInput = document.getElementById('modifyTime');
    const division = document.getElementById("division");

    // 버튼 클릭 이벤트 추가
    function initializePage() {
        const buttons = document.querySelectorAll('.tab-button');

        // 모든 버튼의 active 클래스 제거
        buttons.forEach(button => {
            button.classList.remove('active');
        });

        // 기본적으로 활성화할 버튼을 지정
        const defaultButton = document.querySelector('.tab-button.default');
        if (defaultButton) {
            defaultButton.classList.add('active'); // 기본 버튼에 active 클래스 추가
        }

        // 이벤트 리스너 등록
        buttons.forEach(button => {
            button.addEventListener('click', function() {
                // 모든 버튼의 active 클래스 제거
                buttons.forEach(btn => btn.classList.remove('active'));
                // 클릭한 버튼에 active 클래스 추가
                this.classList.add('active');
            });
        });
    }

    // 페이지가 로드될 때 초기화
    initializePage();




    // 서버에서 데이터 가져오기
    fetch('/attendanceManagement') // API 엔드포인트를 실제 URL로 변경하세요.
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 좋지 않습니다.');
            }
            return response.json();
        })
        .then(data => {
            data.forEach(item => {
                const employeeName = item.employeeJoinListDTO ? item.employeeJoinListDTO.empName : '이름 없음';
                const formattedDate = new Date(item.modifyTime).toISOString().split('T')[0];

                let progressText = '';
                let progressClass = '';

                switch (item.progressCode) {
                    case 'SU1':
                        progressText = '대기중';
                        progressClass = 'label waiting';
                        break;
                    case 'SU2':
                        progressText = '확인중';
                        progressClass = 'label checking';
                        break;
                    case 'SU3':
                        progressText = '승인됨';
                        progressClass = 'label approved';
                        break;
                    case 'SU4':
                        progressText = '반려됨';
                        progressClass = 'label rejected';
                        break;
                    default:
                        progressText = '알 수 없음';
                        progressClass = 'label unknown';
                }

                const workStartTime = new Date(item.workStartTime);
                const workEndTime = new Date(item.workEndTime);
                const totalWorkTimeInSeconds = (workEndTime - workStartTime) / 1000;
                const hours = Math.floor(totalWorkTimeInSeconds / 3600);
                const minutes = Math.floor((totalWorkTimeInSeconds % 3600) / 60);
                const seconds = Math.floor(totalWorkTimeInSeconds % 60);
                const formattedTotalWorkTime = `${hours}시간 ${minutes}분 ${seconds}초`;

                const li = document.createElement('li');

                // division 값에 따라 data-modify-type 설정
                const modifyType = item.division === '출근' ? '출근수정' : item.division === '퇴근' ? '퇴근수정' : '';
                li.setAttribute('data-modify-type', modifyType); // 수정 유형 추가
                li.setAttribute('data-status', progressText); // 상태 추가
                li.innerHTML = `
                    <div class="employee-info">
                        <span class="department">${item.employeeJoinListDTO.deptDTO.deptName}</span>
                        <span class="name">${employeeName}</span>
                        <span class="job">${item.employeeJoinListDTO.jobDTO.jobName}</span>
                    </div>
                    <div class="request-info">
                        <p>수정 요청일: <span class="space3">${formattedDate}</span></p>
                        <p>구<span class="space2">분: <span class="space">${item.division}</span></p>
                        <p><span class="${progressClass}">${progressText}</span></p>
                    </div>
                `;

                // 클릭 이벤트 추가
                li.addEventListener('click', () => {
                    attendanceDetails.innerHTML = '';

                    const details = [
                        `${formattedDate}`,
                        `${workStartTime.toTimeString().split(' ')[0]}`,
                        `${workEndTime.toTimeString().split(' ')[0]}`,
                        formattedTotalWorkTime,
                        `${item.workType}`
                    ];

                    details.forEach(detail => {
                        const detailLi = document.createElement('li');
                        detailLi.textContent = detail;
                        attendanceDetails.appendChild(detailLi);
                    });

                    userReasonTextarea.value = item.workModifyReason || '';

                    const requestDate = new Date(item.modifyTime);
                    const options = { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true };
                    const formattedRequestDate = requestDate.toLocaleString('ko-KR', options).replace(',', '');
                    requestTimeSpan.textContent = formattedRequestDate;

                    requestTitle.textContent = `${employeeName} 근태 수정 요청안`;

                    attendanceDetails.style.display = 'flex';

                    // workCode 및 modifyTime 및 division 값을 설정
                    workCodeInput.value = item.workCode;
                    modifyTimeInput.value = item.modifyTime;
                    division.value = item.division;
                });

                attendanceList.appendChild(li);
            });

            requestTitle.textContent = '근태 수정 요청안';

            // 필터링 초기화
            filterAttendance(); // 데이터 로드 후 필터링 함수 호출
        })
        .catch(error => {
            console.error('데이터를 가져오는 중 오류 발생:', error);
            attendanceList.innerHTML = '<li>데이터를 가져오는 데 실패했습니다.</li>';
        });
});




// 필터링 이벤트 리스너 추가
document.getElementById('modify-type').addEventListener('change', filterAttendance);
document.getElementById('status-filter').addEventListener('change', filterAttendance);


// 필터링 함수
function filterAttendance() {
    const modifyType = document.getElementById('modify-type').value; // 선택된 수정 유형
    const statusValue = document.getElementById('status-filter').value; // 선택된 상태
    const attendanceList = document.getElementById('attendance-list');
    const items = attendanceList.getElementsByTagName('li');

    for (let i = 0; i < items.length; i++) {
        const item = items[i];
        const itemModifyType = item.getAttribute('data-modify-type'); // 항목의 수정 유형
        const itemStatus = item.getAttribute('data-status'); // 항목의 상태

        // 수정 유형과 상태에 대한 필터링 조건
        const matchesModifyType = modifyType === "" || itemModifyType === modifyType;
        const matchesStatus = statusValue === "" || itemStatus === statusValue;

        // 두 조건이 모두 만족하면 항목을 표시하고, 그렇지 않으면 숨김
        if (matchesModifyType && matchesStatus) {
            item.style.display = "";
        } else {
            item.style.display = "none";
        }
    }
}

function handleSubmit(status) {
    const workType = document.querySelector('input[name="workType"]:checked');
    const managerIdea = document.getElementById('user-reason').value;

    // 입력 검증
    if (!workType) {
        alert("근무 유형을 선택해주세요.");
        return;
    }
    if (!managerIdea) {
        alert("관리자 의견을 작성해주세요.");
        return;
    }

    // 확인 알림창
    const confirmation = confirm(status === 'SU3' ? "승인 처리하시겠습니까?" : "반려 처리하시겠습니까?");
    if (confirmation) {
        setValue(status);

        // 폼 제출
        const form = document.getElementById('attendanceForm'); // 폼 ID를 실제 ID로 변경하세요.
        if (form) {
            // 알림창 추가
            if (status === 'SU3') {
                alert("승인 처리되었습니다.");
            } else if (status === 'SU4') {
                alert("반려 처리되었습니다.");
            }

            form.submit(); // 폼 제출
        } else {
            console.error("폼을 찾을 수 없습니다.");
        }
    }
}

function setValue(value) {
    const statusValueInput = document.getElementById('statusValue'); // 상태 값을 저장할 input 요소

    statusValueInput.value = value;
    console.log("설정된 값:", value); // 디버깅을 위한 로그
}


