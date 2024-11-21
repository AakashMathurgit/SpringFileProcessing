#!/bin/bash

# Navigate to the project directory
cd ./source_code/DriverProjectE6/

PORTS=()
MH_VALUE=""
JH_VALUE=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --mh)
      MH_VALUE="$2"
      shift 2
      ;;
    --jh)
      JH_VALUE="$2"
      shift 2
      ;;
    *)
      PORTS+=("$1")
      shift
      ;;
  esac
done

if [[ -n "$MH_VALUE" ]]; then
	echo $MH_VALUE
	MAVEN_HOME=$MH_VALUE
	PATH="$MAVEN_HOME\bin:$PATH"
	export MAVEN_HOME PATH
fi

if [[ -n "$JH_VALUE" ]]; then
	echo $JH_VALUE
	JAVA_HOME=$JH_VALUE
	PATH="$JAVA_HOME\bin:$PATH"
	export JAVA_HOME PATH
fi

echo $PATH
echo $JAVA_HOME
echo $MAVEN_HOME

mvn -version
java -version

PORTS_STRING=$(IFS=','; echo "${PORTS[*]}")
echo $PORTS_STRING
# Run the Spring Boot application
#java -jar DriverProject.jar $enginePorts
mvn spring-boot:run -Dspring-boot.run.arguments=$PORTS_STRING
exit 0

