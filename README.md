# Learn

1. Import as Maven Project
2. Setup Environment variable
MONGODB_URI=<<SRV Connection URI>>
3. Add VM Options provided below
-Djdk.tls.client.protocols=TLSv1.2

URLs
----
GET http://localhost:5000/home

PUT http://localhost:5000/books
    bookId
    bookName
    authorName

GET http://localhost:5000/books/1003

GET http://localhost:5000/books
