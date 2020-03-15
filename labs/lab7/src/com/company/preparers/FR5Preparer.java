package com.company.preparers;

import com.company.ConnectionAdapter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.*;

import static com.company.executors.FR5Executor.*;
public class FR5Preparer{

    private FR5Preparer(){}

    public static PreparedStatement select(Map<String, String> fields, List<String> keys)
            throws Exception
    {

        StringBuilder query = new StringBuilder();
        // gives what fields are set at what index
        List<String> whatFieldsAreSet = new ArrayList<>();
        query.append("SELECT rm.RoomName, res.*\n" + "FROM lab7_reservations res, lab7_rooms rm WHERE res.Room=rm.RoomCode AND ");

        Object []fields_keys = (fields.keySet().toArray());
        int index_not_any = 0;

        long not_any_count = fields.values().stream().filter(p->!p.equals("ANY")).count();

        for(int i = 0; i < fields_keys.length; i++){
            String key = (String)fields_keys[i];
            if(!fields.get(key).equals("ANY")){
                setStringBuilder(query, key, keys, whatFieldsAreSet);
                // if not last add AND (need to count last not ANY"
                if(index_not_any!=not_any_count-1 && not_any_count!=0){
                    query.append(" AND ");
                    index_not_any++;
                }
            }
        }
        PreparedStatement statement = ConnectionAdapter
                                      .getConnection()
                                      .prepareStatement(query.toString());

        // set the different fields
        for (int i=0; i<whatFieldsAreSet.size(); i++){
            statement.setString(i+1, fields.get(whatFieldsAreSet.get(i)));
        }

        return statement;
    }
    private static void setStringBuilder(StringBuilder sb, String key, List<String> keys, List<String> whatFieldsAreSet){
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
            sb.append("Room LIKE ?");
            whatFieldsAreSet.add(keys.get(ROOM_CODE));
        }else if(key.equals(keys.get(RES_CODE))){
            sb.append("CODE LIKE ?");
            whatFieldsAreSet.add(keys.get(RES_CODE));
        }
    }
}

