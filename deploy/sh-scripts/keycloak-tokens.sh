#!/bin/bash

KCHOST=http://localhost:8080
REALM=resource-specifications
CLIENT_ID=resource-specifications-service
UNAME=rs-test
PASSWORD=test_pass

# shellcheck disable=SC2006
ACCESS_TOKEN=`curl \
  -d "client_id=$CLIENT_ID" \
  -d "username=$UNAME" \
  -d "password=$PASSWORD" \
  -d "grant_type=password" \
  "$KCHOST/realms/$REALM/protocol/openid-connect/token"  | jq -r '.access_token'`
echo "$ACCESS_TOKEN"
