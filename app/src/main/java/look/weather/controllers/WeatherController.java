package look.weather.controllers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import look.weather.dto.GetDayDTO;
import look.weather.dto.ResponseDTO;
import look.weather.services.RedisService;
import look.weather.services.WeatherService;

public class WeatherController extends HttpServlet {
    private RedisService redisService = new RedisService();

    private WeatherService weatherService = new WeatherService();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String city = request.getParameter("city");

        ResponseDTO<List<GetDayDTO>> responseDTO = new ResponseDTO<>();

        if(this.redisService.exists(city)) {
            System.out.println("[INFO] Use Redis");

            String json = this.redisService.getString(city);

            List<GetDayDTO> days = this.objectMapper.readValue(json, List.class);

            responseDTO.setData(days);
        } else {
            System.out.println("[INFO] Send request");

            responseDTO = this.weatherService.getDays(city);

            this.redisService.setWithExpiry(
                city,
                this.objectMapper.writeValueAsString(responseDTO.getData()),
                60 * 15
            );
        }

        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpServletResponse.SC_OK);
        
        response.getWriter().println(
            this.objectMapper.writeValueAsString(responseDTO)
        );
    }
}
