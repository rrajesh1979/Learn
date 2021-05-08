db = db.getSiblingDB('learn');

db.books.insertMany(
[{
    "bookId": 1000,
    "bookName": "Harry Potter and the Sorcer's Stone",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1001,
    "bookName": "Harry Potter and the Chamber of Secrets",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1002,
    "bookName": "Harry Potter and the Prisoner of Azkaban",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1003,
    "bookName": "Harry Potter and the Goblet of Fire",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1004,
    "bookName": "Harry Potter and the Order of Phoenix",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1005,
    "bookName": "Harry Potter and the Half Blood Prince",
    "authorName": "JK Rowling",
    "schemaVersion": 1
},{
    "bookId": 1005,
    "bookName": "Harry Potter and the Deathly Hallows",
    "authorName": "JK Rowling",
    "schemaVersion": 1
}]
);