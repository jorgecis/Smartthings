/**
 *  Remotsy
 *
 *  Copyright 2016 Jorge Cisneros
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

// Automatically generated. Make future change here.
definition(
    name: "Remotsy",
    namespace: "jorgeci",
    author: "jorgecis@gmail.com.com",
    description: "Manager the controls from Remotsy",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: true,
    singleInstance: true)

preferences {
	page(name: "auth", title: "Remotsy", nextPage:"", content:"authPage", uninstall: true, install:true)
}

mappings {
    path("/oauth/initialize") {action: [GET: "oauthInitUrl"]}
    path("/oauth/callback") {action: [GET: "callback"]}
}

private getVendorName() 	{ "Remotsy" }
private getVendorAuthPath()	{ "https://www.remotsy.com/oauth/authorize?" }
private getVendorTokenPath(){ "https://www.remotsy.com/oauth/token.php?" }
private getClientId() 		{ "smartthings" } 
private getClientSecret() 	{ "kjgsdfslskudfgj" }
private getCallbackUrl()    { return "https://graph.api.smartthings.com/oauth/callback" }
String toQueryString(Map m) {
        return m.collect { k, v -> "${k}=${URLEncoder.encode(v.toString())}" }.sort().join("&")
}


def authPage() {
   // Check to see if our SmartApp has it's own access token and create one if not.
    if(!state.accessToken) {
        // the createAccessToken() method will store the access token in state.accessToken
        createAccessToken()
    }
    log.debug state.authToken
    def redirectUrl = "https://graph.api.smartthings.com/oauth/initialize?appId=${app.id}&access_token=${state.accessToken}&apiServerUrl=${getApiServerUrl()}"
    // Check to see if we already have an access token from the 3rd party service.
    if(!state.authToken) {
		return dynamicPage(name:"auth", title: "Login", nextPage: "", uninstall:uninstallAllowed) {
			section() {
				paragraph "Tap below to log in to the Remotsy service and authorize SmartThings access. Be sure to scroll down on page 2 and press the 'Allow' button."
				href url:redirectUrl, style:"embedded", required:true, title:"Remotsy", description:description
			}
		}
    } else {
    	return dynamicPage(name: "auth", title: "Ok", nextPage: "", uninstall:uninstallAllowed) {
			section() {
				paragraph "Hope"
				
			}
		}
        // We have the token, so we can just call the 3rd party service to list our devices and select one to install.
    }
}

def oauthInitUrl() {
    state.oauthInitState = UUID.randomUUID().toString()
    def oauthParams = [
        response_type: "code",
        scope: "email",
        client_id: getClientId(),
        client_secret: getClientSecret(),
        state: state.oauthInitState,
        redirect_uri: getCallbackUrl()
    ]
    redirect(location: getVendorAuthPath() + "${toQueryString(oauthParams)}")
}


def callback() {
    def code = params.code
    def oauthState = params.state

    // Validate the response from the 3rd party by making sure oauthState == state.oauthInitState as expected
    if (oauthState == state.oauthInitState){
        def tokenParams = [
            grant_type: "authorization_code",
            code      : code,
            client_id : getClientId(),
            client_secret: getClientSecret(),
            redirect_uri: getCallbackUrl()
        ]
        
        try {
    		httpPost(getVendorTokenPath(), toQueryString(tokenParams)) { resp ->
        	log.debug "response data: ${resp.data}"
        	log.debug "response contentType: ${resp.contentType}"
            state.refreshToken = resp.data.refresh_token
            state.authToken = resp.data.access_token
    	}
		} catch (e) {
    		log.debug "something went wrong: $e"
		}

        if (state.authToken) {
            // call some method that will render the successfully connected message
            success()
        } else {
            // gracefully handle failures
            fail()
        }

    } else {
        log.error "callback() failed. Validation of state did not match. oauthState != state.oauthInitState"
    }
}

// Example success method
def success() {
        def message = """
                <p>Your account is now connected to SmartThings!</p>
                <p>Click 'Done' to finish setup.</p>
        """
        displayMessageAsHtml(message)
}

// Example fail method
def fail() {
    def message = """
        <p>There was an error connecting your account with SmartThings</p>
        <p>Please try again.</p>
    """
    displayMessageAsHtml(message)
}

def displayMessageAsHtml(message) {
    def html = """
        <!DOCTYPE html>
        <html>
            <head>
            </head>
            <body>
                <div>
                    ${message}
                </div>
            </body>
        </html>
    """
    render contentType: 'text/html', data: html
}