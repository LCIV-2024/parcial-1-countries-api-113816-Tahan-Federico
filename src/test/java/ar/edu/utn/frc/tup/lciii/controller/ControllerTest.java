package ar.edu.utn.frc.tup.lciii.controller;

import ar.edu.utn.frc.tup.lciii.controllers.CountryController;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)

public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void GetCountries() throws Exception {
       List<CountryDTO> mockc = new ArrayList<>();

        CountryDTO a =  new CountryDTO("ARG", "Argentina");
        CountryDTO b=   new CountryDTO("BRA", "Brazil");
        mockc.add(a);

        when(countryService.getAllSimpleCountries("Argentina", null, null, null)).thenReturn(mockc);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries")
                        .param("name", "Argentina")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(countryService, times(1)).getAllSimpleCountries("Argentina", null, null, null);
    }

    @Test
    void GetCountriesContinent() throws Exception {
        List<CountryDTO> mockc = new ArrayList<>();

        CountryDTO a = new CountryDTO("ARG", "Argentina");
        CountryDTO b = new CountryDTO("BRA", "Brazil");
        mockc.add(a);
        mockc.add(b);

        when(countryService.getAllSimpleCountries(null, null, "Americas", null)).thenReturn(mockc);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countries/Americas/continent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(countryService, times(1)).getAllSimpleCountries(null, null, "Americas", null);
    }
}
