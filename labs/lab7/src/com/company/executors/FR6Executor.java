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

    public enum MONTH{
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;
        private static MONTH [] months = values();
        /** january = 1, ect. so minus one to relate to enum value **/
        public static MONTH getMonth(int monthNum){
            return months[(monthNum-1) % months.length];
        }


    }

    @Override
    public void execute() {

        PreparedStatement statement;
        Map<MONTH, Double> totalRevPerMonth = new EnumMap<MONTH, Double>(MONTH.class);
        initTotRevPerMonth(totalRevPerMonth);
        MONTH month = MONTH.JANUARY;

        try {
            statement=FR6Preparer.select();
            ResultSet resultSet = statement.executeQuery();

            Map<Integer, FR> res = getReservations(resultSet, new FR6());
            List<FR6> records = res.values().stream().map(f->(FR6)f).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            formatSchema(sb, DateFactory.getMonths());

            Double RoomTotRev=0.0;

            /** Step 1 - go through all the records **/
            for(FR6 record: records){

                /** for the first month get roomname **/
                 if((month = MONTH.getMonth(record.getMonth()))==MONTH.JANUARY) {
                    sb.append(record.getRoomName() + ", ");
                 }
                 /** append month rev **/
                 sb.append(record.getMonthRev());
                 totalRevPerMonth.put(month, totalRevPerMonth.get(month)+record.getMonthRev());
                 /** if not the last month then append comma */
                 if(month != MONTH.DECEMBER){
                     RoomTotRev+=record.getMonthRev();
                     sb.append(", ");
                 }else{
                     RoomTotRev+=record.getMonthRev();
                     sb.append(", " + FR6.roundTwoDecimal(RoomTotRev));
                     sb.append("\n");
                     RoomTotRev=0.0;
                 }
            }
            System.out.print(sb);
            // have to print out the total
            printTotalRecord(totalRevPerMonth);



        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private void printTotalRecord(Map<MONTH, Double> map){
        Double total = 0.0;
        System.out.print("Total, ");
        for(Double monthTot: map.values()){
            System.out.print(FR.roundTwoDecimal(monthTot)+", ");
            total+=monthTot;
        }
        System.out.println(FR.roundTwoDecimal(total));

    }
    private void initTotRevPerMonth(Map<MONTH, Double> map){
        for (MONTH month: MONTH.months){
            map.put(month, 0.0);
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
