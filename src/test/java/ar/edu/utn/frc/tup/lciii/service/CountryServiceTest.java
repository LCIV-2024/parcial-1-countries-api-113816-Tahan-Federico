package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entity.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest

public class CountryServiceTest {
    @MockBean
    CountryRepository countryRepository;
    @MockBean
    RestTemplate restTemplate;
    @SpyBean
    CountryService service;

    @Test
    public void testGetAllCountries() {
        String url = "https://restcountries.com/v3.1/all";
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("name", Map.of("common", "Argentina"), "cca3", "ARG", "borders", List.of("BRA", "CHL"), "population", 45195777, "area", 2780400.0, "region", "Americas", "languages", Map.of("spa", "Spanish")),
                Map.of("name", Map.of("common", "Brazil"), "cca3", "BRA", "borders", List.of("ARG", "URY"), "population", 212559417, "area", 8515767.0, "region", "Americas", "languages", Map.of("por", "Portuguese"))
        );

        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);

        List<Country> countries = service.getAllCountries();

        assertEquals(2, countries.size());
        assertEquals("Argentina", countries.get(0).getName());
        assertEquals("Brazil", countries.get(1).getName());
    }

    @Test
    public void testGetCountryMostBorders() {
        String url = "https://restcountries.com/v3.1/all";
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("name", Map.of("common", "Argentina"), "cca3", "ARG", "borders", List.of("BRA", "CHL"), "population", 45195777, "area", 2780400.0, "region", "Americas", "languages", Map.of("spa", "Spanish")),
                Map.of("name", Map.of("common", "Brazil"), "cca3", "BRA", "borders", List.of("ARG", "URY", "BOL", "PER", "COL", "VEN", "GUY", "SUR", "FRA"), "population", 212559417, "area", 8515767.0, "region", "Americas", "languages", Map.of("por", "Portuguese"))
        );

        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);

        List<CountryDTO> result = service.getCountryMostBorders();

        assertEquals(1, result.size());
        assertEquals("Brazil", result.get(0).getName());
    }

    @Test
    public void testGetAllSimpleCountries() {
        String url = "https://restcountries.com/v3.1/all";
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("name", Map.of("common", "Argentina"), "cca3", "ARG", "borders", List.of("BRA", "CHL"), "population", 45195777, "area", 2780400.0, "region", "Americas", "languages", Map.of("spa", "Spanish")),
                Map.of("name", Map.of("common", "Brazil"), "cca3", "BRA", "borders", List.of("ARG", "URY"), "population", 212559417, "area", 8515767.0, "region", "Americas", "languages", Map.of("por", "Portuguese"))
        );

        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);

        List<CountryDTO> result = service.getAllSimpleCountries("Argentina", null, null, null);

        assertEquals(1, result.size());
        assertEquals("Argentina", result.get(0).getName());

        result = service.getAllSimpleCountries(null, "BRA", null, null);
        assertEquals(1, result.size());
        assertEquals("Brazil", result.get(0).getName());

        result = service.getAllSimpleCountries(null, null, "Americas", null);
        assertEquals(2, result.size());

        result = service.getAllSimpleCountries(null, null, null, "Spanish");
        assertEquals(1, result.size());
        assertEquals("Argentina", result.get(0).getName());
    }

    @Test
    public void testSaveCountries() {
        String url = "https://restcountries.com/v3.1/all";
        List<Map<String, Object>> mockResponse = List.of(
                Map.of("name", Map.of("common", "Argentina"), "cca3", "ARG", "borders", List.of("BRA", "CHL"), "population", 45195777, "area", 2780400.0, "region", "Americas", "languages", Map.of("spa", "Spanish"))
        );

        when(restTemplate.getForObject(url, List.class)).thenReturn(mockResponse);

        List<CountryDTO> result = service.saveCountries(1);

        CountryEntity expectedEntity = new CountryEntity();
        expectedEntity.setCode("ARG");
        expectedEntity.setName("Argentina");
        expectedEntity.setArea(2780400.0);

        verify(countryRepository).saveAll(List.of(expectedEntity));

        assertEquals(1, result.size());
    }
}
