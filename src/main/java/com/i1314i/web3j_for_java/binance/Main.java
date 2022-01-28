package com.i1314i.web3j_for_java.binance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.i1314i.web3j_for_java.excel.JavaPoiUtil;
import io.itit.itf.okhttp.FastHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 计算 币安 指标  5点
 */
public class Main {
    public static void main2(String[] args) throws Exception {
        List<CoinPrice>coinPriceList=getLastCoinPrices(0);
        System.out.println(JSON.toJSONString(coinPriceList));
    }
    public static void main(String[] args) throws Exception {
        List<LastCoinPrice> lastCoinPrices=new ArrayList<>();
        for (int i=0;i<1000;i++){
            try {
                lastCoinPrices.add(getLastCoinPrice(i));
            }catch (Exception e){
                e.printStackTrace();
                break;
            }

        }
        List<LastCoinPrice> duoList=new ArrayList<>();
        List<LastCoinPrice> kongList=new ArrayList<>();
        List<LastCoinPrice> hengpanList=new ArrayList<>();
        for (int i=0;i<lastCoinPrices.size();i++){
            LastCoinPrice lastCoinPrice=lastCoinPrices.get(i);
            lastCoinPrice.setType(0);
            lastCoinPrice.setDuoKong("多");
            if(new BigDecimal(lastCoinPrice.getClosePrice()).subtract(new BigDecimal(lastCoinPrice.getClose4Price())).floatValue()>0){
                duoList.add(lastCoinPrice);
            }else if(new BigDecimal(lastCoinPrice.getClosePrice()).subtract(new BigDecimal(lastCoinPrice.getClose4Price())).floatValue()<0){
                lastCoinPrice.setType(1);
                lastCoinPrice.setDuoKong("空");
                kongList.add(lastCoinPrice);
            }else {
                lastCoinPrice.setType(2);
                lastCoinPrice.setDuoKong("未知");
                hengpanList.add(lastCoinPrice);
            }
        }

        List<List<LastCoinPrice> > allDataList=new ArrayList<>();
        allDataList.add(duoList);
        allDataList.add(kongList);
        allDataList.add(hengpanList);
        List<String>sheetNames= Arrays.asList("做多","做空","未知");
        JavaPoiUtil.createWorkBooksWithSheet(LastCoinPrice.class,allDataList,new String[]{"openTime","close4Price","closePrice","chaPrice","duoKong","highPrice","lowPrice","buyPrice","sellPrice","sunPrice","wonGo","failGo","successStatus"},new String[]{"开盘时间","4收价格","5收价格","价差","多还是空","最高价格","最低价格","模拟挂单价格","模拟止盈价格","模拟止损价格","模拟止盈达成","模拟止损达成","成功失败"},2003,sheetNames).write(new FileOutputStream(System.getProperty("user.dir")+"/coinPrice11.xls"));


        JavaPoiUtil.createWorkBooks(LastCoinPrice.class,lastCoinPrices,new String[]{"openTime","close4Price","closePrice","highPrice","lowPrice","wonPrice","failPrice"},new String[]{"开盘时间","4点收盘价格","5点收盘价格","最高价格","最低价格","5-24时前最高价-收盘价","5-24时前最低价-收盘价"}).write(new FileOutputStream(System.getProperty("user.dir")+"/coinPrice.xls"));
//        JavaPoiUtil.createWorkBook(data,new String[]{"ak","sk","resourcesId","cdnResourceId","meetingResourceId","ipResourceId","natgatewayResourceId","natgatewayResourceSingleSumId","directconnectionResourceId","wafQpsResourceId","lbQpsResourceId"},new String[]{"ak","sk","云拨侧id","CDN资源id","云会议资源id","公网ip资源id","nat网关资源id_流量","nat网关资源id_流量/活跃连接数(分+和)","托管专线资源id","waf资源id_qps","lb资源id_qps"},2003).write(response.getOutputStream());

    }

