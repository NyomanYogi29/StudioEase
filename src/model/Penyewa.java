/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author nyoma
 */
public class Penyewa {
    private String id;
    private String nama;
    private String email;
    private String password;
    private String phoneNumber;
    
    
    public Penyewa(String id, String nama, String email, String password, String phoneNumber)
    {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        
    }
    
    public Penyewa(String nama, String phoneNumber, String email, String password)
    {
        this.nama = nama;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getNama()
    {
        return nama;
    }
    
    public void setNama(String nama)
    {
        this.nama = nama;
    }
    
    public String getPhoneNumber()
    {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
}
