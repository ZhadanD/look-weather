package look.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCityDTO {
    private String name;
    
    private Double latitude;

    private Double longitude;

    private String timezone;
}
