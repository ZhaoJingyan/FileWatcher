@echo off
echo WScript.sleep 5000 > sleep.vbs
Wscript sleep.vbs
del sleep.vbs
echo over