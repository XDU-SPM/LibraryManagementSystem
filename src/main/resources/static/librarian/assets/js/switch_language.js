function switch_language(lang) {
    $.ajax(
        {
            method: 'get',
            url: 'changeSessionLanguage',
            data: {lang: lang},
            success: function () {
                history.go(-1);
            }
        }
    );
}