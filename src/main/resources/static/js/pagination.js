function updatePagination(currentPage, totalPages, totalElements, callback) {
    const pagination = $("#pagination");
    pagination.empty();

    if (totalPages === 0) {
        pagination.hide();
        return;
    }
    pagination.show();

    const paginationHtml = [];
    paginationHtml.push(`<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="0">首页</a></li>`);

    if (currentPage > 0) {
        paginationHtml.push(`<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="${currentPage - 1}">上一页</a></li>`);
    }

    // 显示当前页附近的页码
    const visiblePages = Math.min(5, totalPages); // 每次最多显示 5 个页码
    const startPage = Math.max(0, currentPage - Math.floor(visiblePages / 2));
    const endPage = Math.min(totalPages - 1, startPage + visiblePages - 1);

    if (startPage > 0) {
        paginationHtml.push(`<li class="page-item disabled"><span class="page-link">...</span></li>`);
    }

    for (let i = startPage; i <= endPage; i++) {
        paginationHtml.push(`<li class="page-item ${i === currentPage ? 'active' : ''}"><a class="page-link" href="javascript:void(0)" data-page="${i}">${i + 1}</a></li>`);
    }

    if (endPage < totalPages - 1) {
        paginationHtml.push(`<li class="page-item disabled"><span class="page-link">...</span></li>`);
    }

    if (currentPage < totalPages - 1) {
        paginationHtml.push(`<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="${currentPage + 1}">下一页</a></li>`);
    }

    paginationHtml.push(`<li class="page-item"><a class="page-link" href="javascript:void(0)" data-page="${totalPages - 1}">尾页</a></li>`);

    pagination.html(paginationHtml.join(''));

    // 绑定点击事件
    pagination.off('click', '.page-link').on('click', '.page-link', function() {
        const page = parseInt($(this).data('page'), 10);
        callback(page);
    });
}
