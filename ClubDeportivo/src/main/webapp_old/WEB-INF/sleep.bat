@echo off
set /a i=%1%+1
ping 127.0.0.1 -n %i% -w 1000 > nul