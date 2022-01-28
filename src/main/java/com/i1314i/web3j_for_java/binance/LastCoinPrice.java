package com.i1314i.web3j_for_java.binance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LastCoinPrice {
    private String openTime;
    private String closeTime;
    private String closePrice;
    private String highPrice;
    private String lowPrice;
    private String wonPrice;
    private String failPrice;
    private String close4Price;
    private String duoKong;
    private int type;
    /**
     * 差价
     */
    private String chaPrice;

    /**
     * 模拟挂单价格
     */
    private String buyPrice;

    /**
     * 模拟止盈价格
     */
    private String sellPrice;

    /**
     * 模拟止损价格
     */
    private String sunPrice;

    /**
     * 是否达成止盈
     */
    private String wonGo;
    /**
     * 是否达成止损
     */
    private String failGo;
    /**
     * 成功状态
     */
    private String successStatus;


    public void load(){
        getWonGo();
        getFailGo();
        getBuyPrice();
        getSellPrice();
        getSunPrice();
        getChaPrice();
        getSuccessStatus();
    }

    public String getWonGo() {
        //多
        if(type==0){
            if(new BigDecimal(closePrice).add(new BigDecimal(200)).subtract(new BigDecimal(highPrice)).floatValue()<0){
                wonGo="Y";
            }else {
                wonGo="N";
            }
        }else if(type==1){
            if(new BigDecimal(closePrice).subtract(new BigDecimal(200)).subtract(new BigDecimal(lowPrice)).floatValue()>0){
                wonGo="Y";
            }else{
                wonGo="N";
            }
        }else {
            if(new BigDecimal(closePrice).add(new BigDecimal(200)).subtract(new BigDecimal(highPrice)).floatValue()<0){
                wonGo="Y";
            }else {
                wonGo="N";
            }
        }
        return wonGo;
    }

    public String getFailGo() {
        //多
        if(type==0){
            if(new BigDecimal(closePrice).subtract(new BigDecimal(200)).subtract(new BigDecimal(lowPrice)).floatValue()>0){
                failGo="Y";
            }else {
                failGo="N";
            }
        }else if(type==1){
            if(new BigDecimal(closePrice).add(new BigDecimal(200)).subtract(new BigDecimal(highPrice)).floatValue()<0){
                failGo="Y";
            }else{
                failGo="N";
            }
        }else {
            if(new BigDecimal(closePrice).subtract(new BigDecimal(200)).subtract(new BigDecimal(lowPrice)).floatValue()>0){
                failGo="Y";
            }else {
                failGo="N";
            }
        }
        return failGo;
    }

    public String getSuccessStatus() {
        if(getWonGo().equalsIgnoreCase("Y")&&!getFailGo().equalsIgnoreCase("Y")){
            successStatus= "Y";
        }else if(getWonGo().equalsIgnoreCase("Y")&&getFailGo().equalsIgnoreCase("Y")){
            successStatus= "同时触发-失败";
        }else if(getFailGo().equalsIgnoreCase("Y")){
            successStatus= "N";
        }else if(type==0&&new BigDecimal(buyPrice).subtract(new BigDecimal(lowPrice)).floatValue()>0){
            successStatus= "做多未开仓";
        }else if(type==1&&new BigDecimal(buyPrice).subtract(new BigDecimal(highPrice)).floatValue()>0){
            successStatus= "做空未开仓";
        }else {
            successStatus= "被套";
        }


        return successStatus;
    }

    public String getChaPrice() {
        //多
        if(type==0){
            chaPrice=wonPrice;
        }else if(type==1){
            chaPrice=failPrice;
        }else {
            chaPrice=wonPrice;
        }
        return chaPrice;
    }




    public String getBuyPrice() {
        if(type==0){
            buyPrice=new BigDecimal(closePrice).subtract(new BigDecimal(200)).toString();
        }else if(type==1){
            buyPrice=new BigDecimal(closePrice).add(new BigDecimal(200)).toString();
        }else{
            buyPrice="4小时和5小时收盘价一致";

        }
        return buyPrice;
    }

    public String getSellPrice() {
        if(type==0){
            sellPrice=new BigDecimal(closePrice).add(new BigDecimal(200)).toString();
        }else if(type==1){
            sellPrice=new BigDecimal(closePrice).subtract(new BigDecimal(200)).toString();
        }else{
            sellPrice="4小时和5小时收盘价一致";

        }
        return sellPrice;
    }

    public String getSunPrice() {
        if(type==0){
            sunPrice=new BigDecimal(closePrice).subtract(new BigDecimal(400)).toString();
        }else if(type==1){
            sunPrice=new BigDecimal(closePrice).add(new BigDecimal(400)).toString();
        }else{
            sunPrice="4小时和5小时收盘价一致";

        }
        return sunPrice;
    }
}
