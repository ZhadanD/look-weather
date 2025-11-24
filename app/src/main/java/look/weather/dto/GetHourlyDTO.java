package look.weather.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetHourlyDTO {
    private List<String> time;

    private List<Double> temperature_2m;
}
