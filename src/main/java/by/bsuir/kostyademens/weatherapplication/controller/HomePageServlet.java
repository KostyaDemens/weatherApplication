package by.bsuir.kostyademens.weatherapplication.controller;

import by.bsuir.kostyademens.weatherapplication.dto.CoordinatesDto;
import by.bsuir.kostyademens.weatherapplication.dto.LocationDto;
import by.bsuir.kostyademens.weatherapplication.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/home-page")
public class HomePageServlet extends BaseServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    User user = (User) req.getAttribute("user");
    List<LocationDto> userLocations = userService.getUserLocations(user);
    if (!userLocations.isEmpty()) {
      context.setVariable("forecasts", userLocations);
    }
    engine.process("homePage", context, resp.getWriter());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {

    CoordinatesDto coordinatesDto;
    try {
      BigDecimal longitude = new BigDecimal(req.getParameter("longitude"));
      BigDecimal latitude = new BigDecimal(req.getParameter("latitude"));

      coordinatesDto = CoordinatesDto.builder().lon(longitude).lat(latitude).build();
    } catch (NumberFormatException e) {
      req.getRequestDispatcher("/templates/error.html").forward(req, resp);
      return;
    }

    User user = (User) req.getAttribute("user");
    String locationName = req.getParameter("locationName");

    locationService.delete(coordinatesDto, user);

    if (!locationName.isEmpty()) {
      String encodedLocationName = URLEncoder.encode(locationName, StandardCharsets.UTF_8);
      resp.sendRedirect(req.getContextPath() + "/search?locationName=" + encodedLocationName);
      return;
    }
    resp.sendRedirect(req.getContextPath() + "/home-page");
  }
}
