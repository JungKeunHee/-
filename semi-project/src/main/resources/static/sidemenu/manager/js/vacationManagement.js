// 전역 변수
let currentPage = 1;
const itemsPerPage = 10; // 한 페이지에 표시할 항목 수
let allData = []; // 전체 데이터를 저장할 배열

// API 호출 함수
async function fetchData() {
    try {
        const response = await fetch('/empVacationManagement'); // API URL을 여기에 입력하세요
        if (!response.ok) {
            throw new Error('네트워크 응답이 좋지 않습니다.');
        }
        allData = await response.json(); // JSON 형식으로 응답 데이터 파싱
        updateList(); // 초기 리스트 업데이트
    } catch (error) {
        console.error('데이터를 가져오는 중 오류 발생:', error);
    }
}

// 날짜 형식 변환 함수
function formatDate(dateString) {
    const date = new Date(dateString);
    date.setHours(date.getHours() + 9);
    return date.toISOString().split('T')[0];
}

// 근속년수 계산 함수
function calculateYearsOfService(hireDate) {
    const hireDateObj = new Date(hireDate);
    const currentDate = new Date();
    let yearsOfService = currentDate.getFullYear() - hireDateObj.getFullYear();

    if (
        currentDate.getMonth() < hireDateObj.getMonth() ||
        (currentDate.getMonth() === hireDateObj.getMonth() && currentDate.getDate() < hireDateObj.getDate())
    ) {
        yearsOfService--;
    }

    return yearsOfService;
}

function generateAttendanceRates() {
    const lastYearAttendance = Math.floor(Math.random() * 21) + 80; // 80%에서 100% 사이의 랜덤 값
    return { lastYearAttendance }; // 작년 근태율만 반환
}

// 리스트 업데이트 함수
function updateList(filteredData = allData) {
    const ul = document.createElement('ul'); // 새로운 ul 요소 생성
    ul.classList.add('data-list'); // CSS 클래스 추가
    const startIndex = (currentPage - 1) * itemsPerPage; // 현재 페이지의 시작 인덱스
    const endIndex = startIndex + itemsPerPage; // 현재 페이지의 끝 인덱스
    const paginatedData = filteredData.slice(startIndex, endIndex); // 현재 페이지에 해당하는 데이터

    paginatedData.forEach(item => {
        const li = document.createElement('li'); // 새로운 li 요소 생성
        const formattedHireDate = formatDate(item.hireDate); // hireDate 형식 변환
        const yearsOfService = calculateYearsOfService(item.hireDate); // 근속년수 계산
        let lastYearAttendance;

        if (yearsOfService >= 1) {
            lastYearAttendance = generateAttendanceRates().lastYearAttendance; // 근속년수에 따라 작년 근태율 결정
        } else {
            lastYearAttendance = '1년 미만'; // 1년 미만인 경우 표시
        }

        // 각 항목을 구성하는 HTML 요소 생성
        li.innerHTML = `
            <input type="checkbox" class="item-checkbox" />
            <span>${item.empCode}</span>
            <span>${item.deptDTO.deptName}</span>
            <span>${item.empName}</span>
            <span>${item.jobDTO.jobName}</span>
            <span>${formattedHireDate}</span>
            <span>${yearsOfService}년</span>
            <span>${yearsOfService >= 1 ? lastYearAttendance + '%' : lastYearAttendance}</span>
            <span>100%</span>
            <span>${item.vacationDTO.annualLeave}</span>
            <span>${item.vacationDTO.vacation}</span>
        `;

        ul.appendChild(li); // ul에 li 추가
    });

    // 기존의 ul 요소에 추가
    const headerList = document.querySelector('.header-list');
    const existingUl = headerList.nextSibling; // 기존 ul 요소 찾기
    if (existingUl) {
        headerList.parentNode.removeChild(existingUl); // 기존 ul 요소 제거
    }
    headerList.parentNode.insertBefore(ul, headerList.nextSibling); // 헤드라인 아래에 추가

    updatePagination(filteredData); // 페이지 번호 업데이트
}

// 필터링 함수
function filterData() {
    const searchTerm = document.getElementById('name-search').value.toLowerCase(); // 이름 검색어
    const selectedDepartment = document.getElementById('department-select').value; // 선택된 부서

    const filteredData = allData.filter(item => {
        const matchesName = item.empName.toLowerCase().includes(searchTerm); // 이름 필터링
        const matchesDepartment = selectedDepartment === "" || item.deptDTO.deptName === selectedDepartment; // 부서 필터링
        return matchesName && matchesDepartment; // 두 조건 모두 만족해야 함
    });

    currentPage = 1; // 필터링 후 첫 페이지로 초기화
    updateList(filteredData); // 필터링된 데이터로 리스트 업데이트
    updatePagination(filteredData); // 필터링된 데이터로 페이지 번호 업데이트
}

