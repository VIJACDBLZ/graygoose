@echo off

IF "%1"=="" (
	echo Usage: make ^<profile-name^>
	echo Sample profile names: dev, prod
) ELSE (
	copy profiles\profiles.xml.%1 profiles.xml
	mvn package -Dfile.encoding=UTF-8
)