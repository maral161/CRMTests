package com.alinconsulting.common.crm.keapmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contacts {
    public ArrayList<Contact> contacts;
    public int count;
    public String next;
    public String previous;

}