// 페이지 번호 업데이트 함수
function updatePagination(filteredData) {
    const paginationContainer = document.querySelector('.pagination');
    paginationContainer.innerHTML = ''; // 기존 페이지 번호 초기화

    const totalPages = Math.ceil(filteredData.length / itemsPerPage); // 필터링된 데이터 기준으로 총 페이지 수 계산

    // 페이지 수가 0인 경우 처리
    if (totalPages === 0) {
        return; // 페이지가 없으면 함수 종료
    }

    for (let i = 1; i <= totalPages; i++) {
        const pageButton = document.createElement('button');
        pageButton.textContent = i;
        pageButton.onclick = () => {
            currentPage = i; // 현재 페이지 설정
            updateList(filteredData); // 필터링된 데이터로 리스트 업데이트
        };
        paginationContainer.appendChild(pageButton); // 페이지 버튼 추가
    }
}


// 이름 검색 필터링 기능
document.getElementById('name-search').addEventListener('input', filterData);

// 부서 필터링 기능
document.getElementById('department-select').addEventListener('change', filterData);

// 전체 선택 기능
document.getElementById('select-all').onclick = () => {
    const checkboxes = document.querySelectorAll('.item-checkbox');
    const allChecked = Array.from(checkboxes).every(checkbox => checkbox.checked);
    checkboxes.forEach(checkbox => {
        checkbox.checked = !allChecked; // 현재 상태 반전
    });
};

// 근태율 필터링 기능
document.getElementById('attendance-80').onclick = () => {
    const filteredData = allData.filter(item => {
        const yearsOfService = calculateYearsOfService(item.hireDate); // 근속년수 계산
        const { lastYearAttendance } = generateAttendanceRates(); // 랜덤 근태율 생성

        // 근속년수가 1년 이상이고 작년 근태율이 80% 이상인 경우만 필터링
        return yearsOfService >= 1 && lastYearAttendance >= 80;
    });
    updateList(filteredData);
};

document.getElementById('attendance-100').onclick = () => {
    const filteredData = allData.filter(item => {
        const yearsOfService = calculateYearsOfService(item.hireDate); // 근속년수 계산
        return yearsOfService < 1; // 근속년수 1년 미만인 경우만 필터링
    });
    updateList(filteredData);
};



// 모달 관련 변수
const createModal = document.getElementById('createModal');
const deductModal = document.getElementById('deductModal');
const addLeaveButton = document.getElementById('addLeave');
const deductLeaveButton = document.getElementById('deductLeave');
const closeCreateButton = document.getElementById('closeCreateModal');
const closeDeductButton = document.getElementById('closeDeductModal');
const submitCreateButton = document.getElementById('submitCreateLeave');
const cancelCreateButton = document.getElementById('cancelCreateLeave');
const submitDeductButton = document.getElementById('submitDeductLeave');
const cancelDeductButton = document.getElementById('cancelDeductLeave');

// 체크된 사번 코드를 모달의 입력 필드에 배열로 추가하는 함수
function addCheckedEmpCodesToModal(inputId) {
    const checkboxes = document.querySelectorAll('.item-checkbox:checked'); // 체크된 체크박스만 선택
    const empCodeInput = document.getElementById(inputId); // 모달의 사번 코드 입력 박스
    empCodeInput.value = ''; // 입력 박스 초기화

    const checkedEmpCodes = Array.from(checkboxes)
        .map(checkbox => {
            const li = checkbox.closest('li'); // 체크박스의 부모 li 요소 찾기
            return li ? li.querySelector('span').textContent : ''; // 사번 코드가 있는 span 요소의 텍스트 가져오기
        })
        .filter(code => code); // 빈 값 필터링

    // 배열로 사번 코드를 추가
    empCodeInput.value = JSON.stringify(checkedEmpCodes); // 배열을 JSON 문자열로 변환하여 입력 박스에 추가
}

// 생성 모달 열기
addLeaveButton.onclick = () => {
    const checkboxes = document.querySelectorAll('.item-checkbox:checked');
    if (checkboxes.length === 0) {
        alert('사번 코드를 선택해 주세요.'); // 체크박스가 선택되지 않았을 때 알림
        return; // 모달 열기 중단
    }
    addCheckedEmpCodesToModal('createEmpCode'); // 체크된 사번 코드 추가
    createModal.style.display = 'block';
};

// 차감 모달 열기
deductLeaveButton.onclick = () => {
    const checkboxes = document.querySelectorAll('.item-checkbox:checked');
    if (checkboxes.length === 0) {
        alert('사번 코드를 선택해 주세요.'); // 체크박스가 선택되지 않았을 때 알림
        return; // 모달 열기 중단
    }
    addCheckedEmpCodesToModal('deductEmpCode'); // 체크된 사번 코드 추가
    deductModal.style.display = 'block';
};

// 생성 모달 닫기
closeCreateButton.onclick = () => {
    createModal.style.display = 'none';
};

cancelCreateButton.onclick = () => {
    createModal.style.display = 'none';
};

// 차감 모달 닫기
closeDeductButton.onclick = () => {
    deductModal.style.display = 'none';
};

cancelDeductButton.onclick = () => {
    deductModal.style.display = 'none';
};

