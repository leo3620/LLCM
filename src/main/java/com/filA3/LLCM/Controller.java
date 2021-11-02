package com.filA3.LLCM;

import com.filA3.prm.PRMCalculator;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @PostMapping("/calculate")
  public ResponseEntity<Double> calculate(@RequestBody  JSONObject jsonObject){
        PRMCalculator calculator = new PRMCalculator(jsonObject);
        calculator.parse();
        return ResponseEntity.ok(calculator.calculate());
    }
}
