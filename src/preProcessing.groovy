
    binding.bTranslate = engineEnvironment.getParameter('translate');

    if(binding.bTranslate){

        def props = binding.mTranslationProperties

        def url = props.msUrl

        def queryMap = [
                "api-version"  : props.msApiVersion,
                "to"           : props.solutionLanguage,
                "suggestedFrom": props.translationLanguage
        ]

        def headerMap = [
                "Ocp-Apim-Subscription-Key"   : props.msSubscriptionKey,
                "Ocp-Apim-Subscription-Region": props.msSubscriptionRegion,
                "Content-Type"                : "application/json; charset=UTF-8"
        ]

        def body = [
                ["Text": _.getUserInputText()]
        ]


        def translation = RestClient.post(url, queryMap, headerMap, body)

        if(translation){
            _.setUserInputText(translation[0].translations[0].text)
        }
    }
