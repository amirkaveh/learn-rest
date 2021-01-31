#!/bin/bash
DIRECTORY=$1
REPORT_FILE=report.csv
if [ -z "$DIRECTORY" ]; then
  echo "directory not provided"
  exit 1
fi
if [ "${DIRECTORY: -1}" != "/" ]; then
  DIRECTORY="$1/"
fi
DIR_LEN=$(($(echo "$DIRECTORY" | wc --chars) - 1))
echo "file name,status code,database id" >$REPORT_FILE
for entry in "$DIRECTORY"*.txt; do
  if [ -s "$entry" ]; then
    CONTENT=$(cat "$entry")
    NAME=${entry:DIR_LEN}
    RESPONSE=$(curl -s -w "\n%{http_code}" --request POST 'http://localhost:8080/api/documents' --header 'Content-Type: application/json' \
      --data-raw "{ \"name\": \"${NAME}\" , \"content\": \"${CONTENT}\" }")
    RESPONSE=(${RESPONSE[@]})
    STATUS=${RESPONSE[-1]}
    BODY=${RESPONSE[*]::${#RESPONSE[@]}-1}
    ID=$(echo "$BODY" | jq -r '.id')
    echo "$NAME"",""$STATUS"",""$ID" >> $REPORT_FILE
    unset CONTENT NAME RESPONSE STATUS BODY ID
  fi
done