    public static LastCoinPrice getLastCoinPrice(int timeIndex) throws Exception {
        String body=FastHttpClient.get().url("https://fapi.binance.com/fapi/v1/klines?symbol=BTCUSDT&contractType=PERPETUAL&interval=1h&startTime="+NumberOfDaysStartUnixTime(timeIndex,4)+"&endTime="+NumberOfDaysStartUnixTime(timeIndex,24)).build().execute().string();
        JSONArray array=JSONArray.parseArray(body);
        List<CoinPrice> coinPriceList=new ArrayList<>();
        LastCoinPrice lastCoinPrice=new LastCoinPrice();
        System.out.println(timeIndex);
        for (int i=0;i<array.size();i++){
            String sdata=array.getString(i);
            JSONArray data=JSONArray.parseArray(sdata);
            if(i==0){
                lastCoinPrice.setClose4Price(data.getString(4));
            }

            if(i==1){
                lastCoinPrice.setClosePrice(data.getString(4));
            }

            if(i>=1){
                CoinPrice coinPrice=new CoinPrice();
                coinPrice.setOpenTime(data.getLong(0));
                coinPrice.setOpenTimeString(time2String(data.getLong(0)));
                coinPrice.setOpenPrice(data.getString(1));
                coinPrice.setHighPrice(data.getString(2));
                coinPrice.setLowPrice(data.getString(3));
                coinPrice.setClosePrice(data.getString(4));
                coinPrice.setCloseTime(data.getLong(6));
                coinPrice.setCloseTimeString(time2String(data.getLong(6)));
                coinPrice.setTranNum(data.getString(5));
                coinPriceList.add(coinPrice);
            }

        }

        lastCoinPrice.setHighPrice(high(coinPriceList));
        lastCoinPrice.setLowPrice(low(coinPriceList));
//        lastCoinPrice.setClosePrice(coinPriceList.get(coinPriceList.size()-1).getClosePrice());
        lastCoinPrice.setOpenTime(coinPriceList.get(0).getOpenTimeString());
        lastCoinPrice.setWonPrice(new BigDecimal(lastCoinPrice.getHighPrice()).subtract(new BigDecimal(lastCoinPrice.getClosePrice())).toString());
        lastCoinPrice.setFailPrice(new BigDecimal(lastCoinPrice.getLowPrice()).subtract(new BigDecimal(lastCoinPrice.getClosePrice())).toString());
        lastCoinPrice.load();
        return lastCoinPrice;
    }


    public static List<CoinPrice> getLastCoinPrices(int timeIndex) throws Exception {
        String body=FastHttpClient.get().url("https://fapi.binance.com/fapi/v1/klines?symbol=BTCUSDT&contractType=PERPETUAL&interval=1h&startTime="+NumberOfDaysStartUnixTime(timeIndex,4)+"&endTime="+NumberOfDaysStartUnixTime(timeIndex,24)).build().execute().string();
        JSONArray array=JSONArray.parseArray(body);
        List<CoinPrice> coinPriceList=new ArrayList<>();

        System.out.println(timeIndex);
        for (int i=0;i<array.size();i++){
            String sdata=array.getString(i);
            JSONArray data=JSONArray.parseArray(sdata);
            CoinPrice coinPrice=new CoinPrice();
            coinPrice.setOpenTime(data.getLong(0));
            coinPrice.setOpenTimeString(time2String(data.getLong(0)));
            coinPrice.setOpenPrice(data.getString(1));
            coinPrice.setHighPrice(data.getString(2));
            coinPrice.setLowPrice(data.getString(3));
            coinPrice.setClosePrice(data.getString(4));
            coinPrice.setCloseTime(data.getLong(6));
            coinPrice.setCloseTimeString(time2String(data.getLong(6)));
            coinPrice.setTranNum(data.getString(5));
            coinPriceList.add(coinPrice);

        }
        return coinPriceList;
    }


    public static String high(List<CoinPrice> coinPriceList){
        BigDecimal price=new BigDecimal(0);
        for (int i=0;i<coinPriceList.size();i++){
            BigDecimal high=new BigDecimal(coinPriceList.get(i).getHighPrice());
            if(high.floatValue()>price.floatValue()){
                price=new BigDecimal(coinPriceList.get(i).getHighPrice());
            }

        }
        return price.toString();
    }


    public static String low(List<CoinPrice> coinPriceList){
        BigDecimal price=new BigDecimal(1000000000);
        for (int i=0;i<coinPriceList.size();i++){
            BigDecimal high=new BigDecimal(coinPriceList.get(i).getLowPrice());
            if(high.floatValue()<price.floatValue()){
                price=new BigDecimal(coinPriceList.get(i).getLowPrice());
            }

        }
        return price.toString();
    }

    public static long NumberOfDaysStartUnixTime(int NumberOfDays,int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)-NumberOfDays,hours-1,0,0);
        long yesterdayStart  = calendar.getTimeInMillis();
        return yesterdayStart;

    }

    public static String time2String(long timestamp){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置格式
        String timeText=format.format(timestamp);
        return timeText;
    }


}
