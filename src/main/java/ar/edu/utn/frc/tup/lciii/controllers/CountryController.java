package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveCountries;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/countries")
public class CountryController {

    @Autowired
    private final CountryService countryService;


    @GetMapping()
    public List<CountryDTO> GetCounties(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String code)
    {
        return countryService.getAllSimpleCountries(name, code, null, null);
    }

    @GetMapping("{continet}/continent")
    public List<CountryDTO> GetCountiesByContinent(@PathVariable(required = false) String continet)
    {
        return countryService.getAllSimpleCountries(null, null,continet, null);
    }

    @GetMapping("{language}/language")
    public List<CountryDTO> GetCountryByLanguages(@PathVariable(required = false) String language)
    {
        return countryService.getAllSimpleCountries(null, null,null, language);
    }
    @GetMapping("/most-borders")
    public List<CountryDTO> GetCountryByMostBorders()
    {
        return countryService.getCountryMostBorders();
    }

    @PostMapping()
    public ResponseEntity<List<CountryDTO>> save(@RequestBody(required = true) SaveCountries saveCountries) {
        if (saveCountries.getAmountOfCountryToSave() > 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<>());
        }
        List<CountryDTO> response = countryService.saveCountries(saveCountries.getAmountOfCountryToSave());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}