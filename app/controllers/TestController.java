package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import datasources.THSDataSource;
import models.MarketData;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Result;

import java.util.List;

import static play.mvc.Results.ok;

/**
 * Created by ballma on 16/12/15.
 */
public class TestController {

    private WSClient wsClient;

    @Inject
    public TestController(WSClient wsClient){
        this.wsClient = wsClient;
    }
    public Result initMarketData(String code, String start, String end) throws Exception {
        JsonNode jsonNode = new THSDataSource(this.wsClient).updateMarketData(code, start, end);
        List<MarketData> marketDatas = MarketData.transform(jsonNode);
        return ok(Json.toJson(MarketData.saveAll(marketDatas)));
    }

}
