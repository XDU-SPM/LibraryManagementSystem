function switch_language(lang) {
    $.ajax(
        {
            method: 'get',
            url: 'changeSessionLanguage',
            data: {lang: lang},
            success: function () {
                window.location.reload();
            }
        }
    );
}