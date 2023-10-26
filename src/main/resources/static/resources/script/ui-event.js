(function () {

    getData("", 0, 10);

    getCompanies();
}());

function renderGrid({grid, paging}, currentPage) {
    let current = $(".current-page");
    current.text(currentPage + 1);
    current.attr("data-total", paging.totalPages);
    let htmlString = "";
    for (let {id, ticketNumber, requestorName, description, createdAt} of grid) {
        let row = `
            <tr>
                <td>${ticketNumber}</td>
                <td>${requestorName}</td>
                <td>${description}</td>
                <td>${epochToDate(createdAt)}</td>
                <td>
                    <button class="edit-button" data-id=${id}>Edit</button>
                    <button class="delete-button" data-id=${id}>Delete</button>
                </td>
            </tr>`;
        htmlString = htmlString + row;
    }
    $(".data-grid tbody").html(htmlString);
}

function epochToDate(epoch) {
    // var epoch = epoch + (new Date().getTimezoneOffset() * -1);
    let date = new Date(epoch);
    return date.toLocaleDateString();
}


function renderDropdown({grid}) {
    let htmlString = "<option>Pilih perusahaan..</option>";
    for (let {id, name} of grid) {
        console.log(name);
        let row = `
            <option value="${id}">${name}</option>
`;
        htmlString = htmlString + row;
    }
    $("select#company").html(htmlString);
}
