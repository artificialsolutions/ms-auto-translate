class Main {
    static void main(String[] args) {


        def url = "https://api.cognitive.microsofttranslator.com/translate"

        def queryMap = [
                "api-version"  : "3.0",
                "to"           : "en",
                "suggestedFrom": "es"
        ]

        def headerMap = [
                "Ocp-Apim-Subscription-Key"   : "fd0515d67c90408f885395a2c4d67e2b",
                "Ocp-Apim-Subscription-Region": "northeurope",
                "Content-Type"                : "application/json; charset=UTF-8"
        ]

        def body = [
                ["Text":"Muy buenas tardes."]
        ]


       def translation = solutionLoaded.post(url, queryMap, headerMap, body)[0].translations[0].text

        if(translation) {
            println(translation)
        }
        else{
           println(body[0].Text)
        }





    }
}
