package utils;

import Interfaces.Chromosome;
import algorithms.CONST;
import algorithms.GeneticAlgorithm;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONException;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;


/**
 * Created by root on 27/12/2017.
 */
public class ResultsDbManager {

    final static String filename = CONST.projectPath + "resultsData2.json";
    final static String prefix = "resultsData = ";
    static  JSONArray  allResultsJson;
    final static String csvFileName = "IrGaResults.csv";
    final static String csvFilePath = CONST.projectPath + csvFileName;

    public static class ResultStruct
    {

        final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        private final Chromosome improvedCh;
        private final Chromosome origCh;
        final String  taskID;
        final String  directoryPath;
        final String  fileMode;
        final String  fitnessType;
        final double  originalRMSE;
        final double  originalFitness;
        final double  improvedRMSE;
        final double  improvedFitness;
        final double  RMSEdiff;
        final Date date;
        private final String referencedPath;
        final String sensedPath;
        private final int gen;
        public String improvedImg;
        public String gaResultImg;
        public String tansformIllusImg;



        public ResultStruct(String taskID,
                            String directoryPath,
                            String fileMode,
                            String fitnessType,
                            double originalRMSE,
                            double originalFitness,
                            double improvedRMSE,
                            double improvedFitness,
                            Chromosome origChromosome,
                            Chromosome resChrom, String referencePath, String sensedPath, int gen)
        {
            this.taskID= taskID;
            this.directoryPath  =   directoryPath;
            this.fileMode   =   fileMode;
            this.fitnessType    =   fitnessType;
            this.originalRMSE   =   originalRMSE;
            this.originalFitness =  originalFitness;
            this.improvedRMSE   =   improvedRMSE;
            this.improvedFitness =  improvedFitness;
            this.origCh = origChromosome;
            this.improvedCh = resChrom;
            this.referencedPath = referencePath;
            this.sensedPath = sensedPath;
            RMSEdiff    =   originalRMSE - improvedRMSE;
            date        =   new Date();
            this.gen = gen;
        }

        private String doubleFormat(double num)
        {
            return String.format( "%.2f",num);
        }
        public JSONObject toJson () {
            JSONObject taskData = new JSONObject();
            try {
                taskData.put ("directoryPath", directoryPath);
                taskData.put ("fileMode", fileMode);
                taskData.put ("fitnessType", fitnessType);
                taskData.put ("RMSEimproved", RMSEdiff>0? "+":RMSEdiff<0?"-":"=");
                taskData.put ("TaskID", taskID);
                taskData.put ("date", dateFormat.format(date));
                taskData.put ("referencedPath", referencedPath);
                taskData.put ("sensedPath", sensedPath);
                JSONObject scoreData = new JSONObject();
                taskData.put ("originalRMSE",  originalRMSE);
                taskData.put ("originalFitness",Double.isNaN(originalFitness)? "NaN" : originalFitness );
                taskData.put ("improvedRMSE", improvedRMSE);
                taskData.put ("improvedFitness", Double.isNaN(originalFitness)? "NaN" : improvedFitness);
                taskData.put ("originalCh", origCh.toArray());
                taskData.put ("improvedCh", improvedCh.toArray());
                taskData.put ("RMSEdiff", RMSEdiff);
                taskData.put ("improvedImg",improvedImg);
                taskData.put ("GAresultImg", gaResultImg);
                taskData.put ("tansformIllusImg", tansformIllusImg);
                taskData.put("useReal",CONST.useReal);
                taskData.put("genCount",gen);



            } catch (JSONException e) {
                System.out.println("Failed to save result to json.");
                e.printStackTrace();
            }
            return taskData;
        }
    }

    private static void readFromDB ()  {
        try {
            File dbfile = new File (filename);
            if (dbfile.exists()) {
                byte[] bytes = Files.readAllBytes(new File(filename).toPath());
                String jsonAsString = new String(bytes, "UTF-8").replaceFirst(prefix, "");
                allResultsJson = new JSONArray(jsonAsString);
            }
            else {
                allResultsJson = new JSONArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addToDB (ResultStruct result) throws IOException, ScriptException {
        if (allResultsJson == null)
        {
            readFromDB();
        }
        result.gaResultImg = Utils.writeChromosomeImg(result.improvedCh,result.directoryPath,Integer.valueOf(result.taskID),0,false,"beforeOptim");
        result.improvedImg = Utils.writeChromosomeImg(result.origCh,result.directoryPath,Integer.valueOf(result.taskID),0,false,"fminsearch_"+result.fitnessType);
        //result.tansformIllusImg = Utils.showTransformationIllustration(result.origCh,result.directoryPath,result.taskID+"_chvis_"+result.fitnessType);
        result.tansformIllusImg = "N/A";
        JSONObject resultJson = result.toJson();
        allResultsJson.put(resultJson);
        try (FileWriter file = new FileWriter(filename)) {
            file.write(prefix + allResultsJson.toString(4));
            System.out.println("Successfully Copied JSON Object to File...");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void writeResultSummaryToCSV (GeneticAlgorithm alg, Chromosome origCh, Chromosome improvedCh ,
                                                FileOption fileOption, FminsearchOptimizer.Method method, boolean useReal
                                                , long fminsearchTime)
    {
        final String codeVersion= "V1";
        long gaTime = (alg.current - alg.start) ;
        long totalTime = gaTime + fminsearchTime;

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String fminsearchRMSE = (improvedCh!=null? String.valueOf(alg.calcRMSE(improvedCh)) :"N/A");

        CsvLine csvLine = new CsvLine();
        CsvLine csvHeadLine = new CsvLine();
        csvHeadLine.addField("Date");
        csvLine.addField(date);
        csvHeadLine.addField("CodeVersion");
        csvLine.addField(codeVersion);
        csvHeadLine.addField("FileOption");
        csvLine.addField(fileOption.toString());
        csvHeadLine.addField("UseReal");
        csvLine.addField(String.valueOf(useReal));
        csvHeadLine.addField("FminsearchFitnessMethod");
        csvLine.addField(method.name());
        csvHeadLine.addField("GaGenCount");
        csvLine.addField(String.valueOf(alg._gen));
        csvHeadLine.addField("GaTime");
        csvLine.addField(String.valueOf(gaTime));
        csvHeadLine.addField("GaOrigRMSE");
        csvLine.addField(String.valueOf(alg.calcRMSE(origCh)));
        csvHeadLine.addField("FminsearchTime");
        csvLine.addField(String.valueOf(fminsearchTime));
        csvHeadLine.addField("FminsearchImprovedRmse");
        csvLine.addField(String.valueOf(fminsearchRMSE));
        csvHeadLine.addField("TotalTime");
        csvLine.addField(String.valueOf(totalTime));
        try {
            File f = new File(csvFilePath);
            if(!f.exists()){
                    f.createNewFile();
                //write headline
                Files.write(Paths.get(csvFilePath),csvHeadLine.get().getBytes(), StandardOpenOption.APPEND);
            }
            Files.write(Paths.get(csvFilePath), csvLine.get().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("Failed To Write results to file!");
                System.out.println(csvLine.get());
                e.printStackTrace();
            }
    }
    private static class CsvLine
    {
        String data = "";
        public void addField (String value)
        {
            data += value + ",";
        }
        public String get(){
            return data + "\n";
        }

     }

}


