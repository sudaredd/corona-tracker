package app.corona.coronatracker.service;

import app.corona.coronatracker.model.CoronaStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CoronaTrackerService {

    public static final String CORONA_STATUS_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<CoronaStats> coronaStatsList = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "0 45 6 ? * *")
    public void init() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(CORONA_STATUS_URL))
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        StringReader stringReader = new StringReader(response.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        List<CoronaStats> list = StreamSupport.stream(records.spliterator(), false)
                .map(this::coronaStat).collect(Collectors.toList());
        coronaStatsList = list;
        log.info("records:" + list);
    }

   public List<CoronaStats> latestCoronaStats() {
        return coronaStatsList;
    }

    private CoronaStats coronaStat(CSVRecord record) {
        int latest = Integer.parseInt(record.get(record.size() - 1));
        int last = Integer.parseInt(record.get(record.size() - 2));
        return new CoronaStats(record.get("Province/State"), record.get("Country/Region"), latest, latest-last );
    }
}
