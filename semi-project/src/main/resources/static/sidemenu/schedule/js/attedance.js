document.addEventListener('DOMContentLoaded', function() {
    // 모달 창 열기
    function openModal() {
        const modal = document.getElementById('modal');
        modal.style.display = 'block';
        fetchEmployees();
    }

    // 모달 창 닫기
    function closeModal() {
        const modal = document.getElementById('modal');
        modal.style.display = 'none';
    }

    function fetchEmployees() {
        const employeeList = document.getElementById('employee-list');
        employeeList.innerHTML = '<li>로딩 중...</li>'; // 로딩 상태 표시

        fetch('/api/employees')
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 좋지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                console.log(data); // API 응답 확인
                employeeList.innerHTML = ''; // 기존 목록 초기화

                data.forEach(employee => {
                    const empCode = employee.empCode || 'N/A'; // 사번
                    const empName = employee.empName || 'N/A'; // 이름
                    const jobName = employee.jobDTO ? employee.jobDTO.jobName : 'N/A'; // 직급 이름
                    const deptName = employee.deptDTO ? employee.deptDTO.deptName : 'N/A'; // 부서 이름

                    const li = document.createElement('li');
                    li.innerHTML = `
                        <span>${empCode}</span>
                        <span>${empName}</span>
                        <span>${jobName}</span>
                        <span>${deptName}</span>
                    `; // 직원 정보
                    li.setAttribute('data-name', empName); // 데이터 속성 추가
                    li.onclick = () => selectEmployee(employee); // 직원 객체를 전달
                    employeeList.appendChild(li);
                });

            })
            .catch(error => {
                console.error('직원 목록을 가져오는 중 오류 발생:', error);
                employeeList.innerHTML = '<li>직원 목록을 가져오는 중 오류가 발생했습니다.</li>'; // 오류 메시지 표시
            });
    }

    // 직원 선택 시 동작
    function selectEmployee(employee) {
        // 직원 정보 확인
        if (!employee) {
            console.error("직원 정보가 없습니다.");
            return;
        }

        const empCode = employee.empCode || 'N/A'; // 사번

        // 인풋 박스에 부서 이름, 이름, 직급 순으로 설정
        const selectedManagerInput = document.getElementById('selected-manager');
        if (!selectedManagerInput) {
            console.error("선택된 관리자 입력 박스가 없습니다.");
            return;
        }

        // 인풋 박스의 데이터 값으로 사번 코드 설정
        selectedManagerInput.setAttribute('data-value', empCode); // 데이터 속성에 사번 저장
        selectedManagerInput.value = `${empCode}`; // 부서 이름, 이름, 직급, 사번 순으로 설정

        closeModal(); // 모달 닫기
    }

    // 모달 닫기 버튼 클릭 이벤트
    document.querySelector('.close-button').onclick = closeModal;

    // 인풋 버튼 클릭 이벤트
    document.getElementById('open-modal').onclick = openModal;

});
