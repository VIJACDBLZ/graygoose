@echo off

IF "%1"=="" (
	echo Usage: make ^<profile-name^>
) ELSE (
	copy profiles\profiles.xml.%1 profiles.xml
	mvn package
)
