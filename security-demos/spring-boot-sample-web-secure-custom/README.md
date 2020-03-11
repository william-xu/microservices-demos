## 备忘

异常模拟页面，需登录后才可访问
http://192.168.43.104:9101/foo


有涉及csrf攻击防范相关知识

比如用户登录银行系统A进行转账后没有登出，然后被其他页面诱导到与恶意网站，当你点击某些按钮时会读取用户在系统A的Cookie并以正确的参数调用系统A的转账接口将提交转账命令，如果成功则你的钱就被转走了。


Assume that your bank’s website provides a form that allows transferring money from the currently logged in user to another bank account. For example, the transfer form might look like:

<form method="post"
    action="/transfer">
<input type="text"
    name="amount"/>
<input type="text"
    name="routingNumber"/>
<input type="text"
    name="account"/>
<input type="submit"
    value="Transfer"/>
</form>

the corresponding HTTP request might look like:

Example 28. Transfer HTTP request
POST /transfer HTTP/1.1
Host: bank.example.com
Cookie: JSESSIONID=randomid
Content-Type: application/x-www-form-urlencoded

amount=100.00&routingNumber=1234&account=9876


Now pretend **you authenticate to your bank’s website and then, without logging out, visit an evil website.** The evil website contains an HTML page with the following form:


Example 29. Evil transfer form
<form method="post"
    action="https://bank.example.com/transfer">
<input type="hidden"
    name="amount"
    value="100.00"/>
<input type="hidden"
    name="routingNumber"
    value="evilsRoutingNumber"/>
<input type="hidden"
    name="account"
    value="evilsAccountNumber"/>
<input type="submit"
    value="Win Money!"/>
</form>

You like to win money, so you click on the submit button. In the process, you have unintentionally transferred $100 to a malicious user. This happens because, **while the evil website cannot see your cookies, the cookies associated with your bank are still sent along with the request.**

Worst yet, this whole process could have been automated using JavaScript. This means you didn’t even need to click on the button. Furthermore, it could just as easily happen when visiting an honest site that is a victim of a XSS attack. So how do we protect our users from such attacks?



**To protect against CSRF attacks we need to ensure there is something in the request that the evil site is unable to provide so we can differentiate the two requests.**



Spring provides two mechanisms to protect against CSRF attacks:

The Synchronizer Token Pattern

Specifying the SameSite Attribute on your session cookie


## Synchronizer Token Pattern

The predominant and most comprehensive way to protect against CSRF attacks is to use the Synchronizer Token Pattern. This solution is to ensure that each HTTP request requires, in addition to our session cookie, a secure random generated value called a CSRF token must be present in the HTTP request.

When an HTTP request is submitted, the server must look up the expected CSRF token and compare it against the actual CSRF token in the HTTP request. If the values do not match, the HTTP request should be rejected.

**The key to this working is that the actual CSRF token should be in a part of the HTTP request that is not automatically included by the browser. For example, requiring the actual CSRF token in an HTTP parameter or an HTTP header will protect against CSRF attacks. Requiring the actual CSRF token in a cookie does not work because cookies are automatically included in the HTTP request by the browser.**

We can relax the expectations to only require the actual CSRF token for each HTTP request that updates state of the application. For that to work, our application must ensure that safe HTTP methods are idempotent. This improves usability since we want to allow linking to our website using links from external sites. Additionally, we do not want to include the random token in HTTP GET as this can cause the tokens to be leaked.

Let’s take a look at how our example would change when using the Synchronizer Token Pattern. Assume the actual CSRF token is required to be in an HTTP parameter named _csrf. Our application’s transfer form would look like:

Example 30. Synchronizer Token Form
<form method="post"
    action="/transfer">
<input type="hidden"
    name="_csrf"
    value="4bfd1575-3ad1-4d21-96c7-4ef2d9f86721"/>
<input type="text"
    name="amount"/>
<input type="text"
    name="routingNumber"/>
<input type="hidden"
    name="account"/>
<input type="submit"
    value="Transfer"/>
</form>
The form now contains a hidden input with the value of the CSRF token. External sites cannot read the CSRF token since the same origin policy ensures the evil site cannot read the response.

The corresponding HTTP request to transfer money would look like this:

Example 31. Synchronizer Token request
POST /transfer HTTP/1.1
Host: bank.example.com
Cookie: JSESSIONID=randomid
Content-Type: application/x-www-form-urlencoded

amount=100.00&routingNumber=1234&account=9876&_csrf=4bfd1575-3ad1-4d21-96c7-4ef2d9f86721
You will notice that the HTTP request now contains the _csrf parameter with a secure random value. The evil website will not be able to provide the correct value for the _csrf parameter (which must be explicitly provided on the evil website) and the transfer will fail when the server compares the actual CSRF token to the expected CSRF token.




## SameSite Attribute

An emerging way to protect against CSRF Attacks is to specify the SameSite Attribute on cookies. A server can specify the SameSite attribute when setting a cookie to indicate that the cookie should not be sent when coming from external sites.

Spring Security does not directly control the creation of the session cookie, so it does not provide support for the SameSite attribute. Spring Session provides support for the SameSite attribute in servlet based applications. Spring Framework’s CookieWebSessionIdResolver provides out of the box support for the SameSite attribute in WebFlux based applications.













