package com.alinconsulting.common.crm.keapmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber {
    public String number;
    public Object extension;
    public String field;
    public String type;
}
