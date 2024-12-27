// API 호출 함수
async function fetchPendingApprovals() {
    try {
        const response = await fetch('/api/manager'); // 매니저 API 엔드포인트
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json(); // JSON 형식으로 응답 받기

        // 결재 대기 갯수 업데이트
        updateCounts(data);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

// 전사 게시판 데이터 가져오기
async function fetchBoardData() {
    try {
        const response = await fetch('/api/board'); // 전사 게시판 API 엔드포인트
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json(); // JSON 형식으로 응답 받기

        // 게시판 데이터 표시
        displayBoardData(data);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

// 직원 정보 데이터 가져오기
async function fetchEmployeeInfo() {
    try {
        const response = await fetch('/api/employee'); // 직원 정보 API 엔드포인트
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json(); // JSON 형식으로 응답 받기

        // 직원 정보 데이터 표시
        displayEmployeeInfo(data);
    } catch (error) {
        console.error('Fetch error:', error);
    }
}

// 갯수 업데이트 함수
function updateCounts(data) {
    // 각 리스트의 갯수를 업데이트
    const scheduleCount = data.scheduleList.length; // 근태 요청 갯수
    const cacPaymentCount = data.cacPaymentList.length; // CAC 결재 갯수
    const overTimeCount = data.overTimeList.length; // 초과 근무 갯수
    const retirementCount = data.retirementList.length; // 퇴직 갯수
    const annualPaymentCount = data.annualPaymentList.length; // 연차 갯수
    const vacationPaymentCount = data.vacationPaymentList.length; // 휴가 갯수

    // 전체 대기 갯수
    const totalCount = scheduleCount + cacPaymentCount + overTimeCount + retirementCount + annualPaymentCount + vacationPaymentCount;

    // HTML 요소에 갯수 업데이트
    document.getElementById('total-count').textContent = totalCount; // 전체 대기 갯수
    document.getElementById('attendance-request-count').textContent = scheduleCount; // 근태 요청 갯수
    document.getElementById('annual-leave-count').textContent = annualPaymentCount; // 연차 결재 갯수
    document.getElementById('vacation-count').textContent = vacationPaymentCount; // 휴가 결재 갯수
    document.getElementById('event-count').textContent = cacPaymentCount; // 경조사 결재 (예시로 0으로 설정)
    document.getElementById('extension-count').textContent = overTimeCount; // 연장 결재 (예시로 0으로 설정)
    document.getElementById('retirement-count').textContent = retirementCount; // 퇴직원 결재 갯수
}

// 게시판 데이터 표시 함수
function displayBoardData(data) {
    const boardContent = document.getElementById('board-content');
    boardContent.innerHTML = ''; // 기존 내용 초기화

    data.forEach(item => {
        const li = document.createElement('li');
        li.textContent = item.boardTitle; // 게시판 항목의 제목

        // 클릭 이벤트 추가
        li.addEventListener('click', () => {
            // 해당 게시판 코드로 상세 페이지로 이동
            window.location.href = `/sidemenu/board/${item.boardCode}`; // item.boardCode는 게시판의 고유 코드
        });

        boardContent.appendChild(li);
    });
}

// 직원 정보 데이터 표시 함수
function displayEmployeeInfo(data) {
    const employeeInfoContent = document.getElementById('employee-info-content');
    employeeInfoContent.innerHTML = ''; // 기존 내용 초기화

    data.forEach(item => {
        const li = document.createElement('li');

        const workStartTime = formatTime(item.scheduleDTO.workStartTime) || '출근 전';
        const workEndTime = formatTime(item.scheduleDTO.workEndTime) || '퇴근 전';
        const workType = item.scheduleDTO.workType ? item.scheduleDTO.workType : '결근';

        // 각 항목을 span 태그로 감싸고 스타일 적용
        li.innerHTML = `
            <span class="item">${item.deptDTO.deptName}</span>
            <span class="item">${item.empCode}</span>
            <span class="item">${item.empName}</span>
            <span class="item">${item.jobDTO.jobName}</span>
            <span class="item">${workStartTime}</span>
            <span class="item">${workEndTime}</span>
            <span class="item">${workType}</span>
        `;

        // 클릭 이벤트 추가
        li.addEventListener('click', () => {
            const empCode = item.empCode; // 직원 코드
            window.location.href = `/employee/details/${empCode}`;
        });

        employeeInfoContent.appendChild(li);
    });
}

// 시간을 "HH:MM" 형식으로 변환하는 함수
function formatTime(timeString) {
    if (!timeString) return null;

    const date = new Date(timeString);
    const hours = String(date.getHours()).padStart(2, '0'); // 두 자리로 포맷
    const minutes = String(date.getMinutes()).padStart(2, '0'); // 두 자리로 포맷

    return `${hours}:${minutes}`;
}


// 페이지 로드 시 데이터 가져오기
window.onload = async function() {
    await fetchPendingApprovals(); // 결재 대기 데이터 가져오기
    await fetchBoardData(); // 전사 게시판 데이터 가져오기
    await fetchEmployeeInfo(); // 직원 정보 데이터 가져오기
};


document.addEventListener('DOMContentLoaded', function() {
    const calendarEl = document.getElementById('calendar');
    const todoHeader = document.getElementById('todo-header');
    const todoInput = document.getElementById('todo-input');
    const todoList = document.getElementById('todo-list');
    const writeDateInput = document.getElementById('write-date'); // 숨겨진 입력 필드
    const todoForm = document.getElementById('todo-form'); // 폼 요소 추가
    let selectedDate = null; // 선택된 날짜를 저장할 변수
    let todoDates = new Set(); // To-Do가 있는 날짜를 저장할 Set

    // 현재 날짜를 가져와서 To-Do List 제목 설정
    const today = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric', locale: 'ko-KR' };
    selectedDate = today.toISOString().split('T')[0]; // 오늘 날짜를 ISO 형식으로 저장
    todoHeader.textContent = `${today.toLocaleDateString('ko-KR', options)} To-Do List`;

    // FullCalendar 초기화
    const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: '',
            center: 'title',
            right: 'prev,next'
        },
        locale: 'ko',
        dateClick: function(info) {
            selectedDate = info.dateStr; // 클릭한 날짜 저장
            writeDateInput.value = selectedDate; // 숨겨진 입력 필드에 날짜 설정
            todoHeader.textContent = `${selectedDate} To-Do List`;
            todoInput.value = '';
            todoInput.focus();
            fetchTodoList(); // 선택된 날짜에 맞는 To-Do 리스트 가져오기
        },
        datesSet: function() {
            renderTodoLabels(); // 날짜가 변경될 때 라벨을 렌더링
        }
    });

    calendar.render();

    // 초기 로딩 시 오늘 날짜의 To-Do 리스트 가져오기
    fetchTodoList();

    // 폼 제출 시 입력값 확인
    todoForm.addEventListener('submit', function(event) {
        if (!todoInput.value.trim()) { // 입력값이 비어있거나 공백일 경우
            alert('할 일을 입력해주세요.'); // 알림창 띄우기
            event.preventDefault(); // 폼 제출 방지
        }
    });

    // To-Do 리스트를 가져오는 함수
    async function fetchTodoList() {
        try {
            const response = await fetch('/api/getTodoList', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                throw new Error('네트워크 응답이 좋지 않습니다.');
            }
            const todos = await response.json();
            todoList.innerHTML = ''; // 기존 리스트 초기화
            todoDates.clear(); // 이전 날짜 초기화
            todos.forEach(todo => {
                const todoDate = new Date(new Date(todo.writeDate).getTime() + 9 * 60 * 60 * 1000).toISOString().split('T')[0]; // yyyy-mm-dd 형식으로 변환
                if (todoDate === selectedDate) {
                    addTodoToList(todo.toDoContents, todo.toDoCode); // todo.id를 추가
                }
                todoDates.add(todoDate); // To-Do가 있는 날짜 추가
            });
            renderTodoLabels(); // To-Do 라벨 렌더링
        } catch (error) {
            console.error('할 일 목록을 가져오는 데 오류가 발생했습니다:', error);
        }
    }

    function renderTodoLabels() {
        const dayCells = calendarEl.getElementsByClassName('fc-day');
        for (let dayCell of dayCells) {
            const dateStr = dayCell.getAttribute('data-date');
            let existingLabel = dayCell.querySelector('.todo-label'); // 기존 라벨 찾기

            if (todoDates.has(dateStr)) {
                if (!existingLabel) {
                    const label = document.createElement('div');
                    label.textContent = 'To-Do';
                    label.className = 'todo-label'; // 스타일을 위한 클래스 추가
                    dayCell.appendChild(label);
                }
            } else {
                if (existingLabel) {
                    dayCell.removeChild(existingLabel);
                }
            }
        }
    }



    function addTodoToList(todoText, todoId) {
        const listItem = document.createElement('li');

        // 텍스트를 감싸는 span 요소 추가
        const todoSpan = document.createElement('span');
        todoSpan.textContent = todoText;
        todoSpan.className = 'todo-text'; // 클래스 추가

        // 완료 버튼
        const completeButton = document.createElement('button');
        completeButton.textContent = '완료';
        completeButton.style.backgroundColor = '#28a745'; // 초록색 배경
        completeButton.addEventListener('click', function() {
            listItem.classList.toggle('completed');
            completeButton.disabled = true; // 완료 버튼 비활성화
        });

        // 수정 버튼
        const editButton = document.createElement('button');
        editButton.textContent = '수정';
        editButton.style.backgroundColor = '#007bff'; // 파란색 배경
        editButton.addEventListener('click', async function() {
            const newText = prompt("수정할 내용을 입력하세요:", todoText);
            if (newText) {
                try {
                    const response = await fetch('/api/editTodo', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            toDoCode: todoId, // 할 일의 고유 코드로 todoId 사용
                            toDoContents: newText // 수정할 내용
                        }) // todoId와 새로운 텍스트를 포함하여 수정 요청
                    });
                    if (!response.ok) {
                        throw new Error('수정 요청이 실패했습니다.');
                    }
                    todoSpan.textContent = newText; // 텍스트 업데이트
                } catch (error) {
                    console.error('할 일 수정 중 오류가 발생했습니다:', error);
                }
            }
        });

        // 삭제 버튼
        const deleteButton = document.createElement('button');
        deleteButton.textContent = '삭제';
        deleteButton.style.backgroundColor = '#dc3545'; // 빨간색 배경
        deleteButton.addEventListener('click', async function() {
            try {
                const response = await fetch('/api/deleteTodo', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ toDoCode: todoId }) // todoId를 포함하여 삭제 요청
                });
                if (!response.ok) {
                    throw new Error('삭제 요청이 실패했습니다.');
                }
                todoList.removeChild(listItem); // 리스트에서 항목 삭제
            } catch (error) {
                console.error('할 일 삭제 중 오류가 발생했습니다:', error);
            }
        });

        // 리스트 항목에 요소 추가
        listItem.appendChild(todoSpan);
        listItem.appendChild(completeButton); // 순서 변경: 완료 버튼 먼저 추가
        listItem.appendChild(editButton); // 수정 버튼
        listItem.appendChild(deleteButton); // 삭제 버튼
        todoList.appendChild(listItem);
    }

    // 페이지 로드 시 To-Do 리스트 가져오기
    fetchTodoList(); // 페이지 로드 시 To-Do 리스트 가져오기
});











