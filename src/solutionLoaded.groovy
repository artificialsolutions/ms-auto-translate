import groovy.json.JsonSlurper
import groovy.json.JsonBuilder

binding.bTranslate = true
binding.mTranslationProperties = [
        msUrl: "https://api.cognitive.microsofttranslator.com/translate",
        solutionLanguage: "en",
        translationLanguage: "es",
        msApiVersion: "3.0",
        msSubscriptionKey :  "********",
        msSubscriptionRegion: "northeurope"
]

    static def get(String url, Map query, Map headers) {
        def requestProperties = normalizeRequestProperties(url, query, headers, null)
        def conn = new URL(requestProperties.url).openConnection()
        conn.setRequestMethod("GET")
        requestProperties.headers.each {
            conn.setRequestProperty(it.key as String, it.value as String)
        }
        def responseCode = conn.getResponseCode()
        if (responseCode.equals(200)) {
            println(responseCode)
            def responseContentType = conn.getContentType()
            println(responseContentType)
            def responseBody = conn.getInputStream().getText()

            return parseResponseBody(responseContentType, responseBody)
        } else {
            println( "GET call failed with code: " + responseCode.toString())
            return false
        }
    }

    static def post(String url, Map<String, String> query, Map<String, String> headers, def body) {

        def requestProperties = normalizeRequestProperties(url, query, headers, body)
        def conn = new URL(requestProperties.url).openConnection()
        conn.setRequestMethod("POST")
        requestProperties.headers.each {
            conn.setRequestProperty(it.key as String, it.value as String)
        }
        conn.setDoOutput(true)
        conn.getOutputStream().write(requestProperties.body.getBytes("UTF-8"))
        def responseCode = conn.getResponseCode()
        if (responseCode.equals(200)) {
            println(responseCode)
            def responseContentType = conn.getContentType()
            println(responseContentType)
            def responseBody = conn.getInputStream().getText()

            return parseResponseBody(responseContentType, responseBody)
        } else {
            println("POST call failed with code: " + responseCode.toString())
            return false
        }

    }

    private static def parseResponseBody(responseContentType, responseBody) {
        def parsedBody
        switch (responseContentType) {
            case ~/application\/json((;)?(\s)?(charset=).*)?/:
                def slurper = new JsonSlurper()
                parsedBody = slurper.parseText(responseBody)
                break
            default:
                parsedBody = [responseBody: responseBody]
        }

        return parsedBody
    }

    private static def normalizeRequestProperties(url, query, headers, body) {
        def normalizedProperties = ["url": url, headers: headers]

        //Add query to url
        if (query.size() > 0) {
            normalizedProperties.put("url", url + '?' + makeEncodedString(query))
        }


        //Encode body
        if (body) {
            switch (headers["Content-Type"]) {
                case "application/x-www-form-urlencoded":
                    normalizedProperties.put("body", makeEncodedString(body))
                    break
                case ~/application\/json((;)?(\s)?(charset=).*)?/:
                    normalizedProperties.put("body", new JsonBuilder(body).toString())
                    break
                default:
                    normalizedProperties.put("body", body)
            }
        }


        println(normalizedProperties)
        return normalizedProperties

    }

    private static def makeEncodedString(map) {
        StringBuilder bodyString = new StringBuilder("")
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (bodyString.toString().length() != 0) {
                bodyString.append('&')
            }
            bodyString.append(URLEncoder.encode(item.getKey(), "UTF-8"))
            bodyString.append('=')
            bodyString.append(URLEncoder.encode(item.getValue().toString(), "UTF-8"))
        }
        return bodyString.toString()
    }


