#!/bin/bash
#Test script for web service

## Add new book
 curl -s  \
 -d '{ "bookId": "2000", "bookName": "The Theory of Everything", "authorName": "Stephen Hawking" }' \
 -X PUT -H "Content-Type: application/json" http://localhost:5000/books

# Get book by Id
curl -s http://localhost:5000/books/1003 | jq

# Get list of books in library
curl -s http://localhost:5000/books | jq