// 연차 및 휴가 생성 버튼 클릭 시 처리
submitCreateButton.onclick = () => {
    const empCode = JSON.parse(document.getElementById('createEmpCode').value); // JSON 문자열을 배열로 변환
    const leaveType = document.querySelector('input[name="createLeaveType"]:checked').value;
    const leaveDays = document.getElementById('createLeaveDays').value;

    // 여기서 연차 및 휴가 생성 로직을 추가하세요
    console.log(`생성 - 사번: ${empCode}, 유형: ${leaveType}, 일수: ${leaveDays}`);

    // 모달 닫기
    createModal.style.display = 'none';
};

// 연차 및 휴가 차감 버튼 클릭 시 처리
submitDeductButton.onclick = () => {
    const empCode = JSON.parse(document.getElementById('deductEmpCode').value); // JSON 문자열을 배열로 변환
    const leaveType = document.querySelector('input[name="deductLeaveType"]:checked').value;
    const leaveDays = document.getElementById('deductLeaveDays').value;

    // 여기서 연차 및 휴가 차감 로직을 추가하세요
    console.log(`차감 - 사번: ${empCode}, 유형: ${leaveType}, 일수: ${leaveDays}`);

    // 모달 닫기
    deductModal.style.display = 'none';
};

// 모달 외부 클릭 시 닫기
window.onclick = (event) => {
    if (event.target === createModal) {
        createModal.style.display = 'none';
    }
    if (event.target === deductModal) {
        deductModal.style.display = 'none';
    }
};


// 연차 및 휴가 생성 버튼 클릭 시 처리
submitCreateButton.onclick = async () => {
    const empCodes = JSON.parse(document.getElementById('createEmpCode').value); // JSON 배열로 변환
    const leaveType = document.querySelector('input[name="createLeaveType"]:checked').value;
    const leaveDays = document.getElementById('createLeaveDays').value;

    // 모든 필드가 입력되었는지 확인
    if (empCodes.length > 0 && leaveDays) {
        const confirmationMessage = leaveType === 'annual'
            ? '연차 생성 하시겠습니까?'
            : '휴가 생성 하시겠습니까?';

        if (confirm(confirmationMessage)) {
            try {
                const response = await fetch('/createVacation', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        empCodes: empCodes, // 배열
                        leaveType: leaveType,
                        leaveDays: leaveDays,
                    }),
                });

                if (response.ok) {
                    alert('성공적으로 처리되었습니다.');
                    // 리다이렉트할 URL로 변경
                    window.location.href = '/vacationManagement'; // 원하는 페이지로 리다이렉트
                } else {
                    const errorMessage = await response.text();
                    console.error('오류:', errorMessage);
                    alert('처리 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('오류 발생:', error);
                alert('네트워크 오류가 발생했습니다.');
            }
        }
    } else {
        alert('모든 필드를 입력해 주세요.');
    }
};



// 생성 모달 닫기
document.getElementById('closeCreateModal').onclick = () => {
    document.getElementById('createModal').style.display = 'none';
};

document.getElementById('cancelCreateLeave').onclick = () => {
    document.getElementById('createModal').style.display = 'none';
};


// 차감 버튼 클릭 시 처리
submitDeductLeave.onclick = async (event) => {
    event.preventDefault(); // 기본 폼 제출 방지

    const empCodes = JSON.parse(document.getElementById('deductEmpCode').value); // JSON 배열로 변환
    const leaveType = document.querySelector('input[name="deductLeaveType"]:checked').value; // 선택된 휴가 유형
    const deductDays = document.getElementById('deductLeaveDays').value; // 차감할 일수

    // 모든 필드가 입력되었는지 확인
    if (empCodes.length > 0 && deductDays) {
        const confirmationMessage = leaveType === 'annual'
            ? '연차를 차감하시겠습니까?'
            : '휴가를 차감하시겠습니까?';

        if (confirm(confirmationMessage)) {
            try {
                const response = await fetch('/deductedVacation', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        empCodes: empCodes, // 배열로 전달
                        leaveType: leaveType,
                        leaveDays: deductDays, // 차감할 일수
                    }),
                });

                if (response.ok) {
                    alert('성공적으로 차감되었습니다.');
                    // 리다이렉트할 URL로 변경
                    window.location.href = '/vacationManagement'; // 원하는 페이지로 리다이렉트
                } else {
                    const errorMessage = await response.text();
                    console.error('오류:', errorMessage);
                    alert('처리 중 오류가 발생했습니다.');
                }
            } catch (error) {
                console.error('오류 발생:', error);
                alert('네트워크 오류가 발생했습니다.');
            }
        }
    } else {
        alert('모든 필드를 입력해 주세요.');
    }
};



// 생성 모달 닫기
document.getElementById('closeCreateModal').onclick = () => {
    document.getElementById('createModal').style.display = 'none';
};

document.getElementById('cancelCreateLeave').onclick = () => {
    document.getElementById('createModal').style.display = 'none';
};

// 차감 모달 닫기
document.getElementById('closeDeductModal').onclick = () => {
    document.getElementById('deductModal').style.display = 'none';
};

document.getElementById('cancelDeductLeave').onclick = () => {
    document.getElementById('deductModal').style.display = 'none';
};


// 페이지 로드 시 데이터 가져오기
window.onload = fetchData;
