document.addEventListener('DOMContentLoaded', () => {
    // 데이터 가져오기
    fetch('/api/selectAttedanceRequest') // 여기에 API 엔드포인트를 입력하세요
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // JSON 형태로 응답을 변환
        })
        .then(data => {
            const itemList = document.getElementById('item-list');
            data.forEach(item => {
                const li = document.createElement('li'); // li 요소 생성

                // workDate에 하루 더하기
                const originalDate = new Date(item.workDate);
                originalDate.setDate(originalDate.getDate() + 1); // 하루 더하기
                const workDate = originalDate.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환

                const modifyTime = new Date(item.modifyTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
                const workEndTime = new Date(item.workEndTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

                // 진행상태 변환
                const progressStatusMap = {
                    'SU1': { text: '대기중', class: 'label-gray' },
                    'SU2': { text: '확인중', class: 'label-blue' },
                    'SU3': { text: '승인됨', class: 'label-green' },
                    'SU4': { text: '반려됨', class: 'label-red' }
                };
                const progressStatus = progressStatusMap[item.progressCode] || { text: '', class: '' };

                li.innerHTML = `
                   <input type="checkbox" class="item-checkbox" data-id="${workDate}">
                    
                    <div class="request-date">
                        <span class="label">요<span class="space"></span>청<span class="space"></span>일<span class="space"></span>자</span>
                        <span class="value">${workDate}</span>
                    </div>
                    <div class="division">
                        <span class="label">구<span class="space2"></span>분</span>
                        <span class="value">${item.division}</span>
                    </div>
                    <div class="time-info">
                        <span class="label">수정 전 근태시간</span>
                        <span class="value before-time">${workEndTime}</span>
                    </div>
                    <div class="time-info">
                        <span class="label">수정 후 근태시간</span>
                        <span class="value after-time">${modifyTime}</span>
                    </div>
                    <div class="admin-code">
                        <span class="label">관리자 사번 코드</span>
                        <span class="value">${item.adminCode}</span>
                    </div>
                    <div class="progress-status">
                        <div class="label-message ${progressStatus.class}">
                            ${progressStatus.text}
                        </div>
                    </div>
                `;

                // 체크박스 클릭 시 모달 열리지 않도록 이벤트 전파 방지
                const checkbox = li.querySelector('.item-checkbox');
                checkbox.addEventListener('click', (event) => {
                    event.stopPropagation(); // 이벤트 전파 방지
                });

                // li 클릭 시 모달 열기
                li.addEventListener('click', () => {
                    document.getElementById('modal-reason').textContent = item.workModifyReason;
                    // 내 의견 부분 삭제
                    document.getElementById('admin-opinion').textContent = item.managerIdea || '의견이 없습니다.'; // 관리자 의견 불러오기
                    document.getElementById('attendance-modal').style.display = 'block';

                    console.log('관리자의견', item.managerIdea);
                });

                itemList.appendChild(li); // ul에 li 추가
            });
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });


// 삭제 버튼 클릭 시 선택된 항목 삭제
    document.getElementById('delete-button').addEventListener('click', () => {
        const checkboxes = document.querySelectorAll('.item-checkbox:checked');
        const idsToDelete = Array.from(checkboxes).map(checkbox => checkbox.getAttribute('data-id'));


        if (idsToDelete.length > 0) {
            // 삭제 확인
            const confirmDelete = confirm("정말로 삭제하시겠습니까?");
            if (confirmDelete) {
                // 삭제 요청
                fetch('/api/deleteAttendanceRequest', {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ ids: idsToDelete }), // 삭제할 ID 배열
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        } else {
                            checkboxes.forEach(checkbox => {
                                checkbox.parentElement.remove(); // 체크된 항목 삭제
                            });
                        }
                        location.href = '/attendance-request';
                    })
                    .catch(error => {
                        console.error('There was a problem with the delete operation:', error);
                    });
            } else {
                alert('삭제가 취소되었습니다.');
            }
        } else {
            alert('삭제할 항목을 선택하세요.');
        }
    });


    // 모달 닫기 기능
    document.querySelector('.modal-close-button').addEventListener('click', () => {
        document.getElementById('attendance-modal').style.display = 'none';
    });

    // 모달 외부 클릭 시 닫기
    window.addEventListener('click', (event) => {
        const modal = document.getElementById('attendance-modal');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });
});
