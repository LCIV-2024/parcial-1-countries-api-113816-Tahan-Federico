package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entity.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private final CountryRepository countryRepository;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        public List<CountryDTO> getAllSimpleCountries(String nameFilter, String codeFilter, String continentFilter, String languageFilter) {
                List<Country> countries = this.getAllCountries();
                return countries.stream()
                        .filter(country -> (nameFilter == null || country.getName().equalsIgnoreCase(nameFilter)) &&
                                (codeFilter == null || country.getCode().equalsIgnoreCase(codeFilter)) &&
                                (continentFilter == null || country.getRegion().equalsIgnoreCase(continentFilter)) &&
                                (languageFilter == null || (country.getLanguages() != null &&  country.getLanguages().containsValue(languageFilter))))
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        public List<CountryDTO> getCountryMostBorders() {
                List<Country> countries = this.getAllCountries();
                return countries.stream()
                        .filter(country -> country.getBorders() != null)
                        .sorted((c1, c2) -> Integer.compare(c2.getBorders().size(), c1.getBorders().size()))
                        .limit(1)
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }
        public List<CountryDTO> saveCountries(Integer amount) {

                List<Country> countries = this.getAllCountries();

                Collections.shuffle(countries);
                List<Country> selectedCountries = countries.stream()
                        .limit(amount)
                        .collect(Collectors.toList());

                List<CountryEntity> entities = selectedCountries.stream()
                        .map(this::mapToEntity)
                        .collect(Collectors.toList());
                countryRepository.saveAll(entities);

                return selectedCountries.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
        }

        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .code((String) countryData.get("cca3"))
                        .borders((List<String>) countryData.get("borders"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }



        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }

        private CountryEntity mapToEntity(Country country) {
                CountryEntity response =  new CountryEntity();
                response.setArea(country.getArea());
                response.setName(country.getName());
                response.setCode(country.getCode());
                return response;
        }
}