package com.company.preparers;

import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.*;

import static com.company.reservations.FR5.*;
public class FR5Preparer extends Preparer{
    public PreparedStatement selectFR5(Map<String, String> fields, List<String> keys)
            throws Exception
    {

        StringBuilder query = new StringBuilder();
        // gives what fields are set at what index
        List<String> whatFieldsAreSet = new ArrayList<>();
        query.append("SELECT rm.*, res.*\n" + "FROM lab7_reservations res, lab7_rooms rm WHERE ");
        String []fields_keys = (String[])(fields.keySet().toArray());
        for(int i = 0; i < fields_keys.length; i++){
            String key = fields_keys[i];
            if(!key.equals("ANY")){
                setStringBuilder(query, key, keys, whatFieldsAreSet);
                // if not last add AND
                if(!(i==fields_keys.length-1)){
                    query.append(" AND ");
                }
            }
        }
        PreparedStatement statement = ConnectionAdapter
                                      .getConnection()
                                      .prepareStatement(query.toString());

        // set the different fields
        for (int i=0; i<whatFieldsAreSet.size(); i++){
            statement.setString(i+1, fields.get(whatFieldsAreSet));
        }

        return statement;
    }
    void setStringBuilder(StringBuilder sb, String key, List<String> keys, List<String> whatFieldsAreSet){
        if(key.equals(keys.get(FIRST_NAME))){
           sb.append("FirstName LIKE ?");
           whatFieldsAreSet.add(keys.get(FIRST_NAME));
        }else if(key.equals(keys.get(LAST_NAME))){
            sb.append("LastName LIKE ?");
            whatFieldsAreSet.add(keys.get(LAST_NAME));
        }else if(key.equals(keys.get(BEGIN_DAY))){
            sb.append("CheckIn >= ?");
            whatFieldsAreSet.add(keys.get(BEGIN_DAY));
        }else if(key.equals(keys.get(END_DAY))){
            sb.append("CheckOut <= ?");
            whatFieldsAreSet.add(keys.get(END_DAY));
        }else if(key.equals(keys.get(ROOM_CODE))){
            sb.append("Room <= ?");
            whatFieldsAreSet.add(keys.get(ROOM_CODE));
        }else if(key.equals(keys.get(RES_CODE))){
            sb.append("CODE <= ?");
            whatFieldsAreSet.add(keys.get(ROOM_CODE));
        }
    }
}

