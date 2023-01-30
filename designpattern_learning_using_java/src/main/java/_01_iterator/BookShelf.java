package _01_iterator;

import java.util.Iterator;

public class BookShelf implements Iterable<Book> {

    private Book[] books;

    private int last;

    private BookShelf(int maxsize) {
        this.books = new Book[maxsize];
    }

    public Book getBook(int index) {
        return books[index];
    }

    public void appendBook(Book book) {
        this.books[last] = book;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public Iterator<Book> iterator() {
        //return new BookShelfIterator(this);
        return null;
    }
}
