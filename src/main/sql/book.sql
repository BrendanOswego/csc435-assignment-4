CREATE TABLE book (
    book_id VARCHAR(36),
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(100),
    year_published int,
    pages int,
    PRIMARY KEY (book_id)
);