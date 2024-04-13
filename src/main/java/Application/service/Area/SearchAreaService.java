package Application.service.Area;

import Application.service.DataProcessor;
import Application.service.MusicEntityObj;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Under development
 */
@Service
public class SearchAreaService implements DataProcessor<AreaInfoObj> {
    @Override
    public MusicEntityObj getData(Map<String, String> searchParam) {
        return new MusicEntityObj();
    }
}

