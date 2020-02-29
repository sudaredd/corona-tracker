package app.corona.coronatracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CoronaStats {
    private String state;
    private String country;
    private int count;
    private int diffFromPrevDay;
}
