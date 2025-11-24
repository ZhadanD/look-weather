package look.weather.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetDayDTO {
    private String date;

    private List<GetWeatherForecastDTO> weatherForecast = new ArrayList<>();
}
