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

public class MemberEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    @Column(unique = true)
    private String identityNo;
    private String phoneNo;
    private String address;
    @OneToMany(mappedBy = "memberEntity")
    private List<LendAndReturnEntity> lendings = new ArrayList<>();
    
    
    public MemberEntity()
    {
    }
    
    
    public MemberEntity(String firstName, String lastName, Character gender, Integer age, String identityNo, String phoneNo, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.identityNo = identityNo;
        this.phoneNo = phoneNo;
        this.address = address;
    }
    

    public MemberEntity(Long memberId, String firstName, String lastName, Character gender, Integer age, String identityNo, String phoneNo, String address, List<LendAndReturnEntity> lendings) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.identityNo = identityNo;
        this.phoneNo = phoneNo;
        this.address = address;
        this.lendings = lendings;
    }
    
    
    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (memberId != null ? memberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the memberId fields are not set
        if (!(object instanceof MemberEntity)) {
            return false;
        }
        MemberEntity other = (MemberEntity) object;
        if ((this.memberId == null && other.memberId != null) || (this.memberId != null && !this.memberId.equals(other.memberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "entity.MemberEntity[ id=" + memberId + " ]";
    }
    
    
    public Long getMemberId()
    {
        return memberId;
    }
    
    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Character getGender() {
        return gender;
    }
    
    public void setGender(Character gender) {
        this.gender = gender;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getIdentityNo() {
        return identityNo;
    }
    
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }
    
    public String getPhoneNo() {
        return phoneNo;
    }
    
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public List<LendAndReturnEntity> getLendings() {
        return lendings;
    }

    public void setLendings(List<LendAndReturnEntity> lendings) {
        this.lendings = lendings;
    }
}


