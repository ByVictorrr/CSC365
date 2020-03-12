package com.company.executors;


import com.company.parsers.DateFactory;
import com.company.preparers.FR6Preparer;
import com.company.reservations.FR;
import com.company.reservations.FR6;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

public class FR6Executor extends Executor{
    @Override
    public void execute() {
        FR6Preparer preparer = new FR6Preparer();

        PreparedStatement statement;
        try {
            statement=preparer.selectFR6();
            ResultSet resultSet = statement.executeQuery();

            Map<Integer, FR> res = getReservations(resultSet, new FR6());
            List<FR6> records = res.values().stream().map(f->(FR6)f).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            formatSchema(sb, DateFactory.getMonths());

            Double RoomTotRev=0.0;
            for(FR6 record: records){
                // so append the room name
                 if(record.getMonth()==1) {
                    sb.append(record.getRoomName() + ", ");
                 }
                 sb.append(record.getMonthRev());
                 if(!(record.getMonth()==12)){
                     RoomTotRev+=record.getMonthRev();
                     sb.append(", ");
                 }else{
                     RoomTotRev+=record.getMonthRev();
                     sb.append(", " + FR6.roundTwoDecimal(RoomTotRev));
                     sb.append("\n");
                     RoomTotRev=0.0;
                 }
            }
            System.out.println(sb);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void formatSchema(StringBuilder sb, List<String> months){
        sb.append("RoomName, ");
        for(String month: months){
            sb.append(month + ", ");
        }
        sb.append("Total Revenue\n");
    }
}
