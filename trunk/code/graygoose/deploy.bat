@echo off

IF "%1"=="" (
	echo Usage: deploy ^<gae-application-name^> [^<command (default=update)^>]
) ELSE (
    IF "%2"=="" (
        ..\..\lib\gae-sdk\bin\appcfg.cmd update target/%1/
    ) ELSE (
    	..\..\lib\gae-sdk\bin\appcfg.cmd %2 target/%1/
    )
)
