#!/bin/sh

#Expected parameters:
# $1 - name of Heroku application to be released
# $2 - Heroku API key

echo "Releasing Heroku application with name $1."
IMAGE_ID=$(docker inspect registry.heroku.com/$1/web --format={{.Id}})
echo "IMAGE_ID: $IMAGE_ID"
PAYLOAD='{"updates":[{"type":"web","docker_image":"'"$IMAGE_ID"'"}]}'
echo "PAYLOAD: $PAYLOAD"

curl \
 -n \
 -X PATCH https://api.heroku.com/apps/$1/formation \
 -d "$PAYLOAD" \
 -H "Content-Type: application/json" \
 -H "Accept: application/vnd.heroku+json; version=3.docker-releases" \
 -H "Authorization: Bearer $2"