package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.enumeration.StatusEnum;


@Entity

public class LendAndReturnEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lendId;
    @Temporal(TemporalType.DATE)
    private Date lendDate;
    @Temporal(TemporalType.DATE)
    private Date returnDate;
    @Column(precision = 11, scale = 2)
    private Double fineAmount;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private BookEntity bookEntity;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private MemberEntity memberEntity;

    
    public LendAndReturnEntity()
    {
    }
    
    
    public LendAndReturnEntity(Date lendDate, Date returnDate, Double fineAmount, StatusEnum status)
    {
        this.lendDate = lendDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }
        
        
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lendId != null ? lendId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the lendId fields are not set
        if (!(object instanceof LendAndReturnEntity)) {
            return false;
        }
        LendAndReturnEntity other = (LendAndReturnEntity) object;
        if ((this.lendId == null && other.lendId != null) || (this.lendId != null && !this.lendId.equals(other.lendId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.LendAndReturnEntity[ id=" + lendId + " ]";
    }
    
    
    public Long getLendId() {
        return lendId;
    }

    public void setLendId(Long lendId) {
        this.lendId = lendId;
    }
    
    public Date getLendDate() {
        return lendDate;
    }

    public void setLendDate(Date lendDate) {
        this.lendDate = lendDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public BookEntity getBookEntity() {
        return bookEntity;
    }

    public void setBookEntity(BookEntity bookEntity) {
        this.bookEntity = bookEntity;
    }

    public MemberEntity getMemberEntity() {
        return memberEntity;
    }

    public void setMemberEntity(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }
}
