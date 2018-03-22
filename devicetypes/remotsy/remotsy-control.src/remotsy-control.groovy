/**
 *  Copyright 2016-2018 Remotsy 
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
	definition (name: "Remotsy Control", namespace: "remotsy", author: "jorgecis@gmail.com") {
   		capability "switch" 
            command "power_toggle"
			command "mute" 
			command "source"
			command "menu"    
            command "tools"           
			command "HDMI"    
            command "Sleep"
            command "Up"
            command "Down"
            command "Left"
            command "Right" 
			command "chup" 
 			command "chdown"               
			command "prech"
			command "volup"    
            command "voldown"           
            command "Enter"
            command "Return"
            command "Exit"
	}

    standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
        state "default", label:'Power',  action:"power_toggle", icon:"https://s3.amazonaws.com/remotsy/logo256.png", backgroundColor:"#2a5ca0"
    }
    
    standardTile("power", "device.switch", width: 1, height: 1, canChangeIcon: false) {
        state "default", label:'Power', action:"power_toggle", decoration: "flat", icon:"st.samsung.da.RC_ic_power", backgroundColor:"#ffffff"
    }   
    standardTile("mute", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Mute', action:"mute", icon:"st.custom.sonos.muted", backgroundColor:"#ffffff"
    }    
	standardTile("source", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Source', action:"source", icon:"st.Electronics.electronics15"
    }
	standardTile("tools", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Tools', action:"tools", icon:"st.secondary.tools"
    }
	standardTile("menu", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Menu', action:"menu", icon:"st.vents.vent"
    }
	standardTile("HDMI", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Source', action:"HDMI", icon:"st.Electronics.electronics15"
    }
    standardTile("Sleep", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Sleep', action:"Sleep", icon:"st.Bedroom.bedroom10"
    }
    standardTile("Up", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Up', action:"Up", icon:"st.thermostat.thermostat-up"
    }
    standardTile("Down", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Down', action:"Down", icon:"st.thermostat.thermostat-down"
    }
    standardTile("Left", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Left', action:"Left", icon:"st.thermostat.thermostat-left"
    }
    standardTile("Right", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Right', action:"Right", icon:"st.thermostat.thermostat-right"
    }  
	standardTile("chup", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'CH Up', action:"chup", icon:"st.thermostat.thermostat-up"
    }
	standardTile("chdown", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'CH Down', action:"chdown", icon:"st.thermostat.thermostat-down"
    }
	standardTile("prech", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Pre CH', action:"prech", icon:"st.secondary.refresh-icon"
    }
    standardTile("volup", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Vol Up', action:"volup", icon:"st.thermostat.thermostat-up"
    }
    standardTile("voldown", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Vol Down', action:"voldown", icon:"st.thermostat.thermostat-down"
    }
    standardTile("Enter", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Enter', action:"Enter", icon:"st.illuminance.illuminance.dark"
    }
    standardTile("Return", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Return', action:"Return", icon:"st.secondary.refresh-icon"
    }
    standardTile("Exit", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:'Exit', action:"Exit", icon:"st.locks.lock.unlocked"
    } 
    standardTile("blank", "device.switch", decoration: "flat", canChangeIcon: false) {
        state "default", label:''
    } 
      
    main "switch"
    details (["power","blank","Sleep","chup","prech","volup","chdown","mute","voldown", "menu", "Up", "tools", "Left", "Enter", "Right", "Return", "Down", "Exit"])	
}

def parse(String description) {
	log.debug(description)
    return null
}

def off() {
	log.debug "Power OFF"
    parent.IRAction("POWER OFF",device.deviceNetworkId) 
}

def on() {
	log.debug "Power ON"
    parent.IRAction("POWER ON",device.deviceNetworkId) 
}

def power_toggle() {
	log.debug "POWER TOGGLE"
    parent.IRAction("POWER TOGGLE",device.deviceNetworkId) 
}

def mute() {
	log.trace "MUTE pressed"
    parent.IRAction("MUTE TOGGLE",device.deviceNetworkId) 
}

def source() {
	log.debug "SOURCE pressed"
    parent.IRAction("SOURCE",device.deviceNetworkId) 
}

def menu() {
	log.debug "MENU pressed"
    parent.IRAction("MENU TOGGLE",device.deviceNetworkId) 
}

def tools() {
	log.debug "TOOLS pressed"
    parent.IRAction("TOOLS",device.deviceNetworkId) 
}

def HDMI() {
	log.debug "HDMI pressed"
    parent.IRAction("HDMI",device.deviceNetworkId) 
}

def Sleep() {
	log.debug "SLEEP pressed"
    parent.IRAction("SLEEP",device.deviceNetworkId) 
}

def Up() {
	log.debug "UP pressed"
    parent.IRAction("CURSOR UP",device.deviceNetworkId)
}

def Down() {
	log.debug "DOWN pressed"
    parent.IRAction("CURSOR DOWN",device.deviceNetworkId) 
}

def Left() {
	log.debug "LEFT pressed"
    parent.IRAction("CURSOR LEFT",device.deviceNetworkId) 
}

def Right() {
	log.debug "RIGHT pressed"
    parent.IRAction("CURSOR RIGHT",device.deviceNetworkId) 
}

def chup() {
	log.debug "CHUP pressed"
    parent.IRAction("CHANNEL UP",device.deviceNetworkId)
}

def chdown() {
	log.debug "CHDOWN pressed"
    parent.IRAction("CHANNEL DOWN",device.deviceNetworkId) 
}

def prech() {
	log.debug "PRECH pressed"
    parent.IRAction("PREVIOUS CHANNEL",device.deviceNetworkId)
}

def Exit() {
	log.debug "EXIT pressed"
    parent.IRAction("EXIT",device.deviceNetworkId) 
}

def volup() {
	log.debug "VOLUP pressed"
    parent.IRAction("VOLUME UP",device.deviceNetworkId)
}

def voldown() {
	log.debug "VOLDOWN pressed"
    parent.IRAction("VOLUME DOWN",device.deviceNetworkId) 
}

def Enter() {
	log.debug "ENTER pressed"
    parent.IRAction("CURSOR ENTER",device.deviceNetworkId) 
}

def Return() {
	log.debug "RETURN pressed"
    parent.IRAction("RETURN",device.deviceNetworkId) 
}