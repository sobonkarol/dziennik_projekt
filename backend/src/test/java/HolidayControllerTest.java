import org.example.backend.controller.HolidayController;
import org.example.backend.service.HolidayService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HolidayControllerTest {

    private final HolidayService service = mock(HolidayService.class);
    private final HolidayController controller = new HolidayController(service);

    @Test
    void getHolidays_ShouldReturnList() {
        when(service.getHolidays("PL", 2024)).thenReturn(List.of(Map.of("name", "Boże Narodzenie")));

        List<Map<String, String>> result = controller.getHolidays("PL", 2024);

        assertEquals(1, result.size());
    }
}