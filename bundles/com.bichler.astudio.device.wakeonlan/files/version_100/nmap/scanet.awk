BEGIN {print "NMAP scan network ... "}
{
     if (/^Nmap scan r/) ip="[1] "$5" "$6;
else if (/^Host is up /) up="[2] "$2" "$3;
else if (/^MAC Address/) mc="[3] "$3; hn="[4] "$4" "$5" "$6" "$7;
print "[5] "ip" "up" "mc" "hn;
}
END {print "done."}
