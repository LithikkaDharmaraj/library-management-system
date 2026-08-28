package com.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "emp_roll_no")
    private String empRollNo;

    public User() {}

    public User(String name, String userType, String empRollNo) {
        this.name = name;
        this.userType = userType;
        this.empRollNo = empRollNo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String getEmpRollNo() { return empRollNo; }
    public void setEmpRollNo(String empRollNo) { this.empRollNo = empRollNo; }
}
