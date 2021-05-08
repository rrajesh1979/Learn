#!/bin/bash
#Test script for web service

# curl -s  -d '{ "bookId": "2000", "bookName": "The Theory of Everything", "authorName": "Stephen Hawking" }' \
# -X PUT -H "Content-Type: application/json" http://localhost:5000/books

curl -X PUT -H "Content-Type: application/json" \
'http://localhost:5000/books' \
 -s \
 -d bookId=2000 \
 -d bookName=The Theory of Everything \
 -d authorName=Stephen Hawking