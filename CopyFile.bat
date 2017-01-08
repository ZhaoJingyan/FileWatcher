@echo off
if {%1}=={} goto error
if not exist %1 goto error
REM NET
net use "\\113.160.1.61\water" 123456 /user:gtp
copy "%1" "\\113.160.1.61\water\" /V /Y /Z
if %errorlevel% gtr 0 goto error
goto end
:error
exit /b 1
:end
exit /b 0
