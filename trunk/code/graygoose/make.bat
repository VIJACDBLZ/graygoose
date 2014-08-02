@echo off

IF "%1"=="" (
    echo Usage: make ^<profile-name^>
    echo Example: make dev
) ELSE (
    IF NOT EXIST profiles\profiles.%1.xml (
        echo ERROR: file "profiles\profiles.%1.xml" not found
        pause
    ) ELSE (    
        copy profiles\profiles.%1.xml profiles.xml
        mvn package -Dfile.encoding=UTF-8
    )
)
