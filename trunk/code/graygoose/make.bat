@echo off

IF "%1"=="" (
    echo Usage: make ^<profile-name^>
    echo Example: make sample
) ELSE (
    mvn clean
    IF NOT EXIST profiles\profiles.xml.%1 (
        echo ERROR: file "profiles\profiles.xml.%1" not found
        pause
    ) ELSE (    
        copy profiles\profiles.xml.%1 profiles.xml
        mvn package -Dfile.encoding=UTF-8
    )
)