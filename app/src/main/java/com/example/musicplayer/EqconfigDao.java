package com.example.musicplayer;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EqconfigDao {
    private Map<String,Eqconfig> configs;
    private String dataString;
    private static EqconfigDao instance;
    private SharedPreferences sharedPreferences;

    public EqconfigDao(Context context){
        this.configs = new HashMap<>();
        sharedPreferences = context.getSharedPreferences("eqconfig", Context.MODE_PRIVATE);
        init(sharedPreferences.getString("eqconfig_data","[[Default,50,50,50,50]]"));
    }

    private void init(String data){
        String truncated = data.substring(1,data.length()-1);
        String[] arrayList = truncated.split(";");
        for(String item : arrayList){
            Eqconfig eqConfig = new Eqconfig(item);
            configs.put(eqConfig.getNome(), eqConfig);
        }
    }

    public static synchronized EqconfigDao getInstance(Context context){
        if (instance == null){
            instance = new EqconfigDao(context);

        } return instance;
    }

    private void flushData(){
        String buffer = "[";
        for(int i = 0;i<getAll().size();i++){
            buffer = buffer.concat(getAll().get(i).toStringArray());

            if(i != getAll().size()-1){
                buffer = buffer.concat(";");
            }

        }
        buffer = buffer.concat("]");

        dataString = buffer.toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("eqconfig_data", dataString);
        editor.apply();
    }

    public boolean insert(Eqconfig config){
        if(configs.containsKey(config.getNome())){
            return false;
        }

        configs.put(config.getNome(), config);
        flushData();
        return true;
    }

    public void delete(Eqconfig config){
        configs.remove(config.getNome());
        flushData();
    }

    public void update(Eqconfig config){
        configs.put(config.getNome(), config);
        flushData();
    }

    public List<Eqconfig> getAll(){
        List<Eqconfig> list = new ArrayList<>();
        for(Eqconfig eqConf : configs.values()){
            list.add(eqConf);
        }
        return list;
}

    public Eqconfig getByKey(String nome){
        return configs.get(nome);
    }

}
