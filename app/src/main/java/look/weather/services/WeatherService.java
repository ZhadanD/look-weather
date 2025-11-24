package look.weather.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import look.weather.dto.GetAllCityDTO;
import look.weather.dto.GetCityDTO;
import look.weather.dto.GetDayDTO;
import look.weather.dto.GetWeatherDTO;
import look.weather.dto.GetWeatherForecastDTO;
import look.weather.dto.ResponseDTO;
import look.weather.utils.PropertyUtil;
import look.weather.utils.WeatherUtil;

public class WeatherService {
    private String geocodingUrl;

    private String openMeteoUrl;

    public WeatherService() {
        PropertyUtil propertyUtil = new PropertyUtil();

        Properties properties = propertyUtil.loadProperties();

        this.geocodingUrl = properties.getProperty("geocoding.url");

        this.openMeteoUrl = properties.getProperty("openMeteo.url");
    }

    public ResponseDTO<List<GetDayDTO>> getDays(String city) {
        try {
            GetCityDTO cityDTO = this.sendRequestToGeocoding(city);

            if (cityDTO != null) {
                List<GetDayDTO> days = this.sendRequestToOpenMeteo(
                        cityDTO.getLatitude(),
                        cityDTO.getLongitude()
                );

                return new ResponseDTO<>(days);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<GetDayDTO> sendRequestToOpenMeteo(Double latitude, Double longitude) {
        Client client = ClientBuilder.newClient();

        String url = String.format(
                openMeteoUrl,
                latitude,
                longitude);

        Response openMeteoData = client.target(url)
                .request()
                .get();

        client.close();

        if (openMeteoData.getStatus() == 200) {
            GetWeatherDTO weatherDTO = openMeteoData.readEntity(GetWeatherDTO.class);

            return this.getDayDTOs(weatherDTO);
        }

        return null;
    }

    private GetCityDTO sendRequestToGeocoding(String city) {
        Client client = ClientBuilder.newClient();

        Response geocodingData = client.target(geocodingUrl + city)
                .request()
                .get();

        client.close();

        if (geocodingData.getStatus() == 200) {
            GetAllCityDTO dto = geocodingData.readEntity(GetAllCityDTO.class);

            return getOneCity(dto);
        } else
            return null;
    }

    private GetCityDTO getOneCity(GetAllCityDTO getAllCityDTO) {
        Optional<GetCityDTO> optionalCity = getAllCityDTO.getResults()
                .stream()
                .filter(
                        cityDTO -> cityDTO
                                .getTimezone()
                                .equals("Europe/Moscow"))
                .findFirst();

        return optionalCity.get();
    }

    private List<GetDayDTO> getDayDTOs(GetWeatherDTO weatherDTO) {
        List<String> time = weatherDTO.getHourly().getTime();

        List<Double> temperature_2m = weatherDTO.getHourly().getTemperature_2m();

        List<GetDayDTO> days = new ArrayList<>();

        for (int i = 0; i < time.size(); i++) {
            String dateTime = time.get(i);

            String date = WeatherUtil.getDate(dateTime);

            String hours = WeatherUtil.getTime(dateTime);

            GetDayDTO day = new GetDayDTO();
            day.setDate(date);

            day.getWeatherForecast().add(
                    new GetWeatherForecastDTO(
                            hours,
                            temperature_2m.get(i)));

            int j = i + 1;

            for (; j < time.size(); j++) {
                String dateTime1 = time.get(j);

                String date1 = WeatherUtil.getDate(dateTime1);

                if (!date.equals(date1))
                    break;

                String time1 = WeatherUtil.getTime(dateTime1);

                day.getWeatherForecast().add(
                        new GetWeatherForecastDTO(
                                time1,
                                temperature_2m.get(j)));
            }

            i = j - 1;

            days.add(day);
        }

        return days;
    }
}
