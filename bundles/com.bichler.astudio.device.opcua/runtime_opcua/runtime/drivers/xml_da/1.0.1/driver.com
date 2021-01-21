driverid
1

# here we define the driver type
# following names are valid 
# omron, siemens, beckhoff, mitsubishi, ..
# cpu types and additional type specific configuration should
# we insert into the next section
drivertype
xml_da

# namespace name for that driver
drvns
http://hb-softsolution.com/siemens

# here we can define all driver specific properties
connection properties

# here we can add complex data point types
complexTypes
DB; FB; FC; SFC

#
redundancy no

reconnecttimeout
10000

deviceaddress
ISOTCPS7_300;172.16.2.2;102;0;2

# here we can add additional driver properties
# specifies if the driver directly creates the opc ua address space model
drvproperties
max_bytecount=220

scancyclic
true;10