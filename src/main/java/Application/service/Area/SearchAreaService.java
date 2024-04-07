package Application.service.Area;

import Application.service.DataProcessor;
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
    public AreaInfoObj getData(Map<String, String> searchParam) {
        return null;
    }
}

