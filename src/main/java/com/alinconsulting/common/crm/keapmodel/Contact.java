package com.alinconsulting.common.crm.keapmodel;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    public ArrayList<EmailAddress> email_addresses;
    public boolean email_opted_in;
    public ArrayList<Object> addresses;
    public Date last_updated;
    public ArrayList<Object> tag_ids;
    public int owner_id;
    public Date date_created;
    public String middle_name;
    public String given_name;
    public Object scoreValue;
    public String email_status;
    public ArrayList<PhoneNumber> phone_numbers;
    public Object last_updated_utc_millis;
    public Company company;
    public int id;
    public String family_name;

}
