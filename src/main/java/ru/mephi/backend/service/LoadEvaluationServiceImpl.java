package ru.mephi.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.mephi.backend.dto.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoadEvaluationServiceImpl implements LoadEvaluationService{

    private final String YANDEX_API_KEY = "f5b9bb6a-b7fb-49cc-9fa7-fdaa9cabd18e";
    private final String YANDEX_GEOCODER_URL = "https://geocode-maps.yandex.ru/1.x/?apikey=" + YANDEX_API_KEY +
            "&geocode=%f,%f&kind=%s&format=%s";

    private final String GRAPHHOPPER_API_KEY = "ec4bdfe8-bd3b-49eb-9261-3ef02e206016";
    private final String GRAPHHOPPER_URL = "https://graphhopper.com/api/1/route?key=" + GRAPHHOPPER_API_KEY +
            "&profile=%s&points_encoded=%s";

    // Координаты центра Москвы
    private final Coordinate MoscowCentreCoordinate =
            Coordinate.builder().
            latitude(55.752671f).
            longitude(37.622148f).
            build();

    @Override
    public Map<RoadDTO, Double> getRoadCapacityChanges(List<RoadDTO> roads, int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {
        // распределить доп нагрузку относительно пиковой пропускной способности каждого объекта
        List<RoadDTO> roadsAlongShortestWay = getRoadsAlongShortestWay(roads,
                getCoordinateListOfShortestWayToCityCenter(constructionPoint));
        List<MetroStation> closestMetroStations = getClosestMetroStations(constructionPoint);


        return null;
    }


    @Override
    public Map<MetroStationDTO, Double> getMetroStationCapacityChanges(List<MetroStationDTO> metroStations,
                                                                       int extraLoad, Coordinate constructionPoint) {
        // распределить доп нагрузку относительно пиковой пропускной способности каждого объекта
        return null;
    }

    public Road getClosestRoad(Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {

        String request = String.format(YANDEX_GEOCODER_URL,constructionPoint.getLongitude(),
                constructionPoint.getLatitude(),"street", "json" );

        HttpResponse<String> response = sendHttpRequest(request);

        JSONObject jsonObj = new JSONObject(response.body());
        JSONArray addressComponents = jsonObj.getJSONObject("response")
                .getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember")
                .getJSONObject(0)
                .getJSONObject("GeoObject")
                .getJSONObject("metaDataProperty")
                .getJSONObject("GeocoderMetaData")
                .getJSONObject("Address")
                .getJSONArray("Components");

        String closestRoadName = "";
        for(int i = 0; i < addressComponents.length(); i++){
            JSONObject comp =addressComponents.getJSONObject(i);
            if(comp.get("kind").equals("street"))
                closestRoadName = (String) comp.get("name");
        }

        return Road.builder()
                .name(closestRoadName)
                .build();

    }

    public List<MetroStation> getClosestMetroStations(Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {

        final int limit = 3;
        String request = String.format(YANDEX_GEOCODER_URL,constructionPoint.getLongitude(),
                constructionPoint.getLatitude(),"metro", "json" );

        HttpResponse<String> response = sendHttpRequest(request);

        JSONObject jsonObj = new JSONObject(response.body());
        JSONArray members = jsonObj.getJSONObject("response")
                .getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember");

        List<MetroStation> closestMetroStations = new ArrayList<>();

        for(int i = 0; i < limit; i++){
            if(i>= members.length())
                break;

            JSONArray addressComponents = members.getJSONObject(i)
                    .getJSONObject("GeoObject")
                    .getJSONObject("metaDataProperty")
                    .getJSONObject("GeocoderMetaData")
                    .getJSONObject("Address")
                    .getJSONArray("Components");

            MetroStation metroStation = new MetroStation();
            for(int j = 0; j < addressComponents.length(); j++){
                JSONObject comp = addressComponents.getJSONObject(j);
                if(comp.get("kind").equals("route"))
                    metroStation.setRoute((String) comp.get("name"));
                else if(comp.get("kind").equals("metro"))
                    metroStation.setName((String) comp.get("name"));
            }

            closestMetroStations.add(metroStation);
        }

        return closestMetroStations;
    }

    private List<Coordinate> getCoordinateListOfShortestWayToCityCenter(Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {


        String request = String.format(GRAPHHOPPER_URL, "car", "false") + "&point=" + constructionPoint.getLatitude() +
                "," + constructionPoint.getLongitude() + "&point=" + MoscowCentreCoordinate.getLatitude() +
                "," + MoscowCentreCoordinate.getLongitude();

        System.out.println(request);
        HttpResponse<String> response = sendHttpRequest(request);

        JSONObject jsonObj = new JSONObject(response.body());
        System.out.println(response.body());

       JSONArray coordinatesArray = jsonObj.getJSONArray("paths")
               .getJSONObject(0)
                .getJSONObject("points")
               .getJSONArray("coordinates");

       List<Coordinate> coordinates = new ArrayList<>();

       for(int i = 0; i < coordinatesArray.length(); i++){
           JSONArray arr =  coordinatesArray.getJSONArray(i);
           BigDecimal longitude = (BigDecimal) arr.get(0);
           BigDecimal latitude = (BigDecimal) arr.get(1);
           Coordinate coordinate = new Coordinate( latitude.floatValue(), longitude.floatValue());
           coordinates.add(coordinate);
       }

        return coordinates;
    }

    private HttpResponse<String> sendHttpRequest(String request)
            throws URISyntaxException, IOException, InterruptedException {

        HttpRequest req = HttpRequest
                .newBuilder(new URI(request))
                .build();

        HttpClient client = HttpClient.newBuilder().build();

        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // проверить имеющиеся дороги на фрагмента карты на принадлежность кратчайшему маршруту до центра
    private List<RoadDTO> getRoadsAlongShortestWay(List<RoadDTO> roadsOnMap,
                                                                     List<Coordinate> shortestWayCoordinates){
        // делим точки вдоль кратч пути на отрезки
        // делим точки каждой дороги на фрагменте карты на отрезке
        // проверяем отрезки вдоль дороги на пересечение хотя бы с двумя отрезками вдоль кратч пути

        final int step = 1; // сколько координат пропускаем
        final int intersectionAmountRequired = 1; // сколько пересечений нужно
        boolean latitudeIntersection = false, longitudeIntersection = false;
        int intersections = 0;
        List<RoadDTO> roadsAlongShortestWay = new ArrayList<>();

        for (RoadDTO roadDTO : roadsOnMap) {

            List<Coordinate> coordinates = roadDTO.getCoordinates();

            for (int j = 0; j + step < coordinates.size() && j + step < shortestWayCoordinates.size(); j += step) {
                float roadLeftLatitude = coordinates.get(j).getLatitude();
                float roadRightLatitude = coordinates.get(j + step).getLatitude();
                float roadLeftLongitude = coordinates.get(j).getLongitude();
                float roadRightLongitude = coordinates.get(j + step).getLongitude();

                float shortestWayLeftLatitude = shortestWayCoordinates.get(j).getLatitude();
                float shortestWayRightLatitude = shortestWayCoordinates.get(j + step).getLatitude();
                float shortestWayLeftLongitude = shortestWayCoordinates.get(j).getLongitude();
                float shortestWayRightLongitude = shortestWayCoordinates.get(j + step).getLongitude();

                if (roadLeftLatitude > shortestWayLeftLatitude && roadLeftLatitude < shortestWayRightLatitude ||
                        roadRightLatitude > shortestWayLeftLatitude && roadRightLatitude < shortestWayRightLatitude ||
                        roadLeftLatitude < shortestWayLeftLatitude && roadRightLatitude > shortestWayRightLatitude)
                    latitudeIntersection = true;

                if (roadLeftLongitude > shortestWayLeftLongitude && roadLeftLongitude < shortestWayRightLongitude ||
                        roadRightLongitude > shortestWayLeftLongitude && roadRightLongitude < shortestWayRightLongitude ||
                        roadLeftLongitude < shortestWayLeftLongitude && roadRightLongitude > shortestWayRightLongitude)
                    longitudeIntersection = true;

                if (latitudeIntersection && longitudeIntersection)
                    intersections++;

                if (intersections == intersectionAmountRequired) {
                    roadsAlongShortestWay.add(roadDTO);
                    continue;
                }
            }
        }

        return roadsAlongShortestWay;
    }

}
