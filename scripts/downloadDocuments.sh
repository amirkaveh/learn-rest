#!/bin/bash

# This script download all documents into the current directory.
# It uses Document API implemented in this project.

RESPONSE=$(curl -s -w "\n%{http_code}" --request GET 'http://localhost:8080/api/documents')
RESPONSE=(${RESPONSE[@]})

STATUS=${RESPONSE[-1]}
if [ "$STATUS" != "200" ]; then
  echo "There is a problem" "$STATUS"
  exit 1
fi

BODY=${RESPONSE[*]::${#RESPONSE[@]}-1}

for ENTRY in $(echo "$BODY" | jq -r '.[] | @base64'); do
  _jq() {
    echo $ENTRY | base64 --decode | jq -r ${1}
  }
  ID=$(_jq '.id')
  NAME=$(_jq '.name')
  CONTENT=$(_jq '.content')
  CONTENT=$(sed -e 's/\\n/\n/g' <(echo "$CONTENT"))
  echo "$CONTENT" >"$(echo "$ID"_"$NAME" | xargs)"
  unset ID NAME CONTENT
done

echo "Success!"
exit 0
