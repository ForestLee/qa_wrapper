package com.forest;

import com.alibaba.fastjson.JSONObject;
import com.forest.insurance.InsuranceRequest;
import com.forest.insurance.InsuranceResponse;
import com.forest.insurance.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class QaVectorController {
    private Logger log;


    @Autowired
    private InsuranceService insuranceService;

    public QaVectorController() {
        log = Logger.getLogger("QaVectorController");
        log.setLevel(Level.ALL);
    }

    // for testing
    @RequestMapping("/helloworld")
    public String helloWorld() {
        return "helloworld";
    }

    @RequestMapping(value = "/nlp/qa/insurance", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    String predict(@RequestBody InsuranceRequest request) {
        InsuranceResponse response = null;
        if (request == null || request.getText() == null
                || !StringUtils.hasText(request.getNbest())) {
            log.warning("必填参数不能为空");
            response = new InsuranceResponse();
            response.setCode(-1);
            response.setMessage("必填参数不能为空");
            return JSONObject.toJSONString(response);
        }
        log.info("input text:" + request.getText());
        response = insuranceService.predict(request);
        log.info("result: " + response.toString());
        String responseStr = JSONObject.toJSONString(response);
        return responseStr;
    }


}
