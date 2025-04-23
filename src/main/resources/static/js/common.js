/**
 * 显示消息提示
 * @param message
 * @param type
 */
function showToast(message, type = 'success') {
    const toastHtml = `
       <div class="toast align-items-center text-bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
           <div class="d-flex">
               <div class="toast-body">${message}</div>
               <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
           </div>
       </div>`;

    $('#toastContainer').append(toastHtml);
    const toastEl = new bootstrap.Toast($('#toastContainer .toast').last()[0]);
    toastEl.show();
    setTimeout(() => {
        $('#toastContainer .toast').first().remove();
    }, 5000);
}

/**
 * 显示退出登录确认框
 */
function showLogoutConfirm() {
    const toast = new bootstrap.Toast(document.getElementById('logoutToast'));
    toast.show();
}

function confirmLogout() {
    window.location.href = '/auth/logout';
}

/**
 * HTML转义
 * @param unsafe
 * @returns {*|string}
 */
function escapeHtml(unsafe) {
    if (unsafe === null || unsafe === undefined) {
        return ''; // 将 null 或 undefined 转换为空字符串
    }
    if (typeof unsafe !== 'string') {
        unsafe = String(unsafe); // 将非字符串类型转换为字符串
    }
    return unsafe.replace(/[&<>"']/g, c => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    }[c] || c));
}

