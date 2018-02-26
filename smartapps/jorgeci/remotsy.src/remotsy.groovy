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
    page(name: "Credentials", title: "Remotsy", content: "authPage", install: false)
}

mappings {
	path("/receivedToken") 	{ action: [ POST: "receivedToken", GET: "receivedToken"] }
	path("/receiveToken") 	{ action: [ POST: "receiveToken", GET: "receiveToken"] }
}

private getVendorName() 	{ "Remotsy" }
private getVendorAuthPath()	{ "https://www.remotsy.com:8443/oauth/authorize?" }
private getVendorTokenPath(){ "https://www.remotsy.com:8443/oauth/token" }
private getClientId() 		{ "smartthings" } 
private getClientSecret() 	{ "kjgsdfslskudfgj" }
private getVendorIcon()		{ "https://s3.amazonaws.com/smartthings-device-icons/custom/super-widgets/beertap@2x.png" }
private getServerUrl() 		{ return "https://graph-na04-useast2.api.smartthings.com" }



def authPage() {
	log.debug "In authPage"
    def description = null  
    if (state.vendorAccessToken == null) {   
        log.debug "About to create access token."
        createAccessToken()
        description = "Tap to enter Credentials."
        def redirectUrl = oauthInitUrl()
        return dynamicPage(name: "Credentials", title: "Authorize Connection", nextPage: null, uninstall: false, install:false) {
               section { href url:redirectUrl, style:"embedded", required:false, title:"Connect to ${getVendorName()}:", description:description }
        }
    } else {
    	description = "Press 'Done' to proceed" 
 		
		return dynamicPage(name: "Credentials", title: "Credentials Accepted!", nextPage: null, uninstall: true, install:true) {
               section { href url: buildRedirectUrl("receivedToken"), style:"embedded", required:false, title:"${getVendorName()} is now connected to SmartThings!", description:description }
        }
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
        redirect_uri: buildRedirectUrl("receiveToken")
    ]
    return getVendorAuthPath() + toQueryString(oauthParams)
}

def buildRedirectUrl(endPoint) {
	log.debug "In buildRedirectUrl"
    log.debug getServerUrl()+ "/api/token/${state.accessToken}/smartapps/installations/${app.id}/${endPoint}"
    return getServerUrl() + "/api/token/${state.accessToken}/smartapps/installations/${app.id}/${endPoint}"
}

def receiveToken() {
	log.debug "In receiveToken"
    def oauthParams = [ client_id: getClientId(),
                        client_secret: getClientSecret(),
    				    grant_type: "authorization_code", 
                        code: params.code,
                        redirect_uri: buildRedirectUrl("receiveToken")]                        
	httpPost(getVendorTokenPath(), toQueryString(oauthParams)) { response -> 
    	state.vendorRefreshToken = response.data.refresh_token
        state.vendorAccessToken = response.data.access_token
	}
     
    if ( !state.vendorAccessToken ) {  //We didn't get an access token, bail on install
    	return
    }
    
    /* OAuth Step 3: Use the access token to call into the vendor API throughout your code using state.vendorAccessToken. */
       
    def html = """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name=viewport content="width=300px, height=100%">
        <title>${getVendorName()} Connection</title>
        <style type="text/css">
            @font-face {
                font-family: 'Swiss 721 W01 Thin';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.svg#swis721_th_btthin') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            @font-face {
                font-family: 'Swiss 721 W01 Light';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.svg#swis721_lt_btlight') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            .container {
                width: 560px;
                padding: 0px;
                /*background: #eee;*/
                text-align: center;
            }
            img {
                vertical-align: middle;
            }
            img:nth-child(2) {
                margin: 0 30px;
            }
            p {
                font-size: 2.2em;
                font-family: 'Swiss 721 W01 Thin';
                text-align: center;
                color: #666666;
                padding: 0 40px;
                margin-bottom: 0;
            }
        /*
            p:last-child {
                margin-top: 0px;
            }
        */
            span {
                font-family: 'Swiss 721 W01 Light';
            }
        </style>
        </head>
        <body>
            <div class="container">
                <img src=""" + getVendorIcon() + """ alt="Vendor icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/connected-device-icn%402x.png" alt="connected device icon" />
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/st-logo%402x.png" alt="SmartThings logo" />
                <p>We have located your """ + getVendorName() + """ account.</p>
                <p>Tap 'Done' to process your credentials.</p>
			</div>
        </body>
        </html>
        """
	render contentType: 'text/html', data: html
}

def receivedToken() {
	log.debug "In receivedToken"
    
    def html = """
        <!DOCTYPE html>
        <html>
        <head>
        <meta name="viewport" content="100%">
        <title>Withings Connection</title>
        <style type="text/css">
            @font-face {
                font-family: 'Swiss 721 W01 Thin';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-thin-webfont.svg#swis721_th_btthin') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            @font-face {
                font-family: 'Swiss 721 W01 Light';
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot');
                src: url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.eot?#iefix') format('embedded-opentype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.woff') format('woff'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.ttf') format('truetype'),
                     url('https://s3.amazonaws.com/smartapp-icons/Partner/fonts/swiss-721-light-webfont.svg#swis721_lt_btlight') format('svg');
                font-weight: normal;
                font-style: normal;
            }
            .container {
                width: 100%;
                padding: 0px;
                /*background: #eee;*/
                text-align: center;
            }
            img {
                vertical-align: middle;
            }
            img:nth-child(2) {
                margin: 0 10px;
            }
            p {
                font-size: 1.5em;
                font-family: 'Swiss 721 W01 Thin';
                text-align: center;
                color: #666666;
                padding: 0 40px;
                margin-bottom: 0;
            }
        /*
            p:last-child {
                margin-top: 0px;
            }
        */
            span {
                font-family: 'Swiss 721 W01 Light';
            }
        </style>
        </head>
        <body>
            <div class="container">
                <img src=""" + getVendorIcon() + """ alt="Vendor icon" style="width: 30%;max-height: 30%"/>
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/connected-device-icn%402x.png" alt="connected device icon" style="width: 10%;max-height: 10%"/>
                <img src="https://s3.amazonaws.com/smartapp-icons/Partner/support/st-logo%402x.png" alt="SmartThings logo" style="width: 30%;max-height: 30%"/>
                <p>Your Quirky account is now connected to SmartThings. Tap 'Done' to continue to choose devices.</p>
			</div>
        </body>
        </html>
        """
	render contentType: 'text/html', data: html
}

String toQueryString(Map m) {
        return m.collect { k, v -> "${k}=${URLEncoder.encode(v.toString())}" }.sort().join("&")
}
