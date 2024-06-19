package com.example.musicplayer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "eqconfig_data")
public class Eqconfig {
    @ColumnInfo(name = "nome")
    private String nome;
    @ColumnInfo(name = "hz80value")
    private Integer hz80value;
    @ColumnInfo(name = "hz160value")
    private Integer hz160value;
    @ColumnInfo(name = "hz240value")
    private Integer hz240value;
    @ColumnInfo(name = "hz320value")
    private Integer hz320value;


    @PrimaryKey(autoGenerate = true)
    public Long id;

    public Eqconfig(String nome, Integer hz80value, Integer hz160value, Integer hz240value, Integer hz320value) {
        this.nome = nome;
        this.hz80value = hz80value;
        this.hz160value = hz160value;
        this.hz240value = hz240value;
        this.hz320value = hz320value;
    }

    public Eqconfig(String stringArray){
        String truncated = stringArray.substring(1,stringArray.length()-1);
        String[] splitted = truncated.split(",");
        this.nome = splitted[0];
        this.hz80value = Integer.valueOf(splitted[1]);
        this.hz160value = Integer.valueOf(splitted[2]);
        this.hz240value = Integer.valueOf(splitted[3]);
        this.hz320value = Integer.valueOf(splitted[4]);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setHz80value(Integer hz80value) {
        this.hz80value = hz80value;
    }
    public void setHz160value(Integer hz160value) {
        this.hz160value = hz160value;
    }
    public void setHz240value(Integer hz240value) {
        this.hz240value = hz240value;
    }
    public void setHz320value(Integer hz320value) {
        this.hz320value = hz320value;
    }

    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public Integer getHz80value() {
        return hz80value;
    }
    public Integer getHz160value() {
        return hz160value;
    }
    public Integer getHz240value() {
        return hz240value;
    }
    public Integer getHz320value() {
        return hz320value;
    }


    public String toStringArray(){
        return "["+nome+","+hz80value.toString()+","+hz160value.toString()+","+hz240value.toString()+","+hz320value.toString()+"]";
    }

    @Override
    public String toString() {
        return getNome();
    }
}

