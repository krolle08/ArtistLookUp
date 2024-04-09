package Application.service.Area;

import Application.service.DataProcessor;
import Application.service.MusicEntityObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Under development
 */
@Service
public class SearchAreaService implements DataProcessor<AreaInfoObj> {
    private static final Logger logger = LoggerFactory.getLogger(SearchAreaService.class.getName());

    @Override
    public MusicEntityObj getData(Map<String, String> searchParam) {
        return new MusicEntityObj();
    }
}

