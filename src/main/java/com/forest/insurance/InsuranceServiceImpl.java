package com.forest.insurance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.forest.data.DataProcess;
import com.forest.distance.DistanceUtil;
import com.forest.util.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class InsuranceServiceImpl implements InsuranceService {
    private Logger log;

    @Value("${insuranceModelURL}")
    private String insuranceModelURL;
    @Value("${wordsFile}")
    private String wordsFile;
    @Value("${labelsFile}")
    private String labelsFile;
    @Value("${sentenceLen}")
    private int sentenceLen;
    @Value("${vectorsFile}")
    private String vectorsFile;
    @Value("${vectDim}")
    private int vectDim;
    @Value("${labelNum}")
    private int labelNum;
    @Value("${threshold}")
    private Double threshold;

    private boolean isStart = false;
    private DataProcess dataProcess = null;
    private DistanceUtil distanceUtil = null;
    private List<Double> distances = null;


    @Override
    public InsuranceResponse predict(InsuranceRequest request) {
        if (isStart == false) {
            isStart = true;
            log = Logger.getLogger("InsuranceServiceImpl");
            log.setLevel(Level.ALL);
            java.net.URL uri = this.getClass().getResource("/");
            String resourcePath = uri.getPath();
            dataProcess = new DataProcess(resourcePath + wordsFile, resourcePath + labelsFile, sentenceLen);
            distanceUtil = new DistanceUtil(resourcePath + vectorsFile, vectDim, labelNum, threshold);
            distances = new ArrayList<>(1);
        }
        if (request != null) {
            String text = request.getText();
            if (!text.isEmpty()) {
                List<Integer> textIndexs = dataProcess.str2index(text);
                textIndexs = dataProcess.padding(textIndexs);
//                textIndexs.clear();
//                textIndexs.add(4371);
//                textIndexs.add(3249);
//                textIndexs.add(1283);
//                textIndexs.add(1281);
//                textIndexs.add(3930);
//                textIndexs.add(3136);
//                textIndexs.add(4113);
//                textIndexs.add(3237);
//                textIndexs.add(2581);
//                textIndexs.add(3025);
//                textIndexs.add(949);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//
//                textIndexs.add(7541);
//                textIndexs.add(7541);
//

                log.info("input index:" + textIndexs);
                List<List<Integer>> instances = new ArrayList<List<Integer>>();
                instances.add(textIndexs);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("instances", instances);
                String body = jsonBody.toJSONString();
                String result = HttpClient.sendPost(insuranceModelURL, body);

                JSONObject jsonObject = JSONObject.parseObject(result);
                Object predictions = jsonObject.get("predictions");
                List<Object> listListObjects = JSONArray.parseArray(predictions.toString(), Object.class);
                if (!listListObjects.isEmpty()) {
                    Object listObjects = listListObjects.get(0);
                    List<Double> listObject = JSONArray.parseArray(listObjects.toString(), Double.class);
                    if (!listObject.isEmpty()) {
                        Double vect[] = listObject.toArray(new Double[vectDim]);
                        for (Double d : vect)
                            log.info("vector:" + d);
                        if (!distances.isEmpty())
                            distances.clear();
                        int index = distanceUtil.getDistances(vect, distances);
                        String label = dataProcess.index2label(index);
                        log.info("predict index = " + index + ", distance = " + distances.get(0) +", label = " + label);
                        InsuranceResponse response = new InsuranceResponse();
                        Insurance insurance = new Insurance(index, distances.get(0), label);
                        response.addData(insurance);
                        return response;
                    }
                }

            }
        }
        return null;
    }


}
