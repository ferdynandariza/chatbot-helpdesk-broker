let ipPort = "";//window.location.hostname;
let ticketsEndPoint = `${ipPort}/api/tickets`
let companiessEndPoint = `${ipPort}/api/companies`


function getData(number, page, size){
    let endPoint = `${ticketsEndPoint}?number=${number}&page=${page}&size=${size}`;
    $.ajax({
        url: endPoint,
        crossDomain: true,
        // headers: getBearerToken(),
        success: function ({data, paging}){
            let pageObject = {grid: data, paging:paging};
            renderGrid(pageObject, page);
        }
    });
}

function getCompanies(){
    let endPoint = `${companiessEndPoint}`;
    $.ajax({
        url: endPoint,
        crossDomain: true,
        // headers: getBearerToken(),
        success: function ({data}){
            let pageObject = {grid: data};
            renderDropdown(pageObject);
        }
    });
}