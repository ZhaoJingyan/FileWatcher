@echo off
echo WScript.sleep 1000 > sleep.vbs
Wscript sleep.vbs
echo 1%% test info
Wscript sleep.vbs
echo 50%% test info
Wscript sleep.vbs
echo 100%% test info
del sleep.vbs
echo test over
exit /b 0