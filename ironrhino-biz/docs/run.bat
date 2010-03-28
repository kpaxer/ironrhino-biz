@echo off
tasklist |find /i "mysqld.exe" >nul &&echo on &&goto next1
start "mysql" /B "mysql-5.1.44-win32/bin/mysqld.exe"
:next1
tasklist |find /i "java.exe" >nul &&echo on &&goto next2
set JAVA_HOME=/jdk1.6.0_18
cd apache-tomcat-6.0.24\bin
startup.bat
:next2
echo on
end

