// Ethan Nguyen
// 1002097430
// Project 3 -- Database Integration

package com.example.project3;

// Stores customer data and provides getter/setter methods.
public class Customer {

    private int    id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int    salesRepId;   // FK -> Employee.employee_id

    public Customer() {}

    public Customer(int id, String firstName, String lastName,
                    String phone, String email, int salesRepId) {
        this.id         = id;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.phone      = phone;
        this.email      = email;
        this.salesRepId = salesRepId;
    }

    // Getters
    public int    getId()         { return id;         }
    public String getFirstName()  { return firstName;  }
    public String getLastName()   { return lastName;   }
    public String getPhone()      { return phone;      }
    public String getEmail()      { return email;      }
    public int    getSalesRepId() { return salesRepId; }

    // Setters
    public void setId(int id)                 { this.id         = id;         }
    public void setFirstName(String firstName){ this.firstName  = firstName;  }
    public void setLastName(String lastName)  { this.lastName   = lastName;   }
    public void setPhone(String phone)        { this.phone      = phone;      }
    public void setEmail(String email)        { this.email      = email;      }
    public void setSalesRepId(int salesRepId) { this.salesRepId = salesRepId; }

    // Returns customer data as a JSON-formatted string.
    public String toJson() {
        return "{\n" +
                "  \"firstName\": \""  + firstName  + "\",\n" +
                "  \"lastName\": \""   + lastName   + "\",\n" +
                "  \"phone\": \""      + phone      + "\",\n" +
                "  \"email\": \""      + email      + "\",\n" +
                "  \"salesRepId\": "   + salesRepId + "\n"   +
                "}";
    }
}
