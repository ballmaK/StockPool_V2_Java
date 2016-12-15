package models;

import com.avaje.ebean.Model;

import javax.persistence.Id;

/**
 * Created by ballma on 16/12/15.
 */
public class StockData extends Model{

    @Id
    public Long id;
    public String symbol;
    public String tradeDate;
    public Float open;
    public Float end;
    public Float increase;
    public Float increasePer;
    public Float low;
    public Float high;
    public Float volume;
    public Float amount;

    public static final Model.Finder<Long, StockData> find = new Model.Finder<>(Long.class,
            StockData.class);

    public static StockData findBySymbol(String symbol)
    {
        return find.where()
                .eq("symbol",
                        symbol)
                .findUnique();
    }
}
