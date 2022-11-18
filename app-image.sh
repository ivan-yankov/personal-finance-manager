# jvm version number: 1, 2, 3, ...
export JVM_VERSION=11

export JVM_XMS=2G
export JVM_XMX=6G
export MAIN_CLASS=yankov.finance.manager.Main
export BEFORE="tput civis"
export AFTER="tput cnorm"

# array with application jar files, paths are relative to the project directory
export JARS=("jar/*")

export APPLICATION_NAME=personal-finance-manager
export IS_TERMINAL_APPLICATION=true

# icon file name, relative to the project directory, if empty default icon will be used
export ICON_FILE=icon.png

# application parameters, parameters provided on AppImage run will be appended after this list
export PARAMETERS=""
