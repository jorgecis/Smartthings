/**
 *  Copyright 2016 SmartThings, Inc.
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
metadata {
	definition (name: "smartcontrolir", namespace: "jorgecis", author: "jorgecis@gmail.com") {
		capability "Switch"
        command "poweron"
		command "poweroff"
        command "mute"
	}

tiles (scale: 2){
        standardTile("main", "device.status", canChangeIcon:true) {
            state "default", label:'', icon:"st.Entertainment.entertainment15", backgroundColor:"#FFFFFF"
    	}

 		standardTile("titlemsg", "device.switch",  height:1, width:5, inactiveLabel:true, decoration:"flat") {
            state "default", label:'Control Remote Samsung TV', icon:""
        }

		standardTile("blk", "device.status", inactiveLabel:true, decoration:"flat") {           
        }
        standardTile("poweron", "device.status", inactiveLabel:false, decoration:"flat") {
            state "default", label:'On', action:"on"
           
        }

        standardTile("poweroff", "device.status", inactiveLabel:false, decoration:"flat") {
            state "default", label:'Off', action:"off"
            
        }
        
         standardTile("mute", "device.status", inactiveLabel:false, decoration:"flat") {
            state "default", label:'mute',  action:"mute"
            
        }

  

       

        main(["main"])

        details([
            "titlemsg","blk",
            "poweron",
            "poweroff",
            "mute"
            
        ])
    }
	
}


    
def sendcmd(cmd) {

	def params = [
    	uri: "https://remotsy.com/control_t.php?cmd=${cmd}&id=dev1"
    	
    ]

	try {
    	httpGet(params) { resp ->
    		if (resp.data) {
        		log.debug "Response Data = ${resp.data}"
        		log.debug "Response Status = ${resp.status}"

                resp.headers.each {
  					log.debug "header: ${it.name}: ${it.value}"
				}
        	}
        	if(resp.status == 200) {
	        	log.debug "Request was OK"
    		}
        	else {
        		log.error "Request got http status ${resp.status}"
        	}
        }
    } catch(e)
    {
    	log.debug e
    }
}



def parse(String description) {
	log.debug(description)
}


def on() {
	log.debug "poweron"
    sendcmd("poweron")
}

def off() {
	log.debug "poweroff"
    sendcmd("poweroff")
}


def mute() {
	log.debug "mute"
    sendcmd("mute")
}



def installed() {
}

def poll() {
log.debug "pool"
    TRACE("poll()")
}