package ru.mephi.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Service;
import ru.mephi.backend.dto.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.*;
import java.util.stream.Collectors;

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
    public Map<RoadDTO, Integer> getRoadCapacityChanges(Set<RoadDTO> roads, int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {

        // Получили все дороги вдоль кратчайшего пути
        Set<RoadDTO> roadsAlongShortestRoute =
                getRoadsAlongShortestWay(roads, getCoordinateListOfShortestRoute(constructionPoint));

        // Дороги на фрагменте карты которые не входят в кратчайший путь
        Set<RoadDTO> roadsNotIncludedInShortestRoute =
                roadsAlongShortestRoute.parallelStream().filter( o -> !roads.contains(o)).collect(Collectors.toSet());

        // По условию 80% едет в центр (вдоль кратчайшего пути)
        final float percentCarDrivingToCenter = 0.8f;
        Map<RoadDTO, Integer> roadCapacityChanges
                = calcRoadCapacityChanges(roadsAlongShortestRoute, Math.round(extraLoad * percentCarDrivingToCenter));

        // По условию 20% едет на окраины
        // Объединяем множества
        final float percentCarDrivingToSuburbs = 0.2f;
        roadCapacityChanges.putAll(
                calcRoadCapacityChanges(
                        roadsNotIncludedInShortestRoute, Math.round(extraLoad * percentCarDrivingToSuburbs )));

        return roadCapacityChanges;
    }


    @Override
    public Map<MetroStationDTO, Integer> getMetroStationCapacityChanges(Set<MetroStationDTO> metroStations,
                                                                       int extraLoad, Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {

        // Получили ближайшие станции метро
        Set<MetroStation> closestMS = getClosestMetroStations(constructionPoint);

        // Преобразовали в DTO
        Set<MetroStationDTO> closestMSDTO = mapToDTO(closestMS, metroStations);

        // Более дальние станции метро на фрагменте карты
        Set<MetroStationDTO> notClosestMSDTO = closestMSDTO.parallelStream().
                filter( o -> !metroStations.contains(o)).collect(Collectors.toSet());

        // Предположили, что 80% населения пользуется ближайшими станциями метро
        final float percentPeopleGoingToClosestMS = 0.8f;
        Map<MetroStationDTO, Integer> metroStationCapacityChanges
                = calcMetroStationCapacityChanges(closestMSDTO, Math.round(extraLoad * percentPeopleGoingToClosestMS));

       // Предположим что оставшиеся 20% пользуются более дальними станциями метро
        final float percentPeopleGoingToFartherMS = 0.2f;
        metroStationCapacityChanges.putAll(
                calcMetroStationCapacityChanges(notClosestMSDTO, Math.round(extraLoad * percentPeopleGoingToFartherMS))
        );

        return metroStationCapacityChanges;
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

    public Set<MetroStation> getClosestMetroStations(Coordinate constructionPoint)
            throws URISyntaxException, IOException, InterruptedException {


        // Возьмем, например, две ближайшие станции метро
        final int limit = 2;
        String request = String.format(YANDEX_GEOCODER_URL,constructionPoint.getLongitude(),
                constructionPoint.getLatitude(),"metro", "json" );

        HttpResponse<String> response = sendHttpRequest(request);

        JSONObject jsonObj = new JSONObject(response.body());
        JSONArray members = jsonObj.getJSONObject("response")
                .getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember");

        Set<MetroStation> closestMetroStations = new HashSet<>();

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

    private Set<MetroStationDTO> mapToDTO(Set<MetroStation> msSet, Set<MetroStationDTO> msDTOSet){
        Set<MetroStationDTO> res = new HashSet<>();
        MetroStationDTO msDTO;
        Iterator<MetroStationDTO> iter;

        for(MetroStation metroStation: msSet ){
            iter = msDTOSet.iterator();
            while(iter.hasNext()){
                msDTO = iter.next();
                if(metroStation.getName().equals(msDTO.getName()) && metroStation.getRoute().equals(msDTO.getRoute())){
                    res.add(MetroStationDTO.builder()
                            .name(msDTO.getName())
                            .route(msDTO.getRoute())
                            .coordinate(msDTO.getCoordinate())
                            .capacity(msDTO.getCapacity())
                            .intensity(msDTO.getIntensity())
                            .build()
                    );
                    break;
                }
            }
        }

        return res;
    }

    private List<Coordinate> getCoordinateListOfShortestRoute(Coordinate constructionPoint)
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
    private Set<RoadDTO> getRoadsAlongShortestWay(Set<RoadDTO> roadsOnMap,
                                                  List<Coordinate> shortestRouteCoordinates){

        final int step = 1; // сколько координат пропускаем
        final int intersectionAmountRequired = 1; // сколько пересечений нужно
        int intersections = 0;

        Set<RoadDTO> roadsAlongShortestRoute = new HashSet<>();

        for (RoadDTO roadDTO : roadsOnMap) {

            List<Coordinate> coordinates = roadDTO.getCoordinates();
            for(int i = 0; i + step < coordinates.size(); i++){
                if(isIntersects(coordinates.get(i), coordinates.get(i + step), shortestRouteCoordinates, step))
                    intersections++;

                if (intersections == intersectionAmountRequired) {
                    roadsAlongShortestRoute.add(roadDTO);
                    break;
                }
            }

        }

        return roadsAlongShortestRoute;
    }

    private boolean isIntersects(Coordinate left, Coordinate right, List<Coordinate> shortestRouteCoordinates, int step) {
        float shortestWayLeftLatitude, shortestWayRightLatitude;
        float shortestWayLeftLongitude, shortestWayRightLongitude;

        float roadLeftLatitude = left.getLatitude();
        float roadRightLatitude = right.getLatitude();
        float roadLeftLongitude = left.getLongitude();
        float roadRightLongitude = right.getLongitude();

        boolean latitudeIntersection = false, longitudeIntersection = false;

        for (int j = 0; j + step < shortestRouteCoordinates.size(); j += step) {
            shortestWayLeftLatitude = shortestRouteCoordinates.get(j).getLatitude();
            shortestWayRightLatitude = shortestRouteCoordinates.get(j + step).getLatitude();
            shortestWayLeftLongitude = shortestRouteCoordinates.get(j).getLongitude();
            shortestWayRightLongitude = shortestRouteCoordinates.get(j + step).getLongitude();

            if (roadLeftLatitude > shortestWayLeftLatitude && roadLeftLatitude < shortestWayRightLatitude ||
                    roadRightLatitude > shortestWayLeftLatitude && roadRightLatitude < shortestWayRightLatitude ||
                    roadLeftLatitude < shortestWayLeftLatitude && roadRightLatitude > shortestWayRightLatitude)
                latitudeIntersection = true;

            if (roadLeftLongitude > shortestWayLeftLongitude && roadLeftLongitude < shortestWayRightLongitude ||
                    roadRightLongitude > shortestWayLeftLongitude && roadRightLongitude < shortestWayRightLongitude ||
                    roadLeftLongitude < shortestWayLeftLongitude && roadRightLongitude > shortestWayRightLongitude)
                longitudeIntersection = true;

            if (latitudeIntersection && longitudeIntersection)
                return true;
        }

        return false;
    }

    private Map<RoadDTO, Integer> calcRoadCapacityChanges(Set<RoadDTO> roads, int extraLoad){
        int sum = roads.parallelStream().reduce(0, (x, r) -> x + r.getCapacity(), Integer::sum);
        float x = (float)1/sum;

        return roads.parallelStream().collect(Collectors.toMap(
                r-> r, r -> r.getCapacity() - (r.getIntensity() + Math.round(r.getCapacity() * x)) ));
    }

    private Map<MetroStationDTO, Integer> calcMetroStationCapacityChanges(Set<MetroStationDTO> ms, int extraLoad){
        int sum = ms.parallelStream().reduce(0, (x, r) -> x + r.getCapacity(), Integer::sum);
        float x = (float)1/sum;

        return ms.parallelStream().collect(Collectors.toMap(
                r-> r, r -> r.getCapacity() - (r.getIntensity() + Math.round(r.getCapacity() * x)) ));
    }

}
