package com.learn.models;

import com.google.gson.Gson;
import org.apache.http.auth.AUTH;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Book {
    private int schemaVersion;
//    private ObjectId id;
    private String id;
    private Integer bookId;
    private String bookName;
    private String authorName;
    private boolean populated;

    public static final String ID = "_id";
    public static final String BOOK_ID = "bookId";
    public static final String BOOK_NAME = "bookName";
    public static final String AUTHOR_NAME = "authorName";
    public static final String SCHEMA_VERSION = "schemaVersion";

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id.toHexString();
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isPopulated() {
        return populated;
    }

    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public Document toDocument() {
        Document bookDoc;
        bookDoc = new Document(ID, id)
                .append(BOOK_ID, bookId)
                .append(BOOK_NAME, bookName)
                .append(AUTHOR_NAME, authorName)
                .append(SCHEMA_VERSION, schemaVersion);
        return bookDoc;
    }

}
