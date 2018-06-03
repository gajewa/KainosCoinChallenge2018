package com.kainos.service;

import com.google.gson.Gson;
import com.kainos.entity.Dataset;
import com.kainos.entity.Message;
import com.kainos.entity.PricingSample;
import com.kainos.entity.TrendCalculator;
import com.kainos.tools.GetRequest;
import org.springframework.stereotype.Service;

import java.io.DataOutput;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataService {

    public Dataset getSamples() throws IOException, ParseException {
        GetRequest request = new GetRequest("https://apiv2.bitcoinaverage.com/indices/global/history/BTCUSD?period=monthly&?format=json");



        String next = request.getNextDataset();

        Object[] btcObj = getObjectList(next);
        String[] keyArr = (String[])btcObj[0];
        ArrayList<Double> btcValues = (ArrayList<Double>)btcObj[1];

        request.changePath("https://apiv2.bitcoinaverage.com/indices/global/history/ETHUSD?period=monthly&?format=json");
        next = request.getNextDataset();
        ArrayList<Double> ethValues = (ArrayList<Double>) getObjectList(next)[1];

        request.changePath("https://apiv2.bitcoinaverage.com/indices/global/history/LTCUSD?period=monthly&?format=json");
        next = request.getNextDataset();
        ArrayList<Double> ltcValues = (ArrayList<Double>) getObjectList(next)[1];


        ArrayList<Double> testA = new ArrayList<Double>(){{
            add(17.0);
            add(17.0);
            add(15.0);
            add(13.0);
            add(12.0);
            add(15.0);
            add(17.0);
            add(18.0);
            add(18.0);
            add(16.0);
            add(17.0);
            add(20.0);
            add(22.0);
        }};

        ArrayList<Double> testB = new ArrayList<Double>(){{
            add(10.0);
            add(12.0);
            add(13.0);
            add(11.0);
            add(11.0);
            add(9.0);
            add(11.0);
            add(13.0);
            add(15.0);
            add(15.0);
            add(15.0);
            add(13.0);
            add(11.0);
        }};

        ArrayList<Double> testC = new ArrayList<Double>(){{
            add(1.0);
            add(4.0);
            add(6.0);
            add(8.0);
            add(8.0);
            add(11.0);
            add(13.0);
            add(11.0);
            add(13.0);
            add(13.0);
            add(14.0);
            add(17.0);
            add(18.0);
        }};

        ArrayList<Message> messages = getMessages(btcValues, ethValues, ltcValues);
        TrendCalculator calc = new TrendCalculator();
        ArrayList<ArrayList<Double>> btcTrendSamples= calc.getTrendSamples(btcValues.toArray(new Double[0]));
        ArrayList<ArrayList<Double>> ltcTrendSamples= calc.getTrendSamples(ltcValues.toArray(new Double[0]));
        ArrayList<ArrayList<Double>> ethTrendSamples= calc.getTrendSamples(ethValues.toArray(new Double[0]));

        return new Dataset(keyArr, btcValues, ethValues, ltcValues, messages, btcTrendSamples, ltcTrendSamples, ethTrendSamples);
    }


    private ArrayList<Message> getMessages(ArrayList<Double> btc, ArrayList<Double> eth, ArrayList<Double> ltc){
        ArrayList<Double> btcDer = getDerivative(btc);
        ArrayList<Double> ethDer = getDerivative(eth);
        ArrayList<Double> ltcDer = getDerivative(ltc);
        ArrayList<Message> messages = new ArrayList<Message>();

        for(int i = 2; i < btcDer.size()-2; i++){

            if(ascTrendStart(btcDer, i)){

                if(ascTrendStart(ltcDer, i) || ascTrendStart(ltcDer, i+1)){
                    messages.add(new Message("LTC started growing right after BTC.", true, i-1));
                }

                if(ascTrendStart(ethDer, i) || ascTrendStart(ethDer, i+1)){
                    messages.add(new Message("ETH started growing right after BTC.", true, i-1));
                }
            } else
            if(descTrendStart(btcDer, i)){

                if(descTrendStart(ltcDer, i) || descTrendStart(ltcDer, i+1)){
                    messages.add(new Message("LTC started to drop right after BTC.", false, i-1));
                }

                if(descTrendStart(ethDer, i) || descTrendStart(ethDer, i+1)){
                    messages.add(new Message("ETH started to drop right after BTC.", false, i-1));
                }
            }
        }

        return messages;
    }

    private Object[] getObjectList(String string){
        Gson gson = new Gson();
        PricingSample[] samples = gson.fromJson(string, PricingSample[].class);

        Map<String, Double> counts = Arrays.stream(samples).collect(Collectors.groupingBy(e -> e.getTime().toString().substring(0, 10),
                Collectors.averagingDouble(PricingSample::getAverage)
        ));

        Map<String, Double> newTreemap = new TreeMap<String, Double>(
                new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        o1 += " 2018";
                        o2 += " 2018";
                        try {
                            Date date1 = new SimpleDateFormat("E MMM dd yyyy", Locale.US).parse(o1);
                            Date date2 = new SimpleDateFormat("E MMM dd yyyy", Locale.US).parse(o2);

                            return date1.compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }

                }
        );

        newTreemap.putAll(counts);
        String[] keyArr = newTreemap.keySet().toArray(new String[newTreemap.keySet().size()]);
        ArrayList<Double> btcValues = new ArrayList<Double>();

        for(Double el : newTreemap.values().toArray(new Double[newTreemap.keySet().size()])){
            DecimalFormat f = new DecimalFormat("##.00");
            el = Double.parseDouble(f.format(el).replace(',','.'));
            btcValues.add(el);
        }

        return new Object[] {keyArr, btcValues};

    }

    public ArrayList<Double> getDerivative(ArrayList<Double> valuesList){
        ArrayList<Double> derArr = new ArrayList<Double>();
        DecimalFormat ff = new DecimalFormat();
        String asd = ff.format(valuesList.get(0)).replace(',','.').replaceAll("[\\xA0]", ""); // we replace the commas
                                                                                                // with dots as decimal separators
                                                                                                // and remove all non-breaking spaces (ascii 160)
                                                                                                // so that the parsing goes smoothly
        Double d =  Double.parseDouble(asd);
        derArr.add(d);

        for (int i = 1; i<valuesList.size(); i++){
            DecimalFormat f = new DecimalFormat();
            Double diff =  Double.parseDouble(f.format(valuesList.get(i) - valuesList.get(i-1)).replace(',','.'));
            derArr.add(diff);
        }

        return derArr;
    }

    private boolean ascTrendStart(ArrayList<Double> signal, int i){
        if(signal.get(i-1) <= 0 && signal.get(i) > 0)
            return true;
        else
            return false;
    }

    private boolean descTrendStart(ArrayList<Double> signal, int i){
        if(signal.get(i-1) >= 0 && signal.get(i) < 0)
            return true;
        else
            return false;
    }
}
