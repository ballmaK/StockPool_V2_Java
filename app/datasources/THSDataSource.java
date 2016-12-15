package datasources;

import com.fasterxml.jackson.databind.JsonNode;
import models.MarketData;
import play.inject.ApplicationLifecycle;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by ballma on 16/12/15.
 */
public class THSDataSource {

    private static final String MARKET_DATA_BASEURL = "http://q.stock.sohu.com/hisHq";
    private static final String STOCK_DATA_BASEURL = "http://qd.10jqka.com.cn/api.php";

    private static long WS_TIMEOUT = 10000;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    protected final WSClient wsClient;

    @Inject
    public THSDataSource(WSClient wsClient) {
        this.wsClient = wsClient;
    }

    public JsonNode updateMarketData(String code, String start, String end) throws Exception {
        final WSResponse r = fetchDataResponse(MARKET_DATA_BASEURL,
                new QueryParam("code", code==null? "zs_000001": code),
                new QueryParam("start", start==null? dateFormat.format(new Date()):start),
                new QueryParam("end", end==null? dateFormat.format(new Date()):end),
                new QueryParam("stat", "1"),
                new QueryParam("order", "D"),
                new QueryParam("period", "d"),
                new QueryParam("callback", "historySerchHandler"),
                new QueryParam("rt", "json"),
                new QueryParam("r", String.valueOf(new Date().getTime()))
        );
        JsonNode result = null;
        try {
            result = Json.parse(r.getBody()).get(0);
        }catch (Exception e){
            return null;
        }
        return result;
    }

    protected WSResponse fetchDataResponse(String url, QueryParam... params) throws Exception {
        List queryParams = Arrays.asList(params);
        WSRequest request = this.wsClient.url(url);
        Iterator e = queryParams.iterator();

        while(e.hasNext()) {
            THSDataSource.QueryParam param = (THSDataSource.QueryParam)e.next();
            request.setQueryParameter(param.param, param.value);
        }

        try {
            return (WSResponse)request.get().toCompletableFuture().get(WS_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | TimeoutException | InterruptedException var7) {
            throw new Exception();
        }
    }

    protected class QueryParam {
        private String param;
        private String value;

        public QueryParam(String param, String value) {
            this.param = param;
            this.value = value;
        }
    }

}
