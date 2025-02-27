
BreninSul Servlet interceptor with Spring Boot starter.

Logging filter implementation and stater to auto register it in Spring context




| Parameter                                                                  | Type             | Description                                                             |
|----------------------------------------------------------------------------|------------------|-------------------------------------------------------------------------|
| `servlet.logging-interceptor.enabled`                                      | Boolean          | Enable autoconfig for this starter                                      |
| `servlet.logging-interceptor.resolve-handler-annotation`                   | Boolean          | Enable resolving annotation @ServletLoggingFilter  on controllers       |
| `servlet.logging-interceptor.logging-level`                                | JavaLoggingLevel | Base Logging level of messages                                          |
| `servlet.logging-interceptor.order`                                        | Int              | Filter order (Ordered interface)                                        |
| `servlet.logging-interceptor.new-line-column-symbols`                      | Int              | How many symbols in first column (param name)                           |
| `servlet.logging-interceptor.request.logging-level`                        | Boolean          | Logging level (request) if not equals the base one                      |
| `servlet.logging-interceptor.request.id-included`                          | Boolean          | Is request id included to log message (request)                         |
| `servlet.logging-interceptor.request.uri-included`                         | Boolean          | Is uri included to log message (request)                                |
| `servlet.logging-interceptor.request.took-time-included`                   | Boolean          | Is timing included to log message (request)                             |
| `servlet.logging-interceptor.request.headers-included`                     | Boolean          | Is headers included to log message (request)                            |
| `servlet.logging-interceptor.request.body-included`                        | Boolean          | Is body included to log message (request)                               |
| `servlet.logging-interceptor.request.max-body-size`                        | Int              | Max logging body size   (request)                                       |
| `servlet.logging-interceptor.request.body-size-to-use-temp-file-caching`   | Long             | Body size to use temp file caching instead of memory (request)          |
| `servlet.logging-interceptor.request.mask.mask-headers`                    | String           | Comma separated headers to mask in logs (request)                       |
| `servlet.logging-interceptor.request.mask.mask-query-parameters`           | String           | Comma separated query parameters to mask in logs (request/response)     |
| `servlet.logging-interceptor.request.mask.mask-mask-json-body-keys`        | String           | Comma separated body json keys(fields) to mask in logs (request)        |
| `servlet.logging-interceptor.request.mask.mask-mask-form-urlencoded-body`  | String           | Comma separated form urlencoded keys(fields) to mask in logs (request)  |
| `servlet.logging-interceptor.response.logging-level`                       | Boolean          | Logging level (response) if not equals the base one                     |
| `servlet.logging-interceptor.response.id-included`                         | Boolean          | Is request id included to log message (response)                        |
| `servlet.logging-interceptor.response.uri-included`                        | Boolean          | Is uri included to log message (response)                               |
| `servlet.logging-interceptor.response.took-time-included`                  | Boolean          | Is timing included to log message (response)                            |
| `servlet.logging-interceptor.response.headers-included`                    | Boolean          | Is headers included to log message (response)                           |
| `servlet.logging-interceptor.response.body-included`                       | Boolean          | Is body included to log message (response)                              |
| `servlet.logging-interceptor.response.max-body-size`                       | Int              | Max logging body size   (response)                                      |
| `servlet.logging-interceptor.response.mask.mask-headers`                   | String           | Comma separated headers to mask in logs (response)                      |
| `servlet.logging-interceptor.response.mask.mask-mask-json-body-keys`       | String           | Comma separated body json keys(fields) to mask in logs (response)       |
| `servlet.logging-interceptor.response.mask.mask-mask-form-urlencoded-body` | String           | Comma separated form urlencoded keys(fields) to mask in logs (response) |


You can additionally configure logging for each request by passing headers from `io.github.breninsul.rest.logging.RestTemplateConfigHeaders` to request


add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:servlet-logging-starter:2.1.2")
//Other dependencies
}

````
### Example of log messages

````
===========================Servlet Request begin===========================
=ID           : 4079-74
=URI          : POST /test
=Headers      : authorisation:<MASKED>;user-agent:PostmanRuntime/7.40.0;accept:*/*;postman-token:55500dec-8c8e-4925-abe0-350b4c1cc594;host:127.0.0.1:8080;accept-encoding:gzip, deflate, br;connection:keep-alive;content-type:multipart/form-data; boundary=--------------------------468571742219419283376118;content-length:57001384
=Body         : sosadsadasd:application/zip:<FILE 3846228 bytes>;sss:text/plain:asdsad
===========================Servlet Request end  ===========================

2024-07-11T16:50:06.489+02:00  INFO 16109 --- [nio-8080-exec-1] i.g.b.s.logging.ServletLoggingFilter     : 
===========================Servlet Response begin===========================
=ID           : 4079-74
=URI          : 200 POST /test
=Took         : 229 ms
=Headers      : TestHeader:TestVal;RQ_ID:4079-74;Content-Length:5
=Body         : {"token":"<MASKED>","status":"OK"}
===========================Servlet Response end  ===========================
````

### Example defining custom properties

````properties

servlet.logging-interceptor.request.logging-level=severe
servlet.logging-interceptor.request.id-included=false
servlet.logging-interceptor.request.uri-included=false
servlet.logging-interceptor.request.took-time-included=false
servlet.logging-interceptor.request.headers-included=false
servlet.logging-interceptor.request.body-included=false
servlet.logging-interceptor.response.logging-level=severe
servlet.logging-interceptor.response.id-included=false
servlet.logging-interceptor.response.uri-included=false
servlet.logging-interceptor.response.took-time-included=false
servlet.logging-interceptor.response.headers-included=false
servlet.logging-interceptor.response.body-included=false
servlet.logging-interceptor.request.mask.mask-headers=headerToMask
servlet.logging-interceptor.request.mask.mask-query-parameters=queryParamToMask
servlet.logging-interceptor.request.mask.mask-body-keys.form=formKeyToMask
servlet.logging-interceptor.request.mask.mask-body-keys.json=jsonKeyToMask
servlet.logging-interceptor.response.mask.mask-headers=headerToMask
servlet.logging-interceptor.response.mask.mask-query-parameters=queryParamToMask
servlet.logging-interceptor.response.mask.mask-body-keys.form=formKeyToMask
servlet.logging-interceptor.response.mask.mask-body-keys.json=jsonKeyToMask

````

### Example defining route config (Kotlin)

[Example defining Route config (Kotlin)](src/test/kotlin/io/github/breninsul/servlet/logging2/examples/ExampleKotlinRoutes.kt)
[Example defining Route config (Java)](src/test/kotlin/io/github/breninsul/servlet/logging2/examples/ExampleJavaRoutes.java)

[Example defining Controller annotation config (Kotlin)](src/test/kotlin/io/github/breninsul/servlet/logging2/examples/ExampleKotlinController.kt)
[Example defining Controller annotation config (Kotlin)](src/test/kotlin/io/github/breninsul/servlet/logging2/examples/ExampleJavaController.java)



