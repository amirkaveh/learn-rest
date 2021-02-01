#!/bin/bash

# This script upload all .txt files in the provided directory using Document API.
# The directory should be provided as an argument.
# It creates report.csv in current path, containing upload reports.

DIRECTORY=$1
REPORT_FILE=report.csv

if [ -z "$DIRECTORY" ]; then
  echo "directory not provided"
  exit 1
fi

if [ "${DIRECTORY: -1}" != "/" ]; then
  DIRECTORY="$1/"
fi

echo "file name,status code,database id" >$REPORT_FILE

DIR_LEN=$(($(echo "$DIRECTORY" | wc --chars) - 1))
for ENTRY in "$DIRECTORY"*.txt; do
  if [ -s "$ENTRY" ]; then
    CONTENT=$(sed -E ':a;N;$!ba;s/\r{0,1}\n/\\n/g' <(cat "$ENTRY"))
    NAME=${ENTRY:DIR_LEN}
    RESPONSE=$(curl -s -w "\n%{http_code}" --request POST 'http://localhost:8080/api/documents' --header 'Content-Type: application/json' \
      --data-raw "{ \"name\": \"${NAME}\" , \"content\": \"${CONTENT}\" }")
    RESPONSE=(${RESPONSE[@]})

    STATUS=${RESPONSE[-1]}
    if [ "$STATUS" = "201" ] || [ "$STATUS" = "200" ] || [ "$STATUS" = "409" ]; then
      BODY=${RESPONSE[*]::${#RESPONSE[@]}-1}
      ID=$(echo "$BODY" | jq '.id')
    else
      ID=
    fi

    echo "$NAME"",""$STATUS"",""$ID" >>$REPORT_FILE

    unset CONTENT NAME RESPONSE STATUS BODY ID
  fi
done

exit 0
