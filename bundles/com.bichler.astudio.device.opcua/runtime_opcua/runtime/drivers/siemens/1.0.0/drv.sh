#!/bin/bash
####################################################################################################
#source "/hbin/def-vars.txt.sh"
#source "/hbin/def-func.txt.sh"
#source "/hbin/def-plcn.txt.sh"
####################################################################################################
R_driver_com_s7 () {
# dc_ : driver.com
# ${1} : ${drivername}
driversdir="/hbs/comet/opc_ua_server/servers/HBSServer/drivers"
drivername="${1}"
driver_com="${driversdir}/${drivername}/driver.com"

if [ -f "${driver_com}" ]; then
	while read line; do
	    [[ "${line}" =~ ^driverid         ]] && read line && dc_id="${line}"
	    [[ "${line}" =~ ^drivertype       ]] && read line && dc_type="${line}"
	    [[ "${line}" =~ ^drvns            ]] && read line && dc_nasp="${line}"
	    [[ "${line}" =~ ^drvproperties    ]] && read line && dc_prop="${line}"
	    [[ "${line}" =~ ^complexTypes     ]] && read line && dc_cpxt="${line}"
	    [[ "${line}" =~ ^reconnecttimeout ]] && read line && dc_time="${line}"
	    if [[ "${line}" =~ ^scancyclic ]]; then
	    	read line
	    	dc_scan=$(echo "${line}" | cut -d ';' -f 1)
	    	dc_cycl=$(echo "${line}" | cut -d ';' -f 2)
	    fi
	    if [[ "${line}" =~ ^deviceaddress ]]; then
	    	read line
	    	dc_addn=$(echo "${line}" | cut -d ';' -f 1)
	    	dc_addr=$(echo "${line}" | cut -d ';' -f 2)
	    	dc_port=$(echo "${line}" | cut -d ';' -f 3)
	    	dc_rack=$(echo "${line}" | cut -d ';' -f 4)
	    	dc_slot=$(echo "${line}" | cut -d ';' -f 5)
	    fi
	done <<< "$(cat ${driver_com})"
else
	return 1
fi
return 0
}
####################################################################################################
R_driver_com_s7 "${1}"
####################################################################################################
echo '<input type="hidden" value="'${1}'" name="dc_drivername" />'
echo '<input type="hidden" value="'${dc_addn}'" name="dc_addn" />'
echo "<table>"
#echo "<tbody>"
echo "<tr>"
echo "<td>"
echo "Adresse:</td><td> <input type='text' value='"${dc_addr}"' name='dc_addr' class='dc_inputs' readonly/></td>"
echo "</tr><tr><td>"
echo "Port: </td><td><input type='text' value='"${dc_port}"' name='dc_port' class='dc_inputs' readonly/></td>"
echo "</tr><tr><td>"
echo "Rack: </td><td><input type='text' value='"${dc_rack}"' name='dc_rack' class='dc_inputs' readonly/></td>"
echo "</tr><tr><td>"
echo "Slot: </td><td><input type='text' value='"${dc_slot}"' name='dc_slot' class='dc_inputs' readonly/></td>"
echo "</tr><tr><td>"
echo "Timeout: </td><td><input type='text' value='"${dc_time}"' name='dc_time' class='dc_inputs' readonly/></td>"
echo "</tr><tr><td>"
echo "zyklisch: </td><td><input type='checkbox' name='dc_scan' class='dc_checks' disabled "
test "${dc_scan}" = "true" && echo  "checked "
echo "/></td>"
echo "</tr><tr><td>"
echo "Lesezyklus: </td><td><input type='text' value='"${dc_cycl}"' name='dc_cycl' class='dc_inputs' readonly /></td>"
echo "</tr>"
#echo "</tbody>"
echo "</table>"

# Admin
echo '<input type="button" name="" value="Admin"            class="dc_buttons"        onclick="javascript:show_login('"'driver_com',event"');"                   />'
echo '<input type="button" name="" value="Save+Restart OPC" class="dc_buttons_hidden" onclick="javascript:save_driver_com('"'"${1}"'"');"      hidden="hidden" />'
echo '<input type="button" name="" value="Back"             class="dc_buttons_hidden" onclick="javascript:menu_show_driver('"'"${1}"'"');"     hidden="hidden" />'

####################################################################################################
