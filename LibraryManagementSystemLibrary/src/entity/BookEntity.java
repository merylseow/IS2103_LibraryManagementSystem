package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity

public class BookEntity implements Serializable
{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String title;
    @Column(unique = true)
    private String isbn;
    private String author;
    @OneToMany(mappedBy = "bookEntity")
    private List<LendAndReturnEntity> lendings = new ArrayList<>();
    
    
    public BookEntity()
    {
    }

    
    public BookEntity(String title, String isbn, String author)
    {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
    }
    
    
    public BookEntity(String title, String isbn, String author, List<LendAndReturnEntity> lendings)
    {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.lendings = lendings;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookId fields are not set
        if (!(object instanceof BookEntity)) {
            return false;
        }
        BookEntity other = (BookEntity) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BookEntity[ id=" + bookId + " ]";
    }
    
    
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }

    public List<LendAndReturnEntity> getLendings() {
        return lendings;
    }

    public void setLendings(List<LendAndReturnEntity> lendings) {
        this.lendings = lendings;
    }
    
    // record every time the book is being borrowed
    public void addToBookLendRecord(LendAndReturnEntity lending)
    {
        if (!this.lendings.contains(lending))
        {
            this.lendings.add(lending);
        }
        else
        {
            System.out.println("Lending already exist");
        }
    }
}
