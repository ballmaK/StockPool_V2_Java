package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ballma on 16/12/15.
 */
@Entity
@Table(name = "sp_market")
public class MarketData extends Model{

    public String symbol;
    @Id
    public String tradeDate;
    public Float open;
    public Float end;
    public Float increase;
    public Float increasePer;
    public Float low;
    public Float high;
    public Long volume;
    public Long amount;

    public MarketData(JsonNode jsonNode){
        this.tradeDate = jsonNode.get(0).asText();
        this.open = Float.valueOf(jsonNode.get(1).asText());
        this.end = Float.valueOf(jsonNode.get(2).asText());
        this.increase = Float.valueOf(jsonNode.get(3).asText());
        this.increasePer = Float.valueOf(jsonNode.get(4).asText().replace("%", ""));
        this.low = Float.valueOf(jsonNode.get(5).asText());
        this.high = Float.valueOf(jsonNode.get(6).asText());
        this.volume = jsonNode.get(7).asLong();
        this.amount = jsonNode.get(8).asLong();
    }

    public static final List<MarketData> transform(JsonNode jsonNode){
        List<MarketData> marketDatas = new ArrayList<>();
        Integer status = null;
        String code = null;
        Iterator<JsonNode> jsonNodeIterator = null;
        if (jsonNode.has("status")){
            status = jsonNode.get("status").asInt();
        }
        if (jsonNode.has("code")){
            code = jsonNode.get("code").asText();
        }
        if (jsonNode.has("hq")){
            jsonNodeIterator = jsonNode.get("hq").elements();
        }
        if (status == 0){
            while (jsonNodeIterator.hasNext()){
                MarketData marketData = new MarketData(jsonNodeIterator.next());
                marketData.symbol = code;
                marketDatas.add(marketData);
            }
        }
        return marketDatas;
    }

    public static final List<MarketData> saveAll(List<MarketData> marketDatas){
        for (MarketData marketData: marketDatas){
            try {
                Model.db().save(marketData);
            }catch (PersistenceException e){
                continue;
            }
        }
        return marketDatas;
    }

    public static final Model.Finder<Long, MarketData> find = new Model.Finder<>(Long.class,
            MarketData.class);
}
