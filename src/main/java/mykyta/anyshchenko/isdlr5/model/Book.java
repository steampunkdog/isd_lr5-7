package mykyta.anyshchenko.isdlr5.model;

import java.util.Objects;

public class Book {
    private String id;
    private String name;
    private String author;
    private boolean deleted = false;

    public Book() {
    }

    public Book(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Book(Book source) {
        this.id = String.copyValueOf(source.getId().toCharArray());
        this.name = String.copyValueOf(source.getName().toCharArray());
        this.author = String.copyValueOf(source.getAuthor().toCharArray());
        this.deleted = source.isDeleted();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Book{ ");
        sb.append("id: '").append(id).append('\'');
        sb.append(", name: '").append(name).append('\'');
        sb.append(", author: '").append(author).append('\'');
        sb.append(", deleted: '").append(deleted).append('\'');
        sb.append(" }");
        return sb.toString();
    }
}
