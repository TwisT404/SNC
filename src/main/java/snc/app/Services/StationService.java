package snc.app.Services;

import org.springframework.stereotype.Service;
import snc.app.Model.Station;
import snc.app.Repository.StationRepository;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> afficherStations() {
        return stationRepository.findAll();
    }
}
