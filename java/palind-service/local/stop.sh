#!/bin/bash +x
echo "stopping analytics container"
if [ -f ./docker-compose.yml ]; then
	docker-compose down --remove-orphans
else
  echo "Missing ./docker-compose.yml file!"
  echo "Please cd to the working folder where fhirapi desktop package is extracted. Try again."
fi
exit 0
