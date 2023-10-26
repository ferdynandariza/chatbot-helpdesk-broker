(function(){

    getAuthenticationToken();

    clickDeleteButton();
    
    clickCategoryOption();

}());

function clickDeleteButton(){
    $('.delete-button').each(function(){
        let dataName = $(this).attr('data-name');
        let url = $(this).attr('href');

        $(this).click(function(event){
            event.preventDefault();
            $('.name').text(dataName.trim());
            $('.yes').attr('href', url);
            $('.modal').addClass('pops');
            clickCancelButton();
        });
    });
}

function clickCancelButton(){
    $('.cancel').click(function(event){
        event.preventDefault();
        $('.modal').attr('class', 'modal');
    });
}

function clickCategoryOption(){
    $('.category-select').change(function(event) {
        console.log(this.value);
        getBookByCategory(this.value);
    });
}


function getAuthenticationToken(){
    let endPoint = `http://localhost:7072/api/account`;
    let payload = {
        "username": "ferdy",
        "password": "indocyber",
        "subject": "JWT for winterhold",
        "audience": "MVC",
        "secretKey": "friday is a good day to die, are you willing to die this friday?"
    };
    $.ajax({
        type: "POST",
        url: endPoint,
        data: JSON.stringify(payload),
        contentType: "application/json",
        success: function (response){
            saveAuthenticationToken(response);
        }
    });
}

function saveAuthenticationToken({token, username}){
    sessionStorage.setItem("token", token);
    sessionStorage.setItem("username", username);
}

function getBearerToken(){
    let token = sessionStorage.getItem("token");
    let bearerToken = {
        "Authorization" : `Bearer ${token}`
    }
    return bearerToken;
}

function getBookByCategory(category){
    let endPoint = `http://localhost:7072/api/cascading?category=${category}`;

    $.ajax({
        type: "GET",
        url: endPoint,
        crossDomain: true,
        headers: getBearerToken(),
        success: function (response){
            renderBooks(response);
        }
    });
}

function renderBooks(books){
    let htmlString = '<option>Select a book</option>';
    for(let {value, text} of books){
        let row = `
            <option value="${value}">${text}</option>
            `;
        htmlString = htmlString + row;
    }
    $('.cascade-select').html(htmlString);
}

