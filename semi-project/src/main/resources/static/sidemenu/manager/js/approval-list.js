async function fetchApprovalData() {
    try {
        const response = await fetch('/api/approval-list');

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const data = await response.json();
        const approvalList = document.querySelector('.approval');
        approvalList.innerHTML = '';

        const formatDate = (dateString) => {
            const date = new Date(dateString);
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, '0');
            const day = String(date.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        };

        const getStatus = (value) => {
            if (value === null || value === '') {
                return '확인중';
            } else if (value === '/img/icon/Stamp.png') {
                return '승인';
            } else if (value === 'N') {
                return '반려';
            }
            return value;
        };

        const getProgressStatus = (code) => {
            switch (code) {
                case 'SU1':
                    return '대기중';
                case 'SU2':
                    return '진행중';
                case 'SU3':
                    return '승인';
                case 'SU4':
                    return '반려';
                default:
                    return code;
            }
        };

        const allLists = [
            ...data.cacPaymentList,
            ...data.overTimeList,
            ...data.retirementList,
            ...data.vacPaymentList
        ];

        console.log(allLists); // 데이터 구조 확인

        // 필터링된 결재 목록을 저장할 배열
        let filteredApprovals = allLists;

        // 필터링 함수
        function filterApprovals() {
            const startDate = document.getElementById('startDate').value;
            const endDate = document.getElementById('endDate').value;
            const typeFilter = document.getElementById('typeFilter').value;
            const statusFilter = document.getElementById('statusFilter').value;

            // startDate와 endDate를 Date 객체로 변환
            const start = startDate ? new Date(startDate + 'T00:00:00') : null;
            const end = endDate ? new Date(endDate + 'T23:59:59') : null;

            filteredApprovals = allLists.filter(approval => {
                const approvalDate = new Date(approval.deadLineDate);

                const isDateValid = (!start || approvalDate >= start) &&
                    (!end || approvalDate <= end);
                const isTypeValid = !typeFilter || approval.type === typeFilter;
                const isStatusValid = !statusFilter || getProgressStatus(approval.progressCode) === statusFilter;

                return isDateValid && isTypeValid && isStatusValid;
            });

            displayApprovals(filteredApprovals);
        }


        // 결재 목록 표시 함수
        function displayApprovals(approvals) {
            approvalList.innerHTML = ''; // 기존 목록 초기화

            approvals.forEach(item => {
                const listItem = document.createElement('li');
                listItem.innerHTML = `
                    <span>${item.empCode}</span>
                    <span>${formatDate(item.deadLineDate)}</span>
                    <span>${item.type}</span>
                    <span>${getStatus(item.managerAccept)}</span>
                    <span>${getStatus(item.presidentAccept)}</span>
                    <span>${getProgressStatus(item.progressCode)}</span>
                `;
                listItem.dataset.id = item.documentNo; // documentNo를 ID로 설정

                // 클릭 이벤트 리스너 추가
                listItem.addEventListener('click', () => {
                    // item.type이 "연차" 또는 "휴가"인지 확인
                    if (item.type.includes("연차") || item.type.includes("휴가")) {
                        // 해당 URL로 이동
                        window.location.href = `/readVacRequest?no=${item.documentNo}`;
                    } else if (item.type.includes("결혼") || item.type.includes("조사")) {
                        // "경사" 또는 "조사"일 경우 해당 URL로 이동
                        window.location.href = `/readCacRequest?no=${item.documentNo}`;
                    } else if (item.type.includes("연장근무") || item.type.includes("연장")) {
                        // "연장근무" 또는 "연장"일 경우 해당 URL로 이동
                        window.location.href = `/readOverTimeRequest?no=${item.documentNo}`;
                    } else if (item.type.includes("퇴직")) {
                        // "퇴직"일 경우 해당 URL로 이동
                        window.location.href = `/readRetireMentRequest?no=${item.documentNo}`;
                    }else {
                        // 다른 로직이 필요할 경우 여기에 추가
                        showForm(item.documentNo);
                    }
                });

                approvalList.appendChild(listItem);
            });
        }

        // 초기 표시
        displayApprovals(allLists);

        // 이벤트 리스너 추가
        document.getElementById('startDate').addEventListener('change', filterApprovals);
        document.getElementById('endDate').addEventListener('change', filterApprovals);
        document.getElementById('typeFilter').addEventListener('change', filterApprovals);
        document.getElementById('statusFilter').addEventListener('change', filterApprovals);

    } catch (error) {
        console.error('Fetch error:', error);
    }
}

async function showForm(documentNo) {
    // 페이지 이동을 위해 URL을 설정합니다.
    const url = `/readVacRequest?no=${documentNo}`; // documentNo를 포함한 URL

    // 페이지를 해당 URL로 이동합니다.
    window.location.href = url;
}

// 페이지가 로드될 때 결재 정보 가져오기
window.onload = fetchApprovalData;
