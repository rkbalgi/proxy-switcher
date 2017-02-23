# proxy-switcher
A simple utility to switch between multiple proxy profiles (one for work at, office, mobile etc)

# Installation
* Just build the jar file and place in a directory of your choice
* Now write a couple of powershell scripts (example here) that will do the actual job of changing the proxy settings (in the registry)
* The powershell scripts shall have an extension of .ps1 or .PS1 and should be in the same directory as that of the proxyswitcher jar file
* Once the application is launched (with a java8+ jvm) it will appear in the system tray and please refer to the below picture for usage

![](https://github.com/rkbalgi/github.io/blob/master/ps_img.png)

#Powershell Scripts
Below is an example of the powershell script (acme.ps1)

<pre>
# proxy setting change script for acme
##
##
# Do not change the below line
$regKey="HKCU:\Software\Microsoft\Windows\CurrentVersion\Internet Settings"
# The below line should be proxy/port in your organization or workplace
set-ItemProperty -path $regKey ProxyServer -value 'proxy.acme.com:8080'
# below value should be 1 for enable proxy and 0 for disable
set-ItemProperty -path $regKey ProxyEnable   -value 1
# the list of system names/ip's that should bypass the proxy
Set-ItemProperty -path $regKey ProxyOverride -value '*.acme.com;*hr*;*acmeinternal*'
</pre>
