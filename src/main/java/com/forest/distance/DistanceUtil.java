package com.forest.distance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.sqrt;

public class DistanceUtil {
    private Logger log;

    private Double means[][];
    private Double meanMolds[];
    Double threshold = 0.0;
    int vectDim = 0;
    int labelNum = 0;

    public DistanceUtil(String fileName, int vectDim, int labelNum, Double threshold) {
        log = Logger.getLogger("DistanceUtil");
        log.setLevel(Level.ALL);

        this.threshold = threshold;
        this.labelNum = labelNum;
        this.vectDim = vectDim;

        int len = (int) getLineNumber(fileName);
        if (len != labelNum) {
            log.warning("mean vectors size mismatch!");
        }
        meanMolds = new Double[labelNum];
        means = new Double[labelNum][vectDim];
        loadMeanFromFile(fileName, means);

        for (int i = 0; i < labelNum; i++) {
            double m = getMold(means[i]);
            meanMolds[i] = m;
        }
    }

    public int getDistances(Double vect[], List<Double> distances) {
        Double min = 2.0;
        int index = 0;
        for (int i = 0; i < labelNum; i++) {
            Double dist = getSimilarity(vect, means[i], i);
            if (min > dist) {
                min = dist;
                index = i;
            }
        }
        distances.add(min);
        return index;
    }


    private void splitVector(String str, Double means[], String delimiter) {
        if (!str.isEmpty() && !delimiter.isEmpty()) {
            int i = 0;
            for (String retval : str.split(delimiter)) {
                //System.out.println(retval);
                means[i++] = Double.valueOf(retval);
            }
        }
    }

    private long getLineNumber(String fileName) {
        if (!fileName.isEmpty()) {
            try {
                FileReader fileReader = new FileReader(fileName);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber();
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private int loadMeanFromFile(String fileName, Double means[][]) {
        String delimiter = " ";

        int i = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str = null;
            while ((str = in.readLine()) != null) {
                splitVector(str, means[i], delimiter);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return i;
    }

    private Double getMold(Double vec[]) {
        int n = vectDim;
        Double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += vec[i] * vec[i];
        }
        return sqrt(sum);
    }

    private Double getSimilarity(Double vect[], Double means[], int start) {
        int n = vectDim;

        double tmp = 0.0;  //内积
        for (int i = 0; i < n; i++) {
            tmp += vect[i] * means[i];
        }

        return 1.0 - tmp / (getMold(vect) * meanMolds[start]);
    }

}
