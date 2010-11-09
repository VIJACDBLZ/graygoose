@echo off

IF "%1"=="" (
    echo Usage: make ^<profile-name^>
    echo Example: make sample
) ELSE (
    mvn clean
    if not exist profiles\profiles.xml.%1 (
        echo ERROR: file "profiles\profiles.xml.%1" not found
        pause
    )    
    copy profiles\profiles.xml.%1 profiles.xml
    mvn package -Dfile.encoding=UTF-8
